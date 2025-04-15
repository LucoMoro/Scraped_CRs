/*Fix -v logging level flag.

Rather than ignoring the flag, actually make use of it.

Change-Id:Iade4700b32ed7b4a55bcd3336c74b2127693bebe*/
//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerOptions.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerOptions.java
//Synthetic comment -- index cf193c2..f1577ca 100644

//Synthetic comment -- @@ -33,13 +33,15 @@
private final String backend;
private final Collection<File> plugins;
private final Collection<String> arguments;

private MonkeyRunnerOptions(String hostname, int port, File scriptFile, String backend,
            Collection<File> plugins, Collection<String> arguments) {
this.hostname = hostname;
this.port = port;
this.scriptFile = scriptFile;
this.backend = backend;
this.plugins = plugins;
this.arguments = arguments;
}
//Synthetic comment -- @@ -68,6 +70,10 @@
return arguments;
}

private static void printUsage(String message) {
System.out.println(message);
System.out.println("Usage: monkeyrunner [options] SCRIPT_FILE");
//Synthetic comment -- @@ -92,6 +98,7 @@
File scriptFile = null;
int port = DEFAULT_MONKEY_PORT;
String backend = "adb";

ImmutableList.Builder<File> pluginListBuilder = ImmutableList.builder();
ImmutableList.Builder<String> argumentBuilder = ImmutableList.builder();
//Synthetic comment -- @@ -120,11 +127,7 @@
return null;
}

                Level level = Level.parse(args[index++]);
                LOG.setLevel(level);
                level = LOG.getLevel();
                System.out.println("Log level set to: " + level + "(" + level.intValue() + ").");
                System.out.println("Warning: Log levels below INFO(800) not working currently... parent issues");
} else if ("-be".equals(argument)) {
// quick check on the next argument.
if (index == args.length) {
//Synthetic comment -- @@ -174,7 +177,7 @@
}
};

        return new MonkeyRunnerOptions(hostname, port, scriptFile, backend,
pluginListBuilder.build(), argumentBuilder.build());
}
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerStarter.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerStarter.java
//Synthetic comment -- index 60f059d..90fce6f 100644

//Synthetic comment -- @@ -22,15 +22,6 @@
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
//Synthetic comment -- @@ -184,7 +175,7 @@



    private static final void replaceAllLogFormatters(Formatter form) {
LogManager mgr = LogManager.getLogManager();
Enumeration<String> loggerNames = mgr.getLoggerNames();
while (loggerNames.hasMoreElements()) {
//Synthetic comment -- @@ -192,7 +183,7 @@
Logger logger = mgr.getLogger(loggerName);
for (Handler handler : logger.getHandlers()) {
handler.setFormatter(form);
                handler.setLevel(Level.INFO);
}
}
}
//Synthetic comment -- @@ -201,7 +192,7 @@
MonkeyRunnerOptions options = MonkeyRunnerOptions.processOptions(args);

// logging property files are difficult
        replaceAllLogFormatters(MonkeyFormatter.DEFAULT_INSTANCE);

if (options == null) {
return;







