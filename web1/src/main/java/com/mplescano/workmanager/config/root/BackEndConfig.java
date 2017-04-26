package com.mplescano.workmanager.config.root;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.commonj.WorkManagerTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.mplescano.springevents.multicaster.DistributiveEventMulticaster;

@Configuration
@ComponentScan({ "com.mplescano.workmanager.listener" })
public class BackEndConfig {

    /**
     * It is crucial that the bean's id equals to applicationEventMulticaster.
     * The asynchronous configuration is achieved by setting the taskExecutor
     * property.
     * @see https://www.keyup.eu/en/blog/101-synchronous-and-asynchronous-spring-events-in-one-application 
     * @return
     */
    @Bean
    public ApplicationEventMulticaster applicationEventMulticaster() {
        SimpleApplicationEventMulticaster async = new SimpleApplicationEventMulticaster();
        async.setTaskExecutor(weblogicTaskExecutor());
        ApplicationEventMulticaster eventMulticaster = new DistributiveEventMulticaster(
                new SimpleApplicationEventMulticaster(),
                async
        );
        return eventMulticaster;
    }

    /*@Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }*/
    
    /**
     * 
     * <resource-ref>
     *      <res-ref-name>wm/slowGreetingWorkManager</res-ref-name>
     *      <res-type>commonj.work.WorkManager</res-type>
     *      <res-auth>Container</res-auth>
     *      <res-sharing-scope>Shareable</res-sharing-scope>
     * </resource-ref>
     * 
     * or equivalent
     * 
     * @Resource(name = "wm/slowGreetingWorkManager", lookup = "slow-greeting-work-manager")
     * private WorkManager slowGreetingWorkManager;
     * 
     * 
     * @see http://pcharvat.blogspot.pe/2010/06/workmanager-on-wls-103-with-spring-30.html
     * @see http://stackoverflow.com/questions/21516540/custom-thread-on-weblogic-server-11g
     * 
     * @return
     */
    @Bean
    public Executor weblogicTaskExecutor() {
        WorkManagerTaskExecutor taskExecutor = new WorkManagerTaskExecutor();
        taskExecutor.setWorkManagerName("java:comp/env/wm/slowGreetingWorkManager");
        taskExecutor.setResourceRef(true);
        
        return taskExecutor;
    }
    
}
