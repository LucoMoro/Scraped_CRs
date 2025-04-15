/*Layoutlib Create: Find dependencies.

Usage: layoutlib_create --list-deps /path/to/layoutlib.jar

Prints:
- all classes found in the Jar and the types they use (the dependencies).
- all the dependencies missing from the Jar and what uses them.

Change-Id:I8b2674df127e1494feed7a653282e88e4d2f5494*/
//Synthetic comment -- diff --git a/tools/layoutlib/create/src/com/android/tools/layoutlib/create/AsmAnalyzer.java b/tools/layoutlib/create/src/com/android/tools/layoutlib/create/AsmAnalyzer.java
//Synthetic comment -- index b197ea7..1549cf4 100644

//Synthetic comment -- @@ -45,7 +45,7 @@
public class AsmAnalyzer {

// Note: a bunch of stuff has package-level access for unit tests. Consider it private.
    
/** Output logger. */
private final Log mLog;
/** The input source JAR to parse. */
//Synthetic comment -- @@ -59,11 +59,11 @@

/**
* Creates a new analyzer.
     * 
* @param log The log output.
* @param osJarPath The input source JARs to parse.
* @param gen The generator to fill with the class list and dependency list.
     * @param deriveFrom Keep all classes that derive from these one (these included). 
* @param includeGlobs Glob patterns of classes to keep, e.g. "com.foo.*"
*        ("*" does not matches dots whilst "**" does, "." and "$" are interpreted as-is)
*/
//Synthetic comment -- @@ -81,16 +81,13 @@
* Fills the generator with classes & dependencies found.
*/
public void analyze() throws IOException, LogAbortException {

        AsmAnalyzer visitor = this;
        
Map<String, ClassReader> zipClasses = parseZip(mOsSourceJar);
mLog.info("Found %d classes in input JAR%s.", zipClasses.size(),
mOsSourceJar.size() > 1 ? "s" : "");
        
Map<String, ClassReader> found = findIncludes(zipClasses);
Map<String, ClassReader> deps = findDeps(zipClasses, found);
        
if (mGen != null) {
mGen.setKeep(found);
mGen.setDeps(deps);
//Synthetic comment -- @@ -117,10 +114,10 @@
}
}
}
        
return classes;
}
    
/**
* Utility that returns the fully qualified binary class name for a ClassReader.
* E.g. it returns something like android.view.View.
//Synthetic comment -- @@ -132,7 +129,7 @@
return classReader.getClassName().replace('/', '.');
}
}
    
/**
* Utility that returns the fully qualified binary class name from a path-like FQCN.
* E.g. it returns android.view.View from android/view/View.
//Synthetic comment -- @@ -144,7 +141,7 @@
return className.replace('/', '.');
}
}
    
/**
* Process the "includes" arrays.
* <p/>
//Synthetic comment -- @@ -162,11 +159,11 @@
for (String s : mDeriveFrom) {
findClassesDerivingFrom(s, zipClasses, found);
}
        
return found;
}

    
/**
* Uses ASM to find the class reader for the given FQCN class name.
* If found, insert it in the in_out_found map.
//Synthetic comment -- @@ -215,7 +212,7 @@
globPattern += "$";

Pattern regexp = Pattern.compile(globPattern);
        
for (Entry<String, ClassReader> entry : zipClasses.entrySet()) {
String class_name = entry.getKey();
if (regexp.matcher(class_name).matches()) {
//Synthetic comment -- @@ -229,10 +226,9 @@
* determine if they are derived from the given FQCN super class name.
* Inserts the super class and all the class objects found in the map.
*/
    void findClassesDerivingFrom(String super_name, Map<String, ClassReader> zipClasses,
            Map<String, ClassReader> inOutFound) throws LogAbortException {
        ClassReader super_clazz = findClass(super_name, zipClasses, inOutFound);

for (Entry<String, ClassReader> entry : zipClasses.entrySet()) {
String className = entry.getKey();
if (super_name.equals(className)) {
//Synthetic comment -- @@ -284,7 +280,7 @@
for (ClassReader cr : inOutKeepClasses.values()) {
cr.accept(visitor, 0 /* flags */);
}
        
while (new_deps.size() > 0 || new_keep.size() > 0) {
deps.putAll(new_deps);
inOutKeepClasses.putAll(new_keep);
//Synthetic comment -- @@ -308,12 +304,12 @@
return deps;
}

    

// ----------------------------------
    
/**
     * Visitor to collect all the type dependencies from a class. 
*/
public class DependencyVisitor
implements ClassVisitor, FieldVisitor, MethodVisitor, SignatureVisitor, AnnotationVisitor {
//Synthetic comment -- @@ -333,7 +329,7 @@
* Creates a new visitor that will find all the dependencies for the visited class.
* Types which are already in the zipClasses, keepClasses or inDeps are not marked.
* New dependencies are marked in outDeps.
         * 
* @param zipClasses All classes found in the source JAR.
* @param inKeep Classes from which dependencies are to be found.
* @param inDeps Dependencies already known.
//Synthetic comment -- @@ -350,7 +346,7 @@
mInDeps = inDeps;
mOutDeps = outDeps;
}
        
/**
* Considers the given class name as a dependency.
* If it does, add to the mOutDeps map.
//Synthetic comment -- @@ -361,7 +357,7 @@
}

className = internalToBinaryClassName(className);
            
// exclude classes that have already been found
if (mInKeep.containsKey(className) ||
mOutKeep.containsKey(className) ||
//Synthetic comment -- @@ -384,7 +380,7 @@
} catch (ClassNotFoundException e) {
// ignore
}
            
// accept this class:
// - android classes are added to dependencies
// - non-android classes are added to the list of classes to keep as-is (they don't need
//Synthetic comment -- @@ -395,7 +391,7 @@
mOutKeep.put(className, cr);
}
}
        
/**
* Considers this array of names using considerName().
*/
//Synthetic comment -- @@ -450,7 +446,7 @@
}
}

        
// ---------------------------------------------------
// --- ClassVisitor, FieldVisitor
// ---------------------------------------------------
//Synthetic comment -- @@ -460,7 +456,7 @@
String signature, String superName, String[] interfaces) {
// signature is the signature of this class. May be null if the class is not a generic
// one, and does not extend or implement generic classes or interfaces.
            
if (signature != null) {
considerSignature(signature);
}
//Synthetic comment -- @@ -468,7 +464,7 @@
// superName is the internal of name of the super class (see getInternalName).
// For interfaces, the super class is Object. May be null but only for the Object class.
considerName(superName);
            
// interfaces is the internal names of the class's interfaces (see getInternalName).
// May be null.
considerNames(interfaces);
//Synthetic comment -- @@ -513,7 +509,7 @@
// signature is the method's signature. May be null if the method parameters, return
// type and exceptions do not use generic types.
considerSignature(signature);
            
return this; // returns this to visit the method
}

//Synthetic comment -- @@ -525,7 +521,7 @@
// pass
}

        
// ---------------------------------------------------
// --- MethodVisitor
// ---------------------------------------------------
//Synthetic comment -- @@ -601,7 +597,7 @@

// instruction that invokes a method
public void visitMethodInsn(int opcode, String owner, String name, String desc) {
            
// owner is the internal name of the method's owner class
considerName(owner);
// desc is the method's descriptor (see Type).
//Synthetic comment -- @@ -610,7 +606,7 @@

// instruction multianewarray, whatever that is
public void visitMultiANewArrayInsn(String desc, int dims) {
            
// desc an array type descriptor.
considerDesc(desc);
}
//Synthetic comment -- @@ -624,7 +620,7 @@

public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
// pass -- table switch instruction
            
}

public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
//Synthetic comment -- @@ -641,10 +637,10 @@
}

public void visitVarInsn(int opcode, int var) {
            // pass -- local variable instruction 
}

        
// ---------------------------------------------------
// --- SignatureVisitor
// ---------------------------------------------------
//Synthetic comment -- @@ -716,8 +712,8 @@
public void visitTypeArgument() {
// pass
}
        
        
// ---------------------------------------------------
// --- AnnotationVisitor
// ---------------------------------------------------
//Synthetic comment -- @@ -746,6 +742,6 @@
// desc is the class descriptor of the enumeration class.
considerDesc(desc);
}
        
}
}








//Synthetic comment -- diff --git a/tools/layoutlib/create/src/com/android/tools/layoutlib/create/DependencyFinder.java b/tools/layoutlib/create/src/com/android/tools/layoutlib/create/DependencyFinder.java
new file mode 100755
//Synthetic comment -- index 0000000..22ace1f

//Synthetic comment -- @@ -0,0 +1,681 @@








//Synthetic comment -- diff --git a/tools/layoutlib/create/src/com/android/tools/layoutlib/create/Log.java b/tools/layoutlib/create/src/com/android/tools/layoutlib/create/Log.java
//Synthetic comment -- index 8efd871..c3ba591 100644

//Synthetic comment -- @@ -33,11 +33,19 @@
}
}

public void info(String format, Object... args) {
String s = String.format(format, args);
outPrintln(s);
}
    
public void error(String format, Object... args) {
String s = String.format(format, args);
errPrintln(s);
//Synthetic comment -- @@ -50,15 +58,15 @@
pw.flush();
error(format + "\n" + sw.toString(), args);
}
    
/** for unit testing */
protected void errPrintln(String msg) {
System.err.println(msg);
}
    
/** for unit testing */
protected void outPrintln(String msg) {
System.out.println(msg);
}
    
}








//Synthetic comment -- diff --git a/tools/layoutlib/create/src/com/android/tools/layoutlib/create/Main.java b/tools/layoutlib/create/src/com/android/tools/layoutlib/create/Main.java
//Synthetic comment -- index 4b7a348..9a8da5e 100644

//Synthetic comment -- @@ -18,6 +18,8 @@

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;


//Synthetic comment -- @@ -47,6 +49,7 @@

public static class Options {
public boolean generatePublicAccess = true;
}

public static final Options sOptions = new Options();
//Synthetic comment -- @@ -60,16 +63,29 @@

if (!processArgs(log, args, osJarPath, osDestJar)) {
log.error("Usage: layoutlib_create [-v] [-p] output.jar input.jar ...");
System.exit(1);
}

        log.info("Output: %1$s", osDestJar[0]);
for (String path : osJarPath) {
log.info("Input :      %1$s", path);
}

try {
            AsmGenerator agen = new AsmGenerator(log, osDestJar[0], new CreateInfo());

AsmAnalyzer aa = new AsmAnalyzer(log, osJarPath, agen,
new String[] {                          // derived from
//Synthetic comment -- @@ -116,17 +132,29 @@
for (String path : osJarPath) {
log.info("- Input JAR : %1$s", path);
}
                System.exit(1);
}

            System.exit(0);
} catch (IOException e) {
log.exception(e, "Failed to load jar");
} catch (LogAbortException e) {
e.error(log);
}

        System.exit(1);
}

/**
//Synthetic comment -- @@ -138,14 +166,18 @@
*/
private static boolean processArgs(Log log, String[] args,
ArrayList<String> osJarPath, String[] osDestJar) {
for (int i = 0; i < args.length; i++) {
String s = args[i];
if (s.equals("-v")) {
log.setVerbose(true);
} else if (s.equals("-p")) {
sOptions.generatePublicAccess = false;
} else if (!s.startsWith("-")) {
                if (osDestJar[0] == null) {
osDestJar[0] = s;
} else {
osJarPath.add(s);
//Synthetic comment -- @@ -160,7 +192,7 @@
log.error("Missing parameter: path to input jar");
return false;
}
        if (osDestJar[0] == null) {
log.error("Missing parameter: path to output jar");
return false;
}







