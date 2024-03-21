package com.example.creditmarket.domain.repository;

import com.example.creditmarket.domain.entity.EntityBoard;
import com.example.creditmarket.domain.entity.EntityComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentRepository extends JpaRepository<EntityComment, Integer> {

    Page<EntityComment> findAllByBoard(EntityBoard board, Pageable pageable);

}

