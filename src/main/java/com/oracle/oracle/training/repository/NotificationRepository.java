package com.oracle.oracle.training.repository;

import com.oracle.oracle.training.entity.Notificaton;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  NotificationRepository extends JpaRepository<Notificaton,Integer> {
    List<Notificaton> findByReceiverEmail(String email);
}
