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

package io.github.thierrysquirrel.jellyfish.thread.scheduled.one;

import io.github.thierrysquirrel.jellyfish.thread.scheduled.agent.execute.ThreadScheduledAgentExecute;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Classname: ThreadScheduledOne
 * Description:
 * Date:2025/1/19
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class ThreadScheduledOne {
    private AtomicBoolean isDeleteAll;

    public ThreadScheduledOne() {
        init();
    }

    public void execute(Runnable runnable, int millisecond) {
        ThreadScheduledAgentExecute agent = new ThreadScheduledAgentExecute(runnable, millisecond, isDeleteAll);
        new Thread(agent).start();
    }

    public void deleteAll() {
        isDeleteAll.set(Boolean.TRUE);
    }


    private void init() {
        isDeleteAll = new AtomicBoolean(Boolean.FALSE);
    }

}
