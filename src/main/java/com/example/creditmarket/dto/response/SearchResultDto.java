package com.example.creditmarket.dto.response;

import com.example.creditmarket.entity.EntityOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SearchResultDto {
    private String companyName; // 은행
    private String productName; // 대출 상품
    private String productTypeName; // 대출 종류
    private Double avgInterest; // 평균 금리
    private String optionsInterestType;    // 금리 유형
    private String productId; // 상품 id
    private boolean favorite;

    public static SearchResultDto of(EntityOption option) {
        return SearchResultDto.builder()
                .companyName(option.getEntityFProduct().getFproduct_company_name())
                .productName(option.getEntityFProduct().getFproduct_name())
                .productTypeName(option.getEntityFProduct().getFproduct_credit_product_type_name())
                .avgInterest(option.getOptions_crdt_grad_avg())
                .optionsInterestType(option.getOptions_interest_type())
                .productId(option.getEntityFProduct().getFproduct_id())
                .build();
    }

    // 레디스 테스트시 참조 오류로 인해 임시로 만든 객체
//    public static SearchResultDto test(EntityOption option) {
//        return SearchResultDto.builder()
//                .build();
//    }
}
