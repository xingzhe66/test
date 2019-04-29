package com.dcits.comet.batch.launcher;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description
 */
public interface IJobLauncher {

    public JobExeResult run(String jobName, JobParam jobParam);

}
