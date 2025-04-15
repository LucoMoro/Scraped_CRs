/*Add in support for the sys.executable variable.

This allows things like pydoc to work.

Change-Id:I16206a17099f7a3785ebcb303bd760b65db36b68*/
//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerStarter.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerStarter.java
//Synthetic comment -- index 1f539ba..763362e 100644

//Synthetic comment -- @@ -79,13 +79,17 @@
}

private int run() {
MonkeyRunner.setBackend(backend);
Map<String, Predicate<PythonInterpreter>> plugins = handlePlugins();
if (options.getScriptFile() == null) {
            ScriptRunner.console();
return 0;
} else {
            int error = ScriptRunner.run(options.getScriptFile().getAbsolutePath(),
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
    public static int run(String scriptfilename, Collection<String> args,
            Map<String, Predicate<PythonInterpreter>> plugins) {
// Add the current directory of the script to the python.path search path.
File f = new File(scriptfilename);

//Synthetic comment -- @@ -84,7 +85,7 @@
argv[x++] = arg;
}

        initPython(classpath, argv);

PythonInterpreter python = new PythonInterpreter();

//Synthetic comment -- @@ -119,18 +120,20 @@
return 0;
}

    public static void runString(String script) {
        initPython();
PythonInterpreter python = new PythonInterpreter();
python.exec(script);
}

    public static Map<String, PyObject> runStringAndGet(String script, String... names) {
        return runStringAndGet(script, Arrays.asList(names));
}

    public static Map<String, PyObject> runStringAndGet(String script, Collection<String> names) {
        initPython();
final PythonInterpreter python = new PythonInterpreter();
python.exec(script);

//Synthetic comment -- @@ -141,13 +144,13 @@
return builder.build();
}

    private static void initPython() {
List<String> arg = Collections.emptyList();
        initPython(arg, new String[] {""});
}

    private static void initPython(Collection<String> pythonPath,
            String[] argv) {
Properties props = new Properties();

// Build up the python.path
//Synthetic comment -- @@ -163,14 +166,17 @@
// Choose one of error,warning,message,comment,debug
props.setProperty("python.verbose", "error");

PythonInterpreter.initialize(System.getProperties(), props, argv);
}

/**
* Start an interactive python interpreter.
*/
    public static void console() {
        initPython();
InteractiveConsole python = new JLineConsole();
python.interact();
}







