/*Add in support for PyBoolean in map parsing.

Change-Id:I01bfe706b294c0d745b7d74e340dd8e6208c5858*/
//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/JythonUtils.java b/monkeyrunner/src/com/android/monkeyrunner/JythonUtils.java
//Synthetic comment -- index 864441e..52cfbb0 100644

//Synthetic comment -- @@ -15,6 +15,34 @@
*/
package com.android.monkeyrunner;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
//Synthetic comment -- @@ -29,32 +57,6 @@
import java.util.logging.Level;
import java.util.logging.Logger;

import org.python.core.ArgParser;
import org.python.core.ClassDictInit;
import org.python.core.Py;
import org.python.core.PyDictionary;
import org.python.core.PyFloat;
import org.python.core.PyInteger;
import org.python.core.PyList;
import org.python.core.PyNone;
import org.python.core.PyObject;
import org.python.core.PyReflectedField;
import org.python.core.PyReflectedFunction;
import org.python.core.PyString;
import org.python.core.PyStringMap;
import org.python.core.PyTuple;

import com.android.monkeyrunner.doc.MonkeyRunnerExported;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableMap.Builder;

/**
* Collection of useful utilities function for interacting with the Jython interpreter.
*/
//Synthetic comment -- @@ -73,6 +75,7 @@
// What python calls float, most people call double
builder.put(PyFloat.class, Double.class);
builder.put(PyInteger.class, Integer.class);

PYOBJECT_TO_JAVA_OBJECT_MAP = builder.build();
}







