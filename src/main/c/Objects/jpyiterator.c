/* 
   jep - Java Embedded Python

   Copyright (c) 2017 JEP AUTHORS.

   This file is licensed under the the zlib/libpng License.

   This software is provided 'as-is', without any express or implied
   warranty. In no event will the authors be held liable for any
   damages arising from the use of this software.
   
   Permission is granted to anyone to use this software for any
   purpose, including commercial applications, and to alter it and
   redistribute it freely, subject to the following restrictions:

   1. The origin of this software must not be misrepresented; you
   must not claim that you wrote the original software. If you use
   this software in a product, an acknowledgment in the product
   documentation would be appreciated but is not required.

   2. Altered source versions must be plainly marked as such, and
   must not be misrepresented as being the original software.

   3. This notice may not be removed or altered from any source
   distribution.   
*/

#include "Jep.h"


/*
 * Class:     jep_python_PyIterator
 * Method:    getNext
 * Signature: (JJ)Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL Java_jep_python_PyIterator_getNext
  (JNIEnv *env, jobject obj, jlong tstate, jlong pyobj)
{
    JepThread  *jepThread;
    PyObject   *pyObject;
    PyObject   *next;
    jobject     ret = NULL;

    jepThread = (JepThread *) tstate;
    if (!jepThread) {
        THROW_JEP(env, "Couldn't get thread objects.");
        return ret;
    }

    pyObject = (PyObject*) pyobj;
    PyEval_AcquireThread(jepThread->tstate);
    next = PyIter_Next(pyObject);
    if (process_py_exception(env)) {
        goto EXIT;
    }

    if (next != NULL) {
        ret = PyObject_As_jobject(env, next, JOBJECT_TYPE);
        if (process_py_exception(env)) {
            goto EXIT;
        }
    }


EXIT:
    Py_XDECREF(next);
    PyEval_ReleaseThread(jepThread->tstate);
    return ret;
}

