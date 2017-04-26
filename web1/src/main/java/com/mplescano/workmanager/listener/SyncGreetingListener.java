package com.mplescano.workmanager.listener;

import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.mplescano.workmanager.pojo.event.UserGreetedEvent;

@Component
public class SyncGreetingListener implements ApplicationListener<UserGreetedEvent> {

    @Override
    public void onApplicationEvent(UserGreetedEvent event) {
        String greeting = event.getGreetingData().getName();
        greeting += ", I wish you a pleasant day";
        event.getGreetingData().setName(greeting);
        LogFactory.getLog(this.getClass()).info("Greeted:" + greeting);
    }

}
