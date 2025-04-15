/*Track tools/base API changes

Change-Id:I97f785fdeafebf7c87d180e6b9905b08f7e3b72e*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddSuppressAnnotation.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddSuppressAnnotation.java
//Synthetic comment -- index 365c73d..b047b1b 100644

//Synthetic comment -- @@ -32,7 +32,6 @@
import com.android.tools.lint.checks.AnnotationDetector;
import com.android.tools.lint.checks.ApiDetector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.Scope;

import org.eclipse.core.resources.IMarker;
//Synthetic comment -- @@ -346,7 +345,8 @@
}

Issue issue = EclipseLintClient.getRegistry().getIssue(id);
        boolean isClassDetector = issue != null && issue.getScope().contains(Scope.CLASS_FILE);

// Don't offer to suppress (with an annotation) the annotation checks
if (issue == AnnotationDetector.ISSUE) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddSuppressAttribute.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddSuppressAttribute.java
//Synthetic comment -- index 0d8fd33..b77b475 100644

//Synthetic comment -- @@ -29,7 +29,6 @@
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.android.tools.lint.checks.ApiDetector;
import com.android.tools.lint.detector.api.LintUtils;
import com.google.common.collect.Lists;

import org.eclipse.core.resources.IMarker;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java
//Synthetic comment -- index f6b18e0..45ae2c5 100644

//Synthetic comment -- @@ -105,6 +105,7 @@
import java.util.Map;
import java.util.WeakHashMap;

import lombok.ast.ecj.EcjTreeConverter;
import lombok.ast.grammar.ParseProblem;
import lombok.ast.grammar.Source;
//Synthetic comment -- @@ -614,8 +615,8 @@
return "";
}

        String summary = issue.getDescription();
        String explanation = issue.getExplanationAsSimpleText();

StringBuilder sb = new StringBuilder(summary.length() + explanation.length() + 20);
try {
//Synthetic comment -- @@ -1213,6 +1214,19 @@
@NonNull lombok.ast.Node compilationUnit) {
}

/* Handle for creating positions cheaply and returning full fledged locations later */
private class LocationHandle implements Handle {
private File mFile;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintFix.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintFix.java
//Synthetic comment -- index feb6bb5..401703e 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
import com.android.tools.lint.checks.UseCompoundDrawableDetector;
import com.android.tools.lint.checks.UselessViewDetector;
import com.android.tools.lint.detector.api.Issue;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
//Synthetic comment -- @@ -109,7 +110,7 @@
public String getAdditionalProposalInfo() {
Issue issue = EclipseLintClient.getRegistry().getIssue(mId);
if (issue != null) {
            return issue.getExplanationAsHtml();
}

return null;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintFixGenerator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintFixGenerator.java
//Synthetic comment -- index 22fe23b..ce5fd55 100644

//Synthetic comment -- @@ -27,6 +27,7 @@
import com.android.tools.lint.client.api.DefaultConfiguration;
import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Project;
import com.android.tools.lint.detector.api.Severity;
import com.android.utils.SdkUtils;
//Synthetic comment -- @@ -488,11 +489,12 @@
sb.append('\n').append('\n');
sb.append("Issue Explanation:");
sb.append('\n');
            if (issue.getExplanation() != null) {
sb.append('\n');
                sb.append(issue.getExplanationAsSimpleText());
} else {
                sb.append(issue.getDescription());
}

if (issue.getMoreInfo() != null) {
//Synthetic comment -- @@ -543,7 +545,8 @@
public String getAdditionalProposalInfo() {
return "Provides more information about this issue."
+ "<br><br>" //$NON-NLS-1$
                    + EclipseLintClient.getRegistry().getIssue(mId).getExplanationAsHtml();
}

@Override








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintJob.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintJob.java
//Synthetic comment -- index 95e9f18..bd1eb72 100644

//Synthetic comment -- @@ -140,7 +140,7 @@
if (issue == null) {
continue;
}
                    if (issue.isAdequate(scope)) {
marker.delete();
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/LintPreferencePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/LintPreferencePage.java
//Synthetic comment -- index 6e18837..02af2fd 100644

//Synthetic comment -- @@ -15,6 +15,9 @@
*/
package com.android.ide.eclipse.adt.internal.preferences;

import com.android.annotations.NonNull;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
//Synthetic comment -- @@ -448,8 +451,8 @@
Object data = item != null ? item.getData() : null;
if (data instanceof Issue) {
Issue issue = (Issue) data;
                String summary = issue.getDescription();
                String explanation = issue.getExplanation();

StringBuilder sb = new StringBuilder(summary.length() + explanation.length() + 20);
sb.append(summary);
//Synthetic comment -- @@ -568,7 +571,7 @@
|| issue.getCategory().getName().toLowerCase(Locale.US).startsWith(filter)
|| issue.getCategory().getFullName().toLowerCase(Locale.US).startsWith(filter)
|| issue.getId().toLowerCase(Locale.US).contains(filter)
                || issue.getDescription().toLowerCase(Locale.US).contains(filter);
}

private class ContentProvider extends TreeNodeContentProvider {
//Synthetic comment -- @@ -712,7 +715,7 @@
case 0:
return issue.getId();
case 1:
                    return issue.getDescription();
}

return null;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index 5d9c0ef..f187a3f 100644

//Synthetic comment -- @@ -235,7 +235,8 @@
final ArrayList<String> logMessages = new ArrayList<String>();
ILogger log = new ILogger() {
@Override
                public void error(Throwable throwable, String errorFormat, Object... arg) {
if (errorFormat != null) {
logMessages.add(String.format("Error: " + errorFormat, arg));
}







