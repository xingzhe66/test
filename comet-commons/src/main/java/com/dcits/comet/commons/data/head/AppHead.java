package com.dcits.comet.commons.data.head;

import com.dcits.comet.commons.data.BaseData;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author chengliang
 * @Description //TODO
 * @Date 2019-02-26 16:17
 * @Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class AppHead extends BaseData {

    /**
     * 本页记录总数
     */
    private String totalNum ="-1";


    /**
     * 上翻/下翻标志<br>
     * PGUP_OR_PGDN<br>
     * seqNo:1<br>
     * dataType:STRING(1)<br>
     * cons:取值范围：<br>0-上翻；<br>1-下翻；<br>
     */
    private String pgupOrPgdn = "1";

    /**
     * 总笔数<br>
     * TOTAL_ROWS<br>
     * seqNo:6<br>
     * dataType:STRING(12)<br>
     * cons:数字
     */
    private String totalRows;

    /**
     * 汇总标志<br>
     * TOTAL_FLAG<br>
     * seqNo:8<br>
     * dataType:STRING(1)<br>
     * cons:取值范围：<br>N-不查询总笔数；<br>E-查询总记录数；
     */
    private String totalFlag = "E";


    /**
     * 当前记录号<br>
     * CURRENT_NUM<br>
     * seqNo:3<br>
     * dataType:STRING(12)<br>
     * cons:数字
     */
    private String currentNum = "0";
}
