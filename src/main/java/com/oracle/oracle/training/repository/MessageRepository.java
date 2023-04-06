package com.oracle.oracle.training.repository;

import com.oracle.oracle.training.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message,Integer> {
}
