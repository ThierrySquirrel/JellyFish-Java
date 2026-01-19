/**
 * Copyright 2025/1/19 ThierrySquirrel
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

package io.github.thierrysquirrel.jellyfish.thread.agent;

import io.github.thierrysquirrel.jellyfish.concurrency.deque.array.ConcurrencyArrayDeque;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Classname: AbstractThreadAgent
 * Description:
 * Date:2025/1/19
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public abstract class AbstractThreadAgent implements Runnable {

    private static final Logger logger = Logger.getLogger(AbstractThreadAgent.class.getName());

    private ConcurrencyArrayDeque<Runnable> containerAll;
    private ReentrantLock containerMutex;
    private Condition containerCondition;
    private AtomicBoolean isDeleteAll;

    protected AbstractThreadAgent(ConcurrencyArrayDeque<Runnable> containerAll, ReentrantLock containerMutex, Condition containerCondition, AtomicBoolean isDeleteAll) {
        this.containerAll = containerAll;
        this.containerMutex = containerMutex;
        this.containerCondition = containerCondition;
        this.isDeleteAll = isDeleteAll;
    }

    protected abstract void agentRun(ConcurrencyArrayDeque<Runnable> containerAll, ReentrantLock containerMutex, Condition containerCondition, AtomicBoolean isDeleteAll);

    @Override
    public void run() {
        agentRun(this.containerAll,
                this.containerMutex,
                this.containerCondition,
                this.isDeleteAll);
    }

    protected void lockAwait() {
        try {
            containerMutex.lock();
            containerCondition.await();
            containerMutex.unlock();
        } catch (InterruptedException e) {
            String loeMsg = "lockAwait Error";
            logger.log(Level.WARNING, loeMsg, e);
        }
    }

    protected void allInit() {
        lockAwait();
    }
}
