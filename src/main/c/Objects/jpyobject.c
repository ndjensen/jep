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
 * Class:     jep_python_JPyObject
 * Method:    getAttr
 * Signature: (JJLjava/lang/String;)Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL Java_jep_python_JPyObject_getAttr
  (JNIEnv *env, jobject obj, jlong tstate, jlong pyobj, jstring str)
{
    JepThread  *jepThread;
    PyObject   *pyObject;
    const char *attrName;
    PyObject   *attr;
    jobject     ret = NULL;

    jepThread = (JepThread *) tstate;
    if (!jepThread) {
        THROW_JEP(env, "Couldn't get thread objects.");
        return ret;
    }

    pyObject = (PyObject*) pyobj;
    attrName = jstring2char(env, str);

    PyEval_AcquireThread(jepThread->tstate);
    
    attr = PyObject_GetAttrString(pyObject, attrName);
    if (process_py_exception(env)) {
        goto EXIT;
    }

    ret = PyObject_As_jobject(env, attr, JOBJECT_TYPE);
    if (process_py_exception(env)) {
        goto EXIT;
    }


EXIT:
    Py_XDECREF(attr);
    PyEval_ReleaseThread(jepThread->tstate);
    release_utf_char(env, str, attrName);
    return ret;
}

