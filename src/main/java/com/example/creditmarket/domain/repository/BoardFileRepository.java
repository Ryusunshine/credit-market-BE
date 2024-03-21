package com.example.creditmarket.domain.repository;

import com.example.creditmarket.domain.entity.EntityBoard;
import com.example.creditmarket.domain.entity.EntityBoardFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BoardFileRepository extends JpaRepository<EntityBoardFile, Long>, JpaSpecificationExecutor<EntityBoardFile> {

    Optional<EntityBoardFile> findById(Long id);

    Optional<EntityBoardFile> findByBoard(EntityBoard board);
}