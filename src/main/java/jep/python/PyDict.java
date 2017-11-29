/**
 * Copyright (c) 2017 JEP AUTHORS.
 *
 * This file is licensed under the the zlib/libpng License.
 *
 * This software is provided 'as-is', without any express or implied
 * warranty. In no event will the authors be held liable for any
 * damages arising from the use of this software.
 * 
 * Permission is granted to anyone to use this software for any
 * purpose, including commercial applications, and to alter it and
 * redistribute it freely, subject to the following restrictions:
 * 
 *     1. The origin of this software must not be misrepresented; you
 *     must not claim that you wrote the original software. If you use
 *     this software in a product, an acknowledgment in the product
 *     documentation would be appreciated but is not required.
 * 
 *     2. Altered source versions must be plainly marked as such, and
 *     must not be misrepresented as being the original software.
 * 
 *     3. This notice may not be removed or altered from any source
 *     distribution.
 */
package jep.python;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jep.Jep;
import jep.JepException;

/**
 * 
 * A Java object that wraps a pointer to a Python dictionary and implements the
 * java.util.Map interface.
 * 
 *
 * @author njensen
 * @since 4.0
 */
public class PyDict<K, V> extends PyObject implements Map<K, V> {

    public PyDict(long tstate, long pyObject, Jep jep) throws JepException {
        super(tstate, pyObject, jep);
    }

    @Override
    public int size() {
        try (PyCallable len = (PyCallable) this.getAttr("__len__")) {
            int length = (int) len.call();
            return length;
        } catch (JepException e) {
            throw new RuntimeException("Error calling __len__", e);
        }
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        try (PyCallable has_key = (PyCallable) this.getAttr("has_key")) {
            return (boolean) has_key.call(key);
        } catch (JepException e) {
            throw new RuntimeException("Error calling has_key", e);
        }
    }

    @Override
    public boolean containsValue(Object value) {
        Collection<V> valuesCollection = values();
        return valuesCollection.contains(value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(Object key) {
        try (PyCallable getItem = (PyCallable) this.getAttr("get")) {
            return (V) getItem.call(key);
        } catch (JepException e) {
            throw new RuntimeException("Error calling get", e);
        }
    }

    @Override
    public V put(K key, V value) {
        V previous = null;
        if (containsKey(key)) {
            previous = get(key);
        }
        try (PyCallable setItem = (PyCallable) this.getAttr("__setitem__")) {
            setItem.call(key, value);
            return previous;
        } catch (JepException e) {
            throw new RuntimeException("Error calling __setitem__", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public V remove(Object key) {
        try (PyCallable pop = (PyCallable) this.getAttr("pop")) {
            return (V) pop.call(key);
        } catch (JepException e) {
            throw new RuntimeException("Error calling pop", e);
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        try (PyCallable setItem = (PyCallable) this.getAttr("__setitem__")) {
            for (Map.Entry<? extends K, ? extends V> entry : m.entrySet())
                setItem.call(entry.getKey(), entry.getValue());
        } catch (JepException e) {
            throw new RuntimeException("Error calling __setitem__", e);
        }
    }

    @Override
    public void clear() {
        try (PyCallable clearMethod = (PyCallable) this.getAttr("clear")) {
            clearMethod.call();
        } catch (JepException e) {
            throw new RuntimeException("Error calling clear", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        try (PyCallable keysMethod = (PyCallable) this.getAttr("keys")) {
            try (PyList<K> keys = (PyList<K>) keysMethod.call()) {
                set.addAll(keys);
                return set;
            }
        } catch (JepException e) {
            throw new RuntimeException("Error calling keys", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<V> values() {
        try (PyCallable valuesMethod = (PyCallable) this.getAttr("values")) {
            return (Collection<V>) valuesMethod.call();
        } catch (JepException e) {
            throw new RuntimeException("Error calling values", e);
        }
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> entrySet = new HashSet<>();
        Set<K> keySet = keySet();
        for (K key : keySet) {
            V value = get(key);
            Map.Entry<K, V> entry = new AbstractMap.SimpleEntry<>(key, value);
            entrySet.add(entry);
        }

        return entrySet;
    }

}
