/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.social.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.its.common.persistence.Page;
import com.its.common.service.CrudService;
import com.its.modules.social.bean.SocialPraiseBean;
import com.its.modules.social.dao.SocialCommentDao;
import com.its.modules.social.dao.SocialPraiseDao;
import com.its.modules.social.entity.SocialPraise;

/**
 * 点赞Service
 * @author 刘浩浩
 * @version 2017-08-04
 */
@Service
@Transactional(readOnly = true)
public class SocialPraiseService extends CrudService<SocialPraiseDao, SocialPraise> {
	
	@Autowired
	private SocialPraiseDao socialPraiseDao;

	public SocialPraise get(String id) {
		return super.get(id);
	}
	
	public List<SocialPraise> findList(SocialPraise socialPraise) {
		return super.findList(socialPraise);
	}
	
	public Page<SocialPraise> findPage(Page<SocialPraise> page, SocialPraise socialPraise) {
		return super.findPage(page, socialPraise);
	}
	
	@Transactional(readOnly = false)
	public void save(SocialPraise socialPraise) {
		super.save(socialPraise);
	}
	
	@Transactional(readOnly = false)
	public void delete(SocialPraise socialPraise) {
		super.delete(socialPraise);
	}
	
	/**
	 * @Description：根据发言ID获取点赞bean集合
	 * @Author：刘浩浩
	 * @Date：2017年8月4日
	 * @param socialPraise 查询参数
	 * @param pageIndex 分页页码(从0开始为起始页)
	 * @param pageSize
	 * @return
	 */
	public List<SocialPraiseBean> findPraiseBeanList(SocialPraise socialPraise, int pageIndex, int pageSize){
		return socialPraiseDao.findPraiseBeanList(socialPraise, pageIndex, pageSize);
	}
	
	/**
	 * @Description：根据条件修改赞信息
	 * @Author：刘浩浩
	 * @Date：2017年8月7日
	 * @param socialPraise
	 */
	@Transactional(readOnly = false)
	public void updateSocialPraise(SocialPraise socialPraise) {
		socialPraiseDao.updateSocialPraise(socialPraise);
	}
}