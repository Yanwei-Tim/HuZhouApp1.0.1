package com.database.pojo;

/**
 * 
 * 分类表
 * 
 */
public class CategorysTable extends BaseTable
{
	private static final long serialVersionUID = -6669619143784807646L;
	
	public static final String TABLE_NAME = "CATEGORYS";
	/** 分类名称 */
	public static final String FIELD_NAME = "C_NAME";
	/** 父类ID */
	public static final String FIELD_PARENTID = "C_PARENTID";
	/** 等级 */
	public static final String FIELD_LEVEL = "C_LEVEL";
	/** 顺序 */
	public static final String FIELD_SORT = "C_SORT";
	/** 是否删除 */
	public static final String FIELD_ISDELETE = "C_ISDELETE";
	/** 信息通用表父类名称 */
	public static final String FIELD_PARENTNAME = "C_PARENTNAME";
	/** 模块的分值 */
	public static final String FIELD_WEIGHT = "WEIGHT";
	/** 模块是否可以投稿 */
	public static final String FIELD_ISPOST = "ISPOST";
	/** 分类ID */
	public static final String FIELD_CATEGORYID = "CATEGORYID";
	
	public CategorysTable()
	{
		initTable();
	}
	
	private void initTable()
	{
		setTableName(TABLE_NAME);
	}
}
