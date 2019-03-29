package com.jxufe.tmall.action;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.jxufe.tmall.pojo.Product;
import com.jxufe.tmall.pojo.ProductImage;
import com.jxufe.tmall.service.CategoryService;
import com.jxufe.tmall.service.ProductImageService;
import com.jxufe.tmall.service.ProductService;
import com.jxufe.tmall.util.ImageUtil;

@Namespace("/")
@ParentPackage("basicstruts")
@Results({
	@Result(name="listProductImage",location="/admin/listProductImage.jsp"),
	@Result(name="listProductImagePage", type = "redirect", location="/admin_productImage_list?product.id=${productImage.product.id}"),
})
public class ProductImageAction {

	@Autowired
	ProductImageService productImageService;
	@Autowired
	CategoryService categoryService;
	
	Product product;
	ProductImage productImage;
	File img;
	List<ProductImage> productSingleImages;
	List<ProductImage> productDetailImages;
	
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
	
	@Action("admin_productImage_list")
	public String list() {
		productSingleImages = productImageService.list("product",product,"type", ProductImageService.type_single);
        productDetailImages = productImageService.list("product",product,"type", ProductImageService.type_detail);
        t2p(product);
        return "listProductImage";
	}
	
	@Action("admin_productImage_add")
    public String add() {
 
        productImageService.save(productImage);
         
        String folder = "img/";
        if(ProductImageService.type_single.equals(productImage.getType())){
            folder +="productSingle";
        }
        else{
            folder +="productDetail";
        }
             
        File  imageFolder= new File(ServletActionContext.getServletContext().getRealPath(folder));
        File file = new File(imageFolder,productImage.getId()+".jpg");
        String fileName = file.getName();
        try {
            FileUtils.copyFile(img, file);
            BufferedImage img = ImageUtil.change2jpg(file);
            ImageIO.write(img, "jpg", file);           
        } catch (IOException e) {
             
            e.printStackTrace();
        }
         
        if(ProductImageService.type_single.equals(productImage.getType())){
            String imageFolder_small= ServletActionContext.getServletContext().getRealPath("img/productSingle_small");
            String imageFolder_middle= ServletActionContext.getServletContext().getRealPath("img/productSingle_middle");       
             
            File f_small = new File(imageFolder_small, fileName);
            File f_middle = new File(imageFolder_middle, fileName);
 
            f_small.getParentFile().mkdirs();
            f_middle.getParentFile().mkdirs();
             
            ImageUtil.resizeImage(file, 56, 56, f_small);
            ImageUtil.resizeImage(file, 217, 190, f_middle);
        }      
                 
        return "listProductImagePage";
    }
	
	@Action("admin_productImage_delete")
    public String delete() {
        t2p(productImage);
        productImageService.delete(productImage);
        return "listProductImagePage";
    }

	//getterºÍsetter
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public ProductImage getProductImage() {
		return productImage;
	}

	public void setProductImage(ProductImage productImage) {
		this.productImage = productImage;
	}

	public File getImg() {
		return img;
	}

	public void setImg(File img) {
		this.img = img;
	}

	public List<ProductImage> getProductSingleImages() {
		return productSingleImages;
	}

	public void setProductSingleImages(List<ProductImage> productSingleImages) {
		this.productSingleImages = productSingleImages;
	}

	public List<ProductImage> getProductDetailImages() {
		return productDetailImages;
	}

	public void setProductDetailImages(List<ProductImage> productDetailImages) {
		this.productDetailImages = productDetailImages;
	}
}
