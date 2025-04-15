/*Include library path information in BaseDexClassLoader for better error reporting in Runtime.loadLibrary

(cherry-pick of b6a576f43f1c23bb92493590a04bf9c72f092438.)

Change-Id:I6f34862327cf99d8c6f9a7e9aa3aeab47985969b*/
//Synthetic comment -- diff --git a/dalvik/src/main/java/dalvik/system/BaseDexClassLoader.java b/dalvik/src/main/java/dalvik/system/BaseDexClassLoader.java
//Synthetic comment -- index 5782fd9..62ec5e3 100644

//Synthetic comment -- @@ -28,6 +28,9 @@
/** originally specified path (just used for {@code toString()}) */
private final String originalPath;

/** structured lists of path elements */
private final DexPathList pathList;

//Synthetic comment -- @@ -49,6 +52,7 @@
super(parent);

this.originalPath = dexPath;
this.pathList =
new DexPathList(this, dexPath, libraryPath, optimizedDirectory);
}
//Synthetic comment -- @@ -123,6 +127,7 @@

@Override
public String toString() {
        return getClass().getName() + "[" + originalPath + "]";
}
}








//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/Runtime.java b/luni/src/main/java/java/lang/Runtime.java
//Synthetic comment -- index 320f157..efe2303 100644

//Synthetic comment -- @@ -362,8 +362,9 @@
if (loader != null) {
String filename = loader.findLibrary(libraryName);
if (filename == null) {
                throw new UnsatisfiedLinkError("Couldn't load " + libraryName + ": " +
                        "findLibrary returned null");
}
String error = nativeLoad(filename, loader);
if (error != null) {







