/*35049: @SuppressLint("NewApi") doesn't work on local variables

The @SuppressLint annotation can deliberately be placed on not only
classes and methods but on parameters and local variables too.

For AST-based (Java source-based) lint checks, this works fine.

However, some lint checks, such as the API Check, is based on
analyzing the bytecode. Annotations placed on local variables and
parameters do not make it into the .class file, so these annotations
do not work to suppress errors when placed on local variables (or
parameters).

The @TargetApi annotation applies only to the bytecode based API
check, so its metadata only allows the annotation to be placed on
methods and classes and constructors. However, the @SuppressLint
annotation needs to continue to be available for the AST-based checks.

This CL adds a new lint check, a "meta" check, which actually looks
for invalid @SuppressLint annotations, and warns about these. With the
new lint-on-save behavior, this means you instantly get feedback if
you attempt to suppress an annotation in the wrong place.  (Note that
the quickfix for adding annotations has always enforced this and
placed annotations out at the method level, but as shown in issue
35049, developers place them there deliberately themselves.)

This CL also fixes an unrelated problem (shown in issue 34198) that
the add suppress annotation code could sometimes add multiple versions
of the same id into the annotation.

Change-Id:I5bc61c6315edfcfc20103d1e580e389dd8e6a09b*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddSuppressAnnotation.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddSuppressAnnotation.java
//Synthetic comment -- index 1280bc7..f755e1e 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.tools.lint.checks.AnnotationDetector;
import com.android.tools.lint.checks.ApiDetector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Scope;
//Synthetic comment -- @@ -174,10 +175,14 @@
} else {
Expression existingValue = existing.getValue();
if (existingValue instanceof StringLiteral) {
                StringLiteral stringLiteral = (StringLiteral) existingValue;
                if (mId.equals(stringLiteral.getLiteralValue())) {
                    // Already contains the id
                    return null;
                }
// Create a new array initializer holding the old string plus the new id
ArrayInitializer array = ast.newArrayInitializer();
StringLiteral old = ast.newStringLiteral();
old.setLiteralValue(stringLiteral.getLiteralValue());
array.expressions().add(old);
StringLiteral value = ast.newStringLiteral();
//Synthetic comment -- @@ -187,6 +192,17 @@
} else if (existingValue instanceof ArrayInitializer) {
// Existing array: just append the new string
ArrayInitializer array = (ArrayInitializer) existingValue;
                List expressions = array.expressions();
                if (expressions != null) {
                    for (Object o : expressions) {
                        if (o instanceof StringLiteral) {
                            if (mId.equals(((StringLiteral)o).getLiteralValue())) {
                                // Already contains the id
                                return null;
                            }
                        }
                    }
                }
StringLiteral value = ast.newStringLiteral();
value.setLiteralValue(mId);
ListRewrite listRewrite = rewriter.getListRewrite(array, EXPRESSIONS_PROPERTY);
//Synthetic comment -- @@ -282,6 +298,7 @@
if (document == null) {
return;
}

IWorkingCopyManager manager = JavaUI.getWorkingCopyManager();
ICompilationUnit compilationUnit = manager.getWorkingCopy(editorInput);
int offset = 0;
//Synthetic comment -- @@ -311,6 +328,11 @@
Issue issue = EclipseLintClient.getRegistry().getIssue(id);
boolean isClassDetector = issue != null && issue.getScope().contains(Scope.CLASS_FILE);

        // Don't offer to suppress (with an annotation) the annotation checks
        if (issue == AnnotationDetector.ISSUE) {
            return;
        }

NodeFinder nodeFinder = new NodeFinder(root, offset, length);
ASTNode coveringNode;
if (offset <= 0) {








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index 3e4e922..c61b198 100644

//Synthetic comment -- @@ -175,6 +175,16 @@
}

/**
     * Returns the current {@link IssueRegistry}.
     *
     * @return the current {@link IssueRegistry}
     */
    @NonNull
    public IssueRegistry getRegistry() {
        return mRegistry;
    }

    /**
* Returns the project containing a given file, or null if not found. This searches
* only among the currently checked project and its library projects, not among all
* possible projects being scanned sequentially.








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/AnnotationDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/AnnotationDetector.java
new file mode 100644
//Synthetic comment -- index 0000000..d2d970b

//Synthetic comment -- @@ -0,0 +1,169 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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

import static com.android.tools.lint.detector.api.LintConstants.FQCN_SUPPRESS_LINT;
import static com.android.tools.lint.detector.api.LintConstants.SUPPRESS_LINT;

import com.android.annotations.NonNull;
import com.android.tools.lint.client.api.IssueRegistry;
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
import java.util.Iterator;
import java.util.List;

import lombok.ast.Annotation;
import lombok.ast.AnnotationElement;
import lombok.ast.AnnotationValue;
import lombok.ast.ArrayInitializer;
import lombok.ast.AstVisitor;
import lombok.ast.Expression;
import lombok.ast.ForwardingAstVisitor;
import lombok.ast.Modifiers;
import lombok.ast.Node;
import lombok.ast.StrictListAccessor;
import lombok.ast.StringLiteral;
import lombok.ast.VariableDefinition;

/**
 * Checks annotations to make sure they are valid
 */
public class AnnotationDetector extends Detector implements Detector.JavaScanner {
    /** Placing SuppressLint on a local variable doesn't work for class-file based checks */
    public static final Issue ISSUE = Issue.create(
            "LocalSuppress", //$NON-NLS-1$
            "Looks for @SuppressLint annotations in locations where it doesn't work for class based checks",

            "The @SuppressAnnotation is used to suppress Lint warnings in Java files. However, " +
            "while many lint checks analyzes the Java source code, where they can find " +
            "annotations on (for example) local variables, some checks are analyzing the " +
            ".class files. And in class files, annotations only appear on classes, fields " +
            "and methods. Annotations placed on local variables disappear. If you attempt " +
            "to suppress a lint error for a class-file based lint check, the suppress " +
            "annotation not work. You must move the annotation out to the surrounding method.",

            Category.CORRECTNESS,
            3,
            Severity.ERROR,
            AnnotationDetector.class,
            Scope.JAVA_FILE_SCOPE);

    /** Constructs a new {@link AnnotationDetector} check */
    public AnnotationDetector() {
    }

    @Override
    public boolean appliesTo(@NonNull Context context, @NonNull File file) {
        return true;
    }

    @Override
    public @NonNull Speed getSpeed() {
        return Speed.FAST;
    }

    // ---- Implements JavaScanner ----

    @Override
    public List<Class<? extends Node>> getApplicableNodeTypes() {
        return Collections.<Class<? extends Node>>singletonList(lombok.ast.Annotation.class);
    }

    @Override
    public AstVisitor createJavaVisitor(@NonNull JavaContext context) {
        return new AnnotationChecker(context);
    }

    private static class AnnotationChecker extends ForwardingAstVisitor {
        private final JavaContext mContext;

        public AnnotationChecker(JavaContext context) {
            mContext = context;
        }

        @Override
        public boolean visitAnnotation(Annotation node) {
            String type = node.astAnnotationTypeReference().getTypeName();
            if (SUPPRESS_LINT.equals(type) || FQCN_SUPPRESS_LINT.equals(type)) {
                Node parent = node.getParent();
                if (parent instanceof Modifiers) {
                    parent = parent.getParent();
                    if (parent instanceof VariableDefinition) {
                        for (AnnotationElement element : node.astElements()) {
                            AnnotationValue valueNode = element.astValue();
                            if (valueNode == null) {
                                continue;
                            }
                            if (valueNode instanceof StringLiteral) {
                                StringLiteral literal = (StringLiteral) valueNode;
                                String id = literal.astValue();
                                if (!checkId(node, id)) {
                                    return super.visitAnnotation(node);
                                }
                            } else if (valueNode instanceof ArrayInitializer) {
                                ArrayInitializer array = (ArrayInitializer) valueNode;
                                StrictListAccessor<Expression, ArrayInitializer> expressions =
                                        array.astExpressions();
                                if (expressions == null) {
                                    continue;
                                }
                                Iterator<Expression> arrayIterator = expressions.iterator();
                                while (arrayIterator.hasNext()) {
                                    Expression arrayElement = arrayIterator.next();
                                    if (arrayElement instanceof StringLiteral) {
                                        String id = ((StringLiteral) arrayElement).astValue();
                                        if (!checkId(node, id)) {
                                            return super.visitAnnotation(node);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return super.visitAnnotation(node);
        }

        private boolean checkId(Annotation node, String id) {
            IssueRegistry registry = mContext.getDriver().getRegistry();
            Issue issue = registry.getIssue(id);
            if (issue != null && !issue.getScope().contains(Scope.JAVA_FILE)) {
                // This issue doesn't have AST access: annotations are not
                // available for local variables or parameters
                mContext.report(ISSUE,mContext.getLocation(node), String.format(
                    "The @SuppresLint annotation cannot be used on a local" +
                    " variable  with the lint check '%1$s': move out to the " +
                    "surrounding method", id),
                    null);
                return false;
            }

            return true;
        }
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 4ce06f9..f2b94e8 100644

//Synthetic comment -- @@ -54,7 +54,7 @@
private static final List<Issue> sIssues;

static {
        final int initialCapacity = 98;
List<Issue> issues = new ArrayList<Issue>(initialCapacity);

issues.add(AccessibilityDetector.ISSUE);
//Synthetic comment -- @@ -154,6 +154,7 @@
issues.add(ToastDetector.ISSUE);
issues.add(SharedPrefsDetector.ISSUE);
issues.add(NonInternationalizedSmsDetector.ISSUE);
        issues.add(AnnotationDetector.ISSUE);

assert initialCapacity >= issues.size() : issues.size();









//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/AnnotationDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/AnnotationDetectorTest.java
new file mode 100644
//Synthetic comment -- index 0000000..962b559

//Synthetic comment -- @@ -0,0 +1,54 @@
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
import com.android.tools.lint.detector.api.Issue;

import java.util.List;

@SuppressWarnings("javadoc")
public class AnnotationDetectorTest extends AbstractCheckTest {
    public void test() throws Exception {
        assertEquals(
            "WrongAnnotation.java:11: Error: The @SuppresLint annotation cannot be used on a local variable  with the lint check 'NewApi': move out to the surrounding method\n" +
            "WrongAnnotation.java:13: Error: The @SuppresLint annotation cannot be used on a local variable  with the lint check 'NewApi': move out to the surrounding method\n" +
            "WrongAnnotation.java:8: Error: The @SuppresLint annotation cannot be used on a local variable  with the lint check 'NewApi': move out to the surrounding method\n" +
            "WrongAnnotation.java:9: Error: The @SuppresLint annotation cannot be used on a local variable  with the lint check 'NewApi': move out to the surrounding method",

            lintProject(
                "src/test/pkg/WrongAnnotation.java.txt=>src/test/pkg/WrongAnnotation.java"
            ));
    }

    @Override
    protected Detector getDetector() {
        return new AnnotationDetector();
    }

    @Override
    protected List<Issue> getIssues() {
        List<Issue> issues = super.getIssues();

        // Need these issues on to be found by the registry as well to look up scope
        // in id references (these ids are referenced in the unit test java file below)
        issues.add(ApiDetector.UNSUPPORTED);
        issues.add(SdCardDetector.ISSUE);

        return issues;
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/WrongAnnotation.java.txt b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/WrongAnnotation.java.txt
new file mode 100644
//Synthetic comment -- index 0000000..9256055

//Synthetic comment -- @@ -0,0 +1,19 @@
package com.example.test2;

import android.annotation.SuppressLint;

public class WrongAnnotation {
    @Override
    @SuppressLint("NewApi") // Valid: class-file check on method
    public static void foobar(View view, @SuppressLint("NewApi") int foo) { // Invalid: class-file check
        @SuppressLint("NewApi") // Invalid
        boolean a;
        @SuppressLint({"SdCardPath", "NewApi"}) // Invalid: class-file based check on local variable
        boolean b;
        @android.annotation.SuppressLint({"SdCardPath", "NewApi"}) // Invalid (FQN)
        boolean c;
        @SuppressLint("SdCardPath") // Valid: AST-based check
        boolean d;
    }
}








