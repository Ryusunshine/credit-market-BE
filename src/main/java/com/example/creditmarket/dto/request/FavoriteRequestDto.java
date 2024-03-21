package com.example.creditmarket.dto.request;

import com.example.creditmarket.domain.entity.EntityFProduct;
import com.example.creditmarket.domain.entity.EntityFavorite;
import com.example.creditmarket.domain.entity.EntityUser;
import lombok.*;

@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FavoriteRequestDto {
    private EntityUser user;
    private EntityFProduct product;

    @Builder
    public EntityFavorite toEntity(EntityUser user, EntityFProduct product){
        return EntityFavorite.builder()
                .user(user)
                .fproduct(product)
                .build();
    }
}
