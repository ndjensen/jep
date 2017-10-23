package jep.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import jep.JepException;
import jep.python.PyCallable;
import jep.python.PyIterator;
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

    public boolean testIterator(PyIterator itr) {
        if (!itr.hasNext()) {
            return false;
        }

        List<Integer> values = new ArrayList<>();
        while (itr.hasNext()) {
            values.add((Integer) itr.next());
            // just to mess with things
            values.add((Integer) itr.next());
            itr.hasNext();
        }

        // verify we got what we expected
        List<Integer> expected = Arrays
                .asList(new Integer[] { 5, 6, 7, 8, 9, 10, 11, 12 });
        if (!values.equals(expected)) {
            return false;
        }

        // should have gone through them all by now
        if (itr.hasNext()) {
            return false;
        }

        // next() should fail
        try {
            itr.next();
            return false;
        } catch (NoSuchElementException e) {
            // expected
        }

        return true;
    }

}
