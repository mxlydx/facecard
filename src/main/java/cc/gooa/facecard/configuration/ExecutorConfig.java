package cc.gooa.facecard.configuration;


import cc.gooa.facecard.base.ThreadPoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
public class ExecutorConfig {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Bean
    public Executor asyncExecutor() {
        logger.info("start asyncExecutor");
        ThreadPoolProperties properties = this.loadThreadPoolProperties();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(properties.getCorePoolSize());
        //配置最大线程数
        executor.setMaxPoolSize(properties.getCorePoolSize());
        //配置队列大小
        executor.setQueueCapacity(properties.getQueueCapacity());
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("async-promise-");
        /**
         * rejection-policy：当pool已经达到max size的时候，如何处理新任务
         * CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
         */
        switch (properties.getRejectedExecutionHandler()) {
            case "abortPolicy":
                executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
                break;
            case "callerRunsPolicy":
                executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
                break;
            case "discardOldestPolicy":
                executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
                break;
            case "discardPolicy":
                executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
                break;
            default:
                executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
                break;
        }
        //初始化执行器
        executor.initialize();
        return executor;
    }
    @Bean
    @ConfigurationProperties("executor-config")
    public ThreadPoolProperties loadThreadPoolProperties() {
        return new ThreadPoolProperties();
    }
}
