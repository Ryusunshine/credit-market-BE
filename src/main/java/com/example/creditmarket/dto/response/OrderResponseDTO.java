package com.example.creditmarket.dto.response;

import com.example.creditmarket.domain.entity.EntityOption;
import com.example.creditmarket.domain.entity.EntityOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OrderResponseDTO {
    private final Long orderId;
    private final int orderStatus;
    private final String companyName;
    private final String productName;
    private final String productTypeName;
    private final String productId;
    private final Double avgInterest;
    private final String optionsInterestType;

    @Builder
    public OrderResponseDTO(EntityOrder order, EntityOption option) {
        this.orderId = order.getOrderId();
        this.orderStatus = order.getOrderStatus();
        this.companyName = order.getFproduct().getFproduct_company_name();
        this.productName = order.getFproduct().getFproduct_name();
        this.productTypeName = order.getFproduct().getFproduct_credit_product_type_name();
        this.productId = order.getFproduct().getFproduct_id();
        this.avgInterest = option.getOptions_crdt_grad_avg();
        this.optionsInterestType = option.getOptions_interest_type();
    }
}
