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

package io.github.thierrysquirrel.jellyfish.thread.scheduled.agent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classname: AbstractThreadScheduledAgent
 * Description:
 * Date:2025/1/19
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public abstract class AbstractThreadScheduledAgent implements Runnable {

    private static final Logger logger = Logger.getLogger(AbstractThreadScheduledAgent.class.getName());

    private Runnable runnable;
    private int millisecond;
    private AtomicBoolean isDeleteAll;

    protected AbstractThreadScheduledAgent(Runnable runnable, int millisecond, AtomicBoolean isDeleteAll) {
        this.runnable = runnable;
        this.millisecond = millisecond;
        this.isDeleteAll = isDeleteAll;
    }

    protected abstract void agentRun(Runnable runnable, int millisecond, AtomicBoolean isDeleteAll);

    @Override
    public void run() {
        agentRun(this.runnable,
                this.millisecond,
                this.isDeleteAll);
    }

    protected void lockAwait(ReentrantLock containerMutex, Condition containerCondition, int millisecond) {
        try {
            containerMutex.lock();
            containerCondition.await(millisecond, TimeUnit.MILLISECONDS);
            containerMutex.unlock();
        } catch (InterruptedException e) {
            String loeMsg = "lockAwait Error";
            logger.log(Level.WARNING, loeMsg, e);
        }
    }
}
