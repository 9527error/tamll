package com.jxufe.tmall.util;

public class Page {//Page��¼��ǰҳ����Ϣ��ÿһ�θ������ݼ�Ҫ����Page

	//ͨ��start��countָ��ҳ������
	int start;//��ʼ�ı��
	int count;//ÿҳ��ʾ����
	int total;//�ܸ���
	String param;//����
	
	private static final int defaultCount = 5; //ÿҳĬ����ʾ5��

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
		count=defaultCount;//û�и���countֵʱ����count=defaultCount
		//��Ȼstartû�б���ֵ���ǣ�java��̨��Ȼ��Ϊ�丳��ֵ0
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
		if(start==getLast()) {//��ǰҳ��ʼ���ҳ�Ŀ�ʼ
			return false;
		}
		return true;
	}
	
	public int getTotalPage() {//�õ���ҳ��
		int totalPage;
		if(0 == total % count) {
			totalPage = total / count;
		}else {
			totalPage = total / count + 1;
		}
		
		if(totalPage == 0){//������һҳ
			totalPage = 1;
		}
		return totalPage;
	}
	
	public int getLast() {//�õ����һҳ��ʼһ�еı��
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
