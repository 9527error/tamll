package com.jxufe.tmall.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

@Repository("dao")//springע�⣬���ڱ�עΪDao���,��Ϊdao
public class DAOImpl extends HibernateTemplate{

	@Resource(name="sf")//����ע��sessionFactory
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	};
	
}
