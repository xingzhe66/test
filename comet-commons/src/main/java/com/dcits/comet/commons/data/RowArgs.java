package com.dcits.comet.commons.data;

import com.dcits.comet.commons.utils.BusiUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>Title: RowArgs.java</p>
 * <p>Description: 翻页查询参数</p>
 * <p>Copyright: Copyright (c) 2014-2019</p>
 * <p>Company: dcits</p>
 * <p>2019/4/11 14:29</p>
 *
 * @author cuimh
 * @version v1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RowArgs {

    public RowArgs() {
    }

    public RowArgs(int offset, int currentPage, int limit) {
        this.offset = offset;
        this.pageIndex = currentPage;
        this.limit = limit;
    }

    /**
     * 偏移量
     */
    private int offset = 0;

    /**
     * 当前页数
     */
    private int pageIndex = 0;

    /**
     * 查询条数
     */
    private int limit = 10;
}
