package com.oracle.oracle.training.services;

import com.oracle.oracle.training.entity.post.*;
import com.oracle.oracle.training.entity.User;
import com.oracle.oracle.training.exceptions.AccessDeniedException;
import com.oracle.oracle.training.exceptions.BadRequestException;
import com.oracle.oracle.training.exceptions.ResourceNotFound;
import com.oracle.oracle.training.repository.PostFeedBackRepository;
import com.oracle.oracle.training.repository.ResourcePostRepository;
import com.oracle.oracle.training.repository.TechStackRepository;
import com.oracle.oracle.training.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class ResourcePostServiceImp implements ResourcePostService{
    @Autowired
    ResourcePostRepository resourcePostRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TechStackRepository techStackRepository;
    @Autowired
    PostFeedBackRepository postFeedBackRepository;

    @Autowired
    PostUtilityService utility;
    @Override
    public ResourcePost savePost(String email, ResourcePost resourcePost, Optional<MultipartFile[]> files) {
        User user = userRepository.findByEmail(email);
        List<TechStack> techStackList = resourcePost.getTechStacks();
        List<TechStack> savedStackList = new ArrayList<>();
        for(TechStack t: techStackList){
           TechStack saved = techStackRepository.findByName(t.getName());
           if(saved==null)  saved = techStackRepository.save(t);
           savedStackList.add(saved);
        }

        resourcePost.setTechStacks(savedStackList);
        resourcePost.setUser(user);

        List<PostImage> postImages = new ArrayList<>();
        Set<String> allowedFileTypes = new HashSet<>(Arrays.asList("image/jpeg","image/png"));
        if(files.isPresent()) {
            MultipartFile[] filesArray = files.get();
            for (MultipartFile file : filesArray) {
                if (!allowedFileTypes.contains(file.getContentType())) throw new BadRequestException("Invalid file");
                try {
                    String imageCompressed = Base64.getEncoder().encodeToString(file.getBytes());
                    PostImage postImage = PostImage.builder()
                            .name(file.getOriginalFilename())
                            .type(file.getContentType())
                            .image(imageCompressed.getBytes()).build();
                    postImages.add(postImage);
                } catch (IOException e) {
                    throw new BadRequestException("Invalid files");
                }
            }
            resourcePost.setPostImages(postImages);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("EE MMM d y H:m:s ZZZ");
        String dateString = formatter.format(new Date());
        resourcePost.setDateModified(dateString);
        ResourcePost post = resourcePostRepository.save(resourcePost);

        PostFeedBack postFeedBack = new PostFeedBack();
        postFeedBack.setId(post.getId());
        resourcePost.setPostFeedBack(postFeedBack);
        return resourcePostRepository.save(post);
    }


    @Override
    public List<ResourcePost> getAll(String email) throws ResourceNotFound {
        User user = userRepository.findByEmail(email);
        if(user==null) throw  new ResourceNotFound("No such user");
        List<ResourcePost> resourcePostList =  resourcePostRepository.findByAccessLevel("PUBLIC");
        // convert blob to base64
        for(ResourcePost resourcePost:resourcePostList){
            List<PostImage> postImages = resourcePost.getPostImages();
            for(PostImage postImage:postImages){
                postImage.setBase64Image(new String(postImage.getImage()));
            }
            PostFeedBack postFeedBack = resourcePost.getPostFeedBack();
            postFeedBack.setCommentsList(utility.getComments(postFeedBack.getCommentsData()));
        }
        resourcePostList.sort((a, b) -> b.getId() - a.getId());
        return  resourcePostList;
    }

    @Override
    public ResourcePost getPost(String email, Integer postId) throws ResourceNotFound {
        User user = userRepository.findByEmail(email);
        if(user==null) throw  new ResourceNotFound("No such user");
        Optional<ResourcePost> resourcePost = resourcePostRepository.findById(postId);
        if(!resourcePost.isPresent()) throw new BadRequestException("No such post");
        ResourcePost post = resourcePost.get();
        if(!post.getUser().getEmail().equals(email)) throw new AccessDeniedException("Can only retrieve in your post");
        for(PostImage image:post.getPostImages()){
            image.setBase64Image(Base64.getEncoder().encodeToString(image.getImage()));
        }
        PostFeedBack postFeedBack = post.getPostFeedBack();
        postFeedBack.setCommentsList(utility.getComments(postFeedBack.getCommentsData()));
        return post;
    }

    @Override
    public String upvotePost(String email, Integer postId) throws  ResourceNotFound,BadRequestException {
        User user = userRepository.findByEmail(email);
        if(user==null) throw  new ResourceNotFound("No such user");
        Optional<ResourcePost> resourcePost = resourcePostRepository.findById(postId);
        if(!resourcePost.isPresent()) throw new BadRequestException("No such post");
        ResourcePost post = resourcePost.get();
        PostFeedBack postFeedBack = post.getPostFeedBack();

        postFeedBack.setCommentsList(utility.getComments(postFeedBack.getCommentsData()));
        Set<Integer> userSet =  utility.parseStringToInteger(postFeedBack.getLikedUserList());
        Set<Integer> postSet =  utility.parseStringToInteger(user.getLikedPostsList());

        Boolean upvoted = true;
        // downvote the post
        if(userSet.contains(user.getId())){
            upvoted = false;
            postSet.remove(postId);
            userSet.remove(user.getId());
            postFeedBack.setUpvotesCount(postFeedBack.getUpvotesCount()-1);
            postFeedBack.setLikedUserList(utility.parseSetToString(userSet));
            user.setLikedPostsList(utility.parseSetToString(postSet));
        }
        else{
            // upvote the post
            if(user.getLikedPostsList()==null) {
                user.setLikedPostsList("");
            }
            if(postFeedBack.getLikedUserList()==null){
                postFeedBack.setLikedUserList("");
            }
            postFeedBack.setLikedUserList(postFeedBack.getLikedUserList()+user.getId()+",");
            postFeedBack.setUpvotesCount(postFeedBack.getUpvotesCount()+1);

            user.setLikedPostsList(user.getLikedPostsList()+post.getId()+",");
        }

        postFeedBackRepository.save(postFeedBack);
        userRepository.save(user);
        return upvoted ? "Upvoted Succesfully" : "Downvoted Succesfully";
    }

    @Override
    public List<ResourcePost> getAllLikedPost(String email) {
        User user = userRepository.findByEmail(email);
        if(user==null) throw  new ResourceNotFound("No such user");
        Set<Integer> likedPostsId = utility.parseStringToInteger(user.getLikedPostsList());
        List<ResourcePost> resourcePostList = new ArrayList<>();
        for(Integer id:likedPostsId){
            Optional<ResourcePost> OptionalPost = resourcePostRepository.findById(id);

            if(OptionalPost.isPresent()){
                ResourcePost post = OptionalPost.get();
                for(PostImage postImage:post.getPostImages()){
                    postImage.setBase64Image(new String(postImage.getImage()));
                }
                resourcePostList.add(post);
            }
        }
        return  resourcePostList;
    }

    @Override
    public Comments addComment(String email, Integer postId, Comments comment) {
        User user = userRepository.findByEmail(email);
        if(user==null) throw  new ResourceNotFound("No such user");
        Optional<ResourcePost> resourcePost = resourcePostRepository.findById(postId);
        if(!resourcePost.isPresent()) throw new BadRequestException("No such post");

        if(comment.getMessage().length()==0) throw  new BadRequestException("Comment cannot be empty!");
        ResourcePost post = resourcePost.get();
        PostFeedBack postFeedBack = post.getPostFeedBack();
        comment.setId(postFeedBack.getCommentsCount());
        comment.setUserId(user.getId());
        postFeedBack.setCommentsCount(postFeedBack.getCommentsCount()+1);

        List<Comments> comments = utility.getComments(postFeedBack.getCommentsData());
        SimpleDateFormat formatter = new SimpleDateFormat("EE MMM d y H:m:s ZZZ");
        String dateString = formatter.format(new Date());
        comment.setDateModified(dateString);
        comment.setUserName(user.getFirstName()+" "+user.getLastName());
        comment.setUserProfileImage(user.getProfileImage());
        comments.add(comment);
        postFeedBack.setCommentsData(utility.getBytes(comments));

        postFeedBackRepository.save(postFeedBack);
        return comment;
    }

}
