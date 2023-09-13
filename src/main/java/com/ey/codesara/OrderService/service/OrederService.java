package com.ey.codesara.OrderService.service;

import com.ey.codesara.OrderService.model.OrderRequest;

public interface OrederService {

    long placeOrder(OrderRequest orderRequest);
}
