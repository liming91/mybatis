package com.ming.dao;

import com.ming.po.Order;

public interface OrderMapper {
    Order findOrderListById(Integer id);

    Order findOneOrderById(Integer orderId);
}
