package com.jiandande.review.bean;

import java.util.ArrayList;
import java.util.List;


public class Pagination<T>{
	
	public int page;// 当前页
	
	public int countPage;// 总页数
	public int rowFrom;// 开始的记录数
	public int rowTo;// 结束的记录数 ===pageSize

	public int pageSize;// 每页显示的记录数信息
	
	//结合easyUI 中json的返回格式
	public int total;// 总记录数
	public ArrayList<T> datas; // 记录信息
}
