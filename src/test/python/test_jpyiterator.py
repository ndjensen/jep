import unittest
import jep
TestJPyObjects = jep.findClass('jep.test.TestJPyObjects')

class CustomIterator(object):
    
    def __init__(self):
        self.values = [5, 6, 7, 8, 9, 10, 11, 12]
        self.index = 0
    
    def __iter__(self):
        self.index = 0
        return self
    
    # For Python 3
    def __next__(self):
        if self.index < len(self.values):
            retVal = self.values[self.index]
            self.index += 1
            return retVal
        else:
            raise StopIteration()
    
    # For Python 2
    def next(self):
        if self.index < len(self.values):
            retVal = self.values[self.index]
            self.index += 1
            return retVal
        else:
            raise StopIteration()

class TestPythonIteratorInJava(unittest.TestCase):

    def setUp(self):
        self.test = TestJPyObjects()

    def test_iterator(self):
        x = CustomIterator()
        result = self.test.testIterator(x)
        self.assertTrue(result)

