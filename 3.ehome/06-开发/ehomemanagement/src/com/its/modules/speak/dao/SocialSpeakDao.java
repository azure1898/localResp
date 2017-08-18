/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.speak.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.its.common.persistence.CrudDao;
import com.its.common.persistence.annotation.MyBatisDao;
import com.its.modules.speak.entity.SocialSpeak;

/**
 * 发言管理DAO接口
 * @author wmm
 * @version 2017-08-01
 */
@MyBatisDao
public interface SocialSpeakDao extends CrudDao<SocialSpeak> {
	
	/**
	 * 修改状态
	 * @param socialSpeak
	 * @return
	 */
	public int changeDelFlag(SocialSpeak socialSpeak);
	
	/**
	 * 修改置顶状态
	 * @param socialSpeak
	 * @return
	 */
	public int changeTop(SocialSpeak socialSpeak);
	
	/**
	 * 根据ID查询发言详情
	 * @param id
	 * @return
	 */
	public SocialSpeak findById(SocialSpeak socialSpeak);
	
	/**
	 * @Description：根据id查询转发的发言
	 * @Author：王萌萌
	 * @Date：2017年8月9日
	 * @param rootId
	 * @return
	 */
	public List<SocialSpeak> findForwardsByRootId(@Param("rootId") String rootId);
	
	/**
	 * @Description：查询app对应User
	 * @Author：王萌萌
	 * @Date：2017年8月15日
	 * @param userId
	 * @return
	 */
	public SocialSpeak findUser(@Param("userId") String userId);
}