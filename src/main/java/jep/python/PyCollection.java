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

import java.util.Collection;
import java.util.Iterator;

import jep.Jep;
import jep.JepException;

/**
 * 
 * A Java object that wraps a pointer to a Python collection and implements the
 * java.util.Collection interface.
 * 
 *
 * @author njensen
 * @since 4.0
 */
public abstract class PyCollection<E> extends PyObject
        implements Collection<E> {

    public PyCollection(long tstate, long pyObject, Jep jep)
            throws JepException {
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
    public boolean contains(Object o) {
        try (PyCallable containsMethod = (PyCallable) this
                .getAttr("__contains__")) {
            return (boolean) containsMethod.call(o);
        } catch (JepException e) {
            throw new RuntimeException("Error calling __contains__", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<E> iterator() {
        try (PyCallable iterMethod = (PyCallable) this.getAttr("__iter__")) {
            return (PyIterator<E>) iterMethod.call();
        } catch (JepException e) {
            throw new RuntimeException("Error calling __iter__");
        }

    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        boolean contained = contains(o);
        try (PyCallable removeMethod = (PyCallable) this.getAttr("remove")) {
            removeMethod.call(o);
            return contained;
        } catch (JepException e) {
            throw new RuntimeException("Error calling remove", e);
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        boolean result = true;
        for (Object obj : c) {
            result = contains(obj);
            if (!result) {
                break;
            }
        }
        return result;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object obj : c) {
            modified = remove(obj) || modified;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

}
