package com.example.creditmarket.dto.response;

import com.example.creditmarket.entity.EntityFavorite;
import com.example.creditmarket.entity.EntityOption;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FavoriteResponseDTO {
    private final Long favoriteId;
    private final String companyName;
    private final String productName;
    private final String productTypeName;
    private final String productId;
    private final Double avgInterest;
    private final String optionsInterestType;

    @Builder
    public FavoriteResponseDTO(EntityFavorite favorite, EntityOption option) {
        this.favoriteId = favorite.getFavoriteId();
        this.companyName = favorite.getFproduct().getFproduct_company_name();
        this.productName = favorite.getFproduct().getFproduct_name();
        this.productTypeName = favorite.getFproduct().getFproduct_credit_product_type_name();
        this.productId = favorite.getFproduct().getFproduct_id();
        this.avgInterest = option.getOptions_crdt_grad_avg();
        this.optionsInterestType = option.getOptions_interest_type();
    }
}
