package ${mouldName}.${entityPackage};

import com.dcits.ensemble.cloud.cif.common.base.BaseCifPo;
import lombok.Data;
import lombok.EqualsAndHashCode;
<#if tablePkSize =="Y">
import com.dcits.comet.dao.annotation.TablePk;
</#if>
<#if tableType ??>
import com.dcits.comet.dao.annotation.PartitionKey;
import com.dcits.comet.dao.annotation.TableType;
import com.dcits.comet.dao.annotation.TableTypeEnum;
</#if>
<#if tableType ?? && tableType=="LEVEL">
import com.dcits.comet.dao.annotation.PartitionKey;
</#if>

/**
 * @Author ${author}
 * @Description ${functionComment}
 * @Date ${date}
 * @Version 1.0
 */

@Data
@EqualsAndHashCode(callSuper=false)
<#if tableType ??>
@TableType(name="${tableName}",value=TableTypeEnum.${tableType})
</#if>
public class ${className} extends BaseCifPo{

	<#list cloums as c>
	/**
	* This field corresponds to the database column ${tableName}.${ c.columnNameL}
	* @Description  ${ c.columnComment}
	*/
	<#if c.partitonKey ??>
	${c.partitonKey}
    </#if>
	<#if c.cloumsTop ??>
	${c.cloumsTop}
	</#if>
	private ${c.javaType} ${ c.columnName};
	</#list>
}
