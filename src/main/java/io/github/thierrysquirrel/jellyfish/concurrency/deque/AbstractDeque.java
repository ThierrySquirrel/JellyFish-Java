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

package io.github.thierrysquirrel.jellyfish.concurrency.deque;

import io.github.thierrysquirrel.jellyfish.container.JellyfishContainer;
import io.github.thierrysquirrel.jellyfish.container.build.JellyfishContainerBuild;

import java.util.Deque;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Classname: AbstractDeque
 * Description:
 * Date:2025/1/19
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public abstract class AbstractDeque<V> {
    private Deque<V>[] containerAll;
    private AtomicInteger containerOffset;
    private int locksOffset;
    private ReadWriteLock[] locksContainer;

    protected AbstractDeque(int containerAll) {
        init(containerAll);
    }


    public void pushBack(V value) {
        int mutexOffset = getMutexOffset(value);
        locksContainer[mutexOffset].writeLock().lock();
        containerAll[mutexOffset].addLast(value);
        locksContainer[mutexOffset].writeLock().unlock();
    }

    public JellyfishContainer<V> tryPopBack() {
        int thisContainerOffset = getDequeOffset();
        int size = this.locksOffset;

        for (int i = 0; i < size; i++) {
            locksContainer[thisContainerOffset].writeLock().lock();
            boolean isEmpty = containerAll[thisContainerOffset].isEmpty();

            if (!isEmpty) {

                V value = containerAll[thisContainerOffset].getLast();
                containerAll[thisContainerOffset].removeLast();
                locksContainer[thisContainerOffset].writeLock().unlock();

                return JellyfishContainerBuild.buildSuccess(value);
            }
            locksContainer[thisContainerOffset].writeLock().unlock();

            thisContainerOffset++;

            if (thisContainerOffset == size) {
                thisContainerOffset = 0;
            }
        }
        return JellyfishContainerBuild.buildFail();
    }

    public int getSize() {
        int allSize = 0;
        int size = this.locksOffset;
        for (int i = 0; i < size; i++) {
            locksContainer[i].readLock().lock();
            allSize += containerAll[i].size();
            locksContainer[i].readLock().unlock();
        }
        return allSize;
    }

    protected abstract Deque<V> popBackAll();

    protected Deque<V> popBackAllDeque(Deque<V> deque) {
        int size = this.locksOffset;

        for (int i = 0; i < size; i++) {
            locksContainer[i].writeLock().lock();
            deque.addAll(containerAll[i]);
            containerAll[i].clear();
            locksContainer[i].writeLock().unlock();
        }
        return deque;
    }

    private int getMutexOffset(V key) {
        int signedHash = Objects.hash(key);
        int looks = this.locksOffset;
        return unSignedInt(signedHash, looks);
    }

    private int getDequeOffset() {
        int increment = containerOffset.incrementAndGet();
        int looks = this.locksOffset;
        return unSignedInt(increment, looks);
    }

    private int unSignedInt(int value, int offset) {
        return ((value % offset) + offset) % offset;
    }

    protected abstract void init(int offset);

    public Deque<V>[] getContainerAll() {
        return containerAll;
    }

    public void setContainerAll(Deque<V>[] containerAll) {
        this.containerAll = containerAll;
    }

    public AtomicInteger getContainerOffset() {
        return containerOffset;
    }

    public void setContainerOffset(AtomicInteger containerOffset) {
        this.containerOffset = containerOffset;
    }

    public int getLocksOffset() {
        return locksOffset;
    }

    public void setLocksOffset(int locksOffset) {
        this.locksOffset = locksOffset;
    }

    public ReadWriteLock[] getLocksContainer() {
        return locksContainer;
    }

    public void setLocksContainer(ReadWriteLock[] locksContainer) {
        this.locksContainer = locksContainer;
    }
}
