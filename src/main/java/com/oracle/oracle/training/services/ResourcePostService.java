package com.oracle.oracle.training.services;

import com.oracle.oracle.training.entity.post.Comments;
import com.oracle.oracle.training.entity.post.ResourcePost;
import com.oracle.oracle.training.exceptions.AccessDeniedException;
import com.oracle.oracle.training.exceptions.ResourceNotFound;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ResourcePostService {
    ResourcePost savePost(String email, ResourcePost resourcePost, Optional<MultipartFile[]> files);

    List<ResourcePost> getAll(String email) throws ResourceNotFound;

    ResourcePost getPost(String email, Integer postId);

    String upvotePost(String email,Integer postId);

    List<ResourcePost> getAllLikedPost(String email);

    Comments addComment(String email, Integer postId, Comments comment);
}
