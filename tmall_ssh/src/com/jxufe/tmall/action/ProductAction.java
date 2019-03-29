package com.jxufe.tmall.action;

import com.jxufe.tmall.pojo.Category;
import com.jxufe.tmall.pojo.Product;
import com.jxufe.tmall.service.CategoryService;
import com.jxufe.tmall.service.ProductImageService;
import com.jxufe.tmall.service.ProductService;
import com.jxufe.tmall.util.Page;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

@Namespace("/")
@ParentPackage("basicstruts")
@Results({
	@Result(name="listProductPage", type = "redirect", location="/admin_product_list?category.id=${product.category.id}"),
	@Result(name="listProduct",location="/admin/listProduct.jsp"),
	@Result(name="editProduct",location="/admin/editProduct.jsp")
})
public class ProductAction {

	@Autowired
	ProductService productService;
	@Autowired
	CategoryService categoryService;
	@Autowired
	ProductImageService productImageService;
	
	Category category;
	Product product;
	List<Product> products;
	Page page;
	
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
	
	@Action("admin_product_list")
	public String list() {
		if(page==null) {
			page=new Page();
		}
		int total=productService.total(category);
		page.setTotal(total);
		page.setParam("&category.id="+category.getId());//跳转所需要的参数
		products=productService.list(page, category);
		for (Product product : products) {
			productImageService.setFirstProdutImage(product);//查询的时候刚好把图片信息放进去
		}
		t2p(category);
		return "listProduct";
	}
	
	@Action("admin_product_add")
	public String add() {
		product.setCreateDate(new Date());
        productService.save(product);//此时已经有product不需要t2p
        return "listProductPage";
	}
	
	@Action("admin_product_delete")
	public String delete() {
		t2p(product);
		productService.delete(product);
		return "listProductPage";
	}
	
	@Action("admin_product_edit")
	public String edit() {
		t2p(product);
		return "editProduct";
	}
	
	@Action("admin_product_update")
	public String update() {
		//在jsp中无法手动设置创建时间，所以需要在此处添加时间，否则创建时间为空
		Product productFromDB= (Product)productService.get(product.getId());
        product.setCreateDate(productFromDB.getCreateDate());
        
        productService.update(product);
        return "listProductPage";
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
}
