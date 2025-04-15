/*Added detection code for malformed phone numbers that are passed to the SMS sending API functions

Change-Id:Iad5fd3e1e9c0add65da6717a601fad8ea2e9b84e*/




//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 146da7f..b975ec1 100644

//Synthetic comment -- @@ -54,7 +54,7 @@
private static final List<Issue> sIssues;

static {
        final int initialCapacity = 93;
List<Issue> issues = new ArrayList<Issue>(initialCapacity);

issues.add(AccessibilityDetector.ISSUE);
//Synthetic comment -- @@ -149,6 +149,7 @@
issues.add(SetJavaScriptEnabledDetector.ISSUE);
issues.add(ToastDetector.ISSUE);
issues.add(SharedPrefsDetector.ISSUE);
        issues.add(NonInternationalizedSmsDetector.ISSUE);

assert initialCapacity >= issues.size() : issues.size();









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/NonInternationalizedSmsDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/NonInternationalizedSmsDetector.java
new file mode 100644
//Synthetic comment -- index 0000000..b9a7b79

//Synthetic comment -- @@ -0,0 +1,102 @@
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

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;

import lombok.ast.StringLiteral;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lombok.ast.AstVisitor;
import lombok.ast.Expression;
import lombok.ast.MethodInvocation;
import lombok.ast.StrictListAccessor;

/** Detector looking for text messages sent to an unlocalized phone number. */
public class NonInternationalizedSmsDetector extends Detector implements Detector.JavaScanner {
    /** The main issue discovered by this detector */
    public static final Issue ISSUE = Issue.create(
            "UnlocalizedSms", //$NON-NLS-1$
            "Looks for code sending text messages to unlocalized phone numbers",

            "SMS destination numbers must start with a country code or the application code " +
            "must ensure that the SMS is only sent when the user is in the same country as the receiver.",

            Category.CORRECTNESS,
            5,
            Severity.WARNING,
            NonInternationalizedSmsDetector.class,
            Scope.JAVA_FILE_SCOPE);


    /** Constructs a new {@link NonInternationalizedSmsDetector} check */
    public NonInternationalizedSmsDetector() {
    }

    @Override
    public boolean appliesTo(@NonNull Context context, @NonNull File file) {
        return true;
    }


    // ---- Implements JavaScanner ----

    @Override
    public List<String> getApplicableMethodNames() {
      List<String> methodNames = new ArrayList<String>(2);
      methodNames.add("sendTextMessage");  //$NON-NLS-1$
      methodNames.add("sendMultipartTextMessage");  //$NON-NLS-1$
      return methodNames;
    }

    @Override
    public void visitMethod(@NonNull JavaContext context, @Nullable AstVisitor visitor,
            @NonNull MethodInvocation node) {
        assert node.astName().astValue().equals("sendTextMessage") ||  //$NON-NLS-1$
            node.astName().astValue().equals("sendMultipartTextMessage");  //$NON-NLS-1$
        if (node.astOperand() == null) {
            // "sendTextMessage"/"sendMultipartTextMessage" in the code with no operand
            return;
        }

        StrictListAccessor<Expression, MethodInvocation> args = node.astArguments();
        if (args.size() == 5) {
            Expression destinationAddress = args.first();
            if (destinationAddress instanceof StringLiteral) {
                String number = ((StringLiteral) destinationAddress).astValue();

                if (!number.startsWith("+")) {  //$NON-NLS-1$
                   context.report(ISSUE, context.getLocation(destinationAddress),
                       "To make sure the SMS can be sent by all users, please start the SMS number" +
                       "with a + and a country code or restrict the code invocation to people in the country " +
                       "you are targeting.",
                       null);
                }
            }
        }
    }
}
\ No newline at end of file








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/NonInternationalizedSmsDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/NonInternationalizedSmsDetectorTest.java
new file mode 100644
//Synthetic comment -- index 0000000..04cdfd3

//Synthetic comment -- @@ -0,0 +1,33 @@
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
public class NonInternationalizedSmsDetectorTest extends AbstractCheckTest {
    @Override
    protected Detector getDetector() {
        return new NonInternationalizedSmsDetector();
    }

    public void test() throws Exception {
        assertEquals(
                "NonInternationalizedSmsDetectorTest.java:18: Warning: To make sure the SMS can be sent by all users, please start the SMS numberwith a + and a country code or restrict the code invocation to people in the country you are targeting.",

            lintProject("src/test/pkg/NonInternationalizedSmsDetectorTest.java.txt=>src/test/pkg/NonInternationalizedSmsDetectorTest.java"));
    }
}
\ No newline at end of file








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/NonInternationalizedSmsDetectorTest.java.txt b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/NonInternationalizedSmsDetectorTest.java.txt
new file mode 100644
//Synthetic comment -- index 0000000..354f5bf

//Synthetic comment -- @@ -0,0 +1,20 @@
package foo.bar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.SmsManager;

public class NonInternationalizedSmsDetectorTest {
    private void sendLocalizedMessage(Context context) {
  // Don't warn here
  SmsManager sms = SmsManager.getDefault();
  sms.sendTextMessage("+1234567890", null, null, null, null);
    }

    private void sendAlternativeCountryPrefix(Context context) {
  // Do warn here
  SmsManager sms = SmsManager.getDefault();
  sms.sendMultipartTextMessage("001234567890", null, null, null, null);
    }
}







