/*Eix monkeyrunner pydoc generation.

Change-Id:I0af981f2023abf2cbf92cb1d7c9132936414c559*/




//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/JythonUtils.java b/monkeyrunner/src/com/android/monkeyrunner/JythonUtils.java
//Synthetic comment -- index 258261b..13aa793 100644

//Synthetic comment -- @@ -15,14 +15,19 @@
*/
package com.android.monkeyrunner;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import com.android.monkeyrunner.doc.MonkeyRunnerExported;

import org.python.core.ArgParser;
import org.python.core.ClassDictInit;
import org.python.core.Py;
import org.python.core.PyDictionary;
import org.python.core.PyFloat;
//Synthetic comment -- @@ -30,14 +35,18 @@
import org.python.core.PyList;
import org.python.core.PyNone;
import org.python.core.PyObject;
import org.python.core.PyReflectedFunction;
import org.python.core.PyString;
import org.python.core.PyStringMap;
import org.python.core.PyTuple;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

//Synthetic comment -- @@ -233,4 +242,73 @@
}
return new PyDictionary(resultMap);
}

    /**
     * This function should be called from classDictInit for any classes that are being exported
     * to jython.  This jython converts all the MonkeyRunnerExported annotations for the given class
     * into the proper python form.  It also removes any functions listed in the dictionary that
     * aren't specifically annotated in the java class.
     *
     * NOTE: Make sure the calling class implements {@link ClassDictInit} to ensure that
     * classDictInit gets called.
     *
     * @param clz the class to examine.
     * @param dict the dictionary to update.
     */
    public static void convertDocAnnotationsForClass(Class<?> clz, PyObject dict) {
      Preconditions.checkNotNull(dict);
      Preconditions.checkArgument(dict instanceof PyStringMap);

      // See if the class has the annotation
      if (clz.isAnnotationPresent(MonkeyRunnerExported.class)) {
        MonkeyRunnerExported doc = clz.getAnnotation(MonkeyRunnerExported.class);
        dict.__setitem__("__doc__", new PyString(doc.doc()));
      }

      // Get all the keys from the dict and put them into a set.  As we visit the annotated methods,
      // we will remove them from this set.  At the end, these are the "hidden" methods that
      // should be removed from the dict
      Collection<String> functions = Sets.newHashSet();
      for (PyObject item : dict.asIterable()) {
        functions.add(item.toString());
      }
      // And remove anything that starts with __, as those are pretty important to retain
      functions = Collections2.filter(functions, new Predicate<String>() {
        @Override
        public boolean apply(String value) {
          return !value.startsWith("__");
        }
      });

      // Look at all the methods in the class and find the one's that have the
      // @MonkeyRunnerExported annotation.
      for (Method m : clz.getMethods()) {
        if (m.isAnnotationPresent(MonkeyRunnerExported.class)) {
          String methodName = m.getName();
          PyObject pyFunc = dict.__finditem__(methodName);
          if (pyFunc != null && pyFunc instanceof PyReflectedFunction) {
            PyReflectedFunction realPyFunc = (PyReflectedFunction) pyFunc;
            MonkeyRunnerExported doc = m.getAnnotation(MonkeyRunnerExported.class);

            realPyFunc.__doc__ = new PyString(buildDoc(doc));
            functions.remove(methodName);
          }
        }
      }

      // Now remove any elements left from the functions collection
      for (String name : functions) {
        dict.__delitem__(name);
      }
    }

    /**
     * Build a jython doc-string from the MonkeyRunnerExported annotation.
     *
     * @param doc the annotation to build from
     * @return a jython doc-string
     */
    private static String buildDoc(MonkeyRunnerExported doc) {
      return doc.doc();
    }
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java
//Synthetic comment -- index d150c9b..047fe32 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.monkeyrunner.doc.MonkeyRunnerExported;

import org.python.core.ArgParser;
import org.python.core.ClassDictInit;
import org.python.core.Py;
import org.python.core.PyDictionary;
import org.python.core.PyException;
//Synthetic comment -- @@ -40,7 +41,12 @@
* that device.  Each backend will need to create a concrete
* implementation of this class.
*/
@MonkeyRunnerExported(doc = "Represents a device attached to the system.")
public abstract class MonkeyDevice extends PyObject implements ClassDictInit {
  public static void classDictInit(PyObject dict) {
    JythonUtils.convertDocAnnotationsForClass(MonkeyDevice.class, dict);
  }

/**
* Create a MonkeyMananger for talking to this device.
*
//Synthetic comment -- @@ -90,8 +96,12 @@
argDocs = {"Sends a DOWN event",
"Sends an UP event",
"Sends a DOWN event, immediately followed by an UP event"})
    public enum TouchPressType implements ClassDictInit {
        DOWN, UP, DOWN_AND_UP;

        public static void classDictInit(PyObject dict) {
          JythonUtils.convertDocAnnotationsForClass(TouchPressType.class, dict);
        }
}

@MonkeyRunnerExported(doc = "Sends a touch event at the specified location",








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyImage.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyImage.java
//Synthetic comment -- index 6e32b3d..247d9ca 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.monkeyrunner.doc.MonkeyRunnerExported;

import org.python.core.ArgParser;
import org.python.core.ClassDictInit;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PyTuple;
//Synthetic comment -- @@ -39,7 +40,12 @@
/**
* Jython object to encapsulate images that have been taken.
*/
@MonkeyRunnerExported(doc = "An image")
public abstract class MonkeyImage extends PyObject implements ClassDictInit {
  public static void classDictInit(PyObject dict) {
    JythonUtils.convertDocAnnotationsForClass(MonkeyImage.class, dict);
  }

/**
* Convert the MonkeyImage into a BufferedImage.
*








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunner.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunner.java
//Synthetic comment -- index 63ab9eb..1a5e85b 100644

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.monkeyrunner.doc.MonkeyRunnerExported;

import org.python.core.ArgParser;
import org.python.core.ClassDictInit;
import org.python.core.PyException;
import org.python.core.PyObject;

//Synthetic comment -- @@ -34,10 +35,15 @@
/**
* This is the main interface class into the jython bindings.
*/
@MonkeyRunnerExported(doc = "Main entry point for MonkeyRunner")
public class MonkeyRunner extends PyObject implements ClassDictInit {
private static final Logger LOG = Logger.getLogger(MonkeyRunner.class.getCanonicalName());
private static MonkeyRunnerBackend backend;

    public static void classDictInit(PyObject dict) {
      JythonUtils.convertDocAnnotationsForClass(MonkeyRunner.class, dict);
    }

/**
* Set the backend MonkeyRunner is using.
*








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerHelp.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerHelp.java
//Synthetic comment -- index 124e6cf..0028951 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.google.clearsilver.jsilver.data.Data;
import com.google.clearsilver.jsilver.resourceloader.ClassLoaderResourceLoader;
import com.google.clearsilver.jsilver.resourceloader.ResourceLoader;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
//Synthetic comment -- @@ -32,6 +33,7 @@
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
//Synthetic comment -- @@ -238,4 +240,35 @@

return hdf;
}

    public static Collection<String> getAllDocumentedClasses() {
      Set<Field> fields = Sets.newTreeSet(MEMBER_SORTER);
      Set<Method> methods = Sets.newTreeSet(MEMBER_SORTER);
      Set<Constructor<?>> constructors = Sets.newTreeSet(MEMBER_SORTER);
      Set<Class<?>> classes = Sets.newTreeSet(CLASS_SORTER);
      getAllExportedClasses(fields, methods, constructors, classes);

      // The classes object only captures classes that are specifically exporter, which isn't
      // good enough.  So go through all the fields, methods, etc. and collect those classes as
      // as well
      Set<Class<?>> allClasses = Sets.newHashSet();
      allClasses.addAll(classes);
      for (Field f : fields) {
        allClasses.add(f.getDeclaringClass());
      }
      for (Method m : methods) {
        allClasses.add(m.getDeclaringClass());
      }
      for (Constructor<?> constructor : constructors) {
        allClasses.add(constructor.getDeclaringClass());
      }

      // And transform that collection into a list of simple names.
      return Collections2.transform(allClasses, new Function<Class<?>, String>() {
        @Override
        public String apply(Class<?> clz) {
          return clz.getName();
        }
      });
    }
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerStarter.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerStarter.java
//Synthetic comment -- index 1f539ba..d9dc0dc 100644

//Synthetic comment -- @@ -22,6 +22,15 @@
import com.android.monkeyrunner.adb.AdbBackend;
import com.android.monkeyrunner.stub.StubBackend;

import org.python.core.Py;
import org.python.core.PyBuiltinMethod;
import org.python.core.PyDataDescr;
import org.python.core.PyNewWrapper;
import org.python.core.PyObject;
import org.python.core.PySystemState;
import org.python.core.PyType;
import org.python.expose.BaseTypeBuilder;
import org.python.expose.TypeBuilder;
import org.python.util.PythonInterpreter;

import java.io.File;
//Synthetic comment -- @@ -79,6 +88,10 @@
}

private int run() {
        // This system property gets set by the included starter script
        String monkeyRunnerPath = System.getProperty("com.android.monkeyrunner.bindir") +
            File.separator + "monkeyrunner";
        
MonkeyRunner.setBackend(backend);
Map<String, Predicate<PythonInterpreter>> plugins = handlePlugins();
if (options.getScriptFile() == null) {








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/ScriptRunner.java b/monkeyrunner/src/com/android/monkeyrunner/ScriptRunner.java
//Synthetic comment -- index c247a5f..d1b7364 100644

//Synthetic comment -- @@ -17,12 +17,16 @@

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Lists;

import org.python.core.Py;
import org.python.core.PyException;
import org.python.core.PyJavaPackage;
import org.python.core.PyList;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.InteractiveConsole;
import org.python.util.JLineConsole;
import org.python.util.PythonInterpreter;
//Synthetic comment -- @@ -68,8 +72,10 @@
* @param plugins a list of plugins to load.
* @return the error code from running the script.
*/
    public static int run(String executablePath, String scriptfilename,
            Collection<String> args, Map<String,
            Predicate<PythonInterpreter>> plugins) {

// Add the current directory of the script to the python.path search path.
File f = new File(scriptfilename);

//Synthetic comment -- @@ -164,6 +170,12 @@
props.setProperty("python.verbose", "error");

PythonInterpreter.initialize(System.getProperties(), props, argv);

        String frameworkDir = System.getProperty("java.ext.dirs");
        File monkeyRunnerJar = new File(frameworkDir, "monkeyrunner.jar");
        if (monkeyRunnerJar.canRead()) {
          PySystemState.packageManager.addJar(monkeyRunnerJar.getAbsolutePath(), false);
        }
}

/**







