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

package io.github.thierrysquirrel.jellyfish.thread.scheduled.agent.execute;

import io.github.thierrysquirrel.jellyfish.thread.scheduled.agent.AbstractThreadScheduledAgent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Classname: ThreadScheduledAgentExecute
 * Description:
 * Date:2025/1/19
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class ThreadScheduledAgentExecute extends AbstractThreadScheduledAgent {
    public ThreadScheduledAgentExecute(Runnable runnable, int millisecond, AtomicBoolean isDeleteAll) {
        super(runnable, millisecond, isDeleteAll);
    }

    @Override
    protected void agentRun(Runnable runnable, int millisecond, AtomicBoolean isDeleteAll) {
        ReentrantLock containerMutex = new ReentrantLock();
        Condition containerCondition = containerMutex.newCondition();

        while (!isDeleteAll.get()) {
            runnable.run();

            lockAwait(containerMutex, containerCondition, millisecond);
        }
    }


}
