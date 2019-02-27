package ${mouldName}.${entityPackage};

import com.dcits.ensemble.cloud.cif.common.base.BaseCifPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author ${author}
 * @Description ${functionComment}
 * @Date ${date}
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ${className} extends BaseCifPo{

	<#list cloums as c>
	/**
	* This field corresponds to the database column ${tableName}.${ c.columnNameL}
	* @Description  ${ c.columnComment}
	*/
	private ${c.javaType} ${ c.columnName};
	</#list>


    public void insert(${className}  ${objectName}) {
        daoSupport.insert(${objectName});
    }

    public void updateById(${className}  ${objectName}) {
        daoSupport.update(${objectName});
    }

    public void deleteById(${className}  ${objectName}) {
        daoSupport.delete(${objectName});
    }

}
