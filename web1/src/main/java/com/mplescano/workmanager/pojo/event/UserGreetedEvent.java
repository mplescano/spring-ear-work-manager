package com.mplescano.workmanager.pojo.event;

import org.springframework.context.ApplicationEvent;

import com.mplescano.workmanager.pojo.dto.GreetingData;

public class UserGreetedEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private final GreetingData data;

    public UserGreetedEvent(Object source, GreetingData greetingData) {
        super(source);
        this.data = greetingData;
    }

    public GreetingData getGreetingData() {
        return data;
    }
}
