/*Add lint check to make sure SharedPreference editors are committed

Change-Id:Ifacc8df1ef9169c57725652ae9d921ed2b3129bd*/




//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index b62f34a..ad359ce 100644

//Synthetic comment -- @@ -53,7 +53,7 @@
private static final List<Issue> sIssues;

static {
        final int initialCapacity = 89;
List<Issue> issues = new ArrayList<Issue>(initialCapacity);

issues.add(AccessibilityDetector.ISSUE);
//Synthetic comment -- @@ -144,6 +144,7 @@
issues.add(JavaPerformanceDetector.USE_SPARSEARRAY);
issues.add(SetJavaScriptEnabledDetector.ISSUE);
issues.add(ToastDetector.ISSUE);
        issues.add(SharedPrefsDetector.ISSUE);

assert initialCapacity >= issues.size() : issues.size();









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/SharedPrefsDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/SharedPrefsDetector.java
new file mode 100644
//Synthetic comment -- index 0000000..525ab6d

//Synthetic comment -- @@ -0,0 +1,172 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;

import java.io.File;
import java.util.Collections;
import java.util.List;

import lombok.ast.AstVisitor;
import lombok.ast.ForwardingAstVisitor;
import lombok.ast.MethodDeclaration;
import lombok.ast.MethodInvocation;
import lombok.ast.Node;
import lombok.ast.Return;
import lombok.ast.VariableDefinition;
import lombok.ast.VariableDefinitionEntry;

/**
 * Detector looking for SharedPreferences.edit() calls without a corresponding
 * commit() or apply() call
 */
public class SharedPrefsDetector extends Detector implements Detector.JavaScanner {
    /** The main issue discovered by this detector */
    public static final Issue ISSUE = Issue.create(
            "CommitPrefEdits", //$NON-NLS-1$
            "Looks for code editing a SharedPreference but forgetting to call commit() on it",

            "After calling edit() on a SharedPreference, you must call commit() or apply() on " +
            "the editor to save the results.",

            Category.CORRECTNESS,
            6,
            Severity.WARNING,
            SharedPrefsDetector.class,
            Scope.JAVA_FILE_SCOPE);

    /** Constructs a new {@link SharedPrefsDetector} check */
    public SharedPrefsDetector() {
    }

    @Override
    public boolean appliesTo(Context context, File file) {
        return true;
    }


    @Override
    public Speed getSpeed() {
        return Speed.NORMAL;
    }

    // ---- Implements JavaScanner ----

    @Override
    public List<String> getApplicableMethodNames() {
        return Collections.singletonList("edit"); //$NON-NLS-1$
    }

    private MethodDeclaration findSurroundingMethod(Node scope) {
        while (scope != null) {
            Class<? extends Node> type = scope.getClass();
            // The Lombok AST uses a flat hierarchy of node type implementation classes
            // so no need to do instanceof stuff here.
            if (type == MethodDeclaration.class) {
                return (MethodDeclaration) scope;
            }

            scope = scope.getParent();
        }

        return null;
    }

    @Override
    public void visitMethod(JavaContext context, AstVisitor visitor, MethodInvocation node) {
        assert node.astName().astValue().equals("edit");
        if (node.astOperand() == null) {
            return;
        }

        // Looking for the specific pattern where you assign the edit() result
        // to a local variable; this means we won't recognize some other usages
        // of the API (e.g. assigning it to a previously declared variable) but
        // is needed until we have type attribution in the AST itself.
        if (!(node.getParent() instanceof VariableDefinitionEntry &&
                node.getParent().getParent() instanceof VariableDefinition)) {
            return;
        }
        VariableDefinition definition = (VariableDefinition) node.getParent().getParent();
        String type = definition.astTypeReference().toString();
        if (!type.endsWith("SharedPreferences.Editor")) { //$NON-NLS-1$
            return;
        }

        MethodDeclaration method = findSurroundingMethod(node.getParent());
        if (method == null) {
            return;
        }

        CommitFinder finder = new CommitFinder(node);
        method.accept(finder);
        if (!finder.isCommitCalled()) {
            context.report(ISSUE, method, context.getLocation(node),
                    "SharedPreferences.edit() without a corresponding commit() or apply() call",
                    null);
        }
    }

    private class CommitFinder extends ForwardingAstVisitor {
        /** Whether we've found one of the commit/cancel methods */
        private boolean mFound;
        /** The target edit call */
        private MethodInvocation mTarget;
        /** Whether we've seen the target edit node yet */
        private boolean mSeenTarget;

        private CommitFinder(MethodInvocation target) {
            mTarget = target;
        }

        @Override
        public boolean visitMethodInvocation(MethodInvocation node) {
            if (node == mTarget) {
                mSeenTarget = true;
            } else if (mSeenTarget || node.astOperand() == mTarget) {
                String name = node.astName().astValue();
                if ("commit".equals(name) || "apply".equals(name)) { //$NON-NLS-1$ //$NON-NLS-2$
                    // TODO: Do more flow analysis to see whether we're really calling commit/apply
                    // on the right type of object?
                    mFound = true;
                }
            }

            return true;
        }

        @Override
        public boolean visitReturn(Return node) {
            if (node.astValue() == mTarget) {
                // If you just do "return editor.commit() don't warn
                mFound = true;
            }
            return super.visitReturn(node);
        }

        boolean isCommitCalled() {
            return mFound;
        }
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ToastDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ToastDetector.java
//Synthetic comment -- index c97027f..f199438 100644

//Synthetic comment -- @@ -54,7 +54,8 @@
ToastDetector.class,
Scope.JAVA_FILE_SCOPE);


    /** Constructs a new {@link ToastDetector} check */
public ToastDetector() {
}

//Synthetic comment -- @@ -94,6 +95,10 @@
@Override
public void visitMethod(JavaContext context, AstVisitor visitor, MethodInvocation node) {
assert node.astName().astValue().equals("makeText");
        if (node.astOperand() == null) {
            // "makeText()" in the code with no operand
            return;
        }

String operand = node.astOperand().toString();
if (!(operand.equals("Toast") || operand.endsWith(".Toast"))) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/SharedPrefsDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/SharedPrefsDetectorTest.java
new file mode 100644
//Synthetic comment -- index 0000000..0f50d59

//Synthetic comment -- @@ -0,0 +1,35 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
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
public class SharedPrefsDetectorTest  extends AbstractCheckTest {
    @Override
    protected Detector getDetector() {
        return new SharedPrefsDetector();
    }

    public void test() throws Exception {
        assertEquals(
            "SharedPrefsTest.java:59: Warning: SharedPreferences.edit() without a " +
            "corresponding commit() or apply() call",

            lintProject("src/test/pkg/SharedPrefsTest.java.txt=>" +
                    "src/test/pkg/SharedPrefsTest.java"));
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/SharedPrefsTest.java.txt b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/SharedPrefsTest.java.txt
new file mode 100644
//Synthetic comment -- index 0000000..e6c7300

//Synthetic comment -- @@ -0,0 +1,64 @@
package foo.bar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

public class SharedPrefsText {
    private Toast createToast(Context context) {
	// Don't warn here
	return Toast.makeText(context, "foo", Toast.LENGTH_LONG);
    }

    // OK 1
    public void onCreate1(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("foo", "bar");
        editor.putInt("bar", 42);
        editor.commit();
    }

    // OK 2
    public void onCreate2(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("foo", "bar");
        editor.putInt("bar", 42);
        if (apply) {
            editor.apply();
        }
    }

    // OK 3
    public boolean test1(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("foo", "bar");
        editor.putInt("bar", 42);
        return editor.apply();
    }

    // Not a bug
    public void test(Foo foo) {
        Bar bar1 = foo.edit();
        Bar bar2 = Foo.edit();
        Bar bar3 = edit();
        SharedPreferences.Editor editor = preferences.edit(42);
        apply();
    }

    // Bug
    public void bug1(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("foo", "bar");
        editor.putInt("bar", 42);
    }
 }








