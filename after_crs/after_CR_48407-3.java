/*Support System.loadLibrary for libraries with transitive dependencies.

Bug: 7896159
Bug:http://code.google.com/p/android/issues/detail?id=34416Change-Id:Id1225a353b52c50bb3eedfd48e92ec85dd60134b*/




//Synthetic comment -- diff --git a/dalvik/src/main/java/dalvik/system/BaseDexClassLoader.java b/dalvik/src/main/java/dalvik/system/BaseDexClassLoader.java
//Synthetic comment -- index 1c006ba..6d5ae1a 100644

//Synthetic comment -- @@ -112,6 +112,20 @@
return null;
}

    /**
     * @hide
     */
    public String getLdLibraryPath() {
        StringBuilder result = new StringBuilder();
        for (File directory : path.getNativeLibraryDirectories()) {
            if (result.length() > 0) {
                result.append(':');
            }
            result.append(directory);
        }
        return result.toString();
    }

@Override public String toString() {
return getClass().getName() + "[" + path + "]";
}








//Synthetic comment -- diff --git a/dalvik/src/main/java/dalvik/system/DexPathList.java b/dalvik/src/main/java/dalvik/system/DexPathList.java
//Synthetic comment -- index 1ae48d9..3088ca5 100644

//Synthetic comment -- @@ -109,6 +109,13 @@
}

/**
     * For BaseDexClassLoader.getLdLibraryPath.
     */
    public File[] getNativeLibraryDirectories() {
        return nativeLibraryDirectories;
    }

    /**
* Splits the given dex path string into elements using the path
* separator, pruning out any elements that do not refer to existing
* and readable files. (That is, directories are not included in the








//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/Runtime.java b/luni/src/main/java/java/lang/Runtime.java
//Synthetic comment -- index 58142d3..533373e 100644

//Synthetic comment -- @@ -32,6 +32,7 @@

package java.lang;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.VMDebug;
import dalvik.system.VMStack;
import java.io.File;
//Synthetic comment -- @@ -329,7 +330,7 @@
if (pathName == null) {
throw new NullPointerException("pathName == null");
}
        String error = doLoad(pathName, loader);
if (error != null) {
throw new UnsatisfiedLinkError(error);
}
//Synthetic comment -- @@ -356,11 +357,11 @@
if (loader != null) {
String filename = loader.findLibrary(libraryName);
if (filename == null) {
                throw new UnsatisfiedLinkError("Couldn't load " + libraryName +
                                               " from loader " + loader +
                                               ": findLibrary returned null");
}
            String error = doLoad(filename, loader);
if (error != null) {
throw new UnsatisfiedLinkError(error);
}
//Synthetic comment -- @@ -375,7 +376,7 @@
candidates.add(candidate);

if (IoUtils.canOpenReadOnly(candidate)) {
                String error = doLoad(candidate, loader);
if (error == null) {
return; // We successfully loaded the library. Job done.
}
//Synthetic comment -- @@ -391,7 +392,40 @@

private static native void nativeExit(int code);

    private String doLoad(String name, ClassLoader loader) {
        // Android apps are forked from the zygote, so they can't have a custom LD_LIBRARY_PATH,
        // which means that by default an app's shared library directory isn't on LD_LIBRARY_PATH.

        // The PathClassLoader set up by frameworks/base knows the appropriate path, so we can load
        // libraries with no dependencies just fine, but an app that has multiple libraries that
        // depend on each other needed to load them in most-dependent-first order.

        // We added API to Android's dynamic linker so we can update the library path used for
        // the currently-running process. We pull the desired path out of the ClassLoader here
        // and pass it to nativeLoad so that it can call the private dynamic linker API.

        // We didn't just change frameworks/base to update the LD_LIBRARY_PATH once at the
        // beginning because multiple apks can run in the same process and third party code can
        // use its own BaseDexClassLoader.

        // We didn't just add a dlopen_with_custom_LD_LIBRARY_PATH call because we wanted any
        // dlopen(3) calls made from a .so's JNI_OnLoad to work too.

        // So, find out what the native library search path is for the ClassLoader in question...
        String ldLibraryPath = null;
        if (loader != null && loader instanceof BaseDexClassLoader) {
            ldLibraryPath = ((BaseDexClassLoader) loader).getLdLibraryPath();
        }
        // nativeLoad should be synchronized so there's only one LD_LIBRARY_PATH in use regardless
        // of how many ClassLoaders are in the system, but dalvik doesn't support synchronized
        // internal natives.
        synchronized (this) {
            return nativeLoad(name, loader, ldLibraryPath);
        }
    }

    // TODO: should be synchronized, but dalvik doesn't support synchronized internal natives.
    private static native String nativeLoad(String filename, ClassLoader loader, String ldLibraryPath);

/**
* Provides a hint to the VM that it would be useful to attempt







