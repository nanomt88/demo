package com.demo.threadpool;

import org.apache.log4j.Logger;

public class DefaultExecutorListener implements ExecutorListener {

	private Logger logger = Logger.getLogger(DefaultExecutorListener.class);
	
    @Override
    public void beforeExecute() {
    	logger.debug("-----beforeExecute------");
    }

    @Override
    public void afterExecute() {
    	logger.debug("-----afterExecute------");
    }

    @Override
    public void exceptionHandle(Exception ex) {
    	logger.debug("-----exceptionHandle------");
    }
    
    @Override
    public void faildExecute() {
    	logger.debug("-----faildExecute------");
    }

    @Override
    public void retryExecute(int i) throws Exception {
    	logger.debug("-----retryExecute------" + i);
    }
}
