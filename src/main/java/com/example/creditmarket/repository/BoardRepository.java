package com.example.creditmarket.repository;

import com.example.creditmarket.entity.EntityBoard;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BoardRepository extends JpaRepository<EntityBoard, Long> {

}