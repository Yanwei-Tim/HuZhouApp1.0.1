package com.database.entity;

import java.util.List;

public class UserEntity extends BaseEntity
{
	private static final long serialVersionUID = 8852861219513077015L;
	
	private String userName; //用户名
	private String realName; //真实姓名
	private String telephone; //电话
	private String email; //邮箱
	private String password; //密码
	private String departmentNo; //部门编号
	private String roleNo; //角色编号(不同的编号)
	private String groupNo; //用户组编号
	private String userNum; //用户序号
	private String sex; //性别
	private String address; //地址
	private String birthday; //出生日期
	private String idCardNo; //身份证
	private String headImgUrl; //用户网络头像url
	private List<String> photoUrlList; //用户网络相册url
	
	public UserEntity(String userName)
	{
		
	}
	
	public void initDataFormNetwork(String userName)
	{
		
	}
}
