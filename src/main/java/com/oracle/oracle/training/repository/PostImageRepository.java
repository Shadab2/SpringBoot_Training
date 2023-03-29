package com.oracle.oracle.training.repository;

import com.oracle.oracle.training.entity.post.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage,Integer> {
}
