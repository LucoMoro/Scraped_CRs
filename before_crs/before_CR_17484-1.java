/*Eix monkeyrunner pydoc generation.

Change-Id:I0af981f2023abf2cbf92cb1d7c9132936414c559*/
//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/JythonUtils.java b/monkeyrunner/src/com/android/monkeyrunner/JythonUtils.java
//Synthetic comment -- index 258261b..13aa793 100644

//Synthetic comment -- @@ -15,14 +15,19 @@
*/
package com.android.monkeyrunner;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap.Builder;

import com.android.monkeyrunner.doc.MonkeyRunnerExported;

import org.python.core.ArgParser;
import org.python.core.Py;
import org.python.core.PyDictionary;
import org.python.core.PyFloat;
//Synthetic comment -- @@ -30,14 +35,18 @@
import org.python.core.PyList;
import org.python.core.PyNone;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PyTuple;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

//Synthetic comment -- @@ -233,4 +242,73 @@
}
return new PyDictionary(resultMap);
}
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java
//Synthetic comment -- index d150c9b..047fe32 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.monkeyrunner.doc.MonkeyRunnerExported;

import org.python.core.ArgParser;
import org.python.core.Py;
import org.python.core.PyDictionary;
import org.python.core.PyException;
//Synthetic comment -- @@ -40,7 +41,12 @@
* that device.  Each backend will need to create a concrete
* implementation of this class.
*/
public abstract class MonkeyDevice {
/**
* Create a MonkeyMananger for talking to this device.
*
//Synthetic comment -- @@ -90,8 +96,12 @@
argDocs = {"Sends a DOWN event",
"Sends an UP event",
"Sends a DOWN event, immediately followed by an UP event"})
    public enum TouchPressType {
        DOWN, UP, DOWN_AND_UP
}

@MonkeyRunnerExported(doc = "Sends a touch event at the specified location",








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyImage.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyImage.java
//Synthetic comment -- index 6e32b3d..247d9ca 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.monkeyrunner.doc.MonkeyRunnerExported;

import org.python.core.ArgParser;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PyTuple;
//Synthetic comment -- @@ -39,7 +40,12 @@
/**
* Jython object to encapsulate images that have been taken.
*/
public abstract class MonkeyImage {
/**
* Convert the MonkeyImage into a BufferedImage.
*








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunner.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunner.java
//Synthetic comment -- index 63ab9eb..1a5e85b 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.monkeyrunner.doc.MonkeyRunnerExported;

import org.python.core.ArgParser;
import org.python.core.PyException;
import org.python.core.PyObject;

//Synthetic comment -- @@ -34,10 +35,15 @@
/**
* This is the main interface class into the jython bindings.
*/
public class MonkeyRunner {
private static final Logger LOG = Logger.getLogger(MonkeyRunner.class.getCanonicalName());
private static MonkeyRunnerBackend backend;

/**
* Set the backend MonkeyRunner is using.
*








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerHelp.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerHelp.java
//Synthetic comment -- index 124e6cf..0028951 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.google.clearsilver.jsilver.data.Data;
import com.google.clearsilver.jsilver.resourceloader.ClassLoaderResourceLoader;
import com.google.clearsilver.jsilver.resourceloader.ResourceLoader;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
//Synthetic comment -- @@ -32,6 +33,7 @@
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
//Synthetic comment -- @@ -238,4 +240,35 @@

return hdf;
}
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerStarter.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerStarter.java
//Synthetic comment -- index 1f539ba..d9dc0dc 100644

//Synthetic comment -- @@ -22,6 +22,15 @@
import com.android.monkeyrunner.adb.AdbBackend;
import com.android.monkeyrunner.stub.StubBackend;

import org.python.util.PythonInterpreter;

import java.io.File;
//Synthetic comment -- @@ -79,6 +88,10 @@
}

private int run() {
MonkeyRunner.setBackend(backend);
Map<String, Predicate<PythonInterpreter>> plugins = handlePlugins();
if (options.getScriptFile() == null) {








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/ScriptRunner.java b/monkeyrunner/src/com/android/monkeyrunner/ScriptRunner.java
//Synthetic comment -- index c247a5f..d1b7364 100644

//Synthetic comment -- @@ -17,12 +17,16 @@

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableMap.Builder;

import org.python.core.Py;
import org.python.core.PyException;
import org.python.core.PyObject;
import org.python.util.InteractiveConsole;
import org.python.util.JLineConsole;
import org.python.util.PythonInterpreter;
//Synthetic comment -- @@ -68,8 +72,10 @@
* @param plugins a list of plugins to load.
* @return the error code from running the script.
*/
    public static int run(String scriptfilename, Collection<String> args,
            Map<String, Predicate<PythonInterpreter>> plugins) {
// Add the current directory of the script to the python.path search path.
File f = new File(scriptfilename);

//Synthetic comment -- @@ -164,6 +170,12 @@
props.setProperty("python.verbose", "error");

PythonInterpreter.initialize(System.getProperties(), props, argv);
}

/**







