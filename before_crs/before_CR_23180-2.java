/*Fixed the line length to conform to style guideliens

Change-Id:I678fed2b4294bf5e40e9f4eb43eb722f11bf94cd*/
//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerOptions.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerOptions.java
//Synthetic comment -- index 0deaf16..d4297db 100644

//Synthetic comment -- @@ -163,7 +163,8 @@
return null;
} else {
if (scriptFile == null) {
                    // get the filepath of the script to run.  This will be the last undashed argument.
scriptFile = new File(argument);
if (!scriptFile.exists()) {
printUsage("Can't open specified script file");







