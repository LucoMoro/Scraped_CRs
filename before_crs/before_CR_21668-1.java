/*- Fix a NPE when arguments are not properly understood by MonkeyRunner
- Add the ability to pass Boolean types for Intent extras (extras={'a':True, 'b':False})
- Fix an improper flag being passed to am start
- Pass the key and value to am start instead of just the value.

Change-Id:Ifd0c69ccb4c2755a49efca2d3f8b3befa212a69f*/
//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/JythonUtils.java b/monkeyrunner/src/com/android/monkeyrunner/JythonUtils.java
//Synthetic comment -- index 864441e..7054695 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import org.python.core.ArgParser;
import org.python.core.ClassDictInit;
import org.python.core.Py;
import org.python.core.PyDictionary;
import org.python.core.PyFloat;
import org.python.core.PyInteger;
//Synthetic comment -- @@ -73,6 +74,7 @@
// What python calls float, most people call double
builder.put(PyFloat.class, Double.class);
builder.put(PyInteger.class, Integer.class);

PYOBJECT_TO_JAVA_OBJECT_MAP = builder.build();
}
//Synthetic comment -- @@ -228,6 +230,8 @@
} else if (o instanceof Float) {
float f = (Float) o;
return new PyFloat(f);
}
return Py.None;
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerStarter.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerStarter.java
//Synthetic comment -- index 90fce6f..5244dbc 100644

//Synthetic comment -- @@ -191,13 +191,12 @@
public static void main(String[] args) {
MonkeyRunnerOptions options = MonkeyRunnerOptions.processOptions(args);

        // logging property files are difficult
        replaceAllLogFormatters(MonkeyFormatter.DEFAULT_INSTANCE, options.getLogLevel());

if (options == null) {
return;
}


MonkeyRunnerStarter runner = new MonkeyRunnerStarter(options);
int error = runner.run();








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/adb/AdbMonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/adb/AdbMonkeyDevice.java
//Synthetic comment -- index e7e2e1c..befec28 100644

//Synthetic comment -- @@ -419,9 +419,10 @@
} else {
// treat is as a string.
valueString = value.toString();
                arg = "--esmake";
}
parts.add(arg);
parts.add(valueString);
}








