/*Support directories on the classpath, for resources.

This lets us run tests and legacy code that uses Class.getResource to
access resources, because we can put the resource directories on the
classpath.

I've also rewritten the toString implementation to show what path
we're _really_ using, rather than parroting back the original path we
were passed, because we won't actually look at all entries in the path,
so that can be very misleading. (It certainly confused the hell out of
me while working on this change.)

Change-Id:Iec4dca2244db9c9c793ac157e258fd61557a7a5d*/
//Synthetic comment -- diff --git a/dalvik/src/main/java/dalvik/system/BaseDexClassLoader.java b/dalvik/src/main/java/dalvik/system/BaseDexClassLoader.java
//Synthetic comment -- index 62ec5e3..1c006ba 100644

//Synthetic comment -- @@ -25,14 +25,7 @@
* {@link ClassLoader} implementations.
*/
public class BaseDexClassLoader extends ClassLoader {
    /** originally specified path (just used for {@code toString()}) */
    private final String originalPath;

    /** originally specified library path (just used for {@code toString()}) */
    private final String originalLibraryPath;

    /** structured lists of path elements */
    private final DexPathList pathList;

/**
* Constructs an instance.
//Synthetic comment -- @@ -50,37 +43,31 @@
public BaseDexClassLoader(String dexPath, File optimizedDirectory,
String libraryPath, ClassLoader parent) {
super(parent);

        this.originalPath = dexPath;
        this.originalLibraryPath = libraryPath;
        this.pathList =
            new DexPathList(this, dexPath, libraryPath, optimizedDirectory);
}

@Override
protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class clazz = pathList.findClass(name);

        if (clazz == null) {
            throw new ClassNotFoundException("Didn't find class \"" + name + "\" on path: " + originalPath);
}

        return clazz;
}

@Override
protected URL findResource(String name) {
        return pathList.findResource(name);
}

@Override
protected Enumeration<URL> findResources(String name) {
        return pathList.findResources(name);
}

@Override
public String findLibrary(String name) {
        return pathList.findLibrary(name);
}

/**
//Synthetic comment -- @@ -125,9 +112,7 @@
return null;
}

    @Override
    public String toString() {
        return getClass().getName()
                + "[dexPath=" + originalPath + ",libraryPath=" + originalLibraryPath + "]";
}
}








//Synthetic comment -- diff --git a/dalvik/src/main/java/dalvik/system/DexPathList.java b/dalvik/src/main/java/dalvik/system/DexPathList.java
//Synthetic comment -- index 728639b..1ae48d9 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.regex.Pattern;
//Synthetic comment -- @@ -54,7 +55,7 @@
private final ClassLoader definingContext;

/** list of dex/resource (class path) elements */
    private final Element[] dexElements;

/** list of native library directory elements */
private final File[] nativeLibraryDirectories;
//Synthetic comment -- @@ -98,11 +99,15 @@
}

this.definingContext = definingContext;
        this.dexElements =
            makeDexElements(splitDexPath(dexPath), optimizedDirectory);
this.nativeLibraryDirectories = splitLibraryPath(libraryPath);
}

/**
* Splits the given dex path string into elements using the path
* separator, pruning out any elements that do not refer to existing
//Synthetic comment -- @@ -159,7 +164,7 @@
* Helper for {@link #splitPaths}, which does the actual splitting
* and filtering and adding to a result.
*/
    private static void splitAndAdd(String searchPath, boolean wantDirectories,
ArrayList<File> resultList) {
if (searchPath == null) {
return;
//Synthetic comment -- @@ -167,8 +172,7 @@
for (String path : searchPath.split(":")) {
try {
StructStat sb = Libcore.os.stat(path);
                if ((wantDirectories && S_ISDIR(sb.st_mode)) ||
                        (!wantDirectories && S_ISREG(sb.st_mode))) {
resultList.add(new File(path));
}
} catch (ErrnoException ignored) {
//Synthetic comment -- @@ -215,12 +219,16 @@
* the exception here, and let dex == null.
*/
}
} else {
System.logW("Unknown file type for: " + file);
}

if ((zip != null) || (dex != null)) {
                elements.add(new Element(file, zip, dex));
}
}

//Synthetic comment -- @@ -287,7 +295,7 @@
* found in any of the dex files
*/
public Class findClass(String name) {
        for (Element element : dexElements) {
DexFile dex = element.dexFile;

if (dex != null) {
//Synthetic comment -- @@ -310,7 +318,7 @@
* resource is not found in any of the zip/jar files
*/
public URL findResource(String name) {
        for (Element element : dexElements) {
URL url = element.findResource(name);
if (url != null) {
return url;
//Synthetic comment -- @@ -328,7 +336,7 @@
public Enumeration<URL> findResources(String name) {
ArrayList<URL> result = new ArrayList<URL>();

        for (Element element : dexElements) {
URL url = element.findResource(name);
if (url != null) {
result.add(url);
//Synthetic comment -- @@ -363,26 +371,38 @@
*/
/*package*/ static class Element {
private final File file;
private final File zip;
private final DexFile dexFile;

private ZipFile zipFile;
        private boolean init;

        public Element(File file, File zip, DexFile dexFile) {
this.file = file;
this.zip = zip;
this.dexFile = dexFile;
}

public synchronized void maybeInit() {
            if (init) {
return;
}

            init = true;

            if (zip == null) {
return;
}

//Synthetic comment -- @@ -403,6 +423,19 @@
public URL findResource(String name) {
maybeInit();

if (zipFile == null || zipFile.getEntry(name) == null) {
/*
* Either this element has no zip/jar file (first







