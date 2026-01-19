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

package io.github.thierrysquirrel.jellyfish.concurrency.map.tree;


import io.github.thierrysquirrel.jellyfish.concurrency.map.AbstractMap;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Classname: ConcurrencyTreeMap
 * Description:
 * Date:2025/1/19
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class ConcurrencyTreeMap<K, V> extends AbstractMap<K, V> {


    public ConcurrencyTreeMap(int offset) {
        super(offset);
    }

    @Override
    public Map<K, V> getAll() {
        return super.getAllMap(new TreeMap<>());
    }

    @Override
    protected void init(int offset) {
        setLocksOffset(offset);

        Map<K, V>[] thisMap = new TreeMap[offset];
        ReadWriteLock[] thisLock = new ReadWriteLock[offset];

        for (int i = 0; i < offset; i++) {
            thisMap[i] = new TreeMap<>();
            thisLock[i] = new ReentrantReadWriteLock();
        }

        setContainerAll(thisMap);
        setLocksContainer(thisLock);
    }
}
