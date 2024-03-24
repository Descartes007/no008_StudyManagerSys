package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.Account;
import com.example.entity.Scoreorder;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.ScoreorderMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 积分兑换表业务处理
 **/
@Service
public class ScoreorderService {

    @Resource
    private ScoreorderMapper scoreorderMapper;
    @Resource
    private UserMapper userMapper;

    /**
     * 新增
     */
    public void add(Scoreorder scoreorder) {
        // 判断用户积分够不够
        User user = userMapper.selectById(scoreorder.getUserId());
        if (user.getScore() < scoreorder.getScore()) {
            throw new CustomException(ResultCodeEnum.SCORE_LOWER_ERROR);
        }
        scoreorder.setTime(DateUtil.now());
        scoreorder.setOrderId(DateUtil.format(new Date(), "yyyyMMddHHmmss"));
        scoreorderMapper.insert(scoreorder);

        // 扣除用户积分
        user.setScore(user.getScore() - scoreorder.getScore());
        userMapper.updateById(user);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        scoreorderMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            scoreorderMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Scoreorder scoreorder) {
        scoreorderMapper.updateById(scoreorder);
    }

    /**
     * 根据ID查询
     */
    public Scoreorder selectById(Integer id) {
        return scoreorderMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Scoreorder> selectAll(Scoreorder scoreorder) {
        return scoreorderMapper.selectAll(scoreorder);
    }

    /**
     * 分页查询
     */
    public PageInfo<Scoreorder> selectPage(Scoreorder scoreorder, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Scoreorder> list = scoreorderMapper.selectAll(scoreorder);
        return PageInfo.of(list);
    }

}