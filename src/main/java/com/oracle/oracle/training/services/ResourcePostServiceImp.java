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
import com.oracle.oracle.training.utils.CommaSeperatedParser;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
        return resourcePostRepository.save(resourcePost);
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
        System.out.println(post.getPostImages().get(0).getImage());
        if(!post.getUser().getEmail().equals(email)) throw new AccessDeniedException("Can only add in your post");
        return post;
    }

    @Override
    public void upvotePost(String email, Integer postId) throws  ResourceNotFound,BadRequestException {
        User user = userRepository.findByEmail(email);
        if(user==null) throw  new ResourceNotFound("No such user");
        Optional<ResourcePost> resourcePost = resourcePostRepository.findById(postId);
        if(!resourcePost.isPresent()) throw new BadRequestException("No such post");
        ResourcePost post = resourcePost.get();
        PostFeedBack postFeedBack = post.getPostFeedBack();
        if(postFeedBack==null){
            postFeedBack = new PostFeedBack(postId);
            post.setPostFeedBack(postFeedBack);
            resourcePostRepository.save(post);
        }
        CommaSeperatedParser commaSeperatedParser = new CommaSeperatedParser();
        Set<Integer> userSet =  commaSeperatedParser.parseStringToInteger(postFeedBack.getLikedUserList());
        if(userSet.contains(user.getId())) throw new BadRequestException("Cannot liked an already liked post");
        postFeedBack.setLikedUserList(postFeedBack.getLikedUserList()+user.getId()+",");
        postFeedBack.setUpvotesCount(postFeedBack.getUpvotesCount()+1);
        if(user.getLikedPostsList()==null) {
            user.setLikedPostsList("");
        }
        user.setLikedPostsList(user.getLikedPostsList()+post.getId()+",");
        postFeedBackRepository.save(postFeedBack);
        userRepository.save(user);
    }
}
