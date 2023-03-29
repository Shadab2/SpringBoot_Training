package com.oracle.oracle.training.repository;

import com.oracle.oracle.training.entity.post.PostFeedBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostFeedBackRepository extends JpaRepository<PostFeedBack,Integer> {
}
