/*Issue 6894:   Eclipse ADT layout editor can't handle custom Views: ClassCastException (multiple ClassLoaders?)

Change-Id:I7f273613891c56c0983890270a724777a659eb7c*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index 801b165..6885c3d 100644

//Synthetic comment -- @@ -30,11 +30,13 @@
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolder;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IFileListener;
import com.android.ide.eclipse.adt.internal.sdk.LoadStatus;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.Sdk.ITargetChangeListener;
//Synthetic comment -- @@ -48,6 +50,9 @@
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
//Synthetic comment -- @@ -97,10 +102,14 @@
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* The activator class controls the plug-in life cycle
//Synthetic comment -- @@ -143,6 +152,74 @@

protected boolean mSdkIsLoading;

/**
* An error handler for checkSdkLocationAndId() that will handle the generated error
* or warning message. Each method must return a boolean that will in turn be returned by
//Synthetic comment -- @@ -268,6 +345,10 @@
// Wait 2 seconds before starting the ping job. This leaves some time to the
// other bundles to initialize.
pingJob.schedule(2000 /*milliseconds*/);
}

/*
//Synthetic comment -- @@ -279,6 +360,11 @@
public void stop(BundleContext context) throws Exception {
super.stop(context);

stopEditors();

mRed.dispose();
//Synthetic comment -- @@ -1323,4 +1409,51 @@
}
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index 46461b0..f04eca0 100644

//Synthetic comment -- @@ -74,7 +74,7 @@
}

// load the class.
        ProjectClassLoader loader = new ProjectClassLoader(mParentClassLoader, mProject);
try {
clazz = loader.loadClass(className);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectClassLoader.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectClassLoader.java
//Synthetic comment -- index 5244618..268d704 100644

//Synthetic comment -- @@ -65,7 +65,7 @@

// get the class name segments
String[] segments = name.split("\\."); //$NON-NLS-1$
            
File classFile = getFile(outFolder, segments, 0);
if (classFile == null) {
if (mInsideJarClassLoader == false) {
//Synthetic comment -- @@ -75,18 +75,24 @@
throw new ClassNotFoundException(name);
}
}
            
// load the content of the file and create the class.
            FileInputStream fis = new FileInputStream(classFile);
            byte[] data = new byte[(int)classFile.length()];
int read = 0;
try {
read = fis.read(data);
} catch (IOException e) {
data = null;
}
            fis.close();
            
if (data != null) {
Class<?> clazz = defineClass(null, data, 0, read);
if (clazz != null) {
//Synthetic comment -- @@ -99,7 +105,7 @@

throw new ClassNotFoundException(name);
}
    
/**
* Returns the File matching the a certain path from a root {@link File}.
* <p/>The methods checks that the file ends in .class even though the last segment
//Synthetic comment -- @@ -121,7 +127,7 @@

// we're at the last segments. we look for a matching <file>.class
if (index == segments.length - 1) {
            toMatch = toMatch + ".class"; 

if (files != null) {
for (File file : files) {
//Synthetic comment -- @@ -130,13 +136,13 @@
}
}
}
            
// no match? abort.
throw new FileNotFoundException();
}
        
String innerClassName = null;
        
if (files != null) {
for (File file : files) {
if (file.isDirectory()) {
//Synthetic comment -- @@ -151,20 +157,20 @@
sb.append(segments[i]);
}
sb.append(".class");
                        
innerClassName = sb.toString();
}
                    
if (file.getName().equals(innerClassName)) {
return file;
}
}
}
}
        
return null;
}
    
/**
* Loads a class from the 3rd party jar present in the project
* @throws ClassNotFoundException
//Synthetic comment -- @@ -173,10 +179,10 @@
if (mJarClassLoader == null) {
// get the OS path to all the external jars
URL[] jars = getExternalJars();
            
mJarClassLoader = new URLClassLoader(jars, this /* parent */);
}
        
try {
// because a class loader always look in its parent loader first, we need to know
// that we are querying the jar classloader. This will let us know to not query
//Synthetic comment -- @@ -187,7 +193,7 @@
mInsideJarClassLoader = false;
}
}
    
/**
* Returns an array of external jar files used by the project.
* @return an array of OS-specific absolute file paths
//Synthetic comment -- @@ -195,7 +201,7 @@
private final URL[] getExternalJars() {
// get a java project from it
IJavaProject javaProject = JavaCore.create(mJavaProject.getProject());
        
IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();

ArrayList<URL> oslibraryList = new ArrayList<URL>();
//Synthetic comment -- @@ -206,7 +212,7 @@
e.getEntryKind() == IClasspathEntry.CPE_VARIABLE) {
// if this is a classpath variable reference, we resolve it.
if (e.getEntryKind() == IClasspathEntry.CPE_VARIABLE) {
                        e = JavaCore.getResolvedClasspathEntry(e); 
}

// get the IPath








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidJarLoader.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidJarLoader.java
//Synthetic comment -- index 3bb125a..48e8b49 100644

//Synthetic comment -- @@ -35,7 +35,7 @@
* Custom class loader able to load a class from the SDK jar file.
*/
public class AndroidJarLoader extends ClassLoader implements IAndroidClassLoader {
    
/**
* Wrapper around a {@link Class} to provide the methods of
* {@link IAndroidClassLoader.IClassDescriptor}.
//Synthetic comment -- @@ -72,7 +72,7 @@
public IClassDescriptor getSuperclass() {
return new ClassWrapper(mClass.getSuperclass());
}
        
@Override
public boolean equals(Object clazz) {
if (clazz instanceof ClassWrapper) {
//Synthetic comment -- @@ -80,7 +80,7 @@
}
return super.equals(clazz);
}
        
@Override
public int hashCode() {
return mClass.hashCode();
//Synthetic comment -- @@ -97,28 +97,28 @@
}

}
    
private String mOsFrameworkLocation;
    
/** A cache for binary data extracted from the zip */
private final HashMap<String, byte[]> mEntryCache = new HashMap<String, byte[]>();
/** A cache for already defined Classes */
private final HashMap<String, Class<?> > mClassCache = new HashMap<String, Class<?> >();
    
/**
* Creates the class loader by providing the os path to the framework jar archive
     * 
* @param osFrameworkLocation OS Path of the framework JAR file
*/
public AndroidJarLoader(String osFrameworkLocation) {
super();
mOsFrameworkLocation = osFrameworkLocation;
}
    
public String getSource() {
return mOsFrameworkLocation;
}
    
/**
* Pre-loads all class binary data that belong to the given package by reading the archive
* once and caching them internally.
//Synthetic comment -- @@ -130,7 +130,7 @@
* found later.
* <p/>
* May throw some exceptions if the framework JAR cannot be read.
     * 
* @param packageFilter The package that contains all the class data to preload, using a fully
*                    qualified binary name (.e.g "com.my.package."). The matching algorithm
*                    is simple "startsWith". Use an empty string to include everything.
//Synthetic comment -- @@ -144,41 +144,51 @@
throws IOException, InvalidAttributeValueException, ClassFormatError {
// Transform the package name into a zip entry path
String pathFilter = packageFilter.replaceAll("\\.", "/"); //$NON-NLS-1$ //$NON-NLS-2$
        
SubMonitor progress = SubMonitor.convert(monitor, taskLabel == null ? "" : taskLabel, 100);
        
        // create streams to read the intermediary archive
        FileInputStream fis = new FileInputStream(mOsFrameworkLocation);
        ZipInputStream zis = new ZipInputStream(fis);
        ZipEntry entry;       
        while ((entry = zis.getNextEntry()) != null) {
            // get the name of the entry.
            String entryPath = entry.getName();
            
            if (!entryPath.endsWith(AndroidConstants.DOT_CLASS)) {
                // only accept class files
                continue;
            }

            // check if it is part of the package to preload
            if (pathFilter.length() > 0 && !entryPath.startsWith(pathFilter)) {
                continue;
            }
            String className = entryPathToClassName(entryPath);

            if (!mEntryCache.containsKey(className)) {
                long entrySize = entry.getSize();
                if (entrySize > Integer.MAX_VALUE) {
                    throw new InvalidAttributeValueException();
}
                byte[] data = readZipData(zis, (int)entrySize);
                mEntryCache.put(className, data);
            }

            // advance 5% of whatever is allocated on the progress bar
            progress.setWorkRemaining(100);
            progress.worked(5);
            progress.subTask(String.format("Preload %1$s", className));
}
}

//Synthetic comment -- @@ -186,14 +196,14 @@
* Finds and loads all classes that derive from a given set of super classes.
* <p/>
* As a side-effect this will load and cache most, if not all, classes in the input JAR file.
     * 
* @param packageFilter Base name of package of classes to find.
*                      Use an empty string to find everyting.
     * @param superClasses The super classes of all the classes to find. 
* @return An hash map which keys are the super classes looked for and which values are
*         ArrayList of the classes found. The array lists are always created for all the
*         valid keys, they are simply empty if no deriving class is found for a given
     *         super class. 
* @throws IOException
* @throws InvalidAttributeValueException
* @throws ClassFormatError
//Synthetic comment -- @@ -212,45 +222,56 @@
mClassesFound.put(className, new ArrayList<IClassDescriptor>());
}

        // create streams to read the intermediary archive
        FileInputStream fis = new FileInputStream(mOsFrameworkLocation);
        ZipInputStream zis = new ZipInputStream(fis);
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            // get the name of the entry and convert to a class binary name
            String entryPath = entry.getName();
            if (!entryPath.endsWith(AndroidConstants.DOT_CLASS)) {
                // only accept class files
                continue;
            }
            if (packageFilter.length() > 0 && !entryPath.startsWith(packageFilter)) {
                // only accept stuff from the requested root package.
                continue;
            }
            String className = entryPathToClassName(entryPath);
      
            Class<?> loaded_class = mClassCache.get(className);
            if (loaded_class == null) {
                byte[] data = mEntryCache.get(className);
                if (data == null) {    
                    // Get the class and cache it
                    long entrySize = entry.getSize();
                    if (entrySize > Integer.MAX_VALUE) {
                        throw new InvalidAttributeValueException();
                    }
                    data = readZipData(zis, (int)entrySize);
}
                loaded_class = defineAndCacheClass(className, data);
            }

            for (Class<?> superClass = loaded_class.getSuperclass();
                    superClass != null;
                    superClass = superClass.getSuperclass()) {
                String superName = superClass.getCanonicalName();
                if (mClassesFound.containsKey(superName)) {
                    mClassesFound.get(superName).add(new ClassWrapper(loaded_class));
                    break;
}
}
}

//Synthetic comment -- @@ -268,7 +289,7 @@

/**
* Finds the class with the specified binary name.
     * 
* {@inheritDoc}
*/
@Override
//Synthetic comment -- @@ -282,7 +303,7 @@
} else if (cached_class != null) {
return cached_class;
}
            
// if not found, look it up and cache it
byte[] data = loadClassData(name);
if (data != null) {
//Synthetic comment -- @@ -296,13 +317,13 @@
} catch (ClassNotFoundException e) {
throw e;
} catch (Exception e) {
            throw new ClassNotFoundException(e.getMessage()); 
}
}

/**
* Defines a class based on its binary data and caches the resulting class object.
     * 
* @param name The binary name of the class (i.e. package.class1$class2)
* @param data The binary data from the loader.
* @return The class defined
//Synthetic comment -- @@ -319,17 +340,17 @@
}
return cached_class;
}
    
/**
* Loads a class data from its binary name.
* <p/>
* This uses the class binary data that has been preloaded earlier by the preLoadClasses()
* method if possible.
     * 
* @param className the binary name
* @return an array of bytes representing the class data or null if not found
     * @throws InvalidAttributeValueException 
     * @throws IOException 
*/
private synchronized byte[] loadClassData(String className)
throws InvalidAttributeValueException, IOException {
//Synthetic comment -- @@ -338,7 +359,7 @@
if (data != null) {
return data;
}
        
// The name is a binary name. Something like "android.R", or "android.R$id".
// Make a path out of it.
String entryName = className.replaceAll("\\.", "/") + AndroidConstants.DOT_CLASS; //$NON-NLS-1$ //$NON-NLS-2$
//Synthetic comment -- @@ -346,14 +367,14 @@
// create streams to read the intermediary archive
FileInputStream fis = new FileInputStream(mOsFrameworkLocation);
ZipInputStream zis = new ZipInputStream(fis);
        
// loop on the entries of the intermediary package and put them in the final package.
ZipEntry entry;

while ((entry = zis.getNextEntry()) != null) {
// get the name of the entry.
String currEntryName = entry.getName();
            
if (currEntryName.equals(entryName)) {
long entrySize = entry.getSize();
if (entrySize > Integer.MAX_VALUE) {
//Synthetic comment -- @@ -370,7 +391,7 @@

/**
* Reads data for the <em>current</em> entry from the zip input stream.
     * 
* @param zis The Zip input stream
* @param entrySize The entry size. -1 if unknown.
* @return The new data for the <em>current</em> entry.
//Synthetic comment -- @@ -378,17 +399,17 @@
*/
private byte[] readZipData(ZipInputStream zis, int entrySize) throws IOException {
int block_size = 1024;
        int data_size = entrySize < 1 ? block_size : entrySize; 
int offset = 0;
byte[] data = new byte[data_size];
        
while(zis.available() != 0) {
int count = zis.read(data, offset, data_size - offset);
if (count < 0) {  // read data is done
break;
}
offset += count;
            
if (entrySize >= 1 && offset >= entrySize) {  // we know the size and we're done
break;
}
//Synthetic comment -- @@ -403,7 +424,7 @@
block_size *= 2;
}
}
        
if (offset < data_size) {
// buffer was allocated too large, trim it
byte[] temp = new byte[offset];
//Synthetic comment -- @@ -412,7 +433,7 @@
}
data = temp;
}
        
return data;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetParser.java
//Synthetic comment -- index fb19fc8..afd1211 100644

//Synthetic comment -- @@ -114,7 +114,7 @@
// parse the rest of the data.

AndroidJarLoader classLoader =
                new AndroidJarLoader(mAndroidTarget.getPath(IAndroidTarget.ANDROID_JAR));

preload(classLoader, progress.newChild(40, SubMonitor.SUPPRESS_NONE));

//Synthetic comment -- @@ -662,13 +662,12 @@
AdtPlugin.log(IStatus.ERROR, "layoutlib.jar is missing!"); //$NON-NLS-1$
} else {
URI uri = f.toURI();
                URL url = uri.toURL();

                // create a class loader. Because this jar reference interfaces
                // that are in the editors plugin, it's important to provide
                // a parent class loader.
                layoutBridge.classLoader = new URLClassLoader(new URL[] { url },
                        this.getClass().getClassLoader());

// load the class
Class<?> clazz = layoutBridge.classLoader.loadClass(AndroidConstants.CLASS_BRIDGE);







