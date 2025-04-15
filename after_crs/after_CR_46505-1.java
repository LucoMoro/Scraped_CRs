/*Add lint check for cut & paste errors in findViewById calls*/




//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/JavaContext.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/JavaContext.java
//Synthetic comment -- index ae86568..d91f423 100644

//Synthetic comment -- @@ -23,6 +23,8 @@

import java.io.File;

import lombok.ast.ConstructorDeclaration;
import lombok.ast.MethodDeclaration;
import lombok.ast.Node;

/**
//Synthetic comment -- @@ -105,4 +107,21 @@
}
super.report(issue, location, message, data);
}


    @Nullable
    public static Node findSurroundingMethod(Node scope) {
        while (scope != null) {
            Class<? extends Node> type = scope.getClass();
            // The Lombok AST uses a flat hierarchy of node type implementation classes
            // so no need to do instanceof stuff here.
            if (type == MethodDeclaration.class || type == ConstructorDeclaration.class) {
                return scope;
            }

            scope = scope.getParent();
        }

        return null;
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index cd654ae..52c89d4 100644

//Synthetic comment -- @@ -55,7 +55,7 @@
private static final List<Issue> sIssues;

static {
        final int initialCapacity = 131;
List<Issue> issues = new ArrayList<Issue>(initialCapacity);

issues.add(AccessibilityDetector.ISSUE);
//Synthetic comment -- @@ -183,6 +183,7 @@
issues.add(SetJavaScriptEnabledDetector.ISSUE);
issues.add(ToastDetector.ISSUE);
issues.add(SharedPrefsDetector.ISSUE);
        issues.add(CutPasteDetector.ISSUE);
issues.add(NonInternationalizedSmsDetector.ISSUE);
issues.add(PrivateKeyDetector.ISSUE);
issues.add(AnnotationDetector.ISSUE);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/CutPasteDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/CutPasteDetector.java
new file mode 100644
//Synthetic comment -- index 0000000..193ab8f

//Synthetic comment -- @@ -0,0 +1,240 @@
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

import static com.android.SdkConstants.RESOURCE_CLZ_ID;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.google.common.collect.Maps;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import lombok.ast.ArrayAccess;
import lombok.ast.AstVisitor;
import lombok.ast.BinaryExpression;
import lombok.ast.Cast;
import lombok.ast.Expression;
import lombok.ast.ForwardingAstVisitor;
import lombok.ast.If;
import lombok.ast.MethodInvocation;
import lombok.ast.Node;
import lombok.ast.Select;
import lombok.ast.Statement;
import lombok.ast.VariableDefinitionEntry;
import lombok.ast.VariableReference;

/**
 * Detector looking for cut & paste issues
 */
public class CutPasteDetector extends Detector implements Detector.JavaScanner {
    /** The main issue discovered by this detector */
    public static final Issue ISSUE = Issue.create(
            "CutPasteId", //$NON-NLS-1$
            "Looks for code cut & paste mistakes in findViewbyId() calls",

            "This lint check looks for cases where you have cut & pasted calls to " +
            "`findViewById` but have forgotten to update the R.id field. It's possible " +
            "that your code is simply (redundantly) looking up the field repeatedly, " +
            "but lint cannot distinguish that from a case where you for example want to " +
            "initialize fields `prev` and `next` and you cut & pasted `findViewById(R.id.prev)` " +
            "and forgot to update the second initialization to `R.id.next`.",

            Category.CORRECTNESS,
            6,
            Severity.WARNING,
            CutPasteDetector.class,
            Scope.JAVA_FILE_SCOPE);

    private Node mLastMethod;
    private Map<String, MethodInvocation> mIds;
    private Map<String, String> mLhs;
    private Map<String, String> mCallOperands;

    /** Constructs a new {@link CutPasteDetector} check */
    public CutPasteDetector() {
    }

    @Override
    public boolean appliesTo(@NonNull Context context, @NonNull File file) {
        return true;
    }

    // ---- Implements JavaScanner ----

    @Override
    public List<String> getApplicableMethodNames() {
        return Collections.singletonList("findViewById"); //$NON-NLS-1$
    }

    @Override
    public void visitMethod(@NonNull JavaContext context, @Nullable AstVisitor visitor,
            @NonNull MethodInvocation call) {
        String lhs = getLhs(call);
        if (lhs == null) {
            return;
        }

        Node method = JavaContext.findSurroundingMethod(call);
        if (method == null) {
            return;
        } else if (method != mLastMethod) {
            mIds = Maps.newHashMap();
            mLhs = Maps.newHashMap();
            mCallOperands = Maps.newHashMap();
            mLastMethod = method;
        }

        String callOperand = call.astOperand() != null ? call.astOperand().toString() : "";

        Expression first = call.astArguments().first();
        if (first instanceof Select) {
            Select select = (Select) first;
            String id = select.astIdentifier().astValue();
            Expression operand = select.astOperand();
            if (operand instanceof Select) {
                Select type = (Select) operand;
                if (type.astIdentifier().astValue().equals(RESOURCE_CLZ_ID)) {
                    if (mIds.containsKey(id)) {
                        if (lhs.equals(mLhs.get(id))) {
                            return;
                        }
                        if (!callOperand.equals(mCallOperands.get(id))) {
                            return;
                        }
                        MethodInvocation earlierCall = mIds.get(id);
                        if (!isReachableFrom(method, earlierCall, call)) {
                            return;
                        }
                        Location location = context.getLocation(call);
                        Location secondary = context.getLocation(earlierCall);
                        secondary.setMessage("First usage here");
                        location.setSecondary(secondary);
                        context.report(ISSUE, call, location, String.format(
                            "The id %1$s has already been looked up in this method; possible " +
                            "cut & paste error?", first.toString()), null);
                    } else {
                        mIds.put(id, call);
                        mLhs.put(id, lhs);
                        mCallOperands.put(id, callOperand);
                    }
                }
            }
        }
    }

    @Nullable
    private static String getLhs(@NonNull MethodInvocation call) {
        Node parent = call.getParent();
        if (parent instanceof Cast) {
            parent = parent.getParent();
        }

        if (parent instanceof VariableDefinitionEntry) {
            VariableDefinitionEntry vde = (VariableDefinitionEntry) parent;
            return vde.astName().astValue();
        } else if (parent instanceof BinaryExpression) {
            BinaryExpression be = (BinaryExpression) parent;
            Expression left = be.astLeft();
            if (left instanceof VariableReference || left instanceof Select) {
                return be.astLeft().toString();
            } else if (left instanceof ArrayAccess) {
                ArrayAccess aa = (ArrayAccess) left;
                return aa.astOperand().toString();
            }
        }

        return null;
    }

    private static boolean isReachableFrom(
            @NonNull Node method,
            @NonNull MethodInvocation from,
            @NonNull MethodInvocation to) {
        ReachableVisitor visitor = new ReachableVisitor(from, to);
        method.accept(visitor);

        return visitor.isReachable();
    }

    private static class ReachableVisitor extends ForwardingAstVisitor {
        private final @NonNull MethodInvocation mFrom;
        private final @NonNull MethodInvocation mTo;
        private boolean mReachable;
        private boolean mSeenEnd;

        public ReachableVisitor(@NonNull MethodInvocation from, @NonNull MethodInvocation to) {
            mFrom = from;
            mTo = to;
        }

        boolean isReachable() {
            return mReachable;
        }

        @Override
        public boolean visitMethodInvocation(MethodInvocation node) {
            if (node == mFrom) {
                mReachable = true;
            } else if (node == mTo) {
                mSeenEnd = true;

            }
            return super.visitMethodInvocation(node);
        }

        @Override
        public boolean visitIf(If node) {
            Expression condition = node.astCondition();
            Statement body = node.astStatement();
            Statement elseBody = node.astElseStatement();
            if (condition != null) {
                condition.accept(this);
            }
            if (body != null) {
                boolean wasReachable = mReachable;
                body.accept(this);
                mReachable = wasReachable;
            }
            if (elseBody != null) {
                boolean wasReachable = mReachable;
                elseBody.accept(this);
                mReachable = wasReachable;
            }

            endVisit(node);

            return false;
        }

        @Override
        public boolean visitNode(Node node) {
            return mSeenEnd;
        }
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/SharedPrefsDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/SharedPrefsDetector.java
//Synthetic comment -- index d38b05e..1f432f7 100644

//Synthetic comment -- @@ -81,22 +81,6 @@
}

@Nullable
private static NormalTypeBody findSurroundingTypeBody(Node scope) {
while (scope != null) {
Class<? extends Node> type = scope.getClass();
//Synthetic comment -- @@ -156,7 +140,7 @@
allowCommitBeforeTarget = false;
}

        Node method = JavaContext.findSurroundingMethod(parent);
if (method == null) {
return;
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ToastDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ToastDetector.java
//Synthetic comment -- index 52ae8ed..e8f8d6e 100644

//Synthetic comment -- @@ -31,11 +31,9 @@
import java.util.List;

import lombok.ast.AstVisitor;
import lombok.ast.Expression;
import lombok.ast.ForwardingAstVisitor;
import lombok.ast.IntegralLiteral;
import lombok.ast.MethodInvocation;
import lombok.ast.Node;
import lombok.ast.Return;
//Synthetic comment -- @@ -75,21 +73,6 @@
return Collections.singletonList("makeText"); //$NON-NLS-1$
}

@Override
public void visitMethod(@NonNull JavaContext context, @Nullable AstVisitor visitor,
@NonNull MethodInvocation node) {
//Synthetic comment -- @@ -118,7 +101,7 @@
}
}

        Node method = JavaContext.findSurroundingMethod(node.getParent());
if (method == null) {
return;
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/CutPasteDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/CutPasteDetectorTest.java
new file mode 100644
//Synthetic comment -- index 0000000..df02a41

//Synthetic comment -- @@ -0,0 +1,55 @@
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
public class CutPasteDetectorTest extends AbstractCheckTest {
    @Override
    protected Detector getDetector() {
        return new CutPasteDetector();
    }

    public void test() throws Exception {
        assertEquals(
            "src/test/pkg/PasteError.java:15: Warning: The id R.id.textView1 has already been looked up in this method; possible cut & paste error? [CutPasteId]\n" +
            "        View view2 = findViewById(R.id.textView1);\n" +
            "                     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
            "    src/test/pkg/PasteError.java:14: First usage here\n" +
            "src/test/pkg/PasteError.java:71: Warning: The id R.id.textView1 has already been looked up in this method; possible cut & paste error? [CutPasteId]\n" +
            "            view2 = findViewById(R.id.textView1);\n" +
            "                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
            "    src/test/pkg/PasteError.java:68: First usage here\n" +
            "src/test/pkg/PasteError.java:78: Warning: The id R.id.textView1 has already been looked up in this method; possible cut & paste error? [CutPasteId]\n" +
            "            view2 = findViewById(R.id.textView1);\n" +
            "                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
            "    src/test/pkg/PasteError.java:76: First usage here\n" +
            "src/test/pkg/PasteError.java:86: Warning: The id R.id.textView1 has already been looked up in this method; possible cut & paste error? [CutPasteId]\n" +
            "            view2 = findViewById(R.id.textView1);\n" +
            "                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
            "    src/test/pkg/PasteError.java:83: First usage here\n" +
            "src/test/pkg/PasteError.java:95: Warning: The id R.id.textView1 has already been looked up in this method; possible cut & paste error? [CutPasteId]\n" +
            "                view2 = findViewById(R.id.textView1);\n" +
            "                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
            "    src/test/pkg/PasteError.java:91: First usage here\n" +
            "0 errors, 5 warnings\n",

            lintProject("src/test/pkg/PasteError.java.txt=>" +
                    "src/test/pkg/PasteError.java"));
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/SharedPrefsDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/SharedPrefsDetectorTest.java
//Synthetic comment -- index dbfc838..7035c21 100644

//Synthetic comment -- @@ -19,7 +19,7 @@
import com.android.tools.lint.detector.api.Detector;

@SuppressWarnings("javadoc")
public class SharedPrefsDetectorTest extends AbstractCheckTest {
@Override
protected Detector getDetector() {
return new SharedPrefsDetector();








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/PasteError.java.txt b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/PasteError.java.txt
new file mode 100644
//Synthetic comment -- index 0000000..b12b56b

//Synthetic comment -- @@ -0,0 +1,109 @@
package test.pkg;

import android.app.Activity;
import android.view.View;

public class PasteError extends Activity {
    protected void ok() {
        Button button1 = (Button) findViewById(R.id.textView1);
        mView2 = findViewById(R.id.textView2);
        View view3 = findViewById(R.id.activity_main);
    }

    protected void error() {
        View view1 = findViewById(R.id.textView1);
        View view2 = findViewById(R.id.textView1);
        View view3 = findViewById(R.id.textView2);
    }

    protected void ok2() {
        View view1;
        if (true) {
            view1 = findViewById(R.id.textView1);
        } else {
            view1 = findViewById(R.id.textView1);
        }
    }

    @SuppressLint("CutPasteId")
    protected void suppressed() {
        View view1 = findViewById(R.id.textView1);
        View view2 = findViewById(R.id.textView1);
    }

    private void ok3() {
        if (view == null || view.findViewById(R.id.city_name) == null) {
            view = mInflater.inflate(R.layout.city_list_item, parent, false);
        }
        TextView name = (TextView) view.findViewById(R.id.city_name);
    }

    private void ok4() {
        mPrevAlbumWrapper = mPrevTrackLayout.findViewById(R.id.album_wrapper);
        mNextAlbumWrapper = mNextTrackLayout.findViewById(R.id.album_wrapper);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (getItemViewType(position) == VIEW_TYPE_HEADER) {
            TextView header = (TextView) listItem.findViewById(R.id.name);
        } else if (getItemViewType(position) == VIEW_TYPE_BOOLEAN) {
            TextView filterName = (TextView) listItem.findViewById(R.id.name);
        } else {
            TextView filterName = (TextView) listItem.findViewById(R.id.name);
        }
    }

    protected void ok_branch_1() {
        if (true) {
            view1 = findViewById(R.id.textView1);
        } else {
            view2 = findViewById(R.id.textView1);
        }
    }

    protected void error_branch_1() {
        if (true) {
            view1 = findViewById(R.id.textView1);
        }
        if (true) {
            view2 = findViewById(R.id.textView1);
        }
    }

    protected void error_branch_2() {
        view1 = findViewById(R.id.textView1);
        if (true) {
            view2 = findViewById(R.id.textView1);
        }
    }

    protected void error_branch_3() {
        view1 = findViewById(R.id.textView1);
        if (true) {
        } else {
            view2 = findViewById(R.id.textView1);
        }
    }

    protected void error_branch_4() {
        view1 = findViewById(R.id.textView1);
        if (true) {
        } else {
            if (true) {
                view2 = findViewById(R.id.textView1);
            }
        }
    }

    protected void ok_branch_2() {
        if (true) {
            view1 = findViewById(R.id.textView1);
        } else {
            if (true) {
                view2 = findViewById(R.id.textView1);
            }
        }
    }
}







