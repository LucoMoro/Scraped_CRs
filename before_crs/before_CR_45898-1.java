/*38838: lint -fullpaths doesn't always work

Change-Id:I09500947973897b412b61da17284f04acb8ece20*/
//Synthetic comment -- diff --git a/lint/cli/src/com/android/tools/lint/Main.java b/lint/cli/src/com/android/tools/lint/Main.java
//Synthetic comment -- index ad086c7..d2f9439 100644

//Synthetic comment -- @@ -23,6 +23,7 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.checks.BuiltinIssueRegistry;
import com.android.tools.lint.client.api.Configuration;
import com.android.tools.lint.client.api.DefaultConfiguration;
//Synthetic comment -- @@ -41,6 +42,7 @@
import com.android.tools.lint.detector.api.Project;
import com.android.tools.lint.detector.api.Severity;
import com.android.utils.SdkUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Closeables;
//Synthetic comment -- @@ -1317,6 +1319,62 @@
}
}

String getDisplayPath(Project project, File file) {
String path = file.getPath();
if (!mFullPath && path.startsWith(project.getReferenceDir().getPath())) {
//Synthetic comment -- @@ -1328,6 +1386,8 @@
if (path.length() == 0) {
path = file.getName();
}
}

return path;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/MainTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/MainTest.java
//Synthetic comment -- index 4e7a029..d015a3d 100644

//Synthetic comment -- @@ -260,4 +260,39 @@
super("Unit test");
}
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/XmlReporterTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/XmlReporterTest.java
//Synthetic comment -- index 95147b4..166f721 100644

//Synthetic comment -- @@ -133,6 +133,105 @@
}
}

@Override
protected Detector getDetector() {
fail("Not used in this test");







