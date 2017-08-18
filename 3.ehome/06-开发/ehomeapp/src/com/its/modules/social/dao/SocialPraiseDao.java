/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.social.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import com.its.common.persistence.CrudDao;
import com.its.common.persistence.annotation.MyBatisDao;
import com.its.modules.social.bean.SocialPraiseBean;
import com.its.modules.social.entity.SocialPraise;

/**
 * 点赞DAO接口
 * @author 刘浩浩
 * @version 2017-08-04
 */
@MyBatisDao
public interface SocialPraiseDao extends CrudDao<SocialPraise> {
	
	/**
	 * @Description：根据发言ID获取点赞bean集合
	 * @Author：刘浩浩
	 * @Date：2017年8月4日
	 * @param socialPraise 查询参数
	 * @param pageIndex 分页页码(从0开始为起始页)
	 * @param pageSize
	 * @return
	 */
	public List<SocialPraiseBean> findPraiseBeanList(@Param("socialPraise") SocialPraise socialPraise, @Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize);
	
	/**
	 * @Description：根据条件修改赞信息
	 * @Author：刘浩浩
	 * @Date：2017年8月7日
	 * @param socialPraise
	 */
	public void updateSocialPraise(@Param("socialPraise") SocialPraise socialPraise) ;
	
	/**
	 * @Description：我的赞列表，包括赞的发言、评论
	 * @Author：刘浩浩
	 * @Date：2017年8月8日
	 * @param socialPraise 查询条件
	 * @param pageIndex 分页信息 从0开始
	 * @param pageSize 
	 * @return
	 */
	public List<SocialPraiseBean> getMyPraiseList(@Param("socialPraise") SocialPraise socialPraise, @Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize);
	
	/**
	 * 
	 * @Description：根据发言id查询点赞数量
	 * @Author：邵德才
	 * @Date：2017年8月8日
	 * @param pid
	 * @return
	 */
	public int countPraise(@Param("pid") String pid);
	
	/**
	 * @Description：根据条件查询点赞对象
	 * @Author：刘浩浩
	 * @Date：2017年8月11日
	 * @param socialPraise
	 * @return
	 */
	public SocialPraise getSocialPraise(@Param("socialPraise")  SocialPraise socialPraise);
	
	/**
	 * 
	 * @Description：根据userId查找socialPraise中所有数据
	 * @Author：邵德才
	 * @Date：2017年8月11日
	 * @param touserid
	 * @return
	 */
	public List<SocialPraise> findListByToUserId(@Param("touserid") String touserid);
	
	/**
	 * @Description：根据用户ID查询该用户的点赞数量
	 * @Author：刘浩浩
	 * @Date：2017年8月14日
	 * @param userId
	 * @return
	 */
	public int countPraiseByUserId(String userId);
	
	/**
	 * 
	 * @Description：根据发言id和点赞人得到数据
	 * @Author：邵德才
	 * @Date：2017年8月16日
	 * @param userId
	 * @param pid
	 * @return
	 */
	public int getIsPraise(@Param("userId")String userId, @Param("pid")String pid);
}