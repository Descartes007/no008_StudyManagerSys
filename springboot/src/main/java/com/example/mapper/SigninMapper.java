package com.example.mapper;

import com.example.entity.Signin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作signin相关数据接口
*/
public interface SigninMapper {

    /**
      * 新增
    */
    int insert(Signin signin);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Signin signin);

    /**
      * 根据ID查询
    */
    Signin selectById(Integer id);

    /**
      * 查询所有
    */
    List<Signin> selectAll(Signin signin);

    @Select("select * from signin where user_id = #{userId}")
    Signin selectByUserId(Integer userId);

    @Select("select * from signin where user_id = #{userId} and day = #{day}")
    Signin selectByUserIdAndDay(@Param("userId") Integer userId, @Param("day") String day);
}