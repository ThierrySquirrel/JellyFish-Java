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

package io.github.thierrysquirrel.jellyfish.concurrency.map;

import io.github.thierrysquirrel.jellyfish.concurrency.map.absent.MapAbsent;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;


/**
 * Classname: AbstractMap
 * Description:
 * Date:2025/1/19
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public abstract class AbstractMap<K, V> {

    private Map<K, V>[] containerAll;
    private int locksOffset;
    private ReadWriteLock[] locksContainer;

    protected AbstractMap(int offset) {
        init(offset);
    }

    public void set(K key, V value) {
        int mutexOffset = getMutexOffset(key);

        this.locksContainer[mutexOffset].writeLock().lock();
        this.containerAll[mutexOffset].put(key, value);
        this.locksContainer[mutexOffset].writeLock().unlock();
    }

    public V get(K key) {
        int mutexOffset = getMutexOffset(key);

        this.locksContainer[mutexOffset].readLock().lock();
        V value = containerAll[mutexOffset].get(key);
        this.locksContainer[mutexOffset].readLock().unlock();

        return value;

    }

    public V getIfAbsent(K key, MapAbsent<K, V> mapAbsent) {
        V value=null;
        int mutexOffset = getMutexOffset(key);

        this.locksContainer[mutexOffset].readLock().lock();
        boolean find = containerAll[mutexOffset].containsKey(key);
        if (find) {
            value=containerAll[mutexOffset].get(key);
        }else {
            V absent = mapAbsent.absent(key);
            this.containerAll[mutexOffset].put(key, absent);
            value=absent;
        }
        this.locksContainer[mutexOffset].readLock().unlock();

        return value;
    }

    public boolean isFind(K key) {
        int mutexOffset = getMutexOffset(key);

        this.locksContainer[mutexOffset].readLock().lock();
        boolean find = containerAll[mutexOffset].containsKey(key);
        this.locksContainer[mutexOffset].readLock().unlock();

        return find;
    }

    public int getSize() {
        int allSize = 0;
        int forSize = this.locksOffset;

        for (int i = 0; i < forSize; i++) {
            this.locksContainer[i].readLock().lock();
            allSize += containerAll[i].size();
            this.locksContainer[i].readLock().unlock();
        }
        return allSize;
    }

    public void deleteValue(K key) {
        int mutexOffset = getMutexOffset(key);

        this.locksContainer[mutexOffset].writeLock().lock();
        containerAll[mutexOffset].remove(key);
        this.locksContainer[mutexOffset].writeLock().unlock();
    }

    protected abstract Map<K, V> getAll();

    protected Map<K, V> getAllMap(Map<K, V> map) {
        int forSize = this.locksOffset;

        for (int i = 0; i < forSize; i++) {
            locksContainer[i].readLock().lock();
            map.putAll(containerAll[i]);
            locksContainer[i].readLock().unlock();
        }
        return map;
    }

    private int getMutexOffset(K key) {
        int signedHash = Objects.hash(key);
        int looks = this.locksOffset;
        return unSignedInt(signedHash, looks);
    }

    private int unSignedInt(int value, int offset) {
        return ((value % offset) + offset) % offset;
    }

    protected abstract void init(int offset);

    public Map<K, V>[] getContainerAll() {
        return containerAll;
    }

    public void setContainerAll(Map<K, V>[] containerAll) {
        this.containerAll = containerAll;
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
