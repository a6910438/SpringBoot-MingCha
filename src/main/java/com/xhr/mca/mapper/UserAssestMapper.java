package com.xhr.mca.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.xhr.mca.common.CoreMapper;
import com.xhr.mca.common.WebAppException;
import com.xhr.mca.entity.UserAssest;
import com.xhr.mca.entity.vo.TransactionDetail;
import com.xhr.mca.entity.vo.TransactionVo;

public interface UserAssestMapper extends CoreMapper<UserAssest> {

	@Select("SELECT ua.coin_id, ua.address,ua.ava_balance as avaBalance,c.`name` as name,c.rate as rate,c.rate*ua.ava_balance as rmbMoney  FROM user_assest ua left join coin c on ua.coin_id = c.id where ua.user_id = #{userID};")
	public List<UserAssest> findAssestsByUserId(@Param("userID") Long userID) throws WebAppException;

	@Select("select t.* from (SELECT id,amount,type as typeI,direction,create_time,send, receice FROM sun_record where (send = #{userID} and type not in (3,4,5)) or receice = #{userID} union all "
			+ "select id,amount,2 as typeI,'-' as direction,create_time,0 as send,0 as receice from withdraw where user_id = #{userID} and coin_id = #{coinID} union all "
			+ "select id,amount,1 as typeI,'+' as direction,create_time,0 as send,0 as receice from deposit where user_id = #{userID} and coin_id = #{coinID}) as t where direction like #{direction} or direction is null order by create_time desc limit #{page}, #{rows}")
	public List<TransactionVo> findSunTransactionVos(@Param("userID") Long userID, @Param("coinID") Long coinID,
			@Param("direction") String direction, @Param("page") Integer page, @Param("rows") Integer rows)
			throws WebAppException;

	@Select("select t.* from (SELECT id,amount,type as typeI,direction,create_time,send, receice FROM mca_record where (send = #{userID} and type not in (3,4,5)) or receice = #{userID} union all "
			+ "select id,amount,2 as typeI,'-' as direction,create_time,0 as send,0 as receice from withdraw where user_id = #{userID} and coin_id = #{coinID} union all "
			+ "select id,amount,1 as typeI,'+' as direction,create_time,0 as send,0 as receice from deposit where user_id = #{userID} and coin_id = #{coinID}) as t where direction like #{direction} or direction is null order by create_time desc limit #{page}, #{rows}")
	public List<TransactionVo> findMcaTransactionVos(@Param("userID") Long userID, @Param("coinID") Long coinID,
			@Param("direction") String direction, @Param("page") Integer page, @Param("rows") Integer rows)
			throws WebAppException;

	@Select("select t.* from (SELECT id,amount,type as typeI,direction,create_time,send, receice FROM rmb_record where (send = #{userID} and type not in (3,4,5)) or receice = #{userID} union all "
			+ "select id,amount,2 as typeI,'-' as direction,create_time,0 as send,0 as receice from withdraw where user_id = #{userID} and coin_id = #{coinID} union all "
			+ "select id,amount,1 as typeI,'+' as direction,create_time,0 as send,0 as receice from deposit where user_id = #{userID} and coin_id = #{coinID}) as t where direction like #{direction} or direction is null order by create_time desc limit #{page}, #{rows} ")
	public List<TransactionVo> findRmbTransactionVos(@Param("userID") Long userID, @Param("coinID") Long coinID,
			@Param("direction") String direction, @Param("page") Integer page, @Param("rows") Integer rows)
			throws WebAppException;

	@Select("select t.* from (SELECT id,amount,type as typeI,direction,create_time,send, receice FROM old_record where (send = #{userID} and type not in (3,4,5)) or receice = #{userID} union all "
			+ "select id,amount,2 as typeI,'-' as direction,create_time,0 as send,0 as receice from withdraw where user_id = #{userID} and coin_id = #{coinID} union all "
			+ "select id,amount,1 as typeI,'+' as direction,create_time,0 as send,0 as receice from deposit where user_id = #{userID} and coin_id = #{coinID}) as t where direction like #{direction} or direction is null order by create_time desc limit #{page}, #{rows} ")
	public List<TransactionVo> findOldTransactionVos(@Param("userID") Long userID, @Param("coinID") Long coinID,
			@Param("direction") String direction, @Param("page") Integer page, @Param("rows") Integer rows)
			throws WebAppException;

	@Select("SELECT id, '' AS `from`, wallet_address AS `to`, amount, `hash`, create_time, commission AS fee, CASE WHEN `status` = 0 THEN '审核中' WHEN `status` = 1 THEN '通过' ELSE '拒绝' "
			+ "END as `status` FROM withdraw WHERE id = #{id} ")
	public TransactionDetail findWithdrawDetail(@Param("id") Long id) throws WebAppException;

	@Select("select id, send AS `from`,receice as `to`, amount, `hash`, create_time, fee, '' as status from deposit where id = #{id} ")
	public TransactionDetail findDepositDetail(@Param("id") Long id) throws WebAppException;

}
