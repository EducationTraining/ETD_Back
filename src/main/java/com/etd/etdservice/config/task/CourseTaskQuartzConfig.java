package com.etd.etdservice.config.task;

import com.etd.etdservice.task.CourseTask;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CourseTaskQuartzConfig {

    private static final String COURSE_TASK_IDENTITY = "CourseTaskQuartz";

    @Bean(name = "courseQuartzDetail")
    public JobDetail courseQuartzDetail(){
        return JobBuilder.newJob(CourseTask.class).withIdentity(COURSE_TASK_IDENTITY).storeDurably().build();
    }

    @Bean(name = "courseQuartzTrigger")
    public Trigger courseQuartzTrigger(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//        		.withIntervalInSeconds(10)  //10s执行一次 for test
                .withIntervalInHours(6)  //6个小时执行一次
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(courseQuartzDetail())
                .withIdentity(COURSE_TASK_IDENTITY)
                .withSchedule(scheduleBuilder)
                .build();
    }
}
