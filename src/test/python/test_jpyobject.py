import unittest
import jep
TestJPyObjects = jep.findClass('jep.test.TestJPyObjects')

class HelloWorld(object):
    def helloWorld(self, hello="hello", world="world"):
        return str(hello) + ' ' + str(world)

class TestPythonObjectsInJava(unittest.TestCase):

    def setUp(self):
        self.test = TestJPyObjects()

    def test_reference_equality(self):
        x = HelloWorld()
        y = self.test.testArgumentReturn(x)
        self.assertIs(x, y)

        z = HelloWorld()
        z1 = self.test.testArgumentReturn(z)
        self.assertIsNot(x, z)
        self.assertIsNot(x, z1)
        self.assertIs(z, z1)

    def test_toString(self):
        from java.lang import String
        x = HelloWorld()
        self.assertEqual(str(x), String.valueOf(x))

    def test_invoke(self):
        x = HelloWorld()
        result = self.test.testInvoke(x)
        self.assertEqual(result, "hello world")

    def test_kwargs_invoke(self):        
        x = HelloWorld()
        kw = {'hello':'test', 'world':'passed'}
        result = self.test.testInvokeKwargs(x, kw)
        self.assertEqual(result, 'test passed')

    def test_bound_invoke(self):
        x = HelloWorld()
        method = x.helloWorld
        result = self.test.testBoundInvoke(method)
        self.assertEqual(result, "hello world")

    def test_unbound_invoke(self):
        method = HelloWorld.helloWorld
        x = HelloWorld()
        result = self.test.testUnboundInvoke(x, method)
        self.assertEqual(result, "hello world")

