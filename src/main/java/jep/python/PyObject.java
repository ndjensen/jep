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

import jep.Jep;
import jep.JepException;

/**
 * A Java object that wraps a pointer to a Python object.
 * 
 * close() can be called to immediately decref the associated PyObject* and
 * potentially enable the Python garbage collector to collect it, freeing native
 * memory. If close() is not called, the native memory may still be freed by the
 * Jep instance's associated MemoryManager if Java garbage collects the object.
 * Last but not least, closing the Jep instance that created this PyObject will
 * also free the native memory.
 * 
 *
 * @author njensen
 * @since 3.8
 */
public class PyObject implements AutoCloseable {

    protected final PyPointer pointer;

    protected final Jep jep;

    /**
     * Constructor called from native code
     * 
     * @param tstate
     *            the thread pointer
     * @param pyObject
     *            the PyObject* pointer
     * @param jep
     *            the Jep instance creating this PyObject
     * @throws JepException
     */
    protected PyObject(long tstate, long pyObject, Jep jep)
            throws JepException {
        if (pyObject == 0) {
            throw new JepException("Invalid pointer.");
        }
        this.jep = jep;
        this.pointer = new PyPointer(this, jep, tstate, pyObject);
    }

    /**
     * Check if PyObject is valid
     * 
     * @throws JepException
     *             if an error occurs
     */
    protected void checkValid() throws JepException {
        jep.isValidThread();
    }

    public Object getAttr(String name) throws JepException {
        checkValid();
        return getAttr(pointer.tstate, pointer.pyObject, name);
    }

    private native Object getAttr(long tstate, long pyObject, String name)
            throws JepException;

    /**
     * Called from native code
     * 
     * @return
     */
    protected long getPyObject() {
        return pointer.pyObject;
    }

    @Override
    public String toString() {
        try {
            Object strMethod = getAttr("__str__");
            if (strMethod != null) {
                if (strMethod instanceof PyCallable) {
                    Object str = ((PyCallable) strMethod).call();
                    if (str != null) {
                        return str.toString();
                    } else {
                        throw new IllegalStateException("__str__() on "
                                + super.toString() + " returned null");
                    }
                } else {
                    throw new IllegalStateException("__str__ on "
                            + super.toString() + " is not callable");
                }
            } else {
                throw new IllegalStateException(
                        "No __str__ attribute found on " + super.toString());
            }
        } catch (JepException e) {
            throw new IllegalStateException(
                    "Error determining Python string on " + super.toString(),
                    e);
        }
    }

    @Override
    public void close() throws JepException {
        checkValid();
        this.pointer.dispose();
    }

}
