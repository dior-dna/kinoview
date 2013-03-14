package ru.atott.kinoview.web.schedule;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class UpdateDataJob extends QuartzJobBean {
    private UpdateDataTask updateDataTask;

    public UpdateDataTask getUpdateDataTask() {
        return updateDataTask;
    }

    public void setUpdateDataTask(UpdateDataTask updateDataTask) {
        this.updateDataTask = updateDataTask;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            updateDataTask.update();
        } catch (Exception e) {

        }
    }
}
