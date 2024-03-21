package com.example.creditmarket.service.Impl;

import com.example.creditmarket.common.AppException;
import com.example.creditmarket.common.ErrorCode;
import com.example.creditmarket.domain.entity.*;
import com.example.creditmarket.domain.repository.*;
import com.example.creditmarket.dto.request.OrderSaveRequestDTO;
import com.example.creditmarket.dto.response.ProductDetailResponseDTO;
import com.example.creditmarket.dto.response.RecommendResponseDTO;
import com.example.creditmarket.service.ProductService;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final FProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OptionRepository optionRepository;
    private final FavoriteRepository favoriteRepository;

    @Value("${jwt.token.secret}")
    private String secretKey;

    /**
     * 상품 상세정보 출력(상품명, 개요, 대상, 한도, 금리, 찜 여부 등의 상세정보 출력)
     */
    public ProductDetailResponseDTO getProductDetail(String id, HttpServletRequest request) {
        EntityFProduct product = productRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("상품을 찾을수 없습니다"));
        EntityOption option = optionRepository.findByProductId(id);

        if (getEmailFromToken(request) == null) {
            return ProductDetailResponseDTO.productWithFavor(product, option, false);
        }

        String userEmail = getEmailFromToken(request);
        EntityUser user = userRepository.findByUserEmail(userEmail).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디를 찾을수 없습니다."));
        boolean isFavorite = favoriteRepository.existsByUserAndFproduct(user, product);

        return ProductDetailResponseDTO.productWithFavor(product, option, isFavorite);
    }

    /**
     * 상품 구매
     */
    @Transactional
    public String buyProduct(OrderSaveRequestDTO requestDTO, String userEmail) {
        EntityUser user = userRepository.findByUserEmail(userEmail).orElseThrow(
                () -> new AppException(ErrorCode.USERMAIL_NOT_FOUND, "사용자가 존재하지 않습니다."));

        List<EntityFProduct> products = requestDTO.getProductIds().stream()
                .map(productId -> productRepository.findById(productId).orElseThrow(
                        () -> new AppException(ErrorCode.PRODUCT_NOT_FOUND, "상품이 존재하지 않습니다.")))
                .collect(Collectors.toList());

        products.forEach(product -> {
            EntityOrder order = EntityOrder.builder()
                        .product(product)
                        .orderStatus(1)
                        .user(user)
                        .build();
            try {
                orderRepository.save(order);

            } catch (Exception e){
                throw new AppException(ErrorCode.DATABASE_ERROR, "일시적인 오류입니다.");
            }
        });
        return "success";
    }

    /**
     * 추천 게시글
     */
    @Transactional(readOnly = true)
    public List<RecommendResponseDTO> recommendList(HttpServletRequest request) {
        List<RecommendResponseDTO> list = new ArrayList<>();
        if (getEmailFromToken(request) == null) {
            return null;
        }
        String userEmail = getEmailFromToken(request);
        EntityUser user = userRepository.findById(userEmail).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디를 찾을수 없습니다."));
        List<EntityFProduct> products = productRepository.findProductsByUserPref(user.getUserPrefCreditProductTypeName());

        for (EntityFProduct pr : products) {
            EntityOption op = optionRepository.findOptionByProductIdAndType(pr.getFproduct_id(), user.getUserPrefInterestType());
            boolean isFavorite = favoriteRepository.existsByUserAndFproduct(user, pr);

            if (op != null) {
                list.add(new RecommendResponseDTO(pr, op, isFavorite));
            }
        }
        return list;
    }

    /**
     * 찜 추가 & 취소
     */
    @Transactional
    public String favoriteService(String productId, String userEmail) {
        EntityFProduct product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품을 찾을수 없습니다"));
        EntityUser user = userRepository.findById(userEmail).orElseThrow(
                () -> new IllegalArgumentException("해당 회원을 찾을수 없습니다."));

        EntityFavorite favorite = favoriteRepository.findEntityFavoriteByFproductAndUser(product, user);
        if (favorite == null) {
            EntityFavorite newFavorite = EntityFavorite.builder()
                    .fproduct(product)
                    .user(user)
                    .build();

            try {
                favoriteRepository.save(newFavorite);
            } catch (Exception e){
                throw new AppException(ErrorCode.DATABASE_ERROR, "일시적인 오류입니다.");
            }

        } else {
            favoriteRepository.deleteById(favorite.getFavoriteId());
        }
        return "success";
    }

    @Transactional(readOnly = true)
    public String getEmailFromToken(HttpServletRequest request) {
        try {
            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            String token = authorization.split(" ")[1].trim();
            ;
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                    .getBody().get("userEmail", String.class);
        } catch (Exception e) {
            return null;
        }
    }
}
