package ${mouldName}.entity;

import com.dcits.comet.dao.model.BasePo;
/**
 * @功能说明：${functionComment}
 * @作者： ${author}
 * @创建日期：${date}
 */
public class ${className} extends BasePo{

	//字段
	<#list cloums as c>
	private ${c.javaType} ${ c.columnName};//${ c.columnComment}
	</#list>
	
	//构造方法
	public ${className}() {
	}
	
	//get和set方法
	<#list cloums as c>
	/**
	* @return ${ c.columnName}
	*/
	public ${c.javaType} get${ c.UpUmnName}() {
		return  ${ c.columnName};
	}
	/**
	* @param ${ c.columnName}
	*/
	public void set${ c.UpUmnName}(${c.javaType} ${ c.columnName}) {
		this.${ c.columnName} = ${ c.columnName};
	}

	</#list>
	
}
