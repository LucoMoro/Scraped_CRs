/*Add lint rule to find incorrect alias files

This helps catch errors likehttp://code.google.com/p/android/issues/detail?id=36821Change-Id:I1ceda3183d8b1e29510ba0be2523dc6eb9c7a23b*/




//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 8e4ab9d..3c08e82 100644

//Synthetic comment -- @@ -54,7 +54,7 @@
private static final List<Issue> sIssues;

static {
        final int initialCapacity = 105;
List<Issue> issues = new ArrayList<Issue>(initialCapacity);

issues.add(AccessibilityDetector.ISSUE);
//Synthetic comment -- @@ -145,6 +145,7 @@
issues.add(TypoDetector.ISSUE);
issues.add(ViewTypeDetector.ISSUE);
issues.add(WrongImportDetector.ISSUE);
        issues.add(WrongLocationDetector.ISSUE);
issues.add(ViewConstructorDetector.ISSUE);
issues.add(NamespaceDetector.CUSTOMVIEW);
issues.add(NamespaceDetector.UNUSED);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/WrongLocationDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/WrongLocationDetector.java
new file mode 100644
//Synthetic comment -- index 0000000..8a19e20

//Synthetic comment -- @@ -0,0 +1,68 @@
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

import static com.android.tools.lint.detector.api.LintConstants.TAG_RESOURCES;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LayoutDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;
import com.android.tools.lint.detector.api.XmlContext;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/** Looks for problems with XML files being placed in the wrong folder */
public class WrongLocationDetector extends LayoutDetector {
    /** Main issue investigated by this detector */
    public static final Issue ISSUE = Issue.create(
            "WrongFolder", //$NON-NLS-1$

            "Finds resource files that are placed in the wrong folders",

            "Resource files are sometimes placed in the wrong folder, and it can lead to " +
            "subtle bugs that are hard to understand. This check looks for problems in this " +
            "area, such as attempting to place a layout \"alias\" file in a `layout/` folder " +
            "rather than the `values/` folder where it belongs.",
            Category.CORRECTNESS,
            8,
            Severity.ERROR,
            WrongLocationDetector.class,
            Scope.RESOURCE_FILE_SCOPE);

    /** Constructs a new {@link WrongLocationDetector} check */
    public WrongLocationDetector() {
    }

    @Override
    public @NonNull Speed getSpeed() {
        return Speed.FAST;
    }

    @Override
    public void visitDocument(@NonNull XmlContext context, @NonNull Document document) {
        Element root = document.getDocumentElement();
        if (root != null && root.getTagName().equals(TAG_RESOURCES)) {
            context.report(ISSUE, context.getLocation(root),
                    "This file should be placed in a values/ folder, not a layout/ folder", null);
        }
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/WrongLocationDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/WrongLocationDetectorTest.java
new file mode 100644
//Synthetic comment -- index 0000000..f30a604

//Synthetic comment -- @@ -0,0 +1,43 @@
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
public class WrongLocationDetectorTest extends AbstractCheckTest {
    @Override
    protected Detector getDetector() {
        return new WrongLocationDetector();
    }

    public void test() throws Exception {
        assertEquals(
            "res/layout/alias.xml:17: Error: This file should be placed in a values/ folder, not a layout/ folder [WrongFolder]\n" +
            "<resources>\n" +
            "^\n" +
            "1 errors, 0 warnings\n",

        lintProject("res/values/strings.xml=>res/layout/alias.xml"));
    }

    public void testOk() throws Exception {
        assertEquals("No warnings.",

        lintProject("res/values/strings.xml"));
    }
}







