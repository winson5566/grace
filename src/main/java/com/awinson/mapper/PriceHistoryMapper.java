package com.awinson.mapper;

import com.awinson.Entity.PriceHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

/**
 * Created by winson on 2016/12/2.
 */
@Mapper
public interface PriceHistoryMapper {
    @Select("select * from price_history where type = #{type}")
    PriceHistory findPriceHistoryByType(@Param("type")Integer type);
}
