package com.example.creditmarket.dto.response;

import com.example.creditmarket.entity.EntityCart;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CartResponseDTO {

    private Long cartId;

    private String companyName;

    private String productName;

    private String productId;

    private boolean favorite;

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Builder
    public CartResponseDTO(EntityCart entityCart) {
        this.cartId = entityCart.getCartId();
        this.companyName = entityCart.getFproduct().getFproduct_company_name();
        this.productName = entityCart.getFproduct().getFproduct_name();
        this.productId = entityCart.getFproduct().getFproduct_id();
    }
}
