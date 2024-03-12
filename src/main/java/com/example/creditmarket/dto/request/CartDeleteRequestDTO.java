package com.example.creditmarket.dto.request;

import com.example.creditmarket.entity.EntityCart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDeleteRequestDTO {

    List<Long> cartIds;

    public List<EntityCart> toEntity() {
        return cartIds.stream()
                .map(cartId -> EntityCart.builder()
                        .cartId(cartId)
                        .build())
                .collect(Collectors.toList());
    }
}
