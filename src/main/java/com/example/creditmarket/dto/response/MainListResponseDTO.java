package com.example.creditmarket.dto.response;

import com.example.creditmarket.domain.entity.EntityOption;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class MainListResponseDTO extends SearchResultDto{
    private final boolean favorite;   // 관심상품 등록 여부

    public static MainListResponseDTO of (EntityOption option, boolean favorite){
        return MainListResponseDTO.builder()
                .companyName(option.getEntityFProduct().getFproduct_company_name())
                .productName(option.getEntityFProduct().getFproduct_name())
                .productTypeName(option.getEntityFProduct().getFproduct_credit_product_type_name())
                .avgInterest(option.getOptions_crdt_grad_avg())
                .optionsInterestType(option.getOptions_interest_type())
                .productId(option.getEntityFProduct().getFproduct_id())
                .favorite(favorite)
                .build();
    }

    public static MainListResponseDTO searchDtoToMainListDto (SearchResultDto dto){
        return MainListResponseDTO.builder()
                .companyName(dto.getCompanyName())
                .productName(dto.getProductName())
                .productTypeName(dto.getProductTypeName())
                .avgInterest(dto.getAvgInterest())
                .optionsInterestType(dto.getOptionsInterestType())
                .productId(dto.getProductId())
                .build();
    }

    public static MainListResponseDTO entityToDto (EntityOption option){
        return MainListResponseDTO.builder()
                .companyName(option.getEntityFProduct().getFproduct_company_name())
                .productName(option.getEntityFProduct().getFproduct_name())
                .productTypeName(option.getEntityFProduct().getFproduct_credit_product_type_name())
                .avgInterest(option.getOptions_crdt_grad_avg())
                .optionsInterestType(option.getOptions_interest_type())
                .productId(option.getEntityFProduct().getFproduct_id())
                .build();
    }

}
