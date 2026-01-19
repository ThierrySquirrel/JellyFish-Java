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

package io.github.thierrysquirrel.jellyfish.completable.future;

import io.github.thierrysquirrel.jellyfish.container.JellyfishContainer;
import io.github.thierrysquirrel.jellyfish.container.build.JellyfishContainerBuild;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Classname: CompletableFutureOne
 * Description:
 * Date:2025/1/19
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class CompletableFutureOne<V> {

    private static final Logger logger = Logger.getLogger(CompletableFutureOne.class.getName());

    private AtomicBoolean isComplete;
    private V value;
    private Condition timeoutCondition;
    private ReentrantLock timeoutConditionMutex;


    public CompletableFutureOne() {
        isComplete = new AtomicBoolean(Boolean.FALSE);
        timeoutConditionMutex = new ReentrantLock();
        timeoutCondition = timeoutConditionMutex.newCondition();
    }

    public void tryOneComplete(V value) {
        this.value = value;
        isComplete.set(Boolean.TRUE);

        timeoutConditionMutex.lock();
        timeoutCondition.signal();
        timeoutConditionMutex.unlock();
    }

    public JellyfishContainer<V> tryOneGet(int milliseconds, int maxTryCount) {

        boolean complete;

        for (int i = 0; i < maxTryCount; i++) {
            complete = this.isComplete.get();
            if (complete) {
                return JellyfishContainerBuild.buildSuccess(this.value);
            }

            lockAndAwait(milliseconds);

            complete = this.isComplete.get();
            if (complete) {
                return JellyfishContainerBuild.buildSuccess(this.value);
            }
        }
        return JellyfishContainerBuild.buildFail();
    }

    private void lockAndAwait(int milliseconds) {
        try {
            timeoutConditionMutex.lock();
            timeoutCondition.await(milliseconds, TimeUnit.MILLISECONDS);
            timeoutConditionMutex.unlock();
        } catch (InterruptedException e) {
            String loeMsg = "lockAndAwait Error";
            logger.log(Level.WARNING, loeMsg, e.getMessage());
        }

    }


}
