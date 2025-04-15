/*39134: forgot to call commit() after editing a SharedPreference

This updates the lint check to handle some additional scenarios around
missing commit() calls for SharedPreference.Edit; in particular when
using edit() on field references and in chained method calls.

Change-Id:I1b249ed81ca9d0b0e1c85466ccfa1ecdd6c53fad*/




//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/SharedPrefsDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/SharedPrefsDetector.java
//Synthetic comment -- index 5db301b..d38b05e 100644

//Synthetic comment -- @@ -33,13 +33,16 @@

import lombok.ast.AstVisitor;
import lombok.ast.ConstructorDeclaration;
import lombok.ast.Expression;
import lombok.ast.ForwardingAstVisitor;
import lombok.ast.MethodDeclaration;
import lombok.ast.MethodInvocation;
import lombok.ast.Node;
import lombok.ast.NormalTypeBody;
import lombok.ast.Return;
import lombok.ast.VariableDeclaration;
import lombok.ast.VariableDefinition;
import lombok.ast.VariableReference;

/**
* Detector looking for SharedPreferences.edit() calls without a corresponding
//Synthetic comment -- @@ -77,7 +80,8 @@
return Collections.singletonList("edit"); //$NON-NLS-1$
}

    @Nullable
    private static Node findSurroundingMethod(Node scope) {
while (scope != null) {
Class<? extends Node> type = scope.getClass();
// The Lombok AST uses a flat hierarchy of node type implementation classes
//Synthetic comment -- @@ -92,11 +96,29 @@
return null;
}

    @Nullable
    private static NormalTypeBody findSurroundingTypeBody(Node scope) {
        while (scope != null) {
            Class<? extends Node> type = scope.getClass();
            // The Lombok AST uses a flat hierarchy of node type implementation classes
            // so no need to do instanceof stuff here.
            if (type == NormalTypeBody.class) {
                return (NormalTypeBody) scope;
            }

            scope = scope.getParent();
        }

        return null;
    }


@Override
public void visitMethod(@NonNull JavaContext context, @Nullable AstVisitor visitor,
@NonNull MethodInvocation node) {
assert node.astName().astValue().equals("edit");
        Expression operand = node.astOperand();
        if (operand == null) {
return;
}

//Synthetic comment -- @@ -104,26 +126,42 @@
// to a local variable; this means we won't recognize some other usages
// of the API (e.g. assigning it to a previously declared variable) but
// is needed until we have type attribution in the AST itself.
        Node parent = node.getParent();
        VariableDefinition definition = getLhs(parent);
        boolean allowCommitBeforeTarget;
        if (definition == null) {
            if (operand instanceof VariableReference) {
                NormalTypeBody body = findSurroundingTypeBody(parent);
                if (body == null) {
                    return;
                }
                String variableName = ((VariableReference) operand).astIdentifier().astValue();
                String type = getFieldType(body, variableName);
                if (type == null || !type.equals("SharedPreferences")) { //$NON-NLS-1$
                    return;
                }
                allowCommitBeforeTarget = true;
            } else {
return;
}
        } else {
            String type = definition.astTypeReference().toString();
            if (!type.endsWith("SharedPreferences.Editor")) {                   //$NON-NLS-1$
                if (!type.equals("Editor") ||                                   //$NON-NLS-1$
                        !LintUtils.isImported(context.compilationUnit,
                                "android.content.SharedPreferences.Editor")) {  //$NON-NLS-1$
                    return;
                }
            }
            allowCommitBeforeTarget = false;
}

        Node method = findSurroundingMethod(parent);
if (method == null) {
return;
}

        CommitFinder finder = new CommitFinder(node, allowCommitBeforeTarget);
method.accept(finder);
if (!finder.isCommitCalled()) {
context.report(ISSUE, method, context.getLocation(node),
//Synthetic comment -- @@ -132,6 +170,39 @@
}
}

    @Nullable
    private static String getFieldType(@NonNull NormalTypeBody cls, @NonNull String name) {
        List<Node> children = cls.getChildren();
        for (Node child : children) {
            if (child.getClass() == VariableDeclaration.class) {
                VariableDeclaration declaration = (VariableDeclaration) child;
                VariableDefinition definition = declaration.astDefinition();
                return definition.astTypeReference().toString();
            }
        }

        return null;
    }

    @Nullable
    private static VariableDefinition getLhs(@NonNull Node node) {
        while (node != null) {
            Class<? extends Node> type = node.getClass();
            // The Lombok AST uses a flat hierarchy of node type implementation classes
            // so no need to do instanceof stuff here.
            if (type == MethodDeclaration.class || type == ConstructorDeclaration.class) {
                return null;
            }
            if (type == VariableDefinition.class) {
                return (VariableDefinition) node;
            }

            node = node.getParent();
        }

        return null;
    }

private class CommitFinder extends ForwardingAstVisitor {
/** Whether we've found one of the commit/cancel methods */
private boolean mFound;
//Synthetic comment -- @@ -139,16 +210,18 @@
private MethodInvocation mTarget;
/** Whether we've seen the target edit node yet */
private boolean mSeenTarget;
        private boolean mAllowCommitBeforeTarget;

        private CommitFinder(MethodInvocation target, boolean allowCommitBeforeTarget) {
mTarget = target;
            mAllowCommitBeforeTarget = allowCommitBeforeTarget;
}

@Override
public boolean visitMethodInvocation(MethodInvocation node) {
if (node == mTarget) {
mSeenTarget = true;
            } else if (mAllowCommitBeforeTarget || mSeenTarget || node.astOperand() == mTarget) {
String name = node.astName().astValue();
if ("commit".equals(name) || "apply".equals(name)) { //$NON-NLS-1$ //$NON-NLS-2$
// TODO: Do more flow analysis to see whether we're really calling commit/apply
//Synthetic comment -- @@ -157,7 +230,7 @@
}
}

            return super.visitMethodInvocation(node);
}

@Override








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/SharedPrefsDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/SharedPrefsDetectorTest.java
//Synthetic comment -- index 964232c..dbfc838 100644

//Synthetic comment -- @@ -75,4 +75,35 @@
lintProject("src/test/pkg/SharedPrefsTest4.java.txt=>" +
"src/test/pkg/SharedPrefsTest4.java"));
}

    public void test5() throws Exception {
        // Check fields too: http://code.google.com/p/android/issues/detail?id=39134
        assertEquals(
            "src/test/pkg/SharedPrefsTest5.java:16: Warning: SharedPreferences.edit() without a corresponding commit() or apply() call [CommitPrefEdits]\n" +
            "        mPreferences.edit().putString(PREF_FOO, \"bar\");\n" +
            "        ~~~~~~~~~~~~~~~~~~~\n" +
            "src/test/pkg/SharedPrefsTest5.java:17: Warning: SharedPreferences.edit() without a corresponding commit() or apply() call [CommitPrefEdits]\n" +
            "        mPreferences.edit().remove(PREF_BAZ).remove(PREF_FOO);\n" +
            "        ~~~~~~~~~~~~~~~~~~~\n" +
            "src/test/pkg/SharedPrefsTest5.java:26: Warning: SharedPreferences.edit() without a corresponding commit() or apply() call [CommitPrefEdits]\n" +
            "        preferences.edit().putString(PREF_FOO, \"bar\");\n" +
            "        ~~~~~~~~~~~~~~~~~~\n" +
            "src/test/pkg/SharedPrefsTest5.java:27: Warning: SharedPreferences.edit() without a corresponding commit() or apply() call [CommitPrefEdits]\n" +
            "        preferences.edit().remove(PREF_BAZ).remove(PREF_FOO);\n" +
            "        ~~~~~~~~~~~~~~~~~~\n" +
            "src/test/pkg/SharedPrefsTest5.java:32: Warning: SharedPreferences.edit() without a corresponding commit() or apply() call [CommitPrefEdits]\n" +
            "        preferences.edit().putString(PREF_FOO, \"bar\");\n" +
            "        ~~~~~~~~~~~~~~~~~~\n" +
            "src/test/pkg/SharedPrefsTest5.java:33: Warning: SharedPreferences.edit() without a corresponding commit() or apply() call [CommitPrefEdits]\n" +
            "        preferences.edit().remove(PREF_BAZ).remove(PREF_FOO);\n" +
            "        ~~~~~~~~~~~~~~~~~~\n" +
            "src/test/pkg/SharedPrefsTest5.java:38: Warning: SharedPreferences.edit() without a corresponding commit() or apply() call [CommitPrefEdits]\n" +
            "        Editor editor = preferences.edit().putString(PREF_FOO, \"bar\");\n" +
            "                        ~~~~~~~~~~~~~~~~~~\n" +
            "0 errors, 7 warnings\n",

            lintProject("src/test/pkg/SharedPrefsTest5.java.txt=>" +
                    "src/test/pkg/SharedPrefsTest5.java"));
    }

}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/SharedPrefsTest5.java.txt b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/SharedPrefsTest5.java.txt
new file mode 100644
//Synthetic comment -- index 0000000..f005162

//Synthetic comment -- @@ -0,0 +1,54 @@
package test.pkg;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

@SuppressWarnings("unused")
class SharedPrefsTest5 {
    SharedPreferences mPreferences;
    private static final String PREF_FOO = "foo";
    private static final String PREF_BAZ = "bar";

    private void wrong() {
        // Field reference to preferences
        mPreferences.edit().putString(PREF_FOO, "bar");
        mPreferences.edit().remove(PREF_BAZ).remove(PREF_FOO);
    }

    private void ok() {
        mPreferences.edit().putString(PREF_FOO, "bar").commit();
        mPreferences.edit().remove(PREF_BAZ).remove(PREF_FOO).commit();
    }

    private void wrong2(SharedPreferences preferences) {
        preferences.edit().putString(PREF_FOO, "bar");
        preferences.edit().remove(PREF_BAZ).remove(PREF_FOO);
    }

    private void wrong3(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(PREF_FOO, "bar");
        preferences.edit().remove(PREF_BAZ).remove(PREF_FOO);
    }

    private void wrong4(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit().putString(PREF_FOO, "bar");
    }

    private void ok2(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(PREF_FOO, "bar").commit();
    }

    private final SharedPreferences mPrefs;

    public void ok3() {
        final SharedPreferences.Editor editor = mPrefs.edit().putBoolean(
                PREF_FOO, true);
        editor.putString(PREF_BAZ, "");
        editor.apply();
    }
}







