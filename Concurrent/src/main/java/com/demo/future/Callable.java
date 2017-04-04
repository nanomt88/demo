package com.demo.future;

/**
 * @Author: hongxudong@lxfintech.com
 * @Created: 2017/4/4 ионГ9:19
 * @Description: //TODO
 */

public interface Callable<T> {

    T call() throws InterruptedException;

}
