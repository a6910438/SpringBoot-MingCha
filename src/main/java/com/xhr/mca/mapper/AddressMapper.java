package com.xhr.mca.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.xhr.mca.common.CoreMapper;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.EthAddress;

public interface AddressMapper extends CoreMapper<EthAddress> {

	@Select("select address from eth_address where is_use = 0 order by create_time desc limit 1;")
	public String getNowAddress() throws WebAppException;

	@Update("update eth_address set is_use = 1 where address = #{address}")
	public void updateStatus(@Param("address") String address) throws WebAppException;

	@Select("select count(1)>#{condition} from eth_address where is_use = 0;")
	public boolean addressThanCondition(@Param("condition") Integer condition) throws WebAppException;

}
