/*Check for unsupported Java packages

If you include a library in your class path which was not built as an
Android project it's possible for it to include references to classes
in the java.* and javax.* namespace which are not valid in Android.

This changeset adds a new lint detector for this. It augments the API
database used by the API Checker to record the set of platform
packages, and to be able to quickly query whether an internal class
reference is in one of the valid packages.

The new lint check runs only on libraries (since tools will typically
set up the right classpath to give immediate errors if you try to
access invalid packages from your own code).

This was requested in issue 39109.

Change-Id:Id1ce7982e683bae9a484e7b75d7e77a256ca4414*/




//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiClass.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiClass.java
//Synthetic comment -- index b534b2f..365de80 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import static com.android.SdkConstants.CONSTRUCTOR_NAME;

import com.android.annotations.Nullable;
import com.android.utils.Pair;
import com.google.common.collect.Lists;

//Synthetic comment -- @@ -229,6 +230,16 @@

}

    @Nullable
    public String getPackage() {
        int index = mName.lastIndexOf('/');
        if (index != -1) {
            return mName.substring(0, index);
        }

        return null;
    }

@Override
public String toString() {
return mName;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java
//Synthetic comment -- index ba93d01..fd7bba9 100644

//Synthetic comment -- @@ -312,7 +312,6 @@
className.substring(className.lastIndexOf('/') + 1), null,
SearchHints.create(NEAREST).matchJavaSymbol());
}
}
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiLookup.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiLookup.java
//Synthetic comment -- index 9d661a8..17578b8 100644

//Synthetic comment -- @@ -24,7 +24,9 @@
import com.android.tools.lint.client.api.LintClient;
import com.android.tools.lint.detector.api.LintUtils;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.common.primitives.UnsignedBytes;

//Synthetic comment -- @@ -74,12 +76,12 @@
/** Relative path to the api-versions.xml database file within the Lint installation */
private static final String XML_FILE_PATH = "platform-tools/api/api-versions.xml"; //$NON-NLS-1$
private static final String FILE_HEADER = "API database used by Android lint\000";
    private static final int BINARY_FORMAT_VERSION = 5;
private static final boolean DEBUG_FORCE_REGENERATE_BINARY = false;
private static final boolean DEBUG_SEARCH = false;
private static final boolean WRITE_STATS = false;
/** Default size to reserve for each API entry when creating byte buffer to build up data */
    private static final int BYTES_PER_ENTRY = 36;

private final LintClient mClient;
private final File mXmlFile;
//Synthetic comment -- @@ -89,6 +91,7 @@
private int[] mIndices;
private int mClassCount;
private int mMethodCount;
    private String[] mJavaPackages;

private static WeakReference<ApiLookup> sInstance =
new WeakReference<ApiLookup>(null);
//Synthetic comment -- @@ -237,16 +240,20 @@
*     version, it can ignore it (and regenerate the cache from XML).
* 3. The number of classes [1 int]
* 4. The number of members (across all classes) [1 int].
     * 5. The number of java/javax packages [1 int]
     * 6. The java/javax package name table. Each item consists of a byte count for
     *    the package string (as 1 byte) followed by the UTF-8 encoded bytes for each package.
     *    These are in sorted order.
     * 7. Class offset table (one integer per class, pointing to the byte offset in the
*      file (relative to the beginning of the file) where each class begins.
*      The classes are always sorted alphabetically by fully qualified name.
     * 8. Member offset table (one integer per member, pointing to the byte offset in the
*      file (relative to the beginning of the file) where each member entry begins.
*      The members are always sorted alphabetically.
     * 9. Class entry table. Each class entry consists of the fully qualified class name,
*       in JVM format (using / instead of . in package names and $ for inner classes),
*       followed by the byte 0 as a terminator, followed by the API version as a byte.
     * 10. Member entry table. Each member entry consists of the class number (as a short),
*      followed by the JVM method/field signature, encoded as UTF-8, followed by a 0 byte
*      signature terminator, followed by the API level as a byte.
* <p>
//Synthetic comment -- @@ -290,6 +297,16 @@
mClassCount = buffer.getInt();
mMethodCount = buffer.getInt();

            int javaPackageCount = buffer.getInt();
            // Read in the Java packages
            mJavaPackages = new String[javaPackageCount];
            for (int i = 0; i < javaPackageCount; i++) {
                int count = UnsignedBytes.toInt(buffer.get());
                byte[] bytes = new byte[count];
                buffer.get(bytes, 0, count);
                mJavaPackages[i] = new String(bytes, Charsets.UTF_8);
            }

// Read in the class table indices;
int count = mClassCount + mMethodCount;
int[] offsets = new int[count];
//Synthetic comment -- @@ -345,10 +362,17 @@
Map<ApiClass, List<String>> memberMap =
Maps.newHashMapWithExpectedSize(classMap.size());
int memberCount = 0;
        Set<String> javaPackageSet = Sets.newHashSetWithExpectedSize(70);
for (Map.Entry<String, ApiClass> entry : classMap.entrySet()) {
String className = entry.getKey();
ApiClass apiClass = entry.getValue();

            if (className.startsWith("java/")               //$NON-NLS-1$
                    || className.startsWith("javax/")) {    //$NON-NLS-1$
                String pkg = apiClass.getPackage();
                javaPackageSet.add(pkg);
            }

Set<String> allMethods = apiClass.getAllMethods(info);
Set<String> allFields = apiClass.getAllFields(info);

//Synthetic comment -- @@ -399,6 +423,10 @@
}
Collections.sort(classes);

        List<String> javaPackages = Lists.newArrayList(javaPackageSet);
        Collections.sort(javaPackages);
        int javaPackageCount = javaPackages.size();

int entryCount = classMap.size() + memberCount;
int capacity = entryCount * BYTES_PER_ENTRY;
ByteBuffer buffer = ByteBuffer.allocate(capacity);
//Synthetic comment -- @@ -413,14 +441,26 @@
//      version, it can ignore it (and regenerate the cache from XML).
buffer.put((byte) BINARY_FORMAT_VERSION);

//  3. The number of classes [1 int]
buffer.putInt(classes.size());

//  4. The number of members (across all classes) [1 int].
buffer.putInt(memberCount);

        //  5. The number of Java packages [1 int].
        buffer.putInt(javaPackageCount);

        //  6. The Java package table. There are javaPackage.size() entries, where each entry
        //     consists of a string length, as a byte, followed by the bytes in the package.
        //     There is no terminating 0.
        for (String pkg : javaPackages) {
            byte[] bytes = pkg.getBytes(Charsets.UTF_8);
            assert bytes.length < 255 : pkg;
            buffer.put((byte) bytes.length);
            buffer.put(bytes);
        }

        //  7. Class offset table (one integer per class, pointing to the byte offset in the
//       file (relative to the beginning of the file) where each class begins.
//       The classes are always sorted alphabetically by fully qualified name.
int classOffsetTable = buffer.position();
//Synthetic comment -- @@ -431,7 +471,7 @@
buffer.putInt(0);
}

        //  8. Member offset table (one integer per member, pointing to the byte offset in the
//       file (relative to the beginning of the file) where each member entry begins.
//       The members are always sorted alphabetically.
int methodOffsetTable = buffer.position();
//Synthetic comment -- @@ -442,7 +482,7 @@
int nextEntry = buffer.position();
int nextOffset = classOffsetTable;

        // 9. Class entry table. Each class entry consists of the fully qualified class name,
//      in JVM format (using / instead of . in package names and $ for inner classes),
//      followed by the byte 0 as a terminator, followed by the API version as a byte.
for (String clz : classes) {
//Synthetic comment -- @@ -462,7 +502,7 @@
nextEntry = buffer.position();
}

        //  10. Member entry table. Each member entry consists of the class number (as a short),
//       followed by the JVM method/field signature, encoded as UTF-8, followed by a 0 byte
//       signature terminator, followed by the API level as a byte.
assert nextOffset == methodOffsetTable;
//Synthetic comment -- @@ -715,6 +755,70 @@
return -1;
}

    /**
     * Returns true if the given owner (in VM format) is a valid Java package supported
     * in any version of Android.
     *
     * @param owner the package, in VM format
     * @return true if the package is included in one or more versions of Android
     */
    public boolean isValidJavaPackage(@NonNull String owner) {
        int packageLength = owner.lastIndexOf('/');
        if (packageLength == -1) {
            return false;
        }

        // The index array contains class indexes from 0 to classCount and
        //   member indices from classCount to mIndices.length.
        int low = 0;
        int high = mJavaPackages.length - 1;
        while (low <= high) {
            int middle = (low + high) >>> 1;
            int offset = middle;

            if (DEBUG_SEARCH) {
                System.out.println("Comparing string " + owner + " with entry at " + offset
                        + ": " + mJavaPackages[offset]);
            }

            // Compare the api info at the given index.
            int compare = comparePackage(mJavaPackages[offset], owner, packageLength);
            if (compare == 0) {
                return true;
            }

            if (compare < 0) {
                low = middle + 1;
            } else if (compare > 0) {
                high = middle - 1;
            } else {
                assert false; // compare == 0 already handled above
                return false;
            }
        }

        return false;
    }

    private int comparePackage(String s1, String s2, int max) {
        for (int i = 0; i < max; i++) {
            if (i == s1.length()) {
                return -1;
            }
            char c1 = s1.charAt(i);
            char c2 = s2.charAt(i);
            if (c1 != c2) {
                return c1 - c2;
            }
        }

        if (s1.length() > max) {
            return 1;
        }

        return 0;
    }

/** Returns the class number of the given class, or -1 if it is unknown */
private int findClass(@NonNull String owner) {
assert owner.indexOf('.') == -1 : "Should use / instead of . in owner: " + owner;
//Synthetic comment -- @@ -723,17 +827,17 @@
//   member indices from classCount to mIndices.length.
int low = 0;
int high = mClassCount - 1;
        // Compare the api info at the given index.
        int classNameLength = owner.length();
while (low <= high) {
int middle = (low + high) >>> 1;
int offset = mIndices[middle];

if (DEBUG_SEARCH) {
                System.out.println("Comparing string " + owner + " with entry at " + offset
+ ": " + dumpEntry(offset));
}

int compare = compare(mData, offset, (byte) 0, owner, classNameLength);
if (compare == 0) {
return middle;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 52c89d4..7e7916e 100644

//Synthetic comment -- @@ -55,7 +55,7 @@
private static final List<Issue> sIssues;

static {
        final int initialCapacity = 132;
List<Issue> issues = new ArrayList<Issue>(initialCapacity);

issues.add(AccessibilityDetector.ISSUE);
//Synthetic comment -- @@ -64,6 +64,7 @@
issues.add(FieldGetterDetector.ISSUE);
issues.add(SdCardDetector.ISSUE);
issues.add(ApiDetector.UNSUPPORTED);
        issues.add(InvalidPackageDetector.ISSUE);
issues.add(DuplicateIdDetector.CROSS_LAYOUT);
issues.add(DuplicateIdDetector.WITHIN_LAYOUT);
issues.add(DuplicateResourceDetector.ISSUE);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/InvalidPackageDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/InvalidPackageDetector.java
new file mode 100644
//Synthetic comment -- index 0000000..460995d

//Synthetic comment -- @@ -0,0 +1,279 @@
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

package com.android.tools.lint.checks;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;
import com.google.common.collect.Sets;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import lombok.ast.libs.org.parboiled.google.collect.Lists;

/**
 * Looks for usages of Java packages that are not included in Android.
 */
public class InvalidPackageDetector extends Detector implements Detector.ClassScanner {
    /** Accessing an invalid package */
    public static final Issue ISSUE = Issue.create("InvalidPackage", //$NON-NLS-1$
            "Finds API accesses to APIs that are not supported in Android",

            "This check scans through libraries looking for calls to APIs that are not included " +
            "in Android.\n" +
            "\n" +
            "When you create Android projects, the classpath is set up such that you can only " +
            "access classes in the API packages that are included in Android. However, if you " +
            "add other projects to your libs/ folder, there is no guarantee that those .jar " +
            "files were built with an Android specific classpath, and in particular, they " +
            "could be accessing unsupported APIs such as java.applet.\n" +
            "\n" +
            "This check scans through library jars and looks for references to API packages " +
            "that are not included in Android and flags these. This is only an error if your " +
            "code calls one of the library classes which wind up referencing the unsupported " +
            "package.",

            Category.CORRECTNESS,
            6,
            Severity.ERROR,
            InvalidPackageDetector.class,
            EnumSet.of(Scope.JAVA_LIBRARIES));

    private static final String JAVA_PKG_PREFIX = "java/";    //$NON-NLS-1$
    private static final String JAVAX_PKG_PREFIX = "javax/";  //$NON-NLS-1$

    private ApiLookup mApiDatabase;

    /**
     * List of candidates that are potential package violations. These are
     * recorded as candidates rather than flagged immediately such that we can
     * filter out hits for classes that are also defined as libraries (possibly
     * encountered later in the library traversal).
     */
    private List<Candidate> mCandidates;
    /**
     * Set of Java packages defined in the libraries; this means that if the
     * user has added libraries in this package namespace (such as the
     * null annotations jars) we don't flag these.
     */
    private Set<String> mJavaxLibraryClasses = Sets.newHashSetWithExpectedSize(64);

    /** Constructs a new package check */
    public InvalidPackageDetector() {
    }

    @Override
    public @NonNull Speed getSpeed() {
        return Speed.SLOW;
    }

    @Override
    public void beforeCheckProject(@NonNull Context context) {
        mApiDatabase = ApiLookup.get(context.getClient());
    }

    // ---- Implements ClassScanner ----

    @SuppressWarnings("rawtypes") // ASM API
    @Override
    public void checkClass(final @NonNull ClassContext context, @NonNull ClassNode classNode) {
        if (!context.isFromClassLibrary() || shouldSkip(context.file)) {
            return;
        }

        if (mApiDatabase == null) {
            return;
        }

        if (classNode.name.startsWith(JAVAX_PKG_PREFIX)) {
            mJavaxLibraryClasses.add(classNode.name);
        }

        List methodList = classNode.methods;
        for (Object m : methodList) {
            MethodNode method = (MethodNode) m;

            InsnList nodes = method.instructions;

            // Check return type
            // The parameter types are already handled as local variables so we can skip
            // right to the return type.
            // Check types in parameter list
            String signature = method.desc;
            if (signature != null) {
                int args = signature.indexOf(')');
                if (args != -1 && signature.charAt(args + 1) == 'L') {
                    String type = signature.substring(args + 2, signature.length() - 1);
                    if (isInvalidPackage(type)) {
                        AbstractInsnNode first = nodes.size() > 0 ? nodes.get(0) : null;
                        record(context, method, first, type);
                    }
                }
            }

            for (int i = 0, n = nodes.size(); i < n; i++) {
                AbstractInsnNode instruction = nodes.get(i);
                int type = instruction.getType();
                if (type == AbstractInsnNode.METHOD_INSN) {
                    MethodInsnNode node = (MethodInsnNode) instruction;
                    String owner = node.owner;

                    // No need to check methods in this local class; we know they
                    // won't be an API match
                    if (node.getOpcode() == Opcodes.INVOKEVIRTUAL
                            && owner.equals(classNode.name)) {
                        owner = classNode.superName;
                    }

                    while (owner != null) {
                        if (isInvalidPackage(owner)) {
                            record(context, method, instruction, owner);
                        }

                        // For virtual dispatch, walk up the inheritance chain checking
                        // each inherited method
                        if (owner.startsWith("android/")           //$NON-NLS-1$
                                || owner.startsWith(JAVA_PKG_PREFIX)
                                || owner.startsWith(JAVAX_PKG_PREFIX)) {
                            owner = null;
                        } else if (node.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                            owner = context.getDriver().getSuperClass(owner);
                        } else if (node.getOpcode() == Opcodes.INVOKESTATIC) {
                            // Inherit through static classes as well
                            owner = context.getDriver().getSuperClass(owner);
                        } else {
                            owner = null;
                        }
                    }
                } else if (type == AbstractInsnNode.FIELD_INSN) {
                    FieldInsnNode node = (FieldInsnNode) instruction;
                    String owner = node.owner;
                    if (isInvalidPackage(owner)) {
                        record(context, method, instruction, owner);
                    }
                } else if (type == AbstractInsnNode.LDC_INSN) {
                    LdcInsnNode node = (LdcInsnNode) instruction;
                    if (node.cst instanceof Type) {
                        Type t = (Type) node.cst;
                        String className = t.getInternalName();
                        if (isInvalidPackage(className)) {
                            record(context, method, instruction, className);
                        }
                    }
                }
            }
        }
    }

    private boolean isInvalidPackage(String owner) {
        if (owner.startsWith(JAVA_PKG_PREFIX)
                || owner.startsWith(JAVAX_PKG_PREFIX)) {
            return !mApiDatabase.isValidJavaPackage(owner);
        }

        return false;
    }

    private void record(ClassContext context, MethodNode method,
            AbstractInsnNode instruction, String owner) {
        if (owner.indexOf('$') != -1) {
            // Don't report inner classes too; there will pretty much always be an outer class
            // reference as well
            return;
        }

        if (mCandidates == null) {
            mCandidates = Lists.newArrayList();
        }
        mCandidates.add(new Candidate(owner, context.getClassNode().name, context.getJarFile()));
    }

    @Override
    public void afterCheckProject(@NonNull Context context) {
        if (mCandidates == null) {
            return;
        }

        for (Candidate candidate : mCandidates) {
            String type = candidate.mClass;
            if (mJavaxLibraryClasses.contains(type)) {
                continue;
            }
            File jarFile = candidate.mJarFile;
            String referencedIn = candidate.mReferencedIn;

            Location location = Location.create(jarFile);
            Object pkg = getPackageName(type);
            String message = String.format(
                    "Invalid package reference in library; not included in Android: %1$s. " +
                    "Referenced from %2$s.", pkg, ClassContext.getFqcn(referencedIn));
            context.report(ISSUE, location, message, null);
        }
    }

    private Object getPackageName(String owner) {
        String pkg = owner;
        int index = pkg.lastIndexOf('/');
        if (index != -1) {
            pkg = pkg.substring(0, index);
        }

        return ClassContext.getFqcn(pkg);
    }

    private boolean shouldSkip(File file) {
        // No need to do work on this library, which is included in pretty much all new ADT
        // projects
        if (file.getPath().endsWith("android-support-v4.jar")) { //$NON-NLS-1$
            return true;
        }

        return false;
    }

    private static class Candidate {
        private final String mReferencedIn;
        private final File mJarFile;
        private final String mClass;

        public Candidate(String className, String referencedIn, File jarFile) {
            mClass = className;
            mReferencedIn = referencedIn;
            mJarFile = jarFile;
        }
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/ApiLookupTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/ApiLookupTest.java
//Synthetic comment -- index 2e55533..252ec5e 100644

//Synthetic comment -- @@ -87,6 +87,16 @@
"(Landroid/preference/PreferenceFragment;Landroid/preference/Preference;)"));
}

    public void testIsValidPackage() {
        assertTrue(mDb.isValidJavaPackage("java/lang/Integer"));
        assertTrue(mDb.isValidJavaPackage("javax/crypto/Cipher"));
        assertTrue(mDb.isValidJavaPackage("java/awt/font/NumericShaper"));

        assertFalse(mDb.isValidJavaPackage("javax/swing/JButton"));
        assertFalse(mDb.isValidJavaPackage("java/rmi/Naming"));
        assertFalse(mDb.isValidJavaPackage("java/lang/instrument/Instrumentation"));
    }

@Override
protected Detector getDetector() {
fail("This is not used in the ApiDatabase test");
//Synthetic comment -- @@ -129,6 +139,9 @@
raf.close();
lookup = ApiLookup.get(new LookupTestClient());
String message = mLogBuffer.toString();
        // NOTE: This test is incompatible with the DEBUG_FORCE_REGENERATE_BINARY and WRITE_STATS
        // flags in the ApiLookup class, so if the test fails during development and those are
        // set, clear them.
assertTrue(message.contains("Please delete the file and restart the IDE/lint:"));
assertTrue(message.contains(mCacheDir.getPath()));
ApiLookup.dispose();








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/InvalidPackageDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/InvalidPackageDetectorTest.java
new file mode 100644
//Synthetic comment -- index 0000000..b634255

//Synthetic comment -- @@ -0,0 +1,56 @@
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

package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Detector;

@SuppressWarnings("javadoc")
public class InvalidPackageDetectorTest extends AbstractCheckTest {
    @Override
    protected Detector getDetector() {
        return new InvalidPackageDetector();
    }

    public void testUnsupportedJavaLibraryCode() throws Exception {
        // See http://code.google.com/p/android/issues/detail?id=39109
        assertEquals(
            "libs/unsupported.jar: Error: Invalid package reference in library; not included in Android: java.awt. Referenced from test.pkg.LibraryClass. [InvalidPackage]\n" +
            "libs/unsupported.jar: Error: Invalid package reference in library; not included in Android: javax.swing. Referenced from test.pkg.LibraryClass. [InvalidPackage]\n" +
            "2 errors, 0 warnings\n",

            lintProject(
                    "apicheck/minsdk14.xml=>AndroidManifest.xml",
                    "apicheck/layout.xml=>res/layout/layout.xml",
                    "apicheck/themes.xml=>res/values/themes.xml",
                    "apicheck/themes.xml=>res/color/colors.xml",
                    "apicheck/unsupported.jar.data=>libs/unsupported.jar"
                ));
    }

    public void testOk() throws Exception {
        assertEquals(
            "No warnings.",

            lintProject(
                "apicheck/classpath=>.classpath",
                "apicheck/minsdk2.xml=>AndroidManifest.xml",
                "apicheck/ApiCallTest.class.data=>bin/classes/foo/bar/ApiCallTest.class",
                "bytecode/GetterTest.jar.data=>libs/GetterTest.jar",
                "bytecode/classes.jar=>libs/classes.jar"
            ));
    }
}







