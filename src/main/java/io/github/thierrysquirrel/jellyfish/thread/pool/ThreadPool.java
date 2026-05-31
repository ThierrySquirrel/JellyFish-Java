/**
 * Copyright 2026/6/1 ThierrySquirrel
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

package io.github.thierrysquirrel.jellyfish.thread.pool;

import io.github.thierrysquirrel.jellyfish.completable.future.CompletableFutureOne;
import io.github.thierrysquirrel.jellyfish.concurrency.deque.array.ConcurrencyArrayDeque;
import io.github.thierrysquirrel.jellyfish.thread.agent.execute.ThreadAgentExecute;
import io.github.thierrysquirrel.jellyfish.thread.pool.constant.ThreadPoolConstant;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Classname: ThreadPool
 * Description:
 * Date:2026/6/1
 *
 * @author ThierrySquirrel
 * @since JDK25
 **/
public class ThreadPool {

    private int maxThreadSize;
    private ConcurrencyArrayDeque<Runnable> containerAll;
    private ReentrantLock containerMutex;
    private Condition containerCondition;
    private AtomicBoolean isDeleteAll;

    private AtomicInteger threadSleepSize;
    private CompletableFutureOne<Boolean> threadAllStart;

    public ThreadPool(int maxThreadSize) {
        threadSleepSize = new AtomicInteger(maxThreadSize);
        threadAllStart = new CompletableFutureOne<>();
        init(maxThreadSize);

        threadAllStart.tryOneGet(ThreadPoolConstant.DEFAULT_MILLISECONDS, ThreadPoolConstant.DEFAULT_MAX_TRY_COUNT);

    }

    public void execute(Runnable runnable) {
        containerAll.pushBack(runnable);

        containerMutex.lock();
        containerCondition.signal();
        containerMutex.unlock();
    }

    public void deleteAll() {
        isDeleteAll.set(Boolean.TRUE);
        containerMutex.lock();
        containerCondition.signalAll();
        containerMutex.unlock();

        containerAll.popBackAll();
    }

    private void init(int maxThreadSize) {
        this.maxThreadSize = maxThreadSize;
        containerAll = new ConcurrencyArrayDeque<>(maxThreadSize);
        containerMutex = new ReentrantLock();
        containerCondition = containerMutex.newCondition();
        isDeleteAll = new AtomicBoolean(false);

        for (int i = 0; i < maxThreadSize; i++) {
            ThreadAgentExecute threadRunAgent = new ThreadAgentExecute(this.containerAll,
                    this.containerMutex,
                    this.containerCondition,
                    this.isDeleteAll,
                    this.threadSleepSize,
                    this.threadAllStart
            );
            new Thread(threadRunAgent).start();
        }
    }

}
