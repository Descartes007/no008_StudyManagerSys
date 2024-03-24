package com.example.mapper;

import com.example.entity.Scoreorder;

import java.util.List;

/**
 * 操作scoreorder相关数据接口
*/
public interface ScoreorderMapper {

    /**
      * 新增
    */
    int insert(Scoreorder scoreorder);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Scoreorder scoreorder);

    /**
      * 根据ID查询
    */
    Scoreorder selectById(Integer id);

    /**
      * 查询所有
    */
    List<Scoreorder> selectAll(Scoreorder scoreorder);

}