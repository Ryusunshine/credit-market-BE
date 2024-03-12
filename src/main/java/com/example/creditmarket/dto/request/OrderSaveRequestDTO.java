package com.example.creditmarket.dto.request;

import lombok.*;

import java.util.List;

@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderSaveRequestDTO {

    List<String> productIds;

}
