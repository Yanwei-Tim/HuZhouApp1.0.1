package com.database.pojo;


/**
 * 
 * 用户表
 * 
 */
public class UserTable extends BaseTable
{
	private static final long serialVersionUID = -7359533823172139550L;
	
	public static final String TABLE_NAME = "USERS";
	/** 用户名 */ 
	public static final String FIELD_USERNAME = "USERNAME";
	/** 真实姓名 */ 
	public static final String FIELD_REALNAME = "REALNAME";
	/** 电话 */ 
	public static final String FIELD_TELEPHONE = "TELEPHONE";
	/** 邮箱 */ 
	public static final String FIELD_EMAIL = "EMAIL";
	/** 密码 */ 
	public static final String FIELD_PASSWORD = "PASSWORD";
	/** 部门编号 */ 
	public static final String FIELD_DEPARTMENTNO = "DEPARTMENTNO";
	/** 角色编号 */ 
	public static final String FIELD_ROLENO = "ROLENO";
	/** 组编号 */ 
	public static final String FIELD_GROUPNO = "GROUPNO";
	/** 用户编号 */ 
	public static final String FIELD_USERNUM = "USERNUM";
	
	public UserTable()
	{
		initTable();
	}
	
	private void initTable()
	{
		//创建表名
		setTableName(TABLE_NAME);
	}
	
}
