package com.ming.po;

import java.util.List;

public class Order {
    private Integer id;
    private Integer userId;
    private Long createTime;
    private Long upTime;
    //订单详情列表
    private List<OrderDetail> orderDetailModelList;

    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpTime() {
        return upTime;
    }

    public void setUpTime(Long upTime) {
        this.upTime = upTime;
    }

    public List<OrderDetail> getOrderDetailModelList() {
        return orderDetailModelList;
    }

    public void setOrderDetailModelList(List<OrderDetail> orderDetailModelList) {
        this.orderDetailModelList = orderDetailModelList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", createTime=" + createTime +
                ", upTime=" + upTime +
                ", orderDetailModelList=" + orderDetailModelList +
                ", user=" + user +
                '}';
    }
}
