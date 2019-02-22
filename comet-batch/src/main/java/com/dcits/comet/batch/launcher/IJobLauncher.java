package com.dcits.comet.batch.launcher;

public interface IJobLauncher {

    public JobExeResult run(String jobName, JobParam jobParam);

}
