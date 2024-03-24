package com.example.mapper;

import com.example.entity.Fileorder;

import java.util.List;

/**
 * 操作fileorder相关数据接口
*/
public interface FileorderMapper {

    /**
      * 新增
    */
    int insert(Fileorder fileorder);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Fileorder fileorder);

    /**
      * 根据ID查询
    */
    Fileorder selectById(Integer id);

    /**
      * 查询所有
    */
    List<Fileorder> selectAll(Fileorder fileorder);

}