package com.atmatrix.greenland.dao;

import com.atmatrix.greenland.dao.po.SSOUser;
import com.atmatrix.greenland.dao.po.SSOUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SSOUserMapper {
    long countByExample(SSOUserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SSOUser record);

    int insertSelective(SSOUser record);

    List<SSOUser> selectByExample(SSOUserExample example);

    SSOUser selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SSOUser record, @Param("example") SSOUserExample example);

    int updateByExample(@Param("record") SSOUser record, @Param("example") SSOUserExample example);

    int updateByPrimaryKeySelective(SSOUser record);

    int updateByPrimaryKey(SSOUser record);

    int batchInsert(@Param("list") List<SSOUser> toInsert);


    List<SSOUser> selectByUserId(@Param("userId")Long userId);
}