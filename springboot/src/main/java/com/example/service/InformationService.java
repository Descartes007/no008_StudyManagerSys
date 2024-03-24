package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.RecommendEnum;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.StatusEnum;
import com.example.entity.Account;
import com.example.entity.Fileorder;
import com.example.entity.Information;
import com.example.exception.CustomException;
import com.example.mapper.FileorderMapper;
import com.example.mapper.InformationMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

/**
 * 资料信息表业务处理
 **/
@Service
public class InformationService {

    @Resource
    private InformationMapper informationMapper;
    @Resource
    private FileorderMapper fileorderMapper;

    /**
     * 新增
     */
    public void add(Information information) {
        information.setTime(DateUtil.format(new Date(), "yyyy-MM-dd"));
        informationMapper.insert(information);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        informationMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            informationMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Information information) {
        if (StatusEnum.NO.status.equals(information.getStatus())) {
            information.setRecommend(RecommendEnum.NO.status);
        }
        if (RecommendEnum.YES.status.equals(information.getRecommend())) {
            // 校验一下有没有已经推荐的资料
            Information dbInformation = informationMapper.selectRecommend();
            if (ObjectUtil.isNotEmpty(dbInformation) && !dbInformation.getId().equals(information.getId())) {
                throw new CustomException(ResultCodeEnum.RECOMMEND_ALREADY_ERROR);
            }
        }
        informationMapper.updateById(information);
    }

    /**
     * 根据ID查询
     */
    public Information selectById(Integer id) {
        Information information = informationMapper.selectById(id);

        Account currentUser = TokenUtils.getCurrentUser();
        Fileorder fileorder = new Fileorder();
        fileorder.setUserId(currentUser.getId());
        fileorder.setFileId(information.getId());
        List<Fileorder> fileorders = fileorderMapper.selectAll(fileorder);
        if (CollectionUtil.isEmpty(fileorders)) {
            information.setFile("");
        }
        return information;
    }

    /**
     * 查询所有
     */
    public List<Information> selectAll(Information information) {
        return informationMapper.selectAll(information);
    }

    /**
     * 分页查询
     */
    public PageInfo<Information> selectPage(Information information, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Information> list = informationMapper.selectAll(information);
        return PageInfo.of(list);
    }

    public Information getRecommend() {
        return informationMapper.selectRecommend();
    }

    public List<Information> selectTop8() {
        return informationMapper.selectTop8();
    }
}