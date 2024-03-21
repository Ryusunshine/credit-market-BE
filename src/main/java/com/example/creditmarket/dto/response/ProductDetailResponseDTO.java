package com.example.creditmarket.dto.response;

import com.example.creditmarket.domain.entity.EntityFProduct;
import com.example.creditmarket.domain.entity.EntityOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProductDetailResponseDTO { //상품명, 개요, 대상, 한도, 금리 등의 상세정보 출력
    private String productId;
    private String productName;
    private String companyName;
    private String productTypeName;
    private Double avgInterest; // 평균 금리
    private String optionsInterestType; //금리 구분
    private String productJoinMethod;
    private boolean favorite;

    public static ProductDetailResponseDTO of(EntityFProduct product, EntityOption option){
        ProductDetailResponseDTOBuilder dto = ProductDetailResponseDTO.builder()
                .companyName(product.getFproduct_company_name())
                .productId(product.getFproduct_id())
                .productTypeName(product.getFproduct_credit_product_type_name())
                .productJoinMethod(product.getFproduct_join_method())
                .productName(product.getFproduct_name());
        if (option != null){
            dto.avgInterest(option.getOptions_crdt_grad_avg());
            dto.optionsInterestType(option.getOptions_interest_type());
        }
        return dto.build();
    }

    public static ProductDetailResponseDTO productWithFavor (EntityFProduct product, EntityOption option, boolean isFavorite){
        ProductDetailResponseDTOBuilder dto = ProductDetailResponseDTO.builder()
                .companyName(product.getFproduct_company_name())
                .productId(product.getFproduct_id())
                .productTypeName(product.getFproduct_credit_product_type_name())
                .productJoinMethod(product.getFproduct_join_method())
                .productName(product.getFproduct_name())
                .favorite(isFavorite);
        if (option != null){
            dto.avgInterest(option.getOptions_crdt_grad_avg());
            dto.optionsInterestType(option.getOptions_interest_type());
        }
        return dto.build();
    }
}
