package com.mplescano.workmanager.listener;

import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.mplescano.springevents.multicaster.AsyncListener;
import com.mplescano.workmanager.pojo.event.UserGreetedEvent;

@AsyncListener
@Component
public class SlowGreetingListener implements ApplicationListener<UserGreetedEvent> {

    @Override
    public void onApplicationEvent(UserGreetedEvent event) {
        String name = event.getGreetingData().getName();
        name += " (sorry, but " + name + " sounds really stupid...)";
        event.getGreetingData().setName(name);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            
        }
        LogFactory.getLog(this.getClass()).info("Greeted:" + name);
    }

}
