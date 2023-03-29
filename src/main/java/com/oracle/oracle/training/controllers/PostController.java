package com.oracle.oracle.training.controllers;

import com.oracle.oracle.training.entity.post.ResourcePost;
import com.oracle.oracle.training.services.ResourcePostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    ResourcePostService resourcePostService;
    @GetMapping("/all")
    public ResponseEntity<List<ResourcePost>> getAllPosts(HttpServletRequest request){
        String email = (String)request.getAttribute("email");
        return new ResponseEntity<>(resourcePostService.getAll(email),HttpStatus.OK);
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
        resourcePostService.upvotePost(email,postId);
        return new ResponseEntity<>("post upvoted successfully",HttpStatus.OK);
    }
}
