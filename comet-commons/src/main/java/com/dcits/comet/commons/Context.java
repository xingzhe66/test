/**
 * <p>Title: Context.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: dcits</p>
 *
 * @author Tim
 * @update 2014年11月13日 下午4:36:49
 * @version V1.0
 */
package com.dcits.comet.commons;

import com.dcits.comet.commons.data.head.AppHead;
import com.dcits.comet.commons.data.head.SysHead;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 业务全局上下文
 *
 * @author ChengLiang
 */
@Getter
@Builder
@ToString
public class Context implements Serializable {

    private static Logger logger = LoggerFactory.getLogger(Context.class);

    private static final long serialVersionUID = -1L;


    /**
     * 系统运行日期
     */
    @Setter
    private String runDate;

    /**
     * 上一系统运行日期
     */
    @Setter
    private String lastRunDate;

    /**
     * 下一系统运行日期
     */
    @Setter
    private String nextRunDate;

    /**
     * 原交易日期
     */
    private String tranDate;

    /**
     * 原交易机构
     */
    private String tranBranch;

    /**
     * 交易参考号
     */

    private String reference;

    /**
     * 屏幕号
     */
    private String programId;
    /**
     * Mac校验值
     */
    private String macValue;
    /**
     * 是否批处理
     */
    private boolean isBatch;

    /**
     * 系统ID
     */
    private String systemId;

    /**
     * 渠道
     */
    private String sourceType;

    /**
     * 渠道流水号
     */
    private String seqNo;

    /**
     * 柜员编号
     */
    private String userId;
    /**
     * 操作员语言
     */
    private String userLang;
    /**
     * 法人
     */
    private String company;
    /**
     * 授权柜员
     */
    private String authUserId;
    /**
     * 是否允许跨法人查询
     */
    private String multiCorpQueryAllow;

    /**
     * 是否允许法人信息共享
     */
    private String multiInfoShare;

    /**
     * 数据路由关键字段
     */
    private String routerKey;
    /**
     * 应用头
     */
    private AppHead appHead;
    /**
     * 系统头
     */
    private SysHead sysHead;

    /**
     * @fields platformId
     */
    private String platformId;

    /**
     * @fields map
     */
    private Map<String, Object> definedMap = new HashMap<>();


    public void init(Context context) {
        ThreadLocalManager.setBusiContext(context);
    }


    /**
     * 获取上下文
     *
     * @return
     */
    public static Context getInstance() {
        Context contexts = ThreadLocalManager.getBusiContext();
        return contexts;
    }

    /**
     * 获取上下文栈对象
     *
     * @return
     */
    private static Stack<Context> getContextStack() {
        Stack<Context> contexts = ThreadLocalManager.getBusiContext();
        if (null == contexts) {
            contexts = new Stack<>();
            ThreadLocalManager.setBusiContext(contexts);
            logger.info("Create a stack and fill in ThreadLocal!");
        }
        return contexts;
    }

    /**
     * 将上下文压入栈顶
     *
     * @param context
     */
    public static Context pushContext(Context context) {
        logger.info("Press the context into the top of the stack!");
        Stack<Context> contexts = getContextStack();
        return contexts.push(context);
    }

    /**
     * 移除栈顶对象
     *
     * @return
     */
    public static Context popContext() {
        Stack<Context> contexts = getContextStack();
        if (contexts.size() == 0) {
            return null;
        }
        return contexts.pop();
    }

    /**
     * 清空上下文
     *
     * @description
     * @version 1.0
     * @author Tim
     * @update 2015年1月19日 下午3:20:27
     */
    public void cleanResource() {
        // 为了保持向下兼容性。对此方法调整为移除栈顶对象，并将新的对象压入栈顶；
        //popContext();
        //pushContext(new Context());
        ThreadLocalManager.remove();
    }

    public String getTranDate() {
        return sysHead.getTranDate();
    }

    public String getTranBranch() {
        return sysHead.getBranchId();
    }

    public String getReference() {
        return reference;
    }

    public String getProgramId() {
        return sysHead.getProgramId();
    }

    public boolean isBatch() {
        return isBatch;
    }

    public String getSystemId() {
        return sysHead.getSystemId();
    }

    public String getSourceType() {
        return sysHead.getSourceType();
    }

    public String getUserId() {
        return sysHead.getUserId();
    }

    public String getUserLang() {
        return sysHead.getUserLang();
    }

    public String getCompany() {
        return sysHead.getCompany();
    }

    public String getAuthUserId() {
        return sysHead.getAuthUserId();
    }

    public String getMultiCorpQueryAllow() {
        return multiCorpQueryAllow;
    }

    public String getMultiInfoShare() {
        return multiInfoShare;
    }
}
