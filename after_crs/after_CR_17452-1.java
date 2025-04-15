/*Add in support for the sys.executable variable.

This allows things like pydoc to work.

Change-Id:I16206a17099f7a3785ebcb303bd760b65db36b68*/




//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerStarter.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerStarter.java
//Synthetic comment -- index 1f539ba..763362e 100644

//Synthetic comment -- @@ -79,13 +79,17 @@
}

private int run() {
        // This system property gets set by the included starter script
        String monkeyRunnerPath = System.getProperty("com.android.monkeyrunner.bindir") +
            File.separator + "monkeyrunner";

MonkeyRunner.setBackend(backend);
Map<String, Predicate<PythonInterpreter>> plugins = handlePlugins();
if (options.getScriptFile() == null) {
            ScriptRunner.console(monkeyRunnerPath);
return 0;
} else {
            int error = ScriptRunner.run(monkeyRunnerPath, options.getScriptFile().getAbsolutePath(),
options.getArguments(), plugins);
backend.shutdown();
MonkeyRunner.setBackend(null);
//Synthetic comment -- @@ -194,6 +198,7 @@
return;
}


MonkeyRunnerStarter runner = new MonkeyRunnerStarter(options);
int error = runner.run();









//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/ScriptRunner.java b/monkeyrunner/src/com/android/monkeyrunner/ScriptRunner.java
//Synthetic comment -- index c247a5f..b55be87 100644

//Synthetic comment -- @@ -68,8 +68,9 @@
* @param plugins a list of plugins to load.
* @return the error code from running the script.
*/
    public static int run(String executablePath, String scriptfilename,
            Collection<String> args, Map<String,
            Predicate<PythonInterpreter>> plugins) {
// Add the current directory of the script to the python.path search path.
File f = new File(scriptfilename);

//Synthetic comment -- @@ -84,7 +85,7 @@
argv[x++] = arg;
}

        initPython(executablePath, classpath, argv);

PythonInterpreter python = new PythonInterpreter();

//Synthetic comment -- @@ -119,18 +120,20 @@
return 0;
}

    public static void runString(String executablePath, String script) {
        initPython(executablePath);
PythonInterpreter python = new PythonInterpreter();
python.exec(script);
}

    public static Map<String, PyObject> runStringAndGet(String executablePath,
            String script, String... names) {
        return runStringAndGet(executablePath, script, Arrays.asList(names));
}

    public static Map<String, PyObject> runStringAndGet(String executablePath,
            String script, Collection<String> names) {
        initPython(executablePath);
final PythonInterpreter python = new PythonInterpreter();
python.exec(script);

//Synthetic comment -- @@ -141,13 +144,13 @@
return builder.build();
}

    private static void initPython(String executablePath) {
List<String> arg = Collections.emptyList();
        initPython(executablePath, arg, new String[] {""});
}

    private static void initPython(String executablePath,
            Collection<String> pythonPath, String[] argv) {
Properties props = new Properties();

// Build up the python.path
//Synthetic comment -- @@ -163,14 +166,17 @@
// Choose one of error,warning,message,comment,debug
props.setProperty("python.verbose", "error");

        // This needs to be set for sys.executable to function properly
        props.setProperty("python.executable", executablePath);

PythonInterpreter.initialize(System.getProperties(), props, argv);
}

/**
* Start an interactive python interpreter.
*/
    public static void console(String executablePath) {
        initPython(executablePath);
InteractiveConsole python = new JLineConsole();
python.interact();
}







