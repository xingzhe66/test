#手动添加


#以下为自动生成
validation.notnull=不能为空
<#if validation?exists>
	<#list validation?keys as key>
${key}=[${validation[key]}]
	</#list>
</#if>

