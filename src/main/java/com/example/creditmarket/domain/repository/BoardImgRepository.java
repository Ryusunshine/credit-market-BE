package com.example.creditmarket.domain.repository;


import com.example.creditmarket.domain.entity.EntityBoard;
import com.example.creditmarket.domain.entity.EntityBoardImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardImgRepository extends JpaRepository<EntityBoardImg, Long> {

    Optional<EntityBoardImg> findByBoard(EntityBoard board);
}