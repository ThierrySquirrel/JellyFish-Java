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

package io.github.thierrysquirrel.jellyfish.thread.local.map;

import io.github.thierrysquirrel.jellyfish.concurrency.map.hash.ConcurrencyHashMap;
import io.github.thierrysquirrel.jellyfish.container.JellyfishContainer;
import io.github.thierrysquirrel.jellyfish.container.build.JellyfishContainerBuild;

/**
 * Classname: ThreadLocalMap
 * Description:
 * Date:2025/1/19
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class ThreadLocalMap<V> {
    private ConcurrencyHashMap<Long, V> containerAll;
    private int threadOffset;

    public ThreadLocalMap(int threadOffset) {
        this.threadOffset = threadOffset;
        this.containerAll = new ConcurrencyHashMap<>(threadOffset);
    }

    public void set(V value) {
        long key = getKey();
        containerAll.set(key, value);
    }

    public JellyfishContainer<V> get() {
        long key = getKey();

        boolean find = containerAll.isFind(key);
        if (find) {
            V value = containerAll.get(key);
            return JellyfishContainerBuild.buildSuccess(value);
        }
        return JellyfishContainerBuild.buildFail();
    }

    public void deleteValue() {
        long key = getKey();
        containerAll.deleteValue(key);
    }

    private long getKey() {
        return Thread.currentThread().threadId();
    }
}
