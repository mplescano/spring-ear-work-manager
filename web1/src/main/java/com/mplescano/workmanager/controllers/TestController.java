package com.mplescano.workmanager.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mplescano.workmanager.pojo.dto.GreetingData;
import com.mplescano.workmanager.pojo.event.UserGreetedEvent;
import com.mplescano.workmanager.service.SampleService;
import com.mplescano.workmanager.service.SampleServiceImpl;

@Controller
public class TestController {
	
	private Log logger = LogFactory.getLog(SampleServiceImpl.class);
	
	@Autowired
	private SampleService sampleService;
	
    @Autowired
    private ApplicationEventPublisher eventPublisher;
	
	@RequestMapping(value = "/first", method = RequestMethod.GET)//, produces = MediaType.TEXT_PLAIN_VALUE
	@ResponseBody
	public String testAction() {
		logger.info("testAction init");
		String result = sampleService.sayHello("SampleWeb1");
		logger.info("testAction end");
		return result;
	}

	@RequestMapping(value = "/hello/{name}", method = RequestMethod.GET)//, produces = MediaType.TEXT_PLAIN_VALUE
	@ResponseBody
	public String greetingAction(@PathVariable String name) {
		logger.info("greetingAction init");
		String result = sampleService.sayHello(name);
		logger.info("greetingAction end");
		onAfterGreetingEvent(name);
		return result;
	}
	
	private void onAfterGreetingEvent(String name) {
        // create the event with data
        GreetingData greetingData = new GreetingData(name);
        UserGreetedEvent userGreeted = new UserGreetedEvent(this, greetingData);
        // publish it
        eventPublisher.publishEvent(userGreeted);
	}
}
