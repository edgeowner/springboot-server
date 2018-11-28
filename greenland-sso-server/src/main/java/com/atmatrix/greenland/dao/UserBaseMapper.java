package com.atmatrix.greenland.dao;

import com.atmatrix.greenland.dao.po.UserBase;
import com.atmatrix.greenland.dao.po.UserBaseExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserBaseMapper {
    long countByExample(UserBaseExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserBase record);

    int insertSelective(UserBase record);

    List<UserBase> selectByExample(UserBaseExample example);

    UserBase selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserBase record, @Param("example") UserBaseExample example);

    int updateByExample(@Param("record") UserBase record, @Param("example") UserBaseExample example);

    int updateByPrimaryKeySelective(UserBase record);

    int updateByPrimaryKey(UserBase record);

    List<UserBase>  getAll();
}