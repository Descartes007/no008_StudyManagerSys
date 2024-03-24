package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.Fileorder;
import com.example.entity.Information;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.FileorderMapper;
import com.example.mapper.InformationMapper;
import com.example.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 资料订单表业务处理
 **/
@Service
public class FileorderService {

    @Resource
    private FileorderMapper fileorderMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private InformationMapper informationMapper;

    /**
     * 新增
     */
    public void add(Fileorder fileorder) {
        // 判断用户积分够不够
        User user = userMapper.selectById(fileorder.getUserId());
        if (user.getScore() < fileorder.getScore()) {
            throw new CustomException(ResultCodeEnum.SCORE_LOWER_ERROR);
        }
        fileorder.setTime(DateUtil.now());
        fileorder.setOrderId(DateUtil.format(new Date(), "yyyyMMddHHmmss"));
        fileorderMapper.insert(fileorder);

        // 扣除用户积分
        user.setScore(user.getScore() - fileorder.getScore());
        userMapper.updateById(user);

        // 增加原作者用户的积分
        Information information = informationMapper.selectById(fileorder.getFileId());
        User dbUser = userMapper.selectById(information.getUserId());
        dbUser.setScore(dbUser.getScore() + fileorder.getScore());
        userMapper.updateById(dbUser);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        fileorderMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            fileorderMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Fileorder fileorder) {
        fileorderMapper.updateById(fileorder);
    }

    /**
     * 根据ID查询
     */
    public Fileorder selectById(Integer id) {
        return fileorderMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Fileorder> selectAll(Fileorder fileorder) {
        return fileorderMapper.selectAll(fileorder);
    }

    /**
     * 分页查询
     */
    public PageInfo<Fileorder> selectPage(Fileorder fileorder, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Fileorder> list = fileorderMapper.selectAll(fileorder);
        return PageInfo.of(list);
    }

}