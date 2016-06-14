package com.database.entity;

public class NewsEntity extends BaseEntity
{
	private static final long serialVersionUID = 5781559073758503312L;
	
	private String title; //新闻标题
	private String dateTime; //新闻发布时间
	private String isTop; //是否置顶
	private String isRolling; //是否滚动
	private String isPublish; //是否发布
	private String categoryId; //分类id
	private String newsOrderNum; //序号
	private String writerId; //作者id
	private String header; //政工简报报头
	private String isPassed; //是否通过审核
	private String postTime; //投稿时间
	private String passTime; //审核时间
	private String auditor; //审核人
	private String weight; //模块分值
	private String picurl; //项目标兵图片
	private String reason; //未通过原因
	private String substance; //新闻内容
	private String contentOrderNum; //内容序号
	private String divi;
	
	public NewsEntity()
	{
		
	}
}
