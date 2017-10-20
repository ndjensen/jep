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

import java.util.Map;

import jep.Jep;
import jep.JepException;

/**
 * 
 * A Java object that wraps a pointer to a Python callable.
 * 
 *
 * @author njensen
 * @since 4.0
 */
public class PyCallable extends PyObject {

    public PyCallable(long tstate, long pyObject, Jep jep)
            throws JepException {
        super(tstate, pyObject, jep);
    }

    public Object call(Object... args) throws JepException {
        checkValid();
        return call(pointer.tstate, pointer.pyObject, args, null);
    }

    public Object call(Map<String, Object> kwargs) throws JepException {
        checkValid();
        return call(pointer.tstate, pointer.pyObject, null, kwargs);
    }

    public Object call(Object[] args, Map<String, Object> kwargs)
            throws JepException {
        checkValid();
        return call(pointer.tstate, pointer.pyObject, args, kwargs);
    }

    private native Object call(long tstate, long pyObject, Object[] args,
            Map<String, Object> kwargs) throws JepException;

}
