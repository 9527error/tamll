package com.jxufe.tmall.util;

public class Page {//Page记录当前页的信息，每一次更新数据既要更新Page

	//通过start和count指定页面内容
	int start;//开始的编号
	int count;//每页显示个数
	int total;//总个数
	String param;//参数
	
	private static final int defaultCount = 5; //每页默认显示5条

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Page() {
		count=defaultCount;//没有给出count值时设置count=defaultCount
		//虽然start没有被赋值但是，java后台依然会为其赋初值0
	}
	
	public Page(int start,int count) {
		super();
		this.start=start;
		this.count=count;
	}
	
	public boolean isHasPreviouse() {
		if(start==0) {
			return false;
		}
		return true;
	}
	
	public boolean isHasNext() {
		if(start==getLast()) {//当前页开始最后页的开始
			return false;
		}
		return true;
	}
	
	public int getTotalPage() {//得到总页数
		int totalPage;
		if(0 == total % count) {
			totalPage = total / count;
		}else {
			totalPage = total / count + 1;
		}
		
		if(totalPage == 0){//至少是一页
			totalPage = 1;
		}
		return totalPage;
	}
	
	public int getLast() {//得到最后一页开始一行的编号
		int last;
		if(0 == total % count) {
			last = total - count;
		}else {
			last = total-total % count;
		}
		last = last <0?0:last;
		return last;
	}
	
	public String toString() {
        return "Page [start=" + start + ", count=" + count + ", total=" + total + ", getStart()=" + getStart()
                + ", getCount()=" + getCount() + ", isHasPreviouse()=" + isHasPreviouse() + ", isHasNext()="
                + isHasNext() + ", getTotalPage()=" + getTotalPage() + ", getLast()=" + getLast() + "]";
    }

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
}
