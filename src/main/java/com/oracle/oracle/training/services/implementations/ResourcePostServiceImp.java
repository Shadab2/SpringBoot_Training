package com.oracle.oracle.training.services.implementations;

import com.oracle.oracle.training.entity.NotificationType;
import com.oracle.oracle.training.entity.Notificaton;
import com.oracle.oracle.training.entity.post.*;
import com.oracle.oracle.training.entity.User;
import com.oracle.oracle.training.exceptions.AccessDeniedException;
import com.oracle.oracle.training.exceptions.BadRequestException;
import com.oracle.oracle.training.exceptions.ResourceNotFound;
import com.oracle.oracle.training.repository.*;
import com.oracle.oracle.training.services.functional.PostUtilityService;
import com.oracle.oracle.training.services.interfaces.ResourcePostService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class ResourcePostServiceImp implements ResourcePostService {
    @Autowired
    private ResourcePostRepository resourcePostRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TechStackRepository techStackRepository;
    @Autowired
    private PostFeedBackRepository postFeedBackRepository;

    @Autowired
    private PostImageRepository postImageRepository;

    @Autowired
    private PostUtilityService utility;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public ResourcePost savePost(String email, ResourcePost resourcePost, Optional<MultipartFile[]> files) {
        User user = getUser(email);
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
        resourcePost.setPostFeedBack(new PostFeedBack());
        return resourcePostRepository.save(resourcePost);
    }


    @Override
    public List<ResourcePost> getAll(String email) throws ResourceNotFound {
        User user = getUser(email);
        List<ResourcePost> resourcePostList =  resourcePostRepository.findByAccessLevel("PUBLIC");
        parsePosts(resourcePostList);
        resourcePostList.sort((a, b) -> b.getId() - a.getId());
        return  resourcePostList;
    }

    @Override
    public ResourcePost getPost(String email, Integer postId) throws ResourceNotFound {
        User user = getUser(email);
        ResourcePost post = getPost(postId);
        if(!post.getUser().getEmail().equals(email)) throw new AccessDeniedException("Can only retrieve in your post");
        parseIndividualPost(post);
        return post;
    }

    @Override
    public String upvotePost(String email, Integer postId) throws  ResourceNotFound,BadRequestException {
        User user = getUser(email);
        ResourcePost post = getPost(postId);
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
        if(upvoted){
            Notificaton notificaton = Notificaton.builder()
                    .message("Upvoted the post")
                    .dateCreated(utility.getDate())
                    .type(NotificationType.UPVOTED)
                    .senderEmail(user.getEmail())
                    .senderName(user.getFirstName()+" "+user.getLastName())
                    .postTitle(post.getTitle())
                    .profileImage(user.getProfileImage())
                    .build();
            simpMessagingTemplate.convertAndSend("/global/notification/"+post.getUser().getEmail(),notificaton);
        }
        return upvoted ? "Upvoted Succesfully" : "Downvoted Succesfully";
    }

    @Override
    public List<ResourcePost> getAllLikedPost(String email) {
        User user = getUser(email);
        Set<Integer> likedPostsId = utility.parseStringToInteger(user.getLikedPostsList());
        List<ResourcePost> resourcePostList = new ArrayList<>();
        for(Integer id:likedPostsId){
            Optional<ResourcePost> OptionalPost = resourcePostRepository.findById(id);

            if(OptionalPost.isPresent()){
                ResourcePost post = OptionalPost.get();
                parseIndividualPost(post);
                resourcePostList.add(post);
            }
        }
        return  resourcePostList;
    }

    @Override
    public Comments addComment(String email, Integer postId, Comments comment) {
        User user = getUser(email);
        ResourcePost post = getPost(postId);

        if(comment.getMessage().length()==0) throw  new BadRequestException("Comment cannot be empty!");
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
        Notificaton notificaton = Notificaton.builder()
                .message("commented the post")
                .dateCreated(utility.getDate())
                .type(NotificationType.COMMENTED)
                .content("'"+comment.getMessage()+"'")
                .senderEmail(user.getEmail())
                .postTitle(post.getTitle())
                .senderName(user.getFirstName()+" "+user.getLastName())
                .profileImage(user.getProfileImage())
                .build();
        simpMessagingTemplate.convertAndSend("/global/notification/"+post.getUser().getEmail(),notificaton);
        return comment;
    }

    @Override
    public List<ResourcePost> getTrendingPosts(String email) {
        User user = getUser(email);
        List<ResourcePost> allPosts = getAll(email);
        allPosts.sort((o1, o2) -> {
            PostFeedBack postFeedBack1 = o1.getPostFeedBack();
            PostFeedBack postFeedBack2 = o2.getPostFeedBack();
            Integer engagement1 = postFeedBack1.getCommentsCount() * 10  + postFeedBack1.getUpvotesCount() * 20;
            Integer engagement2 = postFeedBack2.getCommentsCount() * 10  + postFeedBack2.getUpvotesCount() * 20;
            return engagement2 - engagement1;
        });
        List<ResourcePost> resourcePostList= allPosts.subList(0,Math.min(allPosts.size(),5));
        parsePosts(resourcePostList);
        return  resourcePostList;
    }

    @Override
    public List<ResourcePost> getAllUserPost(String email) {
         User user = getUser(email);
        List<ResourcePost> resourcePostList = user.getResourcePostList();
        parsePosts(resourcePostList);
        return resourcePostList;
    }

    @Override
    public List<ResourcePost> search(String email, Map<String, Object> requestMap) {
        User user = getUser(email);
        Map<Integer,ResourcePost> map = new HashMap<>();
        List<ResourcePost> resourcePostList = null;
        if(requestMap.containsKey("tech")){
           TechStack techStack = techStackRepository.findByNameIgnoreCase((String)requestMap.get("tech"));
           if(techStack!=null){
               resourcePostList = techStack.getResourcePostList();
               for(ResourcePost post:resourcePostList) map.putIfAbsent(post.getId(),post);
           }
        }
        if(requestMap.containsKey("title")){
            resourcePostList =  resourcePostRepository.findByAccessLevelAndTitleIgnoreCaseContaining("PUBLIC",(String)requestMap.get("title"));
            for(ResourcePost post:resourcePostList) map.putIfAbsent(post.getId(),post);
        }
        List<ResourcePost> posts = new ArrayList<>(map.values());
        parsePosts(posts);
        return posts;
    }

    @Override
    public List<PostImage> getAllImages(String email) {
        User user = getUser(email);
        List<PostImage> postImages = postImageRepository.findAll();
        for(PostImage postImage:postImages){
            postImage.setBase64Image(new String(postImage.getImage()));
        }
        return postImages;
    }

    @Override
    public List<ResourcePost> getAllUserSavedPost(String email) {
        User user = getUser(email);
        List<ResourcePost> resourcePostList = new ArrayList<>();
        Set<Integer> set = utility.parseStringToInteger(user.getSavedPostsList());
        for(Integer postId:set){
            ResourcePost post = resourcePostRepository.findById(postId).get();
            parseIndividualPost(post);
            resourcePostList.add(post);
        }
        return resourcePostList;
    }

    @Override
    public Map<String, Set<Integer>> getUserMappings(String email) {
       User user = getUser(email);
       if(user.getLikedPostsList()==null) user.setLikedPostsList("");
       if(user.getSavedPostsList()==null) user.setSavedPostsList("");
       Map<String,Set<Integer>> map = new HashMap<>();
       map.put("liked",utility.parseStringToInteger(user.getLikedPostsList()));
       map.put("saved",utility.parseStringToInteger(user.getSavedPostsList()));
       return map;
    }

    @Override
    public String savePostForUser(String email, Integer postId) {
        User user = getUser(email);
        ResourcePost post = getPost(postId);
        if(user.getSavedPostsList()==null){
            user.setSavedPostsList("");
        }
        Set<Integer> set = utility.parseStringToInteger(user.getSavedPostsList());
        boolean saved = false;
        if(set.contains(postId)){
            set.remove(postId);
            user.setSavedPostsList(utility.parseSetToString(set));
        }else{
            saved = true;
            user.setSavedPostsList(user.getSavedPostsList()+postId+",");
        }
        userRepository.save(user);
        return saved ? "Saved sucessfully":"Removed successfully" ;
    }

    private void parsePosts(List<ResourcePost> resourcePostList){
        for(ResourcePost resourcePost:resourcePostList){
            parseIndividualPost(resourcePost);
        }
    }
    private User getUser(String email){
        User user = userRepository.findByEmail(email);
        if(user==null) throw  new ResourceNotFound("No such user");
        return user;
    }

    private ResourcePost getPost(Integer postId){
        Optional<ResourcePost> resourcePost = resourcePostRepository.findById(postId);
        if(!resourcePost.isPresent()) throw new BadRequestException("No such post");
        return resourcePost.get();
    }
    private void parseIndividualPost(ResourcePost resourcePost){
        List<PostImage> postImages = resourcePost.getPostImages();
        // convert blob to base64
        for(PostImage postImage:postImages){
            postImage.setBase64Image(new String(postImage.getImage()));
        }
        PostFeedBack postFeedBack = resourcePost.getPostFeedBack();
        postFeedBack.setCommentsList(utility.getComments(postFeedBack.getCommentsData()));
    }


}
