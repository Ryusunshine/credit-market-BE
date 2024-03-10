package com.example.creditmarket.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_order")
@Getter
@Builder
@RequiredArgsConstructor
public class EntityOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private Integer orderStatus;

    private LocalDateTime orderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email")
    private EntityUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fproduct_id")
    private EntityFProduct fproduct;

    @Builder
    public EntityOrder(Long orderId, int orderStatus, LocalDateTime orderDate, EntityUser user, EntityFProduct product){
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.user = user;
        this.fproduct = product;
    }

    public void updateOrderStatus(Integer status){
        this.orderStatus = status;
    }
}
