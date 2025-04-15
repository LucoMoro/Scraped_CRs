/*Support System.loadLibrary for libraries with transitive dependencies.

Bug: 7896159
Bug:http://code.google.com/p/android/issues/detail?id=34416Change-Id:Id1225a353b52c50bb3eedfd48e92ec85dd60134b*/
//Synthetic comment -- diff --git a/dalvik/src/main/java/dalvik/system/BaseDexClassLoader.java b/dalvik/src/main/java/dalvik/system/BaseDexClassLoader.java
//Synthetic comment -- index 1c006ba..6d5ae1a 100644

//Synthetic comment -- @@ -112,6 +112,20 @@
return null;
}

@Override public String toString() {
return getClass().getName() + "[" + path + "]";
}








//Synthetic comment -- diff --git a/dalvik/src/main/java/dalvik/system/DexPathList.java b/dalvik/src/main/java/dalvik/system/DexPathList.java
//Synthetic comment -- index 1ae48d9..3088ca5 100644

//Synthetic comment -- @@ -109,6 +109,13 @@
}

/**
* Splits the given dex path string into elements using the path
* separator, pruning out any elements that do not refer to existing
* and readable files. (That is, directories are not included in the








//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/Runtime.java b/luni/src/main/java/java/lang/Runtime.java
//Synthetic comment -- index 58142d3..533373e 100644

//Synthetic comment -- @@ -32,6 +32,7 @@

package java.lang;

import dalvik.system.VMDebug;
import dalvik.system.VMStack;
import java.io.File;
//Synthetic comment -- @@ -329,7 +330,7 @@
if (pathName == null) {
throw new NullPointerException("pathName == null");
}
        String error = nativeLoad(pathName, loader);
if (error != null) {
throw new UnsatisfiedLinkError(error);
}
//Synthetic comment -- @@ -356,11 +357,11 @@
if (loader != null) {
String filename = loader.findLibrary(libraryName);
if (filename == null) {
                throw new UnsatisfiedLinkError("Couldn't load " + libraryName
                                               + " from loader " + loader
                                               + ": findLibrary returned null");
}
            String error = nativeLoad(filename, loader);
if (error != null) {
throw new UnsatisfiedLinkError(error);
}
//Synthetic comment -- @@ -375,7 +376,7 @@
candidates.add(candidate);

if (IoUtils.canOpenReadOnly(candidate)) {
                String error = nativeLoad(candidate, loader);
if (error == null) {
return; // We successfully loaded the library. Job done.
}
//Synthetic comment -- @@ -391,7 +392,40 @@

private static native void nativeExit(int code);

    private static native String nativeLoad(String filename, ClassLoader loader);

/**
* Provides a hint to the VM that it would be useful to attempt







