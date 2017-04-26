package com.mplescano.springevents.multicaster;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;

public class DistributiveEventMulticaster implements
		ApplicationEventMulticaster {
	
    private ApplicationEventMulticaster asyncEventMulticaster;
    
    private ApplicationEventMulticaster syncEventMulticaster;

    public DistributiveEventMulticaster(ApplicationEventMulticaster syncEventMulticaster, ApplicationEventMulticaster asyncEventMulticaster) {
    	this.syncEventMulticaster = syncEventMulticaster;
    	this.asyncEventMulticaster = asyncEventMulticaster;
    }
    
	@Override
	public void addApplicationListener(ApplicationListener listener) {
        // choose multicaster by annotation
        if (listener.getClass().getAnnotation(AsyncListener.class) != null) {
            asyncEventMulticaster.addApplicationListener(listener);
        } else {
            syncEventMulticaster.addApplicationListener(listener);
        }
	}

	@Override
	public void addApplicationListenerBean(String listenerBeanName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeApplicationListener(ApplicationListener listener) {
        asyncEventMulticaster.removeApplicationListener(listener);
        syncEventMulticaster.removeApplicationListener(listener);
	}

	@Override
	public void removeApplicationListenerBean(String listenerBeanName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeAllListeners() {
        syncEventMulticaster.removeAllListeners();
        asyncEventMulticaster.removeAllListeners();
	}

	@Override
	public void multicastEvent(ApplicationEvent event) {
        syncEventMulticaster.multicastEvent(event);
        asyncEventMulticaster.multicastEvent(event);
	}

    // ******************** SETTERS ********************

    /*public void setAsyncEventMulticaster(ApplicationEventMulticaster asyncEventMulticaster) {
        this.asyncEventMulticaster = asyncEventMulticaster;
    }

    public void setSyncEventMulticaster(ApplicationEventMulticaster syncEventMulticaster) {
        this.syncEventMulticaster = syncEventMulticaster;
    }*/
}