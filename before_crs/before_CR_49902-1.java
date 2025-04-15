/*Stop Facebook crashing with NoSuchFieldException.

The Facebook app uses reflection to access a private field of
BaseDexClassLoader, and won't start if we rename that field.

Bug: 7726934
Change-Id:I0b8febed1226655dbfeb5d1a539734b860558df4*/
//Synthetic comment -- diff --git a/dalvik/src/main/java/dalvik/system/BaseDexClassLoader.java b/dalvik/src/main/java/dalvik/system/BaseDexClassLoader.java
//Synthetic comment -- index 6d5ae1a..d3ec95a 100644

//Synthetic comment -- @@ -25,7 +25,7 @@
* {@link ClassLoader} implementations.
*/
public class BaseDexClassLoader extends ClassLoader {
    private final DexPathList path;

/**
* Constructs an instance.
//Synthetic comment -- @@ -43,31 +43,31 @@
public BaseDexClassLoader(String dexPath, File optimizedDirectory,
String libraryPath, ClassLoader parent) {
super(parent);
        this.path = new DexPathList(this, dexPath, libraryPath, optimizedDirectory);
}

@Override
protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class c = path.findClass(name);
if (c == null) {
            throw new ClassNotFoundException("Didn't find class \"" + name + "\" on path: " + path);
}
return c;
}

@Override
protected URL findResource(String name) {
        return path.findResource(name);
}

@Override
protected Enumeration<URL> findResources(String name) {
        return path.findResources(name);
}

@Override
public String findLibrary(String name) {
        return path.findLibrary(name);
}

/**
//Synthetic comment -- @@ -117,7 +117,7 @@
*/
public String getLdLibraryPath() {
StringBuilder result = new StringBuilder();
        for (File directory : path.getNativeLibraryDirectories()) {
if (result.length() > 0) {
result.append(':');
}
//Synthetic comment -- @@ -127,6 +127,6 @@
}

@Override public String toString() {
        return getClass().getName() + "[" + path + "]";
}
}







