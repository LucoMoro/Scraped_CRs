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
    void findClassesDerivingFrom(String super_name,
            Map<String, ClassReader> zipClasses,
            Map<String, ClassReader> inOutFound) {
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
//Synthetic comment -- index 0000000..2956958

//Synthetic comment -- @@ -0,0 +1,694 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.tools.layoutlib.create;

import com.android.tools.layoutlib.annotations.VisibleForTesting;
import com.android.tools.layoutlib.annotations.VisibleForTesting.Visibility;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Analyzes the input JAR using the ASM java bytecode manipulation library
 * to list the classes and their dependencies. A "dependency" is a class
 * used by another class.
 */
public class DependencyFinder {

    // Note: a bunch of stuff has package-level access for unit tests. Consider it private.

    /** Output logger. */
    private final Log mLog;

    /**
     * Creates a new analyzer.
     *
     * @param log The log output.
     */
    public DependencyFinder(Log log) {
        mLog = log;
    }

    /**
     * Starts the analysis using parameters from the constructor.
     *
     * @param osJarPath The input source JARs to parse.
     * @return A pair: [0]: map { class FQCN => set of FQCN class dependencies }.
     *                 [1]: map { missing class FQCN => set of FQCN class that uses it. }
     */
    public List<Map<String, Set<String>>> findDeps(List<String> osJarPath) throws IOException {

        Map<String, ClassReader> zipClasses = parseZip(osJarPath);
        mLog.info("Found %d classes in input JAR%s.",
                zipClasses.size(),
                osJarPath.size() > 1 ? "s" : "");

        Map<String, Set<String>> deps = findClassesDeps(zipClasses);

        Map<String, Set<String>> missing = findMissingClasses(deps, zipClasses.keySet());

        List<Map<String, Set<String>>> result = new ArrayList<Map<String,Set<String>>>(2);
        result.add(deps);
        result.add(missing);
        return result;
    }

    /**
     * Prints dependencies to the current logger, found stuff and missing stuff.
     */
    public void printAllDeps(List<Map<String, Set<String>>> result) {
        assert result.size() == 2;
        Map<String, Set<String>> deps = result.get(0);
        Map<String, Set<String>> missing = result.get(1);

        // Print all dependences found in the format:
        // +Found: <FQCN from zip>
        //     uses: FQCN

        mLog.info("++++++ %d Entries found in source JARs", deps.size());
        mLog.info("");

        for (Entry<String, Set<String>> entry : deps.entrySet()) {
            mLog.info(    "+Found  : %s", entry.getKey());
            for (String dep : entry.getValue()) {
                mLog.info("    uses: %s", dep);
            }

            mLog.info("");
        }


        // Now print all missing dependences in the format:
        // -Missing <FQCN>:
        //     used by: <FQCN>

        mLog.info("");
        mLog.info("------ %d Entries missing from source JARs", missing.size());
        mLog.info("");

        for (Entry<String, Set<String>> entry : missing.entrySet()) {
            mLog.info(    "-Missing  : %s", entry.getKey());
            for (String dep : entry.getValue()) {
                mLog.info("   used by: %s", dep);
            }

            mLog.info("");
        }
    }

    /**
     * Prints only a summary of the missing dependencies to the current logger.
     */
    public void printMissingDeps(List<Map<String, Set<String>>> result) {
        assert result.size() == 2;
        @SuppressWarnings("unused") Map<String, Set<String>> deps = result.get(0);
        Map<String, Set<String>> missing = result.get(1);

        for (String fqcn : missing.keySet()) {
            mLog.info("%s", fqcn);
        }
    }

    // ----------------

    /**
     * Parses a JAR file and returns a list of all classes founds using a map
     * class name => ASM ClassReader. Class names are in the form "android.view.View".
     */
    Map<String,ClassReader> parseZip(List<String> jarPathList) throws IOException {
        TreeMap<String, ClassReader> classes = new TreeMap<String, ClassReader>();

        for (String jarPath : jarPathList) {
            ZipFile zip = new ZipFile(jarPath);
            Enumeration<? extends ZipEntry> entries = zip.entries();
            ZipEntry entry;
            while (entries.hasMoreElements()) {
                entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    ClassReader cr = new ClassReader(zip.getInputStream(entry));
                    String className = classReaderToClassName(cr);
                    classes.put(className, cr);
                }
            }
        }

        return classes;
    }

    /**
     * Utility that returns the fully qualified binary class name for a ClassReader.
     * E.g. it returns something like android.view.View.
     */
    static String classReaderToClassName(ClassReader classReader) {
        if (classReader == null) {
            return null;
        } else {
            return classReader.getClassName().replace('/', '.');
        }
    }

    /**
     * Utility that returns the fully qualified binary class name from a path-like FQCN.
     * E.g. it returns android.view.View from android/view/View.
     */
    static String internalToBinaryClassName(String className) {
        if (className == null) {
            return null;
        } else {
            return className.replace('/', '.');
        }
    }

    /**
     * Finds all dependencies for all classes in keepClasses which are also
     * listed in zipClasses. Returns a map of all the dependencies found.
     */
    Map<String, Set<String>> findClassesDeps(Map<String, ClassReader> zipClasses) {

        // The dependencies that we'll collect.
        // It's a map Class name => uses class names.
        Map<String, Set<String>> dependencyMap = new TreeMap<String, Set<String>>();

        DependencyVisitor visitor = getVisitor();

        int count = 0;
        try {
            for (Entry<String, ClassReader> entry : zipClasses.entrySet()) {
                String name = entry.getKey();

                TreeSet<String> set = new TreeSet<String>();
                dependencyMap.put(name, set);
                visitor.setDependencySet(set);

                ClassReader cr = entry.getValue();
                cr.accept(visitor, 0 /* flags */);

                visitor.setDependencySet(null);

                mLog.debugNoln("Visited %d classes\r", ++count);
            }
        } finally {
            mLog.debugNoln("\n");
        }

        return dependencyMap;
    }

    /**
     * Computes which classes FQCN were found as dependencies that are NOT listed
     * in the original JAR classes.
     *
     * @param deps The map { FQCN => dependencies[] } returned by {@link #findClassesDeps(Map)}.
     * @param zipClasses The set of all classes FQCN found in the JAR files.
     * @return A map { FQCN not found in the zipClasses => classes using it }
     */
    private Map<String, Set<String>> findMissingClasses(
            Map<String, Set<String>> deps,
            Set<String> zipClasses) {
        Map<String, Set<String>> missing = new TreeMap<String, Set<String>>();

        for (Entry<String, Set<String>> entry : deps.entrySet()) {
            String name = entry.getKey();

            for (String dep : entry.getValue()) {
                if (!zipClasses.contains(dep)) {
                    // This dependency doesn't exist in the zip classes.
                    Set<String> set = missing.get(dep);
                    if (set == null) {
                        set = new TreeSet<String>();
                        missing.put(dep, set);
                    }
                    set.add(name);
                }
            }

        }

        return missing;
    }


    // ----------------------------------

    /**
     * Instantiates a new DependencyVisitor. Useful for unit tests.
     */
    @VisibleForTesting(visibility=Visibility.PRIVATE)
    DependencyVisitor getVisitor() {
        return new DependencyVisitor();
    }

    /**
     * Visitor to collect all the type dependencies from a class.
     */
    public class DependencyVisitor
        implements ClassVisitor, FieldVisitor, MethodVisitor, SignatureVisitor, AnnotationVisitor {

        private Set<String> mCurrentDepSet;

        /**
         * Creates a new visitor that will find all the dependencies for the visited class.
         */
        public DependencyVisitor() {
        }

        /**
         * Sets the {@link Set} where to record direct dependencies for this class.
         * This will change before each {@link ClassReader#accept(ClassVisitor, int)} call.
         */
        public void setDependencySet(Set<String> set) {
            mCurrentDepSet = set;
        }

        /**
         * Considers the given class name as a dependency.
         */
        public void considerName(String className) {
            if (className == null) {
                return;
            }

            className = internalToBinaryClassName(className);

            try {
                // exclude classes that are part of the default JRE (the one executing this program)
                if (getClass().getClassLoader().loadClass(className) != null) {
                    return;
                }
            } catch (ClassNotFoundException e) {
                // ignore
            }

            // Add it to the dependency set for the currently visited class, as needed.
            assert mCurrentDepSet != null;
            if (mCurrentDepSet != null) {
                mCurrentDepSet.add(className);
            }
        }

        /**
         * Considers this array of names using considerName().
         */
        public void considerNames(String[] classNames) {
            if (classNames != null) {
                for (String className : classNames) {
                    considerName(className);
                }
            }
        }

        /**
         * Considers this signature or type signature by invoking the {@link SignatureVisitor}
         * on it.
         */
        public void considerSignature(String signature) {
            if (signature != null) {
                SignatureReader sr = new SignatureReader(signature);
                // SignatureReader.accept will call accessType so we don't really have
                // to differentiate where the signature comes from.
                sr.accept(this);
            }
        }

        /**
         * Considers this {@link Type}. For arrays, the element type is considered.
         * If the type is an object, it's internal name is considered.
         */
        public void considerType(Type t) {
            if (t != null) {
                if (t.getSort() == Type.ARRAY) {
                    t = t.getElementType();
                }
                if (t.getSort() == Type.OBJECT) {
                    considerName(t.getInternalName());
                }
            }
        }

        /**
         * Considers a descriptor string. The descriptor is converted to a {@link Type}
         * and then considerType() is invoked.
         */
        public boolean considerDesc(String desc) {
            if (desc != null) {
                try {
                    if (desc.length() > 0 && desc.charAt(0) == '(') {
                        // This is a method descriptor with arguments and a return type.
                        Type t = Type.getReturnType(desc);
                        considerType(t);

                        for (Type arg : Type.getArgumentTypes(desc)) {
                            considerType(arg);
                        }

                    } else {
                        Type t = Type.getType(desc);
                        considerType(t);
                    }
                    return true;
                } catch (ArrayIndexOutOfBoundsException e) {
                    // ignore, not a valid type.
                }
            }
            return false;
        }


        // ---------------------------------------------------
        // --- ClassVisitor, FieldVisitor
        // ---------------------------------------------------

        // Visits a class header
        public void visit(int version, int access, String name,
                String signature, String superName, String[] interfaces) {
            // signature is the signature of this class. May be null if the class is not a generic
            // one, and does not extend or implement generic classes or interfaces.

            if (signature != null) {
                considerSignature(signature);
            }

            // superName is the internal of name of the super class (see getInternalName).
            // For interfaces, the super class is Object. May be null but only for the Object class.
            considerName(superName);

            // interfaces is the internal names of the class's interfaces (see getInternalName).
            // May be null.
            considerNames(interfaces);
        }

        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            // desc is the class descriptor of the annotation class.
            considerDesc(desc);
            return this; // return this to visit annotion values
        }

        public void visitAttribute(Attribute attr) {
            // pass
        }

        // Visits the end of a class
        public void visitEnd() {
            // pass
        }

        public FieldVisitor visitField(int access, String name, String desc,
                String signature, Object value) {
            // desc is the field's descriptor (see Type).
            considerDesc(desc);

            // signature is the field's signature. May be null if the field's type does not use
            // generic types.
            considerSignature(signature);

            return this; // a visitor to visit field annotations and attributes
        }

        public void visitInnerClass(String name, String outerName, String innerName, int access) {
            // name is the internal name of an inner class (see getInternalName).
            // Note: outerName/innerName seems to be null when we're reading the
            // _Original_ClassName classes generated by layoutlib_create.
            if (outerName != null) {
                considerName(name);
            }
        }

        public MethodVisitor visitMethod(int access, String name, String desc,
                String signature, String[] exceptions) {
            // desc is the method's descriptor (see Type).
            considerDesc(desc);
            // signature is the method's signature. May be null if the method parameters, return
            // type and exceptions do not use generic types.
            considerSignature(signature);

            return this; // returns this to visit the method
        }

        public void visitOuterClass(String owner, String name, String desc) {
            // pass
        }

        public void visitSource(String source, String debug) {
            // pass
        }


        // ---------------------------------------------------
        // --- MethodVisitor
        // ---------------------------------------------------

        public AnnotationVisitor visitAnnotationDefault() {
            return this; // returns this to visit the default value
        }


        public void visitCode() {
            // pass
        }

        // field instruction
        public void visitFieldInsn(int opcode, String owner, String name, String desc) {
            // name is the field's name, not a type.
            // desc is the field's descriptor (see Type).
            considerDesc(desc);
        }

        public void visitFrame(int type, int local, Object[] local2, int stack, Object[] stack2) {
            // pass
        }

        public void visitIincInsn(int var, int increment) {
            // pass -- an IINC instruction
        }

        public void visitInsn(int opcode) {
            // pass -- a zero operand instruction
        }

        public void visitIntInsn(int opcode, int operand) {
            // pass -- a single int operand instruction
        }

        public void visitJumpInsn(int opcode, Label label) {
            // pass -- a jump instruction
        }

        public void visitLabel(Label label) {
            // pass -- a label target
        }

        // instruction to load a constant from the stack
        public void visitLdcInsn(Object cst) {
            if (cst instanceof Type) {
                considerType((Type) cst);
            }
        }

        public void visitLineNumber(int line, Label start) {
            // pass
        }

        public void visitLocalVariable(String name, String desc,
                String signature, Label start, Label end, int index) {
            // desc is the type descriptor of this local variable.
            considerDesc(desc);
            // signature is the type signature of this local variable. May be null if the local
            // variable type does not use generic types.
            considerSignature(signature);
        }

        public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
            // pass -- a lookup switch instruction
        }

        public void visitMaxs(int maxStack, int maxLocals) {
            // pass
        }

        // instruction that invokes a method
        public void visitMethodInsn(int opcode, String owner, String name, String desc) {

            // owner is the internal name of the method's owner class
            if (!considerDesc(owner) && owner.indexOf('/') != -1) {
                considerName(owner);
            }
            // desc is the method's descriptor (see Type).
            considerDesc(desc);
        }

        // instruction multianewarray, whatever that is
        public void visitMultiANewArrayInsn(String desc, int dims) {

            // desc an array type descriptor.
            considerDesc(desc);
        }

        public AnnotationVisitor visitParameterAnnotation(int parameter, String desc,
                boolean visible) {
            // desc is the class descriptor of the annotation class.
            considerDesc(desc);
            return this; // return this to visit annotation values
        }

        public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
            // pass -- table switch instruction

        }

        public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
            // type is the internal name of the type of exceptions handled by the handler,
            // or null to catch any exceptions (for "finally" blocks).
            considerName(type);
        }

        // type instruction
        public void visitTypeInsn(int opcode, String type) {
            // type is the operand of the instruction to be visited. This operand must be the
            // internal name of an object or array class.
            if (!considerDesc(type) && type.indexOf('/') != -1) {
                considerName(type);
            }
        }

        public void visitVarInsn(int opcode, int var) {
            // pass -- local variable instruction
        }


        // ---------------------------------------------------
        // --- SignatureVisitor
        // ---------------------------------------------------

        private String mCurrentSignatureClass = null;

        // Starts the visit of a signature corresponding to a class or interface type
        public void visitClassType(String name) {
            mCurrentSignatureClass = name;
            considerName(name);
        }

        // Visits an inner class
        public void visitInnerClassType(String name) {
            if (mCurrentSignatureClass != null) {
                mCurrentSignatureClass += "$" + name;
                considerName(mCurrentSignatureClass);
            }
        }

        public SignatureVisitor visitArrayType() {
            return this; // returns this to visit the signature of the array element type
        }

        public void visitBaseType(char descriptor) {
            // pass -- a primitive type, ignored
        }

        public SignatureVisitor visitClassBound() {
            return this; // returns this to visit the signature of the class bound
        }

        public SignatureVisitor visitExceptionType() {
            return this; // return this to visit the signature of the exception type.
        }

        public void visitFormalTypeParameter(String name) {
            // pass
        }

        public SignatureVisitor visitInterface() {
            return this; // returns this to visit the signature of the interface type
        }

        public SignatureVisitor visitInterfaceBound() {
            return this; // returns this to visit the signature of the interface bound
        }

        public SignatureVisitor visitParameterType() {
            return this; // returns this to visit the signature of the parameter type
        }

        public SignatureVisitor visitReturnType() {
            return this; // returns this to visit the signature of the return type
        }

        public SignatureVisitor visitSuperclass() {
            return this; // returns this to visit the signature of the super class type
        }

        public SignatureVisitor visitTypeArgument(char wildcard) {
            return this; // returns this to visit the signature of the type argument
        }

        public void visitTypeVariable(String name) {
            // pass
        }

        public void visitTypeArgument() {
            // pass
        }


        // ---------------------------------------------------
        // --- AnnotationVisitor
        // ---------------------------------------------------


        // Visits a primitive value of an annotation
        public void visit(String name, Object value) {
            // value is the actual value, whose type must be Byte, Boolean, Character, Short,
            // Integer, Long, Float, Double, String or Type
            if (value instanceof Type) {
                considerType((Type) value);
            }
        }

        public AnnotationVisitor visitAnnotation(String name, String desc) {
            // desc is the class descriptor of the nested annotation class.
            considerDesc(desc);
            return this; // returns this to visit the actual nested annotation value
        }

        public AnnotationVisitor visitArray(String name) {
            return this; // returns this to visit the actual array value elements
        }

        public void visitEnum(String name, String desc, String value) {
            // desc is the class descriptor of the enumeration class.
            considerDesc(desc);
        }
    }
}








//Synthetic comment -- diff --git a/tools/layoutlib/create/src/com/android/tools/layoutlib/create/Log.java b/tools/layoutlib/create/src/com/android/tools/layoutlib/create/Log.java
//Synthetic comment -- index 8efd871..c3ba591 100644

//Synthetic comment -- @@ -33,11 +33,19 @@
}
}

    /** Similar to debug() but doesn't do a \n automatically. */
    public void debugNoln(String format, Object... args) {
        if (mVerbose) {
            String s = String.format(format, args);
            System.out.print(s);
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
//Synthetic comment -- index 4b7a348..9cd74db 100644

//Synthetic comment -- @@ -18,6 +18,8 @@

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


//Synthetic comment -- @@ -47,6 +49,8 @@

public static class Options {
public boolean generatePublicAccess = true;
        public boolean listAllDeps = false;
        public boolean listOnlyMissingDeps = false;
}

public static final Options sOptions = new Options();
//Synthetic comment -- @@ -60,16 +64,29 @@

if (!processArgs(log, args, osJarPath, osDestJar)) {
log.error("Usage: layoutlib_create [-v] [-p] output.jar input.jar ...");
            log.error("Usage: layoutlib_create [-v] [--list-deps|--missing-deps] input.jar ...");
System.exit(1);
}

        if (sOptions.listAllDeps || sOptions.listOnlyMissingDeps) {
            System.exit(listDeps(osJarPath, log));

        } else {
            System.exit(createLayoutLib(osDestJar[0], osJarPath, log));
        }


        System.exit(1);
    }

    private static int createLayoutLib(String osDestJar, ArrayList<String> osJarPath, Log log) {
        log.info("Output: %1$s", osDestJar);
for (String path : osJarPath) {
log.info("Input :      %1$s", path);
}

try {
            AsmGenerator agen = new AsmGenerator(log, osDestJar, new CreateInfo());

AsmAnalyzer aa = new AsmAnalyzer(log, osJarPath, agen,
new String[] {                          // derived from
//Synthetic comment -- @@ -116,17 +133,33 @@
for (String path : osJarPath) {
log.info("- Input JAR : %1$s", path);
}
                return 1;
}

            return 0;
} catch (IOException e) {
log.exception(e, "Failed to load jar");
} catch (LogAbortException e) {
e.error(log);
}

        return 1;
    }

    private static int listDeps(ArrayList<String> osJarPath, Log log) {
        DependencyFinder df = new DependencyFinder(log);
        try {
            List<Map<String, Set<String>>> result = df.findDeps(osJarPath);
            if (sOptions.listAllDeps) {
                df.printAllDeps(result);
            } else if (sOptions.listOnlyMissingDeps) {
                df.printMissingDeps(result);
            }
        } catch (IOException e) {
            log.exception(e, "Failed to load jar");
        }

        return 0;
}

/**
//Synthetic comment -- @@ -138,14 +171,21 @@
*/
private static boolean processArgs(Log log, String[] args,
ArrayList<String> osJarPath, String[] osDestJar) {
        boolean needs_dest = true;
for (int i = 0; i < args.length; i++) {
String s = args[i];
if (s.equals("-v")) {
log.setVerbose(true);
} else if (s.equals("-p")) {
sOptions.generatePublicAccess = false;
            } else if (s.equals("--list-deps")) {
                sOptions.listAllDeps = true;
                needs_dest = false;
            } else if (s.equals("--missing-deps")) {
                sOptions.listOnlyMissingDeps = true;
                needs_dest = false;
} else if (!s.startsWith("-")) {
                if (needs_dest && osDestJar[0] == null) {
osDestJar[0] = s;
} else {
osJarPath.add(s);
//Synthetic comment -- @@ -160,7 +200,7 @@
log.error("Missing parameter: path to input jar");
return false;
}
        if (needs_dest && osDestJar[0] == null) {
log.error("Missing parameter: path to output jar");
return false;
}







