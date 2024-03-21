package com.example.creditmarket.domain.repository;

import com.example.creditmarket.domain.entity.EntityAlarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface AlarmRepository  extends JpaRepository<EntityAlarm, Long>, JpaSpecificationExecutor<EntityAlarm> {

    Page<EntityAlarm> findAllByUserUserId(Long userId, Pageable pageable);
}
