package com.jxufe.tmall.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

@Repository("dao")//spring注解，用于标注为Dao组件,名为dao
public class DAOImpl extends HibernateTemplate{

	@Resource(name="sf")//依赖注入sessionFactory
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	};
	
}
