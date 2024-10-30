package com.SpringBoot.TestingHW.repository;

import com.SpringBoot.TestingHW.entity.AuthorEntity;
import com.SpringBoot.TestingHW.entity.bookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<bookEntity,Long> {
    List<bookEntity> findByPublishedOnAfter(LocalDate date);

    List<bookEntity> findByTitle(String upperCase);

    List<bookEntity> findByAuthoredBy(AuthorEntity author);
}
