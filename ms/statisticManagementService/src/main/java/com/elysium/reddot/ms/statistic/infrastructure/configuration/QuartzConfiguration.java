package com.elysium.reddot.ms.statistic.infrastructure.configuration;

import com.elysium.reddot.ms.statistic.application.service.StatisticApplicationServiceImpl;
import com.elysium.reddot.ms.statistic.infrastructure.outbound.job.CalculateDailyUserMessagesStatisticJob;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class QuartzConfiguration {

    private final StatisticApplicationServiceImpl statisticApplicationService;

    @Bean
    public JobDetail userMessageStatisticJobDetail() {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("statisticManagementService", statisticApplicationService);

        return JobBuilder.newJob(CalculateDailyUserMessagesStatisticJob.class)
                .withIdentity("userMessageStatisticJob")
                .setJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger userMessageStatisticJobTrigger() {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 0 * * ?");

        return TriggerBuilder.newTrigger()
                .forJob(userMessageStatisticJobDetail())
                .withIdentity("userMessageStatisticTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }


}
