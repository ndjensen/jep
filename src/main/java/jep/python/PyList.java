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
import java.util.List;
import java.util.ListIterator;

import jep.Jep;
import jep.JepException;

/**
 * 
 * A Java object that wraps a pointer to a Python list and implements the
 * java.util.List interface.
 * 
 *
 * @author njensen
 * @since 4.0
 */
public class PyList<E> extends PyCollection<E> implements List<E> {

    public PyList(long tstate, long pyObject, Jep jep) throws JepException {
        super(tstate, pyObject, jep);
    }

    @Override
    public boolean add(Object o) {
        try (PyCallable append = (PyCallable) this.getAttr("append")) {
            append.call(o);
            return true;
        } catch (JepException e) {
            throw new RuntimeException("Error calling append", e);
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        try (PyCallable append = (PyCallable) this.getAttr("append")) {
            for (Object o : c) {
                append.call(o);
            }
            return true;
        } catch (JepException e) {
            throw new RuntimeException("Error calling append", e);
        }
    }

    @Override
    public void clear() {
        int size = size();
        try (PyCallable delslice = (PyCallable) this.getAttr("__delslice__")) {
            delslice.call(0, size);
        } catch (JepException e) {
            throw new RuntimeException("Error calling __delslice__", e);
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        try (PyCallable setslice = (PyCallable) this.getAttr("__setslice__")) {
            setslice.call(index, index, c);
            return true;
        } catch (JepException e) {
            throw new RuntimeException("Error calling __delslice__", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public E get(int index) {
        try (PyCallable getitem = (PyCallable) this.getAttr("__getitem__")) {
            return (E) getitem.call(index);
        } catch (JepException e) {
            throw new RuntimeException("Error calling __getitem__", e);
        }
    }

    @Override
    public E set(int index, E element) {
        E previous = get(index);
        try (PyCallable setitem = (PyCallable) this.getAttr("__setitem__")) {
            setitem.call(index, element);
            return previous;
        } catch (JepException e) {
            throw new RuntimeException("Error calling __setitem__", e);
        }
    }

    @Override
    public void add(int index, E element) {
        try (PyCallable insert = (PyCallable) this.getAttr("insert")) {
            insert.call(index, element);
        } catch (JepException e) {
            throw new RuntimeException("Error calling insert", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public E remove(int index) {
        try (PyCallable pop = (PyCallable) this.getAttr("pop")) {
            E previous = (E) pop.call(index);
            return previous;
        } catch (JepException e) {
            throw new RuntimeException("Error calling pop", e);
        }
    }

    @Override
    public int indexOf(Object o) {
        /*
         * TODO python will value error if it's not in the list, but java wants
         * a -1
         */
        try (PyCallable index = (PyCallable) this.getAttr("index")) {
            return (int) index.call(o);
        } catch (JepException e) {
            throw new RuntimeException("Error calling index", e);
        }
    }

    @Override
    public int lastIndexOf(Object o) {
        // TODO same problem as above with ValueError from Python
        try (PyCallable index = (PyCallable) this.getAttr("index")) {
            int idx = (int) index.call(o);
            if (idx > -1) {
                int result = idx;
                while (idx > -1) {
                    result = idx;
                    idx = (int) index.call(o, idx + 1);
                }
                return result;
            }
            return idx;
        } catch (JepException e) {
            throw new RuntimeException("Error calling index", e);
        }
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        try (PyCallable getslice = (PyCallable) this.getAttr("__getslice__")) {
            return (List<E>) getslice.call(fromIndex, toIndex);
        } catch (JepException e) {
            throw new RuntimeException("Error calling __getslice__", e);
        }
    }


}
