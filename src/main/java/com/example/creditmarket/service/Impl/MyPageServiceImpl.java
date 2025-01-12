package com.example.creditmarket.service.Impl;

import com.example.creditmarket.common.AppException;
import com.example.creditmarket.common.ErrorCode;
import com.example.creditmarket.domain.entity.EntityFavorite;
import com.example.creditmarket.domain.entity.EntityOrder;
import com.example.creditmarket.domain.entity.EntityUser;
import com.example.creditmarket.domain.repository.FavoriteRepository;
import com.example.creditmarket.domain.repository.OptionRepository;
import com.example.creditmarket.domain.repository.OrderRepository;
import com.example.creditmarket.domain.repository.UserRepository;
import com.example.creditmarket.dto.response.FavoriteListResponseDTO;
import com.example.creditmarket.dto.response.FavoriteResponseDTO;
import com.example.creditmarket.dto.response.OrderListResponseDTO;
import com.example.creditmarket.dto.response.OrderResponseDTO;
import com.example.creditmarket.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final OptionRepository optionRepository;

    public FavoriteListResponseDTO selectFavoriteList(int page, String userEmail) {
        EntityUser user = userRepository.findById(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USERMAIL_NOT_FOUND, userEmail + " 존재하지 않는 회원입니다."));

        if (page < 1) {
            throw new AppException(ErrorCode.PAGE_INDEX_ZERO, "Page가 1보다 작습니다.");
        }
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by("favoriteId").descending());

        List<EntityFavorite> favorites = favoriteRepository.findByUser(user, pageRequest);

        List<FavoriteResponseDTO> list = new ArrayList<>();
        for (EntityFavorite favorite : favorites) {
            FavoriteResponseDTO dto = FavoriteResponseDTO.builder()
                    .favorite(favorite)
                    .option(optionRepository.findByProductId(favorite.getFproduct().getFproduct_id()))
                    .build();

            list.add(dto);
        }

        int totalNum = favoriteRepository.countByUser(user);


        return FavoriteListResponseDTO.builder()
                .list(list)
                .totalNum(totalNum)
                .build();
    }

    public OrderListResponseDTO selectOrderList(int page, String userEmail) {
        EntityUser user = userRepository.findById(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USERMAIL_NOT_FOUND, userEmail + " 존재하지 않는 회원입니다."));

        if (page < 1) {
            throw new AppException(ErrorCode.PAGE_INDEX_ZERO, "Page가 1보다 작습니다.");
        }
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by("orderId").descending());

        List<EntityOrder> orders = orderRepository.findByUser(user, pageRequest);

        List<OrderResponseDTO> list = new ArrayList<>();
        for (EntityOrder order : orders) {
            OrderResponseDTO dto = OrderResponseDTO.builder()
                    .order(order)
                    .option(optionRepository.findByProductId(order.getFproduct().getFproduct_id()))
                    .build();

            list.add(dto);
        }

        int totalNum = orderRepository.countByUser(user);

        return OrderListResponseDTO.builder()
                .list(list)
                .totalNum(totalNum)
                .build();
    }

    @Override
    public String updateOrder(Long orderId, String userEmail) {
        EntityUser user = userRepository.findById(userEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USERMAIL_NOT_FOUND, userEmail + " 존재하지 않는 회원입니다."));

        EntityOrder order = orderRepository.findByUserAndOrderId(user, orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND, orderId + " 존재하지 않는 구매 상품입니다."));

        order.updateOrderStatus(0);

        orderRepository.save(order);

        return "success";
    }
}
