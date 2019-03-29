package com.jxufe.tmall.action;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.jxufe.tmall.pojo.Product;
import com.jxufe.tmall.pojo.PropertyValue;
import com.jxufe.tmall.service.CategoryService;
import com.jxufe.tmall.service.PropertyValueService;

@Namespace("/")
@ParentPackage("basicstruts")
@Results({
	@Result(name="editPropertyValue",location="/admin/editPropertyValue.jsp"),
	@Result(name="success.jsp", location="/success.jsp")
})
public class PropertyValueAction {

	@Autowired
	PropertyValueService propertyValueService;
	@Autowired
	CategoryService categoryService;
	
	Product product;
	PropertyValue propertyValue;
	List<PropertyValue> propertyValues;
	
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
	
	@Action("admin_propertyValue_edit")
    public String edit() {
        
        t2p(product);
        propertyValueService.init(product);//初始化propertyvalue表
        propertyValues = propertyValueService.listByParent(product);
        return "editPropertyValue";
    }
	
	@Action("admin_propertyValue_update")
    public String update() {
		//页面提交数据的时候，只提交了id和value,没有带product.id和property.id
        String value = propertyValue.getValue();
        t2p(propertyValue);
        propertyValue.setValue(value);
        propertyValueService.update(propertyValue);
        return "success.jsp";
    }

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public PropertyValue getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(PropertyValue propertyValue) {
		this.propertyValue = propertyValue;
	}

	public List<PropertyValue> getPropertyValues() {
		return propertyValues;
	}

	public void setPropertyValues(List<PropertyValue> propertyValues) {
		this.propertyValues = propertyValues;
	}
}
