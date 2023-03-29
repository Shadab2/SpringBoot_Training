package com.oracle.oracle.training.repository;

import com.oracle.oracle.training.entity.post.ResourcePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourcePostRepository extends JpaRepository<ResourcePost,Integer> {
   List<ResourcePost> findByAccessLevel(String accessLevel);
}
