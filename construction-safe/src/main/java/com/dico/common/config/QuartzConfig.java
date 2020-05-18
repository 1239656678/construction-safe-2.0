package com.dico.common.config;

import com.dico.modules.controller.InspectionPlanTask;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail teatQuartzDetail() {
        return JobBuilder.newJob(InspectionPlanTask.class).withIdentity("inspectionPlanTask").storeDurably().build();
    }

    @Bean
    public Trigger testQuartzTrigger() {
        return TriggerBuilder.newTrigger().forJob(teatQuartzDetail())
                .withIdentity("inspectionPlanTask")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 7 * * ? *"))
                .build();
    }
}