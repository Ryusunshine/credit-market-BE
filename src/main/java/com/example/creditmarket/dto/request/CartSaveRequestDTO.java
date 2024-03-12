package com.example.creditmarket.dto.request;

import lombok.*;

@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CartSaveRequestDTO {
    private String productId;

}
