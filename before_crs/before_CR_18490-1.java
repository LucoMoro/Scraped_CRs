/*Integrate 5397067d into tools_r8. DO NOT MERGE.

Fix field visibility by ensuring they stay in __dict__.

Change-Id:I2e93dd4ba8c8cc3f05ca477091932268ec5d78da*/
//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/JythonUtils.java b/monkeyrunner/src/com/android/monkeyrunner/JythonUtils.java
//Synthetic comment -- index 8d25dd9..864441e 100644

//Synthetic comment -- @@ -38,6 +38,7 @@
import org.python.core.PyList;
import org.python.core.PyNone;
import org.python.core.PyObject;
import org.python.core.PyReflectedFunction;
import org.python.core.PyString;
import org.python.core.PyStringMap;
//Synthetic comment -- @@ -302,9 +303,26 @@
}
}

// Now remove any elements left from the functions collection
for (String name : functions) {
        dict.__delitem__(name);
}
}








