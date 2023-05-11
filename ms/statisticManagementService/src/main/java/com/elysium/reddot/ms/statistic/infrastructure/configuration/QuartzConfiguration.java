package com.elysium.reddot.ms.statistic.infrastructure.configuration;

import com.elysium.reddot.ms.statistic.infrastructure.outbound.job.CalculateDailyUserMessagesStatisticJob;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class QuartzConfiguration {

    private final CalculateDailyUserMessagesStatisticJob statisticApplicationService;

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

        return TriggerBuilder.newTrigger()
                .forJob(userMessageStatisticJobDetail())
                .withIdentity("userMessageStatisticTrigger")
                .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever())
                .build();
    }


}
