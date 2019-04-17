package com.dcits.yunyun.entity;

import com.dcits.comet.dao.annotation.TablePk;
import com.dcits.comet.dao.annotation.TableType;
import com.dcits.comet.dao.annotation.TableTypeEnum;
import com.dcits.comet.dao.model.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author guihj
 * @Description 行业代码表(PARAM)
 * @Date 2019-04-12 09:38:32
 * @Version 1.0
 */

@Data
@EqualsAndHashCode(callSuper=false)
@TableType(name="CIF_BUSINESS",value= TableTypeEnum.PARAM)
public class CifBusinessPo extends BasePo {

	/**
	* This field corresponds to the database column CIF_BUSINESS.business
	* @Description  行业代码
	*/
	@TablePk(index=1)
	private String business;
	/**
	* This field corresponds to the database column CIF_BUSINESS.business_desc
	* @Description  行业代码说明
	*/
	private String businessDesc;
	/**
	* This field corresponds to the database column CIF_BUSINESS.company
	* @Description  法人代码
	*/
	private String company;
	/**
	* This field corresponds to the database column CIF_BUSINESS.tran_timestamp
	* @Description  交易时间戳
	*/
	private String tranTimestamp;
	/**
	* This field corresponds to the database column CIF_BUSINESS.tran_time
	* @Description  交易时间
	*/
	private Long tranTime;
}
