package com.xhr.mca.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.xhr.mca.common.CoreMapper;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.UserAssest;
import com.xhr.mca.entity.vo.TransactionVo;

public interface UserAssestMapper extends CoreMapper<UserAssest> {

	@Select("SELECT ua.coin_id,ua.ava_balance as avaBalance,c.`name` as name,c.rate as rate,c.rate*ua.ava_balance as rmbMoney  FROM user_assest ua left join coin c on ua.coin_id = c.id where ua.user_id = #{userID};")
	public List<UserAssest> findAssestsByUserId(@Param("userID") Long userID) throws WebAppException;

	@Select("select t.* from (SELECT amount,type as typeI,direction,create_time FROM sun_record where send = #{userID} or receice = #{userID} union all "
			+ "select amount,2 as typeI,'-' as direction,create_time from withdraw where user_id = #{userID} and coin_id = #{coinID} union all "
			+ "select amount,3 as typeI,'+' as direction,create_time from deposit where user_id = #{userID} and coin_id = #{coinID}) as t where direction like #{direction} limit #{page}, #{rows}")
	public List<TransactionVo> findSunTransactionVos(@Param("userID") Long userID, @Param("coinID") Long coinID, @Param("direction") String direction, @Param("page") Integer page, @Param("rows") Integer rows)
			throws WebAppException;

	@Select("select t.* from (SELECT amount,type as typeI,direction,create_time FROM mca_record where send = #{userID} or receice = #{userID} union all "
			+ "select amount,2 as typeI,'-' as direction,create_time from withdraw where user_id = #{userID} and coin_id = #{coinID} union all "
			+ "select amount,3 as typeI,'+' as direction,create_time from deposit where user_id = #{userID} and coin_id = #{coinID}) as t where direction like #{direction} limit #{page}, #{rows}")
	public List<TransactionVo> findMcaTransactionVos(@Param("userID") Long userID, @Param("coinID") Long coinID, @Param("direction") String direction, @Param("page") Integer page, @Param("rows") Integer rows)
			throws WebAppException;

	@Select("select t.* from (SELECT amount,type as typeI,direction,create_time FROM rmb_record where send = #{userID} or receice = #{userID} union all "
			+ "select amount,2 as typeI,'-' as direction,create_time from withdraw where user_id = #{userID} and coin_id = #{coinID} union all "
			+ "select amount,3 as typeI,'+' as direction,create_time from deposit where user_id = #{userID} and coin_id = #{coinID}) as t where direction like #{direction} limit #{page}, #{rows} ")
	public List<TransactionVo> findRmbTransactionVos(@Param("userID") Long userID, @Param("coinID") Long coinID, @Param("direction") String direction, @Param("page") Integer page, @Param("rows") Integer rows)
			throws WebAppException;

}
