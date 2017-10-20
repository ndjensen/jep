package jep.test;

import java.util.Map;

import jep.JepException;
import jep.python.PyCallable;
import jep.python.PyObject;

/**
 * Tests native Python objects being used in Java. Used by test_jpyobject.py.
 * 
 * Created: October 2017
 * 
 * @author Nate Jensen
 * @since 3.8
 */
public class TestJPyObjects {

    public Object testArgumentReturn(Object obj) {
        return obj;
    }

    public PyObject testType(PyObject obj) {
        return obj;
    }

    public String testInvoke(PyObject obj) throws JepException {
        PyCallable callable = (PyCallable) obj.getAttr("helloWorld");
        return (String) callable.call();
    }

    public String testInvokeKwargs(PyObject obj, Map<String, Object> kwargs)
            throws JepException {
        PyCallable callable = (PyCallable) obj.getAttr("helloWorld");
        return (String) callable.call(kwargs);
    }

    public String testBoundInvoke(PyCallable obj) throws JepException {
        return (String) obj.call();
    }

    public String testUnboundInvoke(PyObject instance, PyCallable obj)
            throws JepException {
        return (String) obj.call(instance);
    }

}
