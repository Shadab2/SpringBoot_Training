package com.oracle.oracle.training.services.interfaces;

import com.oracle.oracle.training.entity.post.Comments;
import com.oracle.oracle.training.entity.post.PostImage;
import com.oracle.oracle.training.entity.post.ResourcePost;
import com.oracle.oracle.training.exceptions.AccessDeniedException;
import com.oracle.oracle.training.exceptions.ResourceNotFound;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ResourcePostService {
    ResourcePost savePost(String email, ResourcePost resourcePost, Optional<MultipartFile[]> files);

    List<ResourcePost> getAll(String email) throws ResourceNotFound;

    ResourcePost getPost(String email, Integer postId);

    String upvotePost(String email,Integer postId);

    List<ResourcePost> getAllLikedPost(String email);

    Comments addComment(String email, Integer postId, Comments comment);

    List<ResourcePost> getTrendingPosts(String email);

    List<ResourcePost> getAllUserPost(String email);
    List<ResourcePost> getAllUserSavedPost(String email);

    List<ResourcePost> search(String email, Map<String, Object> requestMap);

    List<PostImage> getAllImages(String email);

    String savePostForUser(String email, Integer postId);

    Map<String, Set<Integer>> getUserMappings(String email);
}
