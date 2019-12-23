package com.xhr.mca.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.xhr.mca.common.CoreMapper;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.Order;

public interface OrderMapper extends CoreMapper<Order> {

	@Select("SELECT IFNULL(sum(pay_amount),0) FROM mca_order where user_id = #{userId} and pay_status = 1;")
	public BigDecimal sumOrdersAmountByUserId(@Param(value = "userId") Long userId) throws WebAppException;

	@Select("select IFNULL(SUM(number),0) FROM mca_order where product_sn = #{productSn} and (order_status = 1 or order_status = 2 or order_status = 4)")
	public int sumStockNumberByProductSn(@Param(value = "productSn") String productSn) throws WebAppException;

}
