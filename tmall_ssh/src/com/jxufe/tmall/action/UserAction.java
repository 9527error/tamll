package com.jxufe.tmall.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.HtmlUtils;

import com.jxufe.tmall.pojo.User;
import com.jxufe.tmall.service.UserService;
import com.jxufe.tmall.util.Page;

@Namespace("/")
@ParentPackage("basicstruts")
@Results({
	@Result(name="listUser",location="/admin/listUser.jsp"),
	@Result(name="listUserPage",type="redirect",
		location="admin_user_list")
})
public class UserAction {
	
	@Autowired
	UserService userService;
	Page page;
	List<User> users;
	User user;
	
	@Action("admin_user_list")
	public String list() {
		if(page==null) {
			page=new Page();
		}
		int total=userService.total();
		page.setTotal(total);
		users=userService.listByPage(page);
		return "listUser";
	}
	
	@Action("admin_user_delete")
	public String delete() {
		userService.delete(user);
		return "listUserPage";
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
