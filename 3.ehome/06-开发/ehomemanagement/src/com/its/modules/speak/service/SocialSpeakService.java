/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.speak.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.its.common.persistence.Page;
import com.its.common.service.CrudService;
import com.its.modules.speak.entity.SocialSpeak;
import com.its.modules.speak.dao.SocialSpeakDao;

/**
 * 发言管理Service
 * @author wmm
 * @version 2017-08-01
 */
@Service
@Transactional(readOnly = true)
public class SocialSpeakService extends CrudService<SocialSpeakDao, SocialSpeak> {
	
	@Autowired
	private SocialSpeakDao socialSpeakDao;

	public SocialSpeak get(String id) {
		return super.get(id);
	}
	
	public List<SocialSpeak> findList(SocialSpeak socialSpeak) {
		return super.findList(socialSpeak);
	}
	
	public Page<SocialSpeak> findPage(Page<SocialSpeak> page, SocialSpeak socialSpeak) {
		return super.findPage(page, socialSpeak);
	}
	
	@Transactional(readOnly = false)
	public void save(SocialSpeak socialSpeak) {
		super.save(socialSpeak);
	}
	
	@Transactional(readOnly = false)
	public void delete(SocialSpeak socialSpeak) {
		super.delete(socialSpeak);
	}
	
	/**
	 * 修改状态
	 * @param socialSpeak
	 * @return
	 */
	@Transactional(readOnly = false)
	public int changeDelFlag(SocialSpeak socialSpeak) {
		int resultNum = socialSpeakDao.changeDelFlag(socialSpeak);
		return resultNum;
	}
	
	/**
	 * 修改置顶状态
	 * @param socialSpeak
	 * @return
	 */
	@Transactional(readOnly = false)
	public int changeTop(SocialSpeak socialSpeak) {
		int resultNum = socialSpeakDao.changeTop(socialSpeak);
		return resultNum;
	}
	
	/**
	 * 根据ID查询发言详情
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
	public SocialSpeak findById(SocialSpeak socialSpeak) {
		return socialSpeakDao.findById(socialSpeak);
	}
	
	/**
	 * @Description：根据id查询转发的发言
	 * @Author：王萌萌
	 * @Date：2017年8月9日
	 * @param rootId
	 * @return
	 */
	@Transactional(readOnly = false)
	public List<SocialSpeak> findForwardsByRootId(String rootId) {
		return socialSpeakDao.findForwardsByRootId(rootId);
	}
	
	/**
	 * @Description：查询app对应User
	 * @Author：王萌萌
	 * @Date：2017年8月15日
	 * @param userId
	 * @return
	 */
	public SocialSpeak findUser(String userId) {
		return socialSpeakDao.findUser(userId);
	}
	
}