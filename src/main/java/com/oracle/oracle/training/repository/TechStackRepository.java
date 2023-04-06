package com.oracle.oracle.training.repository;

import com.oracle.oracle.training.entity.post.TechStack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechStackRepository extends JpaRepository<TechStack,Integer> {
    TechStack findByName(String name);

    TechStack findByNameIgnoreCase(String name);
}
