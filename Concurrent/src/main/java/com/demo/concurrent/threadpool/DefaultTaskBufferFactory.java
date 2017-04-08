package com.demo.concurrent.threadpool;

public class DefaultTaskBufferFactory implements TaskBufferFactory {

	@Override
    public <T> TaskBuffer<T> createInstance(){
        TaskBuffer<T> aTaskBuffer = new DefaultTaskBuffer<T>();
        return aTaskBuffer;
    }
}
