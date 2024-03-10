package com.example.creditmarket.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tb_cart")
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class EntityCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email")
    private EntityUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fproduct_id")
    private EntityFProduct fproduct;
}
