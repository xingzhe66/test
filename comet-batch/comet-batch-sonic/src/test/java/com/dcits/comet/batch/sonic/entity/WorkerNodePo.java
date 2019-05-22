package com.dcits.comet.batch.sonic.entity;

import com.dcits.comet.dao.annotation.TableType;
import com.dcits.comet.dao.annotation.TableTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author leijian
 * @version 1.0
 * @date 2019/4/25 17:39
 **/
@Data
@EqualsAndHashCode(callSuper=false)
@TableType(name="WORKER_NODE",value= TableTypeEnum.PARAM)
public class WorkerNodePo implements Serializable {

    private long id;
    private String hostName;
    private String port;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }


    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

}
