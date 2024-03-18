package com.example.creditmarket.repository;


import com.example.creditmarket.entity.EntityBoard;
import com.example.creditmarket.entity.EntityBoardImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardImgRepository extends JpaRepository<EntityBoardImg, Long> {

    Optional<EntityBoardImg> findByBoard(EntityBoard board);
}