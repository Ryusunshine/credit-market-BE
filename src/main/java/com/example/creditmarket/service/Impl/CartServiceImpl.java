package com.example.creditmarket.service.Impl;

import com.example.creditmarket.common.AppException;
import com.example.creditmarket.common.ErrorCode;
import com.example.creditmarket.domain.entity.EntityCart;
import com.example.creditmarket.domain.entity.EntityFProduct;
import com.example.creditmarket.domain.entity.EntityUser;
import com.example.creditmarket.domain.repository.CartRepository;
import com.example.creditmarket.domain.repository.FProductRepository;
import com.example.creditmarket.domain.repository.FavoriteRepository;
import com.example.creditmarket.domain.repository.UserRepository;
import com.example.creditmarket.dto.request.CartDeleteRequestDTO;
import com.example.creditmarket.dto.request.CartSaveRequestDTO;
import com.example.creditmarket.dto.response.CartResponseDTO;
import com.example.creditmarket.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final FProductRepository productRepository;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;

    @Override
    public String saveCart(CartSaveRequestDTO cartRequestDTO, String userEmail) {
        EntityUser user = userRepository.findById(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USERMAIL_NOT_FOUND, userEmail + " 존재하지 않는 회원입니다."));

        String productId = cartRequestDTO.getProductId();
        EntityFProduct fProduct = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND, productId + " 존재하지 않는 상품입니다."));

        if (cartRepository.existsByUserAndFproduct(user, fProduct)) {
            return "isDupl";
        }

        EntityCart entityCart = EntityCart.builder()
                .user(user)
                .fproduct(fProduct)
                .build();

        cartRepository.save(entityCart);

        return "success";
    }

    @Override
    public List<CartResponseDTO> getCartList(String userEmail) {
        EntityUser user = userRepository.findById(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USERMAIL_NOT_FOUND, userEmail + " 존재하지 않는 회원입니다."));

        List<EntityCart> entityCartList = cartRepository.findByUserOrderByCartIdDesc(user);

        return entityCartList.stream()
                .map(this::checkedFavorite)
                .collect(Collectors.toList());
    }

    @Override
    public String deleteCart(CartDeleteRequestDTO cartDeleteRequestDTO, String userEmail) {
        EntityUser user = userRepository.findById(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USERMAIL_NOT_FOUND, userEmail + " 존재하지 않는 회원입니다."));

        List<EntityCart> entityCartList = cartDeleteRequestDTO.toEntity();

        entityCartList.forEach(entityCart -> cartRepository.findByUserAndCartId(user, entityCart.getCartId())
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND, entityCart.getCartId() + " 존재하지 않는 장바구니 상품입니다.")));

        cartRepository.deleteAll(entityCartList);

        return "success";
    }

    //장바구니에 관심 상품표시를 체크하는 메서드
    private CartResponseDTO checkedFavorite(EntityCart entityCart) {
        CartResponseDTO responseDTO = CartResponseDTO.builder()
                .entityCart(entityCart)
                .build();

        if (favoriteRepository.existsByUserAndFproduct(entityCart.getUser(), entityCart.getFproduct())) {
            responseDTO.setFavorite(true);
        }

        return responseDTO;
    }
}
