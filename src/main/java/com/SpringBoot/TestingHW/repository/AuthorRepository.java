package com.SpringBoot.TestingHW.repository;

import com.SpringBoot.TestingHW.entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity,Long> {
    List<AuthorEntity> findByName(String upperCase);
}
