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

package io.github.thierrysquirrel.jellyfish.concurrency.deque.array;

import io.github.thierrysquirrel.jellyfish.concurrency.deque.AbstractDeque;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * Classname: ConcurrencyArrayDeque
 * Description:
 * Date:2025/1/19
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class ConcurrencyArrayDeque<V> extends AbstractDeque<V> {
    public ConcurrencyArrayDeque(int containerAll) {
        super(containerAll);
    }

    @Override
    public Deque<V> popBackAll() {
        return super.popBackAllDeque(new ArrayDeque<>());
    }

    @Override
    protected void init(int offset) {
        setLocksOffset(offset);
        setContainerOffset(new AtomicInteger(0));

        Deque<V>[] thisDeque = new ArrayDeque[offset];
        ReadWriteLock[] thisLock = new ReadWriteLock[offset];

        for (int i = 0; i < offset; i++) {
            thisDeque[i] = new ArrayDeque<>();
            thisLock[i] = new ReentrantReadWriteLock();
        }

        setContainerAll(thisDeque);
        setLocksContainer(thisLock);
    }
}
