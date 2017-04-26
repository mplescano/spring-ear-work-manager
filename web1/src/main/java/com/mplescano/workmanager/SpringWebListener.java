package com.mplescano.workmanager;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import commonj.work.WorkManager;

@WebListener
public class SpringWebListener implements ServletContextListener {
    
    /**
     * In order to define a resource accesible by jndi java:comp/env/... 
     */
    @Resource(name = "wm/slowGreetingWorkManager", lookup = "slow-greeting-work-manager")
    private WorkManager slowGreetingWorkManager;

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //sce.getServletContext().getAttribute(arg0)
    }

}
