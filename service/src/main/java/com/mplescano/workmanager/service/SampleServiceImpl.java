package com.mplescano.workmanager.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by Simone Ricciardi on 25/02/14.
 */
@Service
public class SampleServiceImpl implements SampleService {

	private Log logger = LogFactory.getLog(SampleServiceImpl.class);
	
    @PostConstruct
    private void init(){
    	logger.info("---------------------------------");
    	logger.info("---------------------------------");
    	logger.info("------SampleService created------");
    	logger.info("---------------------------------");
    	logger.info("---------------------------------");

    }

   public String sayHello(String greeting) {
      return "Hello "+greeting+" from " + this.toString();
   }

}