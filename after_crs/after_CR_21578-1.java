/*Remove trailing comma that was causing compile problems.

Change-Id:Ia80bd2c25b5898a6ad99b9404b89c1b41c8290ba*/




//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunner.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunner.java
//Synthetic comment -- index 5d12955..5f137cd 100644

//Synthetic comment -- @@ -178,7 +178,7 @@
args = { "path" },
argDocs = {
"The path to the file to load.  This file path is in terms of the computer running " +
            "MonkeyRunner and not a path on the Android Device. " },
returns = "A new MonkeyImage representing the specified file")
public static MonkeyImage loadImageFromFile(PyObject[] args, String kws[]) {
ArgParser ap = JythonUtils.createArgParser(args, kws);







