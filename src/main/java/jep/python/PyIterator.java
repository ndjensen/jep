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

import java.util.Iterator;
import java.util.NoSuchElementException;

import jep.Jep;
import jep.JepException;

/**
 * A Java object that wraps a pointer to a Python iterator.
 *
 * @author njensen
 * @since 4.0
 */
public class PyIterator<E> extends PyObject implements Iterator<E> {

    private E next = null;

    protected PyIterator(long tstate, long pyObject, Jep jep)
            throws JepException {
        super(tstate, pyObject, jep);
    }

    @Override
    public boolean hasNext() {
        if (next == null) {
            try {
                checkValid();
                next = getNext(pointer.tstate, pointer.pyObject);
            } catch (JepException e) {
                throw new RuntimeException("Error calling next", e);
            }

        }
        return next != null;
    }

    private native E getNext(long tstate, long pyObject) throws JepException;

    @Override
    public E next() {
        if (next == null) {
            try {
                checkValid();
                next = getNext(pointer.tstate, pointer.pyObject);
            } catch (JepException e) {
                throw new RuntimeException("Error calling next", e);
            }

        }

        if (next == null) {
            throw new NoSuchElementException();
        }

        E ret = next;
        next = null;
        return ret;
    }

}
