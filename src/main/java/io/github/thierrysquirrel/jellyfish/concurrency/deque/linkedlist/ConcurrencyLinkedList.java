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

package io.github.thierrysquirrel.jellyfish.concurrency.deque.linkedlist;

import io.github.thierrysquirrel.jellyfish.concurrency.deque.AbstractDeque;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Classname: ConcurrencyLinkedList
 * Description:
 * Date:2025/1/19
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class ConcurrencyLinkedList<V> extends AbstractDeque<V> {

    public ConcurrencyLinkedList(int containerAll) {
        super(containerAll);
    }

    @Override
    public Deque<V> popBackAll() {
        return super.popBackAllDeque(new LinkedList<>());
    }

    @Override
    protected void init(int offset) {
        setLocksOffset(offset);
        setContainerOffset(new AtomicInteger(0));

        Deque<V>[] thisDeque = new LinkedList[offset];
        ReadWriteLock[] thisLock = new ReadWriteLock[offset];

        for (int i = 0; i < offset; i++) {
            thisDeque[i] = new LinkedList<>();
            thisLock[i] = new ReentrantReadWriteLock();
        }

        setContainerAll(thisDeque);
        setLocksContainer(thisLock);
    }
}
