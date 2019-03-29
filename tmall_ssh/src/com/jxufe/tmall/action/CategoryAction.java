package com.jxufe.tmall.action;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.jxufe.tmall.pojo.Category;
import com.jxufe.tmall.service.CategoryService;
import com.jxufe.tmall.util.ImageUtil;
import com.jxufe.tmall.util.Page;

//依靠注解的方式实现struts2零配置
@Namespace("/")
@ParentPackage("basicstruts")
@Results({
			@Result(name="listCategory",location="/admin/listCategory.jsp"),
			@Result(name="listCategoryPage",type="redirect",//访问action需要加type="redirect"
			location="admin_category_list"),
			@Result(name="editCategory",location="/admin/editCategory.jsp"),
		})
public class CategoryAction {

	@Autowired
	CategoryService categoryService;
	List<Category> categorys;
	Page page;
	
	Category category;
	File img;
	
	@Action("admin_category_list")
	public String list() {
		if(page==null)
			page=new Page();//第一次加载,page为null
		
		int total=categoryService.total();
		page.setTotal(total);//这里的Page没有设置start和count，所以默认start=0,count=5
		categorys=categoryService.listByPage(page);//根据Page获得categorys
		System.out.println(categorys);
		return "listCategory";//指定名为listCategory的Result
	}
	
	@Action("admin_category_add")
	public String add() {
		categoryService.save(category);
		/*
		 * 存放图片需要绝对地址
		 * 已知图片文件夹相对地址
		 * 以此获得绝对地址
		 */
		File imageFolder=new 
				File(ServletActionContext.getServletContext().getRealPath("img/category"));
		File file=new File(imageFolder,category.getId()+".jpg");
		try{
			FileUtils.copyFile(img, file);
			BufferedImage img = ImageUtil.change2jpg(file);//借助ImageUtil把这个文件的格式转换为真正的JPG格式
			ImageIO.write(img, "jpg", file);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return "listCategoryPage";
	}
	
	@Action("admin_category_delete")
	public String delete() {
		categoryService.delete(category);
		return "listCategoryPage";
	}
	
	@Action("admin_category_edit")
	public String edit() {
		int id=category.getId();
		category=(Category) categoryService.get(Category.class, id);//将数据放在category中以便前端读取
		return "editCategory";
	}
	
	@Action("admin_category_update")
	public String update() {
		categoryService.update(category);
		if(null!=img) {
			File  imageFolder= new File(ServletActionContext.getServletContext().getRealPath("img/category"));
            File file = new File(imageFolder,category.getId()+".jpg");
            try {
                FileUtils.copyFile(img, file);
                BufferedImage img = ImageUtil.change2jpg(file);
                ImageIO.write(img, "jpg", file);//更新图片后，谷歌浏览器需要强制刷新Ctrl+f5
            } catch (IOException e) {
                e.printStackTrace();
            }    
		}
		return "listCategoryPage";
	}
	
	//getter和setter
	public List<Category> getCategorys() {
		return categorys;
	}

	public void setCategorys(List<Category> categorys) {
		this.categorys = categorys;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public File getImg() {
		return img;
	}

	public void setImg(File img) {
		this.img = img;
	}
}
