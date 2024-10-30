package com.SpringBoot.TestingHW.repository;

import com.SpringBoot.TestingHW.entity.bookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<bookEntity,Long> {
}
