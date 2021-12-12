package com.demo.concurrent.future;

/**
 * @Author: nanomt88@gmail.com
 * @Created: 2017/4/4 上午9:19
 * @Description: //TODO
 */

public interface Callable<T> {

    T call() throws InterruptedException;

}
