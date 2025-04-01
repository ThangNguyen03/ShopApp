package com.project.shopapp.service;

import com.project.shopapp.dto.OrderDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.responses.OrderResponse;

import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO order) throws DataNotFoundException;
    Order getOrder(Long id);
    Order updateOrder(Long id, OrderDTO order);
    void deleteOrder(Long id);
    List<Order> findByUserId(Long userId);
}
