package com.jxufe.tmall.service;

import java.util.List;

import com.jxufe.tmall.pojo.Category;
import com.jxufe.tmall.util.Page;

public interface BaseService {//�ع�CRUD

	public Integer save(Object object);
	public void update(Object object);
	public void delete(Object object);
	public Object get(Class clazz,int id) ;
	public Object get(int id);
	
	public List list();
	public List listByPage(Page page);
	public int total();

	public List listByParent(Object parent);//�������Category��id��ѯ��Ӧ��property
    public List list(Page page, Object parent);
    public int total(Object parentObject);
    
    public List list(Object ...pairParms);
}
