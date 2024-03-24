package com.example.mapper;

import com.example.entity.Information;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作information相关数据接口
*/
public interface InformationMapper {

    /**
      * 新增
    */
    int insert(Information information);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Information information);

    /**
      * 根据ID查询
    */
    Information selectById(Integer id);

    /**
      * 查询所有
    */
    List<Information> selectAll(Information information);

    @Select("select * from information where recommend = '是' and status = '审核通过'")
    Information selectRecommend();

    @Select("select * from information where recommend = '否' and status = '审核通过' order by id desc limit 8")
    List<Information> selectTop8();
}