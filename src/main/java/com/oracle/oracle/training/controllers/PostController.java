package com.oracle.oracle.training.controllers;

import com.oracle.oracle.training.entity.post.Comments;
import com.oracle.oracle.training.entity.post.PostImage;
import com.oracle.oracle.training.entity.post.ResourcePost;
import com.oracle.oracle.training.services.interfaces.ResourcePostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    ResourcePostService resourcePostService;


    @GetMapping
    public ResponseEntity<List<ResourcePost>> gellAllPostsOfUser(HttpServletRequest request){
        String email = (String) request.getAttribute("email");
        return  new ResponseEntity<>(resourcePostService.getAllUserPost(email),HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<List<ResourcePost>> getAllPosts(HttpServletRequest request){
        String email = (String)request.getAttribute("email");
        return new ResponseEntity<>(resourcePostService.getAll(email),HttpStatus.OK);
    }
    @GetMapping("/trending")
    public ResponseEntity<List<ResourcePost>> getTrendingPosts(HttpServletRequest request){
        String email = (String)request.getAttribute("email");
        return new ResponseEntity<>(resourcePostService.getTrendingPosts(email),HttpStatus.OK);
    }

    @GetMapping("/all/upvoted")
    public ResponseEntity<List<ResourcePost>> getAllUpvotedPosts(HttpServletRequest request){
        String email = (String)request.getAttribute("email");
        return new ResponseEntity<>(resourcePostService.getAllLikedPost(email),HttpStatus.OK);
    }
    @GetMapping("/all/saved")
    public ResponseEntity<List<ResourcePost>> getAllUserSavedPosts(HttpServletRequest request){
        String email = (String)request.getAttribute("email");
        return new ResponseEntity<>(resourcePostService.getAllUserSavedPost(email),HttpStatus.OK);
    }
    @GetMapping("/all/images")
    public ResponseEntity<List<PostImage>> getAllImages(HttpServletRequest request){
        String email = (String)request.getAttribute("email");
        return new ResponseEntity<>(resourcePostService.getAllImages(email),HttpStatus.OK);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResourcePost> savePost(HttpServletRequest request, @RequestPart ResourcePost resourcePost,@RequestPart("files") Optional<MultipartFile[]> files){
        String email = (String)request.getAttribute("email");
        return new ResponseEntity<>(resourcePostService.savePost(email,resourcePost,files), HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ResourcePost> getPost(HttpServletRequest request,@PathVariable("postId") Integer postId){
        String email = (String) request.getAttribute("email");
        return new ResponseEntity<>(resourcePostService.getPost(email,postId),HttpStatus.OK);
    }

    @PostMapping("/{postId}/upvote")
    public ResponseEntity<String> upvotePost(HttpServletRequest request,@PathVariable("postId") Integer postId){
        String email = (String)request.getAttribute("email");
        return new ResponseEntity<>(resourcePostService.upvotePost(email,postId),HttpStatus.OK);
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<Comments> addComment(HttpServletRequest request, @PathVariable("postId") Integer postId, @RequestBody Comments comment){
        String email = (String)request.getAttribute("email");
        return new ResponseEntity<>(resourcePostService.addComment(email,postId,comment),HttpStatus.OK);
    }

    @PostMapping("/{postId}/save")
    public ResponseEntity<String> savePostForUser(HttpServletRequest request,@PathVariable("postId") Integer postId){
        String email = (String) request.getAttribute("email");
        return new ResponseEntity<>(resourcePostService.savePostForUser(email,postId),HttpStatus.OK);
    }
    @PostMapping("/search")
    public  ResponseEntity<List<ResourcePost>> searchPosts(HttpServletRequest request,@RequestBody Map<String,Object> requestMap){
        String email = (String)request.getAttribute("email");
        return new ResponseEntity<>(resourcePostService.search(email,requestMap),HttpStatus.OK);
    }

    @GetMapping("/user-mappings")
    public ResponseEntity<Map<String, Set<Integer>>> getUserMappings(HttpServletRequest request){
        String email = (String) request.getAttribute("email");
        return new ResponseEntity<>(resourcePostService.getUserMappings(email),HttpStatus.OK);
    }


}
