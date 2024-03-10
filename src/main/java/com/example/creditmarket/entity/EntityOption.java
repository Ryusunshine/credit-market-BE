package com.example.creditmarket.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tb_fpoption")
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class EntityOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long option_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fproduct_id")
    private EntityFProduct entityFProduct;

    private String options_interest_code;

    private String options_interest_type;

    private Double options_crdt_grad_1;

    private Double options_crdt_grad_4;

    private Double options_crdt_grad_5;

    private Double options_crdt_grad_6;

    private Double options_crdt_grad_10;

    private Double options_crdt_grad_11;

    private Double options_crdt_grad_12;

    private Double options_crdt_grad_13;

    private Double options_crdt_grad_avg;
}
