package guru.sfg.brewery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * Enable spring config to run a scheduled task
 * and will be picked up by any scheduling to run a schedular
 */
@Configuration
@EnableScheduling
@EnableAsync
public class AccountUnlockConfig {

    @Bean
    TaskExecutor taskExecutor( ) {
        return new SimpleAsyncTaskExecutor();
    }
}
