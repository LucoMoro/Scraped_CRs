/*BuildDalvikSuite: Remove the unused scanner2

scanner2 was probably used to parse @title/@constraint but was then
replaced by regexps.

Change-Id:I35cfd3b2bb6ae698f74a5c9ebc5375301c547fec*/




//Synthetic comment -- diff --git a/tools/vm-tests-tf/src/util/build/BuildDalvikSuite.java b/tools/vm-tests-tf/src/util/build/BuildDalvikSuite.java
//Synthetic comment -- index bd688fe..20429f0 100644

//Synthetic comment -- @@ -576,14 +576,6 @@
}

// find the @title/@constraint in javadoc comment for this method
// using platform's default charset
String all = new String(FileUtils.readFile(f));
// System.out.println("grepping javadoc found for method " + method +
//Synthetic comment -- @@ -630,9 +622,6 @@
if (scanner != null) {
scanner.close();
}
return md;
}








