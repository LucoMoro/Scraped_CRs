/*More backwards compatibility for Facebook.

Another day, another private field accessed.

Bug: 7726934
Change-Id:I1cf2e9b9c9c7c53afd43642fcbf8f1a1d203bf6c*/
//Synthetic comment -- diff --git a/dalvik/src/main/java/dalvik/system/DexPathList.java b/dalvik/src/main/java/dalvik/system/DexPathList.java
//Synthetic comment -- index 3088ca5..3d9ee3e 100644

//Synthetic comment -- @@ -54,10 +54,14 @@
/** class definition context */
private final ClassLoader definingContext;

    /** list of dex/resource (class path) elements */
    private final Element[] pathElements;

    /** list of native library directory elements */
private final File[] nativeLibraryDirectories;

/**
//Synthetic comment -- @@ -99,12 +103,12 @@
}

this.definingContext = definingContext;
        this.pathElements = makeDexElements(splitDexPath(dexPath), optimizedDirectory);
this.nativeLibraryDirectories = splitLibraryPath(libraryPath);
}

@Override public String toString() {
        return "DexPathList[pathElements=" + Arrays.toString(pathElements) +
",nativeLibraryDirectories=" + Arrays.toString(nativeLibraryDirectories) + "]";
}

//Synthetic comment -- @@ -302,7 +306,7 @@
* found in any of the dex files
*/
public Class findClass(String name) {
        for (Element element : pathElements) {
DexFile dex = element.dexFile;

if (dex != null) {
//Synthetic comment -- @@ -325,7 +329,7 @@
* resource is not found in any of the zip/jar files
*/
public URL findResource(String name) {
        for (Element element : pathElements) {
URL url = element.findResource(name);
if (url != null) {
return url;
//Synthetic comment -- @@ -343,7 +347,7 @@
public Enumeration<URL> findResources(String name) {
ArrayList<URL> result = new ArrayList<URL>();

        for (Element element : pathElements) {
URL url = element.findResource(name);
if (url != null) {
result.add(url);







