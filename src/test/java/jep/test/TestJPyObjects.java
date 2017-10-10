package jep.test;

import java.util.Map;

import jep.JepException;
import jep.python.JPyCallable;
import jep.python.JPyObject;

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

    public JPyObject testType(JPyObject obj) {
        return obj;
    }

    public String testInvoke(JPyObject obj) throws JepException {
        JPyCallable callable = (JPyCallable) obj.getAttr("helloWorld");
        return (String) callable.call();
    }

    public String testInvokeKwargs(JPyObject obj, Map<String, Object> kwargs)
            throws JepException {
        JPyCallable callable = (JPyCallable) obj.getAttr("helloWorld");
        return (String) callable.call(kwargs);
    }

    public String testBoundInvoke(JPyCallable obj) throws JepException {
        return (String) obj.call();
    }

    public String testUnboundInvoke(JPyObject instance, JPyCallable obj)
            throws JepException {
        return (String) obj.call(instance);
    }

}
