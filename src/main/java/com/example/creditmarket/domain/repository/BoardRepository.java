package com.example.creditmarket.domain.repository;

import com.example.creditmarket.domain.entity.EntityBoard;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BoardRepository extends JpaRepository<EntityBoard, Long> {

}