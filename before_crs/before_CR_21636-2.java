/*Add in support for PyBoolean in map parsing.

Change-Id:I01bfe706b294c0d745b7d74e340dd8e6208c5858*/
//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/JythonUtils.java b/monkeyrunner/src/com/android/monkeyrunner/JythonUtils.java
//Synthetic comment -- index 864441e..54978f9 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import org.python.core.ArgParser;
import org.python.core.ClassDictInit;
import org.python.core.Py;
import org.python.core.PyDictionary;
import org.python.core.PyFloat;
import org.python.core.PyInteger;
//Synthetic comment -- @@ -45,15 +46,16 @@
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
//Synthetic comment -- @@ -73,6 +75,7 @@
// What python calls float, most people call double
builder.put(PyFloat.class, Double.class);
builder.put(PyInteger.class, Integer.class);

PYOBJECT_TO_JAVA_OBJECT_MAP = builder.build();
}







