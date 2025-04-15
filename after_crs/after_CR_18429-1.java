/*Fix field visibility by ensuring they stay in __dict__.

Change-Id:I0f632f799ac7f554bb524099208385973423a0d0*/




//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/JythonUtils.java b/monkeyrunner/src/com/android/monkeyrunner/JythonUtils.java
//Synthetic comment -- index 8d25dd9..864441e 100644

//Synthetic comment -- @@ -38,6 +38,7 @@
import org.python.core.PyList;
import org.python.core.PyNone;
import org.python.core.PyObject;
import org.python.core.PyReflectedField;
import org.python.core.PyReflectedFunction;
import org.python.core.PyString;
import org.python.core.PyStringMap;
//Synthetic comment -- @@ -302,9 +303,26 @@
}
}

      // Also look at all the fields (both static and instance).
      for (Field f : clz.getFields()) {
          if (f.isAnnotationPresent(MonkeyRunnerExported.class)) {
              String fieldName = f.getName();
              PyObject pyField = dict.__finditem__(fieldName);
              if (pyField != null && pyField instanceof PyReflectedField) {
                  PyReflectedField realPyfield = (PyReflectedField) pyField;
                MonkeyRunnerExported doc = f.getAnnotation(MonkeyRunnerExported.class);

                // TODO: figure out how to set field documentation.  __doc__ is Read Only
                // in this context.
                // realPyfield.__setattr__("__doc__", new PyString(buildDoc(doc)));
                functions.remove(fieldName);
              }
            }
      }

// Now remove any elements left from the functions collection
for (String name : functions) {
          dict.__delitem__(name);
}
}








