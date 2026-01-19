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

package io.github.thierrysquirrel.jellyfish.thread.agent.execute;

import io.github.thierrysquirrel.jellyfish.concurrency.deque.array.ConcurrencyArrayDeque;
import io.github.thierrysquirrel.jellyfish.container.JellyfishContainer;
import io.github.thierrysquirrel.jellyfish.thread.agent.AbstractThreadAgent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Classname: ThreadAgentExecute
 * Description:
 * Date:2025/1/19
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class ThreadAgentExecute extends AbstractThreadAgent {
    public ThreadAgentExecute(ConcurrencyArrayDeque<Runnable> containerAll, ReentrantLock containerMutex, Condition containerCondition, AtomicBoolean isDeleteAll) {
        super(containerAll, containerMutex, containerCondition, isDeleteAll);
    }

    @Override
    protected void agentRun(ConcurrencyArrayDeque<Runnable> containerAll, ReentrantLock containerMutex, Condition containerCondition, AtomicBoolean isDeleteAll) {
        super.allInit();

        while (!isDeleteAll.get()) {

            JellyfishContainer<Runnable> threadRun = containerAll.tryPopBack();
            if (threadRun.isEmpty()) {
                super.lockAwait();
                continue;
            }
            threadRun.getValue().run();
        }

    }
}
