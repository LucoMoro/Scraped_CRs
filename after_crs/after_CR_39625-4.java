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
//Synthetic comment -- index 0000000..81e2934

//Synthetic comment -- @@ -0,0 +1,205 @@
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

package com.android.tools.lint.client.api;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Detector.ClassScanner;
import com.google.common.annotations.Beta;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Specialized visitor for running detectors on a class object model.
 * <p>
 * It operates in two phases:
 * <ol>
 *   <li> First, it computes a set of maps where it generates a map from each
 *        significant method name to a list of detectors to consult for that method
 *        name. The set of method names that a detector is interested in is provided
 *        by the detectors themselves.
 *   <li> Second, it iterates over the DOM a single time. For each method call it finds,
 *        it dispatches to any check that has registered interest in that method name.
 *   <li> Finally, it runs a full check on those class scanners that do not register
 *        specific method names to be checked. This is intended for those detectors
 *        that do custom work, not related specifically to method calls.
 * </ol>
 * It also notifies all the detectors before and after the document is processed
 * such that they can do pre- and post-processing.
 * <p>
 * <b>NOTE: This is not a public or final API; if you rely on this be prepared
 * to adjust your code for the next tools release.</b>
 */
@Beta
class AsmVisitor {
    /**
     * Number of distinct node types specified in {@link AbstractInsnNode}. Sadly
     * there isn't a max-constant there, so update this along with ASM library
     * updates.
     */
    public final static int TYPE_COUNT = AbstractInsnNode.LINE + 1;
    private final Map<String, List<ClassScanner>> mMethodNameToChecks =
            new HashMap<String, List<ClassScanner>>();
    private final Map<String, List<ClassScanner>> mMethodOwnerToChecks =
            new HashMap<String, List<ClassScanner>>();
    private final List<Detector> mFullClassChecks = new ArrayList<Detector>();

    private final LintClient mClient;
    private final List<? extends Detector> mAllDetectors;
    private List<ClassScanner>[] mNodeTypeDetectors;

    // Really want this:
    //<T extends List<Detector> & Detector.ClassScanner> ClassVisitor(T xmlDetectors) {
    // but it makes client code tricky and ugly.
    @SuppressWarnings("unchecked")
    AsmVisitor(@NonNull LintClient client, @NonNull List<? extends Detector> classDetectors) {
        mClient = client;
        mAllDetectors = classDetectors;

        // TODO: Check appliesTo() for files, and find a quick way to enable/disable
        // rules when running through a full project!
        for (Detector detector : classDetectors) {
            Detector.ClassScanner scanner = (Detector.ClassScanner) detector;

            boolean checkFullClass = true;

            Collection<String> names = scanner.getApplicableCallNames();
            if (names != null) {
                checkFullClass = false;
                for (String element : names) {
                    List<Detector.ClassScanner> list = mMethodNameToChecks.get(element);
                    if (list == null) {
                        list = new ArrayList<Detector.ClassScanner>();
                        mMethodNameToChecks.put(element, list);
                    }
                    list.add(scanner);
                }
            }

            Collection<String> owners = scanner.getApplicableCallOwners();
            if (owners != null) {
                checkFullClass = false;
                for (String element : owners) {
                    List<Detector.ClassScanner> list = mMethodOwnerToChecks.get(element);
                    if (list == null) {
                        list = new ArrayList<Detector.ClassScanner>();
                        mMethodOwnerToChecks.put(element, list);
                    }
                    list.add(scanner);
                }
            }

            int[] types = scanner.getApplicableAsmNodeTypes();
            if (types != null) {
                checkFullClass = false;
                for (int i = 0, n = types.length; i < n; i++) {
                    int type = types[i];
                    if (type < 0 || type >= TYPE_COUNT) {
                        // Can't support this node type: looks like ASM wasn't updated correctly.
                        mClient.log(null, "Out of range node type %1$d from detector %2$s",
                                type, scanner);
                        continue;
                    }
                    if (mNodeTypeDetectors == null) {
                        mNodeTypeDetectors = new List[TYPE_COUNT];
                    }
                    List<ClassScanner> checks = mNodeTypeDetectors[type];
                    if (checks == null) {
                        checks = new ArrayList<ClassScanner>();
                        mNodeTypeDetectors[type] = checks;
                    }
                    checks.add(scanner);
                }
            }

            if (checkFullClass) {
                mFullClassChecks.add(detector);
            }
        }
    }

    @SuppressWarnings("rawtypes") // ASM API uses raw types
    void runClassDetectors(ClassContext context) {
        ClassNode classNode = context.getClassNode();

        for (Detector detector : mAllDetectors) {
            detector.beforeCheckFile(context);
        }

        for (Detector detector : mFullClassChecks) {
            Detector.ClassScanner scanner = (Detector.ClassScanner) detector;
            scanner.checkClass(context, classNode);
            detector.afterCheckFile(context);
        }

        if (!mMethodNameToChecks.isEmpty() || !mMethodOwnerToChecks.isEmpty() ||
                mNodeTypeDetectors != null && mNodeTypeDetectors.length > 0) {
            List methodList = classNode.methods;
            for (Object m : methodList) {
                MethodNode method = (MethodNode) m;
                InsnList nodes = method.instructions;
                for (int i = 0, n = nodes.size(); i < n; i++) {
                    AbstractInsnNode instruction = nodes.get(i);
                    int type = instruction.getType();
                    if (type == AbstractInsnNode.METHOD_INSN) {
                        MethodInsnNode call = (MethodInsnNode) instruction;

                        String owner = call.owner;
                        List<ClassScanner> scanners = mMethodOwnerToChecks.get(owner);
                        if (scanners != null) {
                            for (ClassScanner scanner : scanners) {
                                scanner.checkCall(context, classNode, method, call);
                            }
                        }

                        String name = call.name;
                        scanners = mMethodNameToChecks.get(name);
                        if (scanners != null) {
                            for (ClassScanner scanner : scanners) {
                                scanner.checkCall(context, classNode, method, call);
                            }
                        }
                    }

                    if (mNodeTypeDetectors != null && type < mNodeTypeDetectors.length) {
                        List<ClassScanner> scanners = mNodeTypeDetectors[type];
                        if (scanners != null) {
                            for (ClassScanner scanner : scanners) {
                                scanner.checkInstruction(context, classNode, method, instruction);
                            }
                        }
                    }
                }
            }
        }

        for (Detector detector : mAllDetectors) {
            detector.afterCheckFile(context);
        }
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index 3e4e922..b4e4a7d 100644

//Synthetic comment -- @@ -1023,6 +1023,8 @@
if (mScope.contains(scope)) {
List<Detector> classDetectors = mScopeDetectors.get(scope);
if (classDetectors != null && classDetectors.size() > 0 && entries.size() > 0) {
                AsmVisitor visitor = new AsmVisitor(mClient, classDetectors);

mOuterClasses = new ArrayDeque<ClassNode>();
for (ClassEntry entry : entries) {
ClassReader reader;
//Synthetic comment -- @@ -1055,7 +1057,11 @@
ClassContext context = new ClassContext(this, project, main,
entry.file, entry.jarFile, entry.binDir, entry.bytes,
classNode, scope == Scope.JAVA_LIBRARIES /*fromLibrary*/);
                    try {
                        visitor.runClassDetectors(context);
                    } catch (Exception e) {
                        mClient.log(e, null);
                    }

if (mCanceled) {
return;
//Synthetic comment -- @@ -1199,29 +1205,6 @@
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

    void visitFile(@NonNull XmlContext context, @NonNull File file) {
assert LintUtils.isXmlFile(file);
context.parser = mParser;









//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/ClassContext.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/ClassContext.java
//Synthetic comment -- index aa558ec..4aa21d4 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import static com.android.tools.lint.detector.api.LintConstants.DOT_JAVA;
import static com.android.tools.lint.detector.api.Location.SearchDirection.BACKWARD;
import static com.android.tools.lint.detector.api.Location.SearchDirection.EOL_BACKWARD;
import static com.android.tools.lint.detector.api.Location.SearchDirection.FORWARD;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -33,6 +34,7 @@
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
//Synthetic comment -- @@ -494,6 +496,38 @@
SearchHints.create(EOL_BACKWARD).matchJavaSymbol());
}

    /**
     * Returns a location for the given {@link AbstractInsnNode}.
     *
     * @param instruction the instruction to look up the location for
     * @return a location pointing to the instruction, or as close to it
     *         as possible
     */
    @NonNull
    public Location getLocation(@NonNull AbstractInsnNode instruction) {
        String pattern = null;
        if (instruction instanceof MethodInsnNode) {
            MethodInsnNode call = (MethodInsnNode) instruction;
            if (call.name.equals(CONSTRUCTOR_NAME)) {
                pattern = call.owner;
            } else {
                pattern = call.name;
            }
            int index = pattern.lastIndexOf('$');
            if (index != -1) {
                pattern = pattern.substring(index + 1);
            }
            index = pattern.lastIndexOf('/');
            if (index != -1) {
                pattern = pattern.substring(index + 1);
            }
        }

        int line = findLineNumber(instruction);
        return getLocationForLine(line, pattern, null,
                SearchHints.create(FORWARD).matchJavaSymbol());
    }

private static boolean isAnonymousClass(@NonNull String fqcn) {
int lastIndex = fqcn.lastIndexOf('$');
if (lastIndex != -1 && lastIndex < fqcn.length() - 1) {








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Detector.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Detector.java
//Synthetic comment -- index c8f9fd2..e2c5907 100644

//Synthetic comment -- @@ -21,7 +21,10 @@
import com.android.tools.lint.client.api.LintDriver;
import com.google.common.annotations.Beta;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
//Synthetic comment -- @@ -203,6 +206,98 @@
* @param classNode the root class node
*/
void checkClass(@NonNull ClassContext context, @NonNull ClassNode classNode);

        /**
         * Returns the list of node types (corresponding to the constants in the
         * {@link AbstractInsnNode} class) that this scanner applies to. The
         * {@link #checkInstruction(ClassContext, ClassNode, MethodNode, AbstractInsnNode)}
         * method will be called for each match.
         *
         * @return an array containing all the node types this detector should be
         *         called for, or null if none.
         */
        @Nullable
        int[] getApplicableAsmNodeTypes();

        /**
         * Process a given instruction node, and register lint issues if
         * applicable.
         *
         * @param context the context of the lint check, pointing to for example
         *            the file
         * @param classNode the root class node
         * @param method the method node containing the call
         * @param instruction the actual instruction
         */
        void checkInstruction(@NonNull ClassContext context, @NonNull ClassNode classNode,
                @NonNull MethodNode method, @NonNull AbstractInsnNode instruction);

        /**
         * Return the list of method call names (in VM format, e.g. "<init>" for
         * constructors, etc) for method calls this detector is interested in,
         * or null. T his will be used to dispatch calls to
         * {@link #checkCall(ClassContext, ClassNode, MethodNode, MethodInsnNode)}
         * for only the method calls in owners that the detector is interested
         * in.
         * <p>
         * <b>NOTE</b>: If you return non null from this method, then <b>only</b>
         * {@link #checkCall(ClassContext, ClassNode, MethodNode, MethodInsnNode)}
         * will be called if a suitable method is found;
         * {@link #checkClass(ClassContext, ClassNode)} will not be called under
         * any circumstances.
         * <p>
         * This makes it easy to write detectors that focus on some fixed calls,
         * and allows lint to make a single pass over the bytecode over a class,
         * and efficiently dispatch method calls to any detectors that are
         * interested in it. Without this, each new lint check interested in a
         * single method, would be doing a complete pass through all the
         * bytecode instructions of the class via the
         * {@link #checkClass(ClassContext, ClassNode)} method, which would make
         * each newly added lint check make lint slower. Now a single dispatch
         * map is used instead, and for each encountered call in the single
         * dispatch, it looks up in the map which if any detectors are
         * interested in the given call name, and dispatches to each one in
         * turn.
         *
         * @return a list of applicable method names, or null.
         */
        @Nullable
        List<String> getApplicableCallNames();

        /**
         * Just like {@link Detector#getApplicableCallNames()}, but for the owner
         * field instead. The
         * {@link #checkCall(ClassContext, ClassNode, MethodNode, MethodInsnNode)}
         * method will be called for all {@link MethodInsnNode} instances where the
         * owner field matches any of the members returned in this node.
         * <p>
         * Note that if your detector provides both a name and an owner, the
         * method will be called for any nodes matching either the name <b>or</b>
         * the owner, not only where they match <b>both</b>. Note also that it will
         * be called twice - once for the name match, and (at least once) for the owner
         * match.
         *
         * @return a list of applicable owner names, or null.
         */
        @Nullable
        List<String> getApplicableCallOwners();

        /**
         * Process a given method call node, and register lint issues if
         * applicable. This is similar to the
         * {@link #checkInstruction(ClassContext, ClassNode, MethodNode, AbstractInsnNode)}
         * method, but has the additional advantage that it is only called for known
         * method names or method owners, according to
         * {@link #getApplicableCallNames()} and {@link #getApplicableCallOwners()}.
         *
         * @param context the context of the lint check, pointing to for example
         *            the file
         * @param classNode the root class node
         * @param method the method node containing the call
         * @param call the actual method call node
         */
        void checkCall(@NonNull ClassContext context, @NonNull ClassNode classNode,
                @NonNull MethodNode method, @NonNull MethodInsnNode call);
}

/** Specialized interface for detectors that scan XML files */
//Synthetic comment -- @@ -454,4 +549,33 @@
@SuppressWarnings("javadoc")
public void checkClass(@NonNull ClassContext context, @NonNull ClassNode classNode) {
}

    @SuppressWarnings("javadoc")
    @Nullable
    public List<String> getApplicableCallNames() {
        return null;
    }

    @SuppressWarnings("javadoc")
    @Nullable
    public List<String> getApplicableCallOwners() {
        return null;
    }

    @SuppressWarnings("javadoc")
    public void checkCall(@NonNull ClassContext context, @NonNull ClassNode classNode,
            @NonNull MethodNode method, @NonNull MethodInsnNode call) {
    }

    @SuppressWarnings("javadoc")
    @Nullable
    public int[] getApplicableAsmNodeTypes() {
        return null;
    }

    @SuppressWarnings("javadoc")
    public void checkInstruction(@NonNull ClassContext context, @NonNull ClassNode classNode,
            @NonNull MethodNode method, @NonNull AbstractInsnNode instruction) {
    }

}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LintConstants.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LintConstants.java
//Synthetic comment -- index b920b5e..47a52c0 100644

//Synthetic comment -- @@ -332,4 +332,10 @@
public static final String CONSTRUCTOR_NAME = "<init>";                          //$NON-NLS-1$
public static final String FRAGMENT = "android/app/Fragment";                    //$NON-NLS-1$
public static final String FRAGMENT_V4 = "android/support/v4/app/Fragment";      //$NON-NLS-1$
    public static final String ANDROID_APP_ACTIVITY = "android/app/Activity";        //$NON-NLS-1$
    public static final String ANDROID_APP_SERVICE = "android/app/Service";          //$NON-NLS-1$
    public static final String ANDROID_CONTENT_CONTENT_PROVIDER =
            "android/content/ContentProvider";                                       //$NON-NLS-1$
    public static final String ANDROID_CONTENT_BROADCAST_RECEIVER =
            "android/content/BroadcastReceiver";                                     //$NON-NLS-1$
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LintUtils.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LintUtils.java
//Synthetic comment -- index 0969228..9bfb7cc 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import com.google.common.io.Files;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.w3c.dom.Element;
//Synthetic comment -- @@ -522,6 +523,87 @@
}

/**
     * Returns the previous opcode prior to the given node, ignoring label and
     * line number nodes
     *
     * @param node the node to look up the previous opcode for
     * @return the previous opcode, or {@link Opcodes#NOP} if no previous node
     *         was found
     */
    public static int getPrevOpcode(@NonNull AbstractInsnNode node) {
        AbstractInsnNode prev = getPrevInstruction(node);
        if (prev != null) {
            return prev.getOpcode();
        } else {
            return Opcodes.NOP;
        }
    }

    /**
     * Returns the previous instruction prior to the given node, ignoring label
     * and line number nodes.
     *
     * @param node the node to look up the previous instruction for
     * @return the previous instruction, or null if no previous node was found
     */
    @Nullable
    public static AbstractInsnNode getPrevInstruction(@NonNull AbstractInsnNode node) {
        AbstractInsnNode prev = node;
        while (true) {
            prev = prev.getPrevious();
            if (prev == null) {
                return null;
            } else {
                int type = prev.getType();
                if (type != AbstractInsnNode.LINE && type != AbstractInsnNode.LABEL
                        && type != AbstractInsnNode.FRAME) {
                    return prev;
                }
            }
        }
    }

    /**
     * Returns the next opcode after to the given node, ignoring label and line
     * number nodes
     *
     * @param node the node to look up the next opcode for
     * @return the next opcode, or {@link Opcodes#NOP} if no next node was found
     */
    public static int getNextOpcode(@NonNull AbstractInsnNode node) {
        AbstractInsnNode next = getNextInstruction(node);
        if (next != null) {
            return next.getOpcode();
        } else {
            return Opcodes.NOP;
        }
    }

    /**
     * Returns the next instruction after to the given node, ignoring label and
     * line number nodes.
     *
     * @param node the node to look up the next node for
     * @return the next instruction, or null if no next node was found
     */
    @Nullable
    public static AbstractInsnNode getNextInstruction(@NonNull AbstractInsnNode node) {
        AbstractInsnNode next = node;
        while (true) {
            next = next.getNext();
            if (next == null) {
                return null;
            } else {
                int type = next.getType();
                if (type != AbstractInsnNode.LINE && type != AbstractInsnNode.LABEL
                        && type != AbstractInsnNode.FRAME) {
                    return next;
                }
            }
        }
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

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
//Synthetic comment -- @@ -101,11 +100,6 @@
}

@Override
public @NonNull Speed getSpeed() {
return Speed.SLOW;
}
//Synthetic comment -- @@ -493,8 +487,15 @@
// method name (<init>) but the class name
if (patternStart != null && patternStart.equals(CONSTRUCTOR_NAME)
&& node instanceof MethodInsnNode) {
            patternStart = ((MethodInsnNode) node).owner;
            int index = patternStart.lastIndexOf('$');
            if (index != -1) {
                patternStart = patternStart.substring(index + 1);
            }
            index = patternStart.lastIndexOf('/');
            if (index != -1) {
                patternStart = patternStart.substring(index + 1);
            }
}

Location location = context.getLocationForLine(lineNumber, patternStart, patternEnd, hints);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/FieldGetterDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/FieldGetterDetector.java
//Synthetic comment -- index f126074..b73b9ae 100644

//Synthetic comment -- @@ -16,8 +16,6 @@

package com.android.tools.lint.checks;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
//Synthetic comment -- @@ -25,7 +23,6 @@
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;
//Synthetic comment -- @@ -36,11 +33,9 @@
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
//Synthetic comment -- @@ -74,95 +69,78 @@
// This is a micro-optimization: not enabled by default
setEnabledByDefault(false).setMoreInfo(
"http://developer.android.com/guide/practices/design/performance.html#internal_get_set"); //$NON-NLS-1$
    private ArrayList<Entry> mPendingCalls;

/** Constructs a new {@link FieldGetterDetector} check */
public FieldGetterDetector() {
}

@Override
public @NonNull Speed getSpeed() {
return Speed.FAST;
}

// ---- Implements ClassScanner ----

@Override
    public int[] getApplicableAsmNodeTypes() {
        return new int[] { AbstractInsnNode.METHOD_INSN };
    }

    @Override
    public void checkInstruction(@NonNull ClassContext context, @NonNull ClassNode classNode,
            @NonNull MethodNode method, @NonNull AbstractInsnNode instruction) {
// As of Gingerbread/API 9, Dalvik performs this optimization automatically
if (context.getProject().getMinSdk() >= 9) {
return;
}

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
            if (mPendingCalls == null) {
                mPendingCalls = new ArrayList<Entry>();
}

            mPendingCalls.add(new Entry(name, node, method));
}

        super.checkInstruction(context, classNode, method, instruction);
    }

    @Override
    public void afterCheckFile(@NonNull Context c) {
        ClassContext context = (ClassContext) c;

        if (mPendingCalls != null) {
            Set<String> names = new HashSet<String>(mPendingCalls.size());
            for (Entry entry : mPendingCalls) {
names.add(entry.name);
}

Map<String, String> getters = checkMethods(context.getClassNode(), names);
if (getters.size() > 0) {
for (String getter : getters.keySet()) {
                    for (Entry entry : mPendingCalls) {
String name = entry.name;
// There can be more than one reference to the same name:
// one for each call site
if (name.equals(getter)) {
                            Location location = context.getLocation(entry.call);
String fieldName = getters.get(getter);
if (fieldName == null) {
fieldName = "";
//Synthetic comment -- @@ -180,13 +158,13 @@
// Holder class for getters to be checked
private static class Entry {
public final String name;
public final MethodNode method;
        public final MethodInsnNode call;

        public Entry(String name, MethodInsnNode call, MethodNode method) {
super();
this.name = name;
            this.call = call;
this.method = method;
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/FragmentDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/FragmentDetector.java
//Synthetic comment -- index 6839004..2fc3034 100644

//Synthetic comment -- @@ -24,7 +24,6 @@
import com.android.tools.lint.client.api.LintDriver;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Detector.ClassScanner;
import com.android.tools.lint.detector.api.Issue;
//Synthetic comment -- @@ -37,7 +36,6 @@
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

/**
//Synthetic comment -- @@ -82,11 +80,6 @@
return Speed.FAST;
}

// ---- Implements ClassScanner ----

@Override








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/HandlerDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/HandlerDetector.java
//Synthetic comment -- index 9efd666..7d5a5e5 100644

//Synthetic comment -- @@ -19,7 +19,6 @@
import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Detector.ClassScanner;
import com.android.tools.lint.detector.api.Issue;
//Synthetic comment -- @@ -31,8 +30,6 @@

import org.objectweb.asm.tree.ClassNode;

/**
* Checks that Handler implementations are top level classes or static.
* See the corresponding check in the android.os.Handler source code.
//Synthetic comment -- @@ -65,11 +62,6 @@
return Speed.FAST;
}

// ---- Implements ClassScanner ----

@Override








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/MathDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/MathDetector.java
//Synthetic comment -- index f93a788..7d6424b 100644

//Synthetic comment -- @@ -16,31 +16,26 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintUtils.getNextOpcode;
import static com.android.tools.lint.detector.api.LintUtils.getPrevOpcode;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Arrays;
import java.util.List;

/**
* Looks for usages of {@link java.lang.Math} methods which can be replaced with
//Synthetic comment -- @@ -72,93 +67,48 @@
}

@Override
public @NonNull Speed getSpeed() {
return Speed.FAST;
}

// ---- Implements ClassScanner ----

@Override
    @Nullable
    public List<String> getApplicableCallNames() {
        return Arrays.asList(
                "sin",   //$NON-NLS-1$
                "cos",   //$NON-NLS-1$
                "ceil",  //$NON-NLS-1$
                "sqrt",  //$NON-NLS-1$
                "floor"  //$NON-NLS-1$
        );
    }

    @Override
    public void checkCall(@NonNull ClassContext context, @NonNull ClassNode classNode,
            @NonNull MethodNode method, @NonNull MethodInsnNode call) {
        String owner = call.owner;

        if (owner.equals("java/lang/Math")) { //$NON-NLS-1$
            String name = call.name;
            boolean paramFromFloat = getPrevOpcode(call) == Opcodes.F2D;
            boolean returnToFloat = getNextOpcode(call) == Opcodes.D2F;
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
                context.report(ISSUE, method, context.getLocation(call), message, null /*data*/);
}
}
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/OnClickDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/OnClickDetector.java
//Synthetic comment -- index 0b819cc..bb9c998 100644

//Synthetic comment -- @@ -41,7 +41,6 @@
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
//Synthetic comment -- @@ -87,11 +86,6 @@
}

@Override
public void afterCheckProject(@NonNull Context context) {
if (mNames != null && mNames.size() > 0 && mHaveBytecode) {
List<String> names = new ArrayList<String>(mNames.keySet());








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/RegistrationDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/RegistrationDetector.java
//Synthetic comment -- index 81425db..2455365 100644

//Synthetic comment -- @@ -16,6 +16,10 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_APP_ACTIVITY;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_APP_SERVICE;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_CONTENT_BROADCAST_RECEIVER;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_CONTENT_CONTENT_PROVIDER;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_NAME;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_PACKAGE;
//Synthetic comment -- @@ -27,7 +31,6 @@
import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Detector.ClassScanner;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
//Synthetic comment -- @@ -43,7 +46,6 @@
import org.objectweb.asm.tree.ClassNode;
import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
//Synthetic comment -- @@ -54,13 +56,6 @@
* and also makes sure that they are registered with the correct tag
*/
public class RegistrationDetector extends LayoutDetector implements ClassScanner {
/** Unregistered activities and services */
public static final Issue ISSUE = Issue.create(
"Registered", //$NON-NLS-1$
//Synthetic comment -- @@ -90,11 +85,6 @@
return Speed.FAST;
}

// ---- Implements XmlScanner ----

@Override








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ViewConstructorDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ViewConstructorDetector.java
//Synthetic comment -- index 538c0d6..68062c5 100644

//Synthetic comment -- @@ -21,7 +21,6 @@
import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.ClassContext;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
//Synthetic comment -- @@ -74,11 +73,6 @@
}

@Override
public @NonNull Speed getSpeed() {
return Speed.FAST;
}







