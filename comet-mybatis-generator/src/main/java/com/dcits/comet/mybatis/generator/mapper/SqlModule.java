package com.dcits.comet.mybatis.generator.mapper;

import com.dcits.comet.mybatis.generator.Config;
import com.dcits.comet.mybatis.generator.util.DbUtil;
import com.dcits.comet.mybatis.generator.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

public class SqlModule {
	
	final static Logger logger = LoggerFactory.getLogger(SqlModule.class);
	
	/**
	 * class名称
	 */
	private String className;
	
	/**
	 * 数据库表名
	 */
	private String tableName;
	
	/**
	 * ID列名
	 */
	private String idName;
	
	/**
	 * 命名空间
	 * class的类型
	 */
	private String namespace;
	
	/**
	 * 列名
	 */
	private String tableColumns = new String();
	
	/**
	 * 值
	 */
	private String tableValues = new String();
	
	private String tableValuesItem = new String();
	
	/**
	 * 实体与数据库列名的对照
	 * <属性值, 数据库列名>
	 */
	private Map<String, Object> propMap = new LinkedHashMap<String, Object>();





	public SqlModule(final Class<?> clazz) {
		//基础字段
		className = clazz.getSimpleName();
		final Field[] fs = clazz.getDeclaredFields();
		for (final Field fe : fs) {
			//是基础数据类型，具有get和set方法
			if (ReflectionUtil.isPrimitive(fe.getType()) && isHasGetAndSet(fe)) {
				final String FeName = fe.getName();
				String DBName;
				if(Config.columnRule == 1){
					//todo 默认命名规则：数据库是下划线分割，java entity是驼峰；
					DBName = DbUtil.fieldName2DbName(FeName);
				}else{
					DBName = FeName;
				}
				PropBean propBean=new PropBean();
				propBean.setDbname(DBName);
				//todo 数据库类型转换
				propBean.setType(fe.getType().getSimpleName());
				propMap.put(FeName, propBean);
			}
		}
		String[] arrs = clazz.getName().split("\\.");
		//namespace = arrs[0] + "." + arrs[1] + "." + arrs[2] + "." + arrs[3] + "." + arrs[4] + "." + "dao.impl." + className + "DaoImpl";
		namespace = Config.packageName + "." + className ;
		for (final String key : propMap.keySet()) {
			tableColumns = tableColumns + ((PropBean)propMap.get(key)).getDbname() + ",";
			tableValues = tableValues + "#{" + key + "}" + ",";
			tableValuesItem = tableValuesItem + "#{item." + key + "}" + ",";
		}
		tableColumns = tableColumns.substring(0, tableColumns.length()-1);
		tableValues = tableValues.substring(0, tableValues.length()-1);
		tableValuesItem = tableValuesItem.substring(0, tableValuesItem.length()-1);
		if(Config.idRule == 1){
			idName = "id";
		}else{
			idName = className.substring(0,1).toLowerCase()+ className.substring(1,className.length()) +"Id";
		}
	}
	
	/**
	 * 判断field对象是有get和set方法
	 * @return
	 */
	private Boolean isHasGetAndSet(final Field fe) {
		Method m1 = ReflectionUtil.getGetMethod(fe);
		if (m1 == null) {
			return Boolean.FALSE;
		}
		m1 = ReflectionUtil.getSetMethod(fe);
		if (m1 == null) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(final String className) {
		this.className = className;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(final String tableName) {
		this.tableName = tableName;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(final String namespace) {
		this.namespace = namespace;
	}

	public String getTableColumns() {
		return tableColumns;
	}

	public void setTableColumns(final String tableColumns) {
		this.tableColumns = tableColumns;
	}

	public String getTableValues() {
		return tableValues;
	}

	public void setTableValues(final String tableValues) {
		this.tableValues = tableValues;
	}

	public Map<String, Object> getPropMap() {
		return propMap;
	}

	public void setPropMap(final Map<String, Object> propMap) {
		this.propMap = propMap;
	}

	public String getTableValuesItem() {
		return tableValuesItem;
	}

	public void setTableValuesItem(String tableValuesItem) {
		this.tableValuesItem = tableValuesItem;
	}

	public String getIdName() {
		return idName;
	}

	public void setIdName(String idName) {
		this.idName = idName;
	}


}

