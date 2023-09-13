package com.ey.codesara.OrderService.service;

import com.ey.codesara.OrderService.entity.Order;
import com.ey.codesara.OrderService.extenal.client.ProductService;
import com.ey.codesara.OrderService.model.OrderRequest;
import com.ey.codesara.OrderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class OrederServiceImpl implements OrederService{
    private OrderRepository orderRepository;

    private ProductService productService;

//    @Autowired
//    public OrederServiceImpl(ProductService productService) {
//        this.productService = productService;
//    }

    @Autowired
    public OrederServiceImpl(OrderRepository orderRepository, ProductService productService) {

        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    @Override
    public long placeOrder(OrderRequest orderRequest) {
        //Order Entity -> save the Data with Status Order created
        log.info("Placing Order Request: {}", orderRequest);
        Order order = Order.builder()
                .amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();

        order = orderRepository.save(order);
        log.info("Order Places successfully with Order Id: {}", order.getId());

        //Product Service -> Block Products(Reduce the Quantity)
        productService.reduceQuantity(orderRequest.getProductId(),orderRequest.getQuantity());
        log.info(" Creating Order with Status CREATED ");
        //Payment Service -> Payment-SUCCESS-> COMPLETE, Else ->CANCELLED
        return order.getId();
    }
}
