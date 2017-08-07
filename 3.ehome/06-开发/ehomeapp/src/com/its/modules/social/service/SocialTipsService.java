/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.social.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.its.common.persistence.Page;
import com.its.common.service.CrudService;
import com.its.modules.social.entity.SocialTips;
import com.its.modules.social.dao.SocialTipsDao;

/**
 * 消息提醒Service
 * @author 刘浩浩
 * @version 2017-08-07
 */
@Service
@Transactional(readOnly = true)
public class SocialTipsService extends CrudService<SocialTipsDao, SocialTips> {

	public SocialTips get(String id) {
		return super.get(id);
	}
	
	public List<SocialTips> findList(SocialTips socialTips) {
		return super.findList(socialTips);
	}
	
	public Page<SocialTips> findPage(Page<SocialTips> page, SocialTips socialTips) {
		return super.findPage(page, socialTips);
	}
	
	@Transactional(readOnly = false)
	public void save(SocialTips socialTips) {
		super.save(socialTips);
	}
	
	@Transactional(readOnly = false)
	public void delete(SocialTips socialTips) {
		super.delete(socialTips);
	}
	
}