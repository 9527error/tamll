package com.jxufe.tmall.action;
 
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

import com.jxufe.tmall.pojo.Order;
import com.jxufe.tmall.service.CategoryService;
import com.jxufe.tmall.service.OrderItemService;
import com.jxufe.tmall.service.OrderService;
import com.jxufe.tmall.util.Page;

@Namespace("/")
@ParentPackage("basicstruts")
@Results({
	@Result(name="listOrder",location="/admin/listOrder.jsp"),
	@Result(name="listOrderPage",type="redirect",//访问action需要加type="redirect"
	location="admin_order_list")
})
public class OrderAction{
	
	@Autowired
	CategoryService categoryService;
	@Autowired
	OrderService orderService;
	@Autowired
	OrderItemService orderItemService;
	
	Page page;
	List<Order> orders;
	Order order;
	
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
	
	
    @Action("admin_order_list")
    public String list() {
        if (page == null)
            page = new Page();
        int total = orderService.total();
        page.setTotal(total);
        orders = orderService.listByPage(page);
        orderItemService.fill(orders);
        return "listOrder";
    }
 
    @Action("admin_order_delivery")
    public String delivery() {
        t2p(order);
        order.setDeliveryDate(new Date());
        order.setStatus(OrderService.waitConfirm);
        orderService.update(order);
        return "listOrderPage";
    }

    //setter和getter
	public Page getPage() {
		return page;
	}


	public void setPage(Page page) {
		this.page = page;
	}


	public List<Order> getOrders() {
		return orders;
	}


	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}


	public Order getOrder() {
		return order;
	}


	public void setOrder(Order order) {
		this.order = order;
	}
}