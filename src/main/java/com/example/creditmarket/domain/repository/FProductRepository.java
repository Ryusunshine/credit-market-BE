package com.example.creditmarket.domain.repository;

import com.example.creditmarket.domain.entity.EntityFProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FProductRepository extends JpaRepository<EntityFProduct, String> {

    @Query(value = "select * from tb_fproduct where fproduct_credit_product_type_name = :type", nativeQuery = true)
    List<EntityFProduct> findProductsByUserPref(@Param("type") String type);
}
