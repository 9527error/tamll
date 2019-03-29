package com.jxufe.tmall.action;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.HtmlUtils;

import com.jxufe.tmall.pojo.Category;
import com.jxufe.tmall.pojo.OrderItem;
import com.jxufe.tmall.pojo.Product;
import com.jxufe.tmall.pojo.PropertyValue;
import com.jxufe.tmall.pojo.Review;
import com.jxufe.tmall.pojo.User;
import com.jxufe.tmall.service.CategoryService;
import com.jxufe.tmall.service.OrderItemService;
import com.jxufe.tmall.service.ProductImageService;
import com.jxufe.tmall.service.ProductService;
import com.jxufe.tmall.service.PropertyValueService;
import com.jxufe.tmall.service.ReviewService;
import com.jxufe.tmall.service.UserService;
import com.opensymphony.xwork2.ActionContext;

import tmall.comparator.ProductAllComparator;
import tmall.comparator.ProductDateComparator;
import tmall.comparator.ProductPriceComparator;
import tmall.comparator.ProductReviewComparator;
import tmall.comparator.ProductSaleCountComparator;

@Namespace("/")
@ParentPackage("basicstruts")
@Results({ 
		@Result(name = "home.jsp", location = "/home.jsp"),
		@Result(name = "register.jsp", location = "/register.jsp"),
		@Result(name = "registerSuccessPage.jsp", location = "/registerSuccess.jsp"),
		@Result(name = "login.jsp", location = "/login.jsp"),
		@Result(name = "homePage", type = "redirect", location = "forehome"),
		@Result(name = "product.jsp", location = "/product.jsp"),
		@Result(name = "success.jsp", location = "/success.jsp"), @Result(name = "fail.jsp", location = "/fail.jsp"),
		@Result(name = "category.jsp", location = "/category.jsp"),
		@Result(name = "searchResult.jsp", location = "/searchResult.jsp"),
		@Result(name = "buyPage", type = "redirect", location = "forebuy?oiids=${oiid}"),
		@Result(name = "buy.jsp", location = "/buy.jsp"), 
		@Result(name = "cart.jsp", location="/cart.jsp"),
})
public class ForeAction {

	@Autowired
	CategoryService categoryService;
	@Autowired
	ProductService productService;
	@Autowired
	UserService userService;
	@Autowired
	ProductImageService productImageService;
	@Autowired
	PropertyValueService propertyValueService;
	@Autowired
	OrderItemService orderItemService;
	@Autowired
	ReviewService reviewService;

	List<Category> categorys;
	User user;
	String msg;
	String keyword;
	Product product;
	Category category;
	String sort;
	int num;// 前台传入的product的购买数量
	int oiid;
	int[] oiids;
	float total;
	List productSingleImages;
	List productDetailImages;
	List<Review> reviews;
	List<PropertyValue> propertyValues;
	List<Product> products;
	List<OrderItem> orderItems;

	public void t2p(Object o) {
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

	@Action("forecart")//显示购物车
	public String cart() {
	    User user =(User) ActionContext.getContext().getSession().get("user");
	    orderItems = orderItemService.list("user",user,"order", null);
	    for (OrderItem orderItem : orderItems)
	        productImageService.setFirstProdutImage(orderItem.getProduct());
	    return "cart.jsp";
	}
	
	@Action("foreaddCart")//加入购物车
	public String addCart() {
		User user=(User)ActionContext.getContext().getSession().get("user");
		boolean found=false;
		List<OrderItem> ois=orderItemService.list("user",user,"order",null);
		for(OrderItem oi:ois) {
			if(oi.getProduct().getId()==product.getId()) {
				oi.setNumber(oi.getNumber()+num);
	            orderItemService.update(oi);
	            found = true;
	            break;
			}
		}
		
		if(!found){
	        OrderItem oi = new OrderItem();
	        oi.setUser(user);
	        oi.setNumber(num);
	        oi.setProduct(product);
	        orderItemService.save(oi);
	    }
		
		return "success.jsp";
	}
	
	@Action("forebuy")
	public String buy() {// 结算
		orderItems = new ArrayList<>();
		for (int oiid : oiids) {
			OrderItem oi = (OrderItem) orderItemService.get(oiid);
			total += oi.getProduct().getPromotePrice() * oi.getNumber();
			orderItems.add(oi);
			productImageService.setFirstProdutImage(oi.getProduct());
		}
		ActionContext.getContext().getSession().put("orderItems", orderItems);
		return "buy.jsp";
	}

	@Action("forebuyone") // 单击购买后调用
	public String buyone() {
		User user = (User) ActionContext.getContext().getSession().get("user");
		boolean found = false;
		List<OrderItem> ois = orderItemService.list("user", user, "order", null);// 该用户尚未生成订单的订单项
		for (OrderItem oi : ois) {// 从已经生成的订单项中找到本产品订单项
			if (oi.getProduct().getId() == product.getId()) {
				oi.setNumber(oi.getNumber() + num);
				orderItemService.update(oi);
				found = true;
				oiid = oi.getId();
				break;
			}
		}

		if (!found) {// 如果没有发现同类产品的订单项
			OrderItem oi = new OrderItem();
			oi.setUser(user);
			oi.setNumber(num);
			oi.setProduct(product);
			orderItemService.save(oi);
			oiid = oi.getId();
		}

		return "buyPage";
	}

	@Action("foresearch")
	public String search() {
		products = productService.search(keyword, 0, 20);
		productService.setSaleAndReviewNumber(products);
		for (Product product : products)
			productImageService.setFirstProdutImage(product);

		return "searchResult.jsp";
	}

	@Action("forecategory")
	public String category() {
		t2p(category);
		productService.fill(category);
		productService.setSaleAndReviewNumber(category.getProducts());

		if (null != sort) {
			switch (sort) {
			case "review":
				Collections.sort(category.getProducts(), new ProductReviewComparator());
				break;
			case "date":
				Collections.sort(category.getProducts(), new ProductDateComparator());
				break;

			case "saleCount":
				Collections.sort(category.getProducts(), new ProductSaleCountComparator());
				break;

			case "price":
				Collections.sort(category.getProducts(), new ProductPriceComparator());
				break;

			case "all":
				Collections.sort(category.getProducts(), new ProductAllComparator());
				break;
			}
		}
		return "category.jsp";
	}

	@Action("forecheckLogin")
	public String checkLogin() {
		User u = (User) ActionContext.getContext().getSession().get("user");
		if (null == u)
			return "fail.jsp";
		else
			return "success.jsp";
	}

	@Action("foreloginAjax")
	public String loginAjax() {

		user.setName(HtmlUtils.htmlEscape(user.getName()));
		User user_session = userService.get(user.getName(), user.getPassword());

		if (null == user_session)
			return "fail.jsp";

		ActionContext.getContext().getSession().put("user", user_session);
		return "success.jsp";
	}

	@Action("forehome")
	public String home() {
		categorys = categoryService.list();
		productService.fill(categorys);
		productService.fillByRow(categorys);
		return "home.jsp";
	}

	@Action("foreregister")
	public String register() {
		user.setName(HtmlUtils.htmlEscape(user.getName()));
		boolean exit = userService.isExist(user.getName());
		if (exit) {
			msg = "用户名已经存在!";
			return "register.jsp";
		}
		userService.save(user);
		return "registerSuccessPage.jsp";
	}

	@Action("forelogin")
	public String login() {
		user.setName(HtmlUtils.htmlEscape(user.getName()));
		User user_session = userService.get(user.getName(), user.getPassword());
		if (null == user_session) {
			msg = "账号密码错误";
			return "login.jsp";
		}
		ActionContext.getContext().getSession().put("user", user_session);
		return "homePage";
	}

	@Action("forelogout")
	public String logout() {
		ActionContext.getContext().getSession().remove("user");
		return "homePage";
	}

	@Action("foreproduct")
	public String product() {
		t2p(product);

		productImageService.setFirstProdutImage(product);
		productSingleImages = productImageService.list("product", product, "type", ProductImageService.type_single);
		productDetailImages = productImageService.list("product", product, "type", ProductImageService.type_detail);
		product.setProductSingleImages(productSingleImages);
		product.setProductDetailImages(productDetailImages);

		propertyValues = propertyValueService.listByParent(product);
		reviews = reviewService.listByParent(product);
		productService.setSaleAndReviewNumber(product);
		return "product.jsp";
	}

	public List<Category> getCategorys() {
		return categorys;
	}

	public void setCategorys(List<Category> categorys) {
		this.categorys = categorys;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getOiid() {
		return oiid;
	}

	public void setOiid(int oiid) {
		this.oiid = oiid;
	}

	public int[] getOiids() {
		return oiids;
	}

	public void setOiids(int[] oiids) {
		this.oiids = oiids;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public List getProductSingleImages() {
		return productSingleImages;
	}

	public void setProductSingleImages(List productSingleImages) {
		this.productSingleImages = productSingleImages;
	}

	public List getProductDetailImages() {
		return productDetailImages;
	}

	public void setProductDetailImages(List productDetailImages) {
		this.productDetailImages = productDetailImages;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public List<PropertyValue> getPropertyValues() {
		return propertyValues;
	}

	public void setPropertyValues(List<PropertyValue> propertyValues) {
		this.propertyValues = propertyValues;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

}