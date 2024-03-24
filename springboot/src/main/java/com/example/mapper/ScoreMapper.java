package com.example.mapper;

import com.example.entity.Score;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作score相关数据接口
*/
public interface ScoreMapper {

    /**
      * 新增
    */
    int insert(Score score);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Score score);

    /**
      * 根据ID查询
    */
    Score selectById(Integer id);

    /**
      * 查询所有
    */
    List<Score> selectAll(Score score);

    @Select("select * from score where recommend = '是'")
    Score selectRecommend();

    @Select("select * from score where recommend = '否' order by id desc limit 8")
    List<Score> selectTop8();

}