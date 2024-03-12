package com.example.creditmarket.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tb_favorite")
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class EntityFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email")
    private EntityUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fproduct_id")
    private EntityFProduct fproduct;

    @Builder
    public EntityFavorite(EntityUser user, EntityFProduct product){
        this.user = user;
        this.fproduct = product;
    }
}