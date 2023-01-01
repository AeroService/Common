/*
 * Copyright 2020-2022 Conelux
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.conelux.common.runnable;

/**
 * The {@code ThrowableRunnable} interface should be implemented by any class whose instances are intended to be
 * executed by a thread. The class must define a method of no arguments called {@code run}. Function might throw a
 * checked {@link Throwable}.
 * <p>
 * This interface is designed to provide a common protocol for objects that wish to execute code while they are active.
 * For example, {@code ThrowableRunnable} is implemented by class {@code Thread}. Being active simply means that a
 * thread has been started and has not yet been stopped.
 * <p>
 * In addition, {@code ThrowableRunnable} provides the means for a class to be active while not subclassing
 * {@code Thread}. A class that implements {@code ThrowableRunnable} can run without subclassing {@code Thread} by
 * instantiating a {@code Thread} instance and passing itself in as the target.  In most cases, the
 * {@code ThrowableRunnable} interface should be used if you are only planning to override the {@code run()} method and
 * no other {@code Thread} methods. This is important because classes should not be subclassed unless the programmer
 * intends on modifying or enhancing the fundamental behavior of the class.
 *
 * @see java.lang.Thread
 * @see java.util.concurrent.Callable
 * @see Runnable
 */
@FunctionalInterface
public interface ThrowableRunnable<T extends Throwable> {

    /**
     * When an object implementing interface {@code ThrowableRunnable} is used to create a thread, starting the thread
     * causes the object's {@code run} method to be called in that separately executing thread.
     * <p>
     * The general contract of the method {@code run} is that it may take any action whatsoever.
     *
     * @throws T the potentially thrown {@link Throwable}
     * @see java.lang.Thread#run()
     */
    void run() throws T;
}
