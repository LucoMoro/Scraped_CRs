/*Add bytecode method call dispatch for lint rules

This changeset adds shared dispatch for bytecode based rules. We
already have this for XML and Java source trees: detectors can
register interest in a particular element or attribute name, or a
particular method call name, and then the source is processed in a
single pass, and a quick map lookup dispatches to any detectors
interested in that particular item.

This has the important property that adding new detectors does not
linearly slow down lint: it does not process each XML document an
extra time for each newly added rule: the new rule just adds one more
entry into a dispatch table, and only for the specifically interesting
tag name (for example) is there an extra dispatch to the new detector.

This changeset adds a similar mechanism for the ASM based bytecode
detectors. Until now, each detector would be handed the class node of
the outer class. This changeset adds a couple of new dispatch methods:
First, a way to register interests in method calls of a particular
type, and second, a way to registere interest in types of ASM nodes.

In addition to the new visitor, this changeset rewrites a couple of
detectors to use the new dispatch approach (there are new detectors in
the pipeline which will also take advantage of this), and cleans up
handling of positions a bit.

Change-Id:Ib115bd3418b6c63bdcd49fec046e91a73daf38a9*/
//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/AsmVisitor.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/AsmVisitor.java
new file mode 100644
//Synthetic comment -- index 0000000..f2c29bf

//Synthetic comment -- @@ -0,0 +1,205 @@








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index 3e4e922..b4e4a7d 100644

//Synthetic comment -- @@ -1023,6 +1023,8 @@
if (mScope.contains(scope)) {
List<Detector> classDetectors = mScopeDetectors.get(scope);
if (classDetectors != null && classDetectors.size() > 0 && entries.size() > 0) {
mOuterClasses = new ArrayDeque<ClassNode>();
for (ClassEntry entry : entries) {
ClassReader reader;
//Synthetic comment -- @@ -1055,7 +1057,11 @@
ClassContext context = new ClassContext(this, project, main,
entry.file, entry.jarFile, entry.binDir, entry.bytes,
classNode, scope == Scope.JAVA_LIBRARIES /*fromLibrary*/);
                    runClassDetectors(context, classDetectors);

if (mCanceled) {
return;
//Synthetic comment -- @@ -1199,29 +1205,6 @@
}
}

    private void runClassDetectors(ClassContext context, @NonNull List<Detector> checks) {
        try {
            File file = context.file;
            ClassNode classNode = context.getClassNode();
            for (Detector detector : checks) {
                if (detector.appliesTo(context, file)) {
                    fireEvent(EventType.SCANNING_FILE, context);
                    detector.beforeCheckFile(context);

                    Detector.ClassScanner scanner = (Detector.ClassScanner) detector;
                    scanner.checkClass(context, classNode);
                    detector.afterCheckFile(context);
                }

                if (mCanceled) {
                    return;
                }
            }
        } catch (Exception e) {
            mClient.log(e, null);
        }
    }

private void addClassFiles(@NonNull File dir, @NonNull List<File> classFiles) {
// Process the resource folder
File[] files = dir.listFiles();








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/XmlVisitor.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/XmlVisitor.java
//Synthetic comment -- index c76d44d..816c028 100644

//Synthetic comment -- @@ -118,7 +118,7 @@
}
}

    void visitFile(@NonNull XmlContext context,@NonNull  File file) {
assert LintUtils.isXmlFile(file);
context.parser = mParser;









//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/ClassContext.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/ClassContext.java
//Synthetic comment -- index aa558ec..4aa21d4 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import static com.android.tools.lint.detector.api.LintConstants.DOT_JAVA;
import static com.android.tools.lint.detector.api.Location.SearchDirection.BACKWARD;
import static com.android.tools.lint.detector.api.Location.SearchDirection.EOL_BACKWARD;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -33,6 +34,7 @@
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
//Synthetic comment -- @@ -494,6 +496,38 @@
SearchHints.create(EOL_BACKWARD).matchJavaSymbol());
}

private static boolean isAnonymousClass(@NonNull String fqcn) {
int lastIndex = fqcn.lastIndexOf('$');
if (lastIndex != -1 && lastIndex < fqcn.length() - 1) {








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Detector.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Detector.java
//Synthetic comment -- index c8f9fd2..e2c5907 100644

//Synthetic comment -- @@ -21,7 +21,10 @@
import com.android.tools.lint.client.api.LintDriver;
import com.google.common.annotations.Beta;

import org.objectweb.asm.tree.ClassNode;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
//Synthetic comment -- @@ -203,6 +206,98 @@
* @param classNode the root class node
*/
void checkClass(@NonNull ClassContext context, @NonNull ClassNode classNode);
}

/** Specialized interface for detectors that scan XML files */
//Synthetic comment -- @@ -454,4 +549,33 @@
@SuppressWarnings("javadoc")
public void checkClass(@NonNull ClassContext context, @NonNull ClassNode classNode) {
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LintConstants.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LintConstants.java
//Synthetic comment -- index b920b5e..47a52c0 100644

//Synthetic comment -- @@ -332,4 +332,10 @@
public static final String CONSTRUCTOR_NAME = "<init>";                          //$NON-NLS-1$
public static final String FRAGMENT = "android/app/Fragment";                    //$NON-NLS-1$
public static final String FRAGMENT_V4 = "android/support/v4/app/Fragment";      //$NON-NLS-1$
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LintUtils.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LintUtils.java
//Synthetic comment -- index 0969228..9bfb7cc 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import com.google.common.io.Files;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.w3c.dom.Element;
//Synthetic comment -- @@ -522,6 +523,87 @@
}

/**
* Returns true if the given directory represents an Android project
* directory. Note: This doesn't necessarily mean it's an Eclipse directory,
* only that it looks like it contains a logical Android project -- one








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java
//Synthetic comment -- index 9fef44e..7f24b8e 100644

//Synthetic comment -- @@ -58,7 +58,6 @@
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
//Synthetic comment -- @@ -101,11 +100,6 @@
}

@Override
    public boolean appliesTo(@NonNull Context context, @NonNull File file) {
        return true;
    }

    @Override
public @NonNull Speed getSpeed() {
return Speed.SLOW;
}
//Synthetic comment -- @@ -493,8 +487,15 @@
// method name (<init>) but the class name
if (patternStart != null && patternStart.equals(CONSTRUCTOR_NAME)
&& node instanceof MethodInsnNode) {
            String owner = ((MethodInsnNode) node).owner;
            patternStart = owner.substring(owner.lastIndexOf('/') + 1);
}

Location location = context.getLocationForLine(lineNumber, patternStart, patternEnd, hints);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/FieldGetterDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/FieldGetterDetector.java
//Synthetic comment -- index f126074..b73b9ae 100644

//Synthetic comment -- @@ -16,8 +16,6 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.Location.SearchDirection.FORWARD;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
//Synthetic comment -- @@ -25,7 +23,6 @@
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Location.SearchHints;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;
//Synthetic comment -- @@ -36,11 +33,9 @@
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
//Synthetic comment -- @@ -74,95 +69,78 @@
// This is a micro-optimization: not enabled by default
setEnabledByDefault(false).setMoreInfo(
"http://developer.android.com/guide/practices/design/performance.html#internal_get_set"); //$NON-NLS-1$

/** Constructs a new {@link FieldGetterDetector} check */
public FieldGetterDetector() {
}

@Override
    public boolean appliesTo(@NonNull Context context, @NonNull File file) {
        return true;
    }

    @Override
public @NonNull Speed getSpeed() {
return Speed.FAST;
}

// ---- Implements ClassScanner ----

    @SuppressWarnings("rawtypes")
@Override
    public void checkClass(@NonNull ClassContext context, @NonNull ClassNode classNode) {
// As of Gingerbread/API 9, Dalvik performs this optimization automatically
if (context.getProject().getMinSdk() >= 9) {
return;
}

        List<Entry> pendingCalls = null;
        int currentLine = 0;
        List methodList = classNode.methods;
        for (Object m : methodList) {
            MethodNode method = (MethodNode) m;
            InsnList nodes = method.instructions;
            for (int i = 0, n = nodes.size(); i < n; i++) {
                AbstractInsnNode instruction = nodes.get(i);
                int type = instruction.getType();
                if (type == AbstractInsnNode.LINE) {
                    currentLine = ((LineNumberNode) instruction).line;
                } else if (type == AbstractInsnNode.METHOD_INSN) {
                    MethodInsnNode node = (MethodInsnNode) instruction;
                    String name = node.name;
                    String owner = node.owner;

                    if (((name.startsWith("get") && name.length() > 3     //$NON-NLS-1$
                            && Character.isUpperCase(name.charAt(3)))
                        || (name.startsWith("is") && name.length() > 2    //$NON-NLS-1$
                            && Character.isUpperCase(name.charAt(2))))
                            && owner.equals(classNode.name)) {
                        // Calling a potential getter method on self. We now need to
                        // investigate the method body of the getter call and make sure
                        // it's really a plain getter, not just a method which happens
                        // to have a method name like a getter, or a method which not
                        // only returns a field but possibly computes it or performs
                        // other initialization or side effects. This is done in a
                        // second pass over the bytecode, initiated by the finish()
                        // method.
                        if (pendingCalls == null) {
                            pendingCalls = new ArrayList<Entry>();
                        }

                        pendingCalls.add(new Entry(name, currentLine, method));
                    }
                }
}
}

        if (pendingCalls != null) {
            Set<String> names = new HashSet<String>(pendingCalls.size());
            for (Entry entry : pendingCalls) {
names.add(entry.name);
}

Map<String, String> getters = checkMethods(context.getClassNode(), names);
if (getters.size() > 0) {
                File source = context.getSourceFile();
                String contents = context.getSourceContents();
for (String getter : getters.keySet()) {
                    for (Entry entry : pendingCalls) {
String name = entry.name;
// There can be more than one reference to the same name:
// one for each call site
if (name.equals(getter)) {
                            int line = entry.lineNumber;
                            Location location = null;
                            if (source != null) {
                                // ASM line numbers are 1-based, Lint needs 0-based
                                location = Location.create(source, contents, line - 1, name,
                                        null, SearchHints.create(FORWARD).matchJavaSymbol());
                            } else {
                                location = Location.create(context.file);
                            }
String fieldName = getters.get(getter);
if (fieldName == null) {
fieldName = "";
//Synthetic comment -- @@ -180,13 +158,13 @@
// Holder class for getters to be checked
private static class Entry {
public final String name;
        public final int lineNumber;
public final MethodNode method;

        public Entry(String name, int lineNumber, MethodNode method) {
super();
this.name = name;
            this.lineNumber = lineNumber;
this.method = method;
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/FragmentDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/FragmentDetector.java
//Synthetic comment -- index 6839004..2fc3034 100644

//Synthetic comment -- @@ -24,7 +24,6 @@
import com.android.tools.lint.client.api.LintDriver;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Detector.ClassScanner;
import com.android.tools.lint.detector.api.Issue;
//Synthetic comment -- @@ -37,7 +36,6 @@
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.util.List;

/**
//Synthetic comment -- @@ -82,11 +80,6 @@
return Speed.FAST;
}

    @Override
    public boolean appliesTo(@NonNull Context context, @NonNull File file) {
        return true;
    }

// ---- Implements ClassScanner ----

@Override








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/HandlerDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/HandlerDetector.java
//Synthetic comment -- index 9efd666..7d5a5e5 100644

//Synthetic comment -- @@ -19,7 +19,6 @@
import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Detector.ClassScanner;
import com.android.tools.lint.detector.api.Issue;
//Synthetic comment -- @@ -31,8 +30,6 @@

import org.objectweb.asm.tree.ClassNode;

import java.io.File;

/**
* Checks that Handler implementations are top level classes or static.
* See the corresponding check in the android.os.Handler source code.
//Synthetic comment -- @@ -65,11 +62,6 @@
return Speed.FAST;
}

    @Override
    public boolean appliesTo(@NonNull Context context, @NonNull File file) {
        return true;
    }

// ---- Implements ClassScanner ----

@Override








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/MathDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/MathDetector.java
//Synthetic comment -- index f93a788..7d6424b 100644

//Synthetic comment -- @@ -16,31 +16,26 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.Location.SearchDirection.FORWARD;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Location.SearchHints;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* Looks for usages of {@link java.lang.Math} methods which can be replaced with
//Synthetic comment -- @@ -72,93 +67,48 @@
}

@Override
    public boolean appliesTo(@NonNull Context context, @NonNull File file) {
        return true;
    }

    @Override
public @NonNull Speed getSpeed() {
return Speed.FAST;
}

// ---- Implements ClassScanner ----

    @SuppressWarnings("rawtypes")
@Override
    public void checkClass(@NonNull ClassContext context, @NonNull ClassNode classNode) {
        List methodList = classNode.methods;
        for (Object m : methodList) {
            MethodNode method = (MethodNode) m;
            InsnList nodes = method.instructions;
            for (int i = 0, n = nodes.size(); i < n; i++) {
                AbstractInsnNode instruction = nodes.get(i);
                int type = instruction.getType();
                if (type == AbstractInsnNode.METHOD_INSN) {
                    MethodInsnNode node = (MethodInsnNode) instruction;
                    String name = node.name;
                    String owner = node.owner;

                    if (sFloatMethods.contains(name)
                            && owner.equals("java/lang/Math")) { //$NON-NLS-1$
                        boolean paramFromFloat = getPrevOpcode(nodes, i) == Opcodes.F2D;
                        boolean returnToFloat = getNextOpcode(nodes, i) == Opcodes.D2F;
                        if (paramFromFloat || returnToFloat) {
                            String message;
                            if (paramFromFloat) {
                                message = String.format(
                                        "Use android.util.FloatMath#%1$s() instead of " +
                                        "java.lang.Math#%1$s to avoid argument float to " +
                                        "double conversion", name);
                            } else {
                                message = String.format(
                                        "Use android.util.FloatMath#%1$s() instead of " +
                                        "java.lang.Math#%1$s to avoid double to float return " +
                                        "value conversion", name);
                            }
                            int lineNumber = ClassContext.findLineNumber(instruction);
                            Location location = context.getLocationForLine(lineNumber, name, null,
                                    SearchHints.create(FORWARD).matchJavaSymbol());
                            context.report(ISSUE, method, location, message, null /*data*/);
                        }
                    }
}
}
}
}

    private int getPrevOpcode(InsnList nodes, int i) {
        for (i--; i >= 0; i--) {
            AbstractInsnNode node = nodes.get(i);
            int type = node.getType();
            if (type == AbstractInsnNode.LINE || type == AbstractInsnNode.LABEL) {
                continue;
            }
            return node.getOpcode();
        }

        return Opcodes.NOP;
    }

    private int getNextOpcode(InsnList nodes, int i) {
        for (i++; i < nodes.size(); i++) {
            AbstractInsnNode node = nodes.get(i);
            int type = node.getType();
            if (type == AbstractInsnNode.LINE || type == AbstractInsnNode.LABEL) {
                continue;
            }
            return node.getOpcode();
        }

        return Opcodes.NOP;
    }

    /** Methods on java.lang.Math that we want to find and suggest replacements for */
    private static final Set<String> sFloatMethods = new HashSet<String>();
    static {
        sFloatMethods.add("sin");   //$NON-NLS-1$
        sFloatMethods.add("cos");   //$NON-NLS-1$
        sFloatMethods.add("ceil");  //$NON-NLS-1$
        sFloatMethods.add("sqrt");  //$NON-NLS-1$
        sFloatMethods.add("floor"); //$NON-NLS-1$
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/OnClickDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/OnClickDetector.java
//Synthetic comment -- index 0b819cc..bb9c998 100644

//Synthetic comment -- @@ -41,7 +41,6 @@
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
//Synthetic comment -- @@ -87,11 +86,6 @@
}

@Override
    public boolean appliesTo(@NonNull Context context, @NonNull File file) {
        return true;
    }

    @Override
public void afterCheckProject(@NonNull Context context) {
if (mNames != null && mNames.size() > 0 && mHaveBytecode) {
List<String> names = new ArrayList<String>(mNames.keySet());








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/RegistrationDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/RegistrationDetector.java
//Synthetic comment -- index 81425db..2455365 100644

//Synthetic comment -- @@ -16,6 +16,10 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_NAME;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_PACKAGE;
//Synthetic comment -- @@ -27,7 +31,6 @@
import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector.ClassScanner;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
//Synthetic comment -- @@ -43,7 +46,6 @@
import org.objectweb.asm.tree.ClassNode;
import org.w3c.dom.Element;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
//Synthetic comment -- @@ -54,13 +56,6 @@
* and also makes sure that they are registered with the correct tag
*/
public class RegistrationDetector extends LayoutDetector implements ClassScanner {
    private static final String ANDROID_APP_ACTIVITY = "android/app/Activity";  //$NON-NLS-1$
    private static final String ANDROID_APP_SERVICE = "android/app/Service";    //$NON-NLS-1$
    private static final String ANDROID_CONTENT_CONTENT_PROVIDER =
            "android/content/ContentProvider";   //$NON-NLS-1$
    private static final String ANDROID_CONTENT_BROADCAST_RECEIVER =
            "android/content/BroadcastReceiver"; //$NON-NLS-1$

/** Unregistered activities and services */
public static final Issue ISSUE = Issue.create(
"Registered", //$NON-NLS-1$
//Synthetic comment -- @@ -90,11 +85,6 @@
return Speed.FAST;
}

    @Override
    public boolean appliesTo(@NonNull Context context, @NonNull File file) {
        return true;
    }

// ---- Implements XmlScanner ----

@Override








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ViewConstructorDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ViewConstructorDetector.java
//Synthetic comment -- index 538c0d6..68062c5 100644

//Synthetic comment -- @@ -21,7 +21,6 @@
import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
//Synthetic comment -- @@ -74,11 +73,6 @@
}

@Override
    public boolean appliesTo(@NonNull Context context, @NonNull File file) {
        return true;
    }

    @Override
public @NonNull Speed getSpeed() {
return Speed.FAST;
}







