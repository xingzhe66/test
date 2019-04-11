package com.dcits.comet.commons.utils;

import com.dcits.comet.commons.data.RowArgs;
import com.dcits.comet.commons.data.head.AppHead;


/**
 * <p>Title: PageUtil.java</p>
 * <p>Description: 翻页相关</p>
 * <p>Copyright: Copyright (c) 2014-2019</p>
 * <p>Company: dcits</p>
 * <p>2019/4/11 14:13</p>
 *
 * @author cuimh
 * @version v1.0
 */
public class PageUtil {

    /**
     * 应用头与RowAags转换方法
     *
     * @param appHead 应用头
     * @return 根据业务头计算翻页的参数；如果AppHead中的TotalNum为-1返回Null
     * @throws com.dcits.comet.commons.exception.BusinessException
     */
    public static RowArgs convertAppHead(AppHead appHead) {
        if (appHead == null) {
            throw BusiUtil.createBusinessException("100311");
        }
        //上翻或者下翻  1：下翻 0：上翻
        int pgupOrPgdn = Integer.valueOf(appHead.getPgupOrPgdn());
        //每页记录总数，即请求查询记录数
        int totalNum = Integer.valueOf(appHead.getTotalNum());
        //当前记录数
        int currentNum = Integer.valueOf(appHead.getCurrentNum());
        // 约定totalNum=-1时，默认不翻页，返回NUll
        if (totalNum == -1) {
            return null;
        }
        // 每页记录数不能小于-1或者等于0
        if (totalNum <= 0 || currentNum < 0) {
            throw BusiUtil.createBusinessException("100312");
        }
        //当前页数，（0~totalNum-1为第一页，currentNum=totalNum时为第二页）
        int currentPage = (currentNum / totalNum) + 1;
        //当前页首记录
        int start = (currentPage - 1) * totalNum;
        if (pgupOrPgdn == 1) {
            return new RowArgs(start, currentPage, totalNum);
        } else {
            start -= totalNum;
            if (start < 0) {
                start = 0;
            }

            return new RowArgs(start, currentPage, totalNum);
        }
    }
}
