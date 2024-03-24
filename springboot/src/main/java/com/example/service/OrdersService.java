package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.MemberEnum;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.Account;
import com.example.entity.Course;
import com.example.entity.Orders;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.CourseMapper;
import com.example.mapper.OrdersMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 课程订单表业务处理
 **/
@Service
public class OrdersService {

    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CourseMapper courseMapper;

    /**
     * 新增
     */
    public void add(Orders orders) {
        orders.setTime(DateUtil.now());
        orders.setOrderId(DateUtil.format(new Date(), "yyyyMMddHHmmss"));

        // 计算价格
        Account currentUser = TokenUtils.getCurrentUser();
        User user = userMapper.selectById(currentUser.getId());
        Course course = courseMapper.selectById(orders.getCourseId());
        Double price = course.getPrice();
        if (MemberEnum.YES.info.equals(user.getMember())) {
            // 享受折扣价
            price = course.getPrice() * course.getDiscount();
        }
        // 判断余额够不够
        if (user.getAccount() < price) {
            throw new CustomException(ResultCodeEnum.ACCOUNT_LOWER_ERROR);
        }
        orders.setPrice(price);
        // 创建订单记录
        ordersMapper.insert(orders);
        // 扣除用于余额
        user.setAccount(user.getAccount() - price);
        userMapper.updateById(user);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        ordersMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            ordersMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Orders orders) {
        ordersMapper.updateById(orders);
    }

    /**
     * 根据ID查询
     */
    public Orders selectById(Integer id) {
        return ordersMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Orders> selectAll(Orders orders) {
        return ordersMapper.selectAll(orders);
    }

    /**
     * 分页查询
     */
    public PageInfo<Orders> selectPage(Orders orders, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Orders> list = ordersMapper.selectAll(orders);
        return PageInfo.of(list);
    }

}