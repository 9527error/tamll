package com.jxufe.tmall.action;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.jxufe.tmall.pojo.Category;
import com.jxufe.tmall.pojo.Property;
import com.jxufe.tmall.service.CategoryService;
import com.jxufe.tmall.service.PropertyService;
import com.jxufe.tmall.util.Page;

import org.apache.commons.lang3.text.WordUtils;

@Namespace("/")
@ParentPackage("basicstruts")
@Results({
			@Result(name="listPropertyPage", type = "redirect", location="/admin_property_list?category.id=${property.category.id}"),
			@Result(name="listProperty",location="/admin/listProperty.jsp"),
			@Result(name="editProperty",location="/admin/editProperty.jsp"),
		})
public class PropertyAction {

	@Autowired
	protected CategoryService categoryService;
    @Autowired
    protected PropertyService propertyService;
    
	protected Category category;
	protected Page page;
	protected List propertys;
	protected Property property;
    /**
     * transient to persistent
          * 瞬时对象转换为持久对象
     * @param o
     */
	public void t2p(Object o){
        try {
            Class clazz = o.getClass();
            int id = (Integer) clazz.getMethod("getId").invoke(o);
            Object persistentBean = categoryService.get(clazz, id);

            String beanName = clazz.getSimpleName();
            Method setMethod = getClass().getMethod("set" + WordUtils.capitalize(beanName), clazz);
            setMethod.invoke(this, persistentBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	
	@Action("admin_property_list")
	public String List() {
		
		if(page==null)
            page = new Page();
		int total=propertyService.total(category);
		page.setTotal(total);
		page.setParam("&category.id="+category.getId());
		propertys=propertyService.list(page, category);
		t2p(category);//原先只有id，从数据库中补全信息
		return "listProperty";
	}
	
	@Action("admin_property_add")
    public String add() {
		propertyService.save(property);
        return "listPropertyPage";
    }
	
	@Action("admin_property_delete")
	public String delete() {
		//删除后跳转需要property.category.id，但是从jsp中传入的property中只有id，所以需要t2p
		t2p(property);
		propertyService.delete(property);
		return "listPropertyPage";
	}
	
	@Action("admin_property_edit")
    public String edit() {
        t2p(property);
        return "editProperty";
    }
	
    @Action("admin_property_update")
    public String update() {
        propertyService.update(property);
        return "listPropertyPage";
    }

    //getter和setter
	public Category getCategory() {
		return category;
	}


	public void setCategory(Category category) {
		this.category = category;
	}


	public Page getPage() {
		return page;
	}


	public void setPage(Page page) {
		this.page = page;
	}


	public List getPropertys() {
		return propertys;
	}


	public void setPropertys(List propertys) {
		this.propertys = propertys;
	}


	public Property getProperty() {
		return property;
	}


	public void setProperty(Property property) {
		this.property = property;
	}
}
