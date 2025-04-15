/*More backwards compatibility for Facebook.

Another day, another private field accessed.

Bug: 7726934
Change-Id:I1cf2e9b9c9c7c53afd43642fcbf8f1a1d203bf6c*/




//Synthetic comment -- diff --git a/dalvik/src/main/java/dalvik/system/DexPathList.java b/dalvik/src/main/java/dalvik/system/DexPathList.java
//Synthetic comment -- index 3088ca5..96000da 100644

//Synthetic comment -- @@ -56,6 +56,8 @@

/** list of dex/resource (class path) elements */
private final Element[] pathElements;
    /** Backwards-compatible synonym for 'pathElements' to keep Facebook running (http://b/7726934). */
    private final Element[] dexElements;

/** list of native library directory elements */
private final File[] nativeLibraryDirectories;
//Synthetic comment -- @@ -100,6 +102,7 @@

this.definingContext = definingContext;
this.pathElements = makeDexElements(splitDexPath(dexPath), optimizedDirectory);
        this.dexElements = this.pathElements; // http://b/7726934
this.nativeLibraryDirectories = splitLibraryPath(libraryPath);
}








