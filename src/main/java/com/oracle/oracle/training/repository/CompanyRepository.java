package com.oracle.oracle.training.repository;

import com.oracle.oracle.training.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public  interface CompanyRepository extends JpaRepository<Company,Integer>{
    boolean existsByName(String name);

    Company findByName(String name);
}