package com.etd.etdservice.task;

import com.etd.etdservice.serivce.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class CourseTask extends QuartzJobBean {
    @Autowired
    CourseService courseService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
        log.info("CourseTask-------- {}", sdf.format(new Date()));

    }

}