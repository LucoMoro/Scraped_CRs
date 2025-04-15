/*Fix other file visitor to handle incremental analysis properly

The other file visitor would unconditionally scan the full
project contents for each scope, even when project.getSubset()
(used for incremental analysis in IDEs) was non null.

Also avoid NPE when lint is scanning a java file without a
package declaration.

Change-Id:I42143616f630689cadef3c3168fe17f9b2a82922*/
//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/OtherFileVisitor.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/OtherFileVisitor.java
//Synthetic comment -- index adc36de..573d4f0 100644

//Synthetic comment -- @@ -15,19 +15,29 @@
*/
package com.android.tools.lint.client.api;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Project;
import com.android.tools.lint.detector.api.Scope;
import com.google.common.collect.Lists;

import java.io.File;
import java.util.*;

import static com.android.SdkConstants.FD_ASSETS;
import static com.android.tools.lint.detector.api.Detector.OtherFileScanner;

/**
* Visitor for "other" files: files that aren't java sources,
//Synthetic comment -- @@ -64,40 +74,95 @@
scopes.addAll(applicable);
}

if (scopes.contains(Scope.RESOURCE_FILE)) {
            List<File> files = Lists.newArrayListWithExpectedSize(100);
            for (File res : project.getResourceFolders()) {
                collectFiles(files, res);
            }
            File assets = new File(projectFolder, FD_ASSETS);
            if (assets.exists()) {
                collectFiles(files, assets);
            }
            if (!files.isEmpty()) {
                mFiles.put(Scope.RESOURCE_FILE, files);
}
}

if (scopes.contains(Scope.JAVA_FILE)) {
            List<File> files = Lists.newArrayListWithExpectedSize(100);
            for (File srcFolder : project.getJavaSourceFolders()) {
                collectFiles(files, srcFolder);
}
            mFiles.put(Scope.JAVA_FILE, files);
}

if (scopes.contains(Scope.CLASS_FILE)) {
            List<File> files = Lists.newArrayListWithExpectedSize(100);
            for (File classFolder : project.getJavaClassFolders()) {
                collectFiles(files, classFolder);
}
            mFiles.put(Scope.JAVA_FILE, files);
}

if (scopes.contains(Scope.MANIFEST)) {
            File manifestFile = project.getManifestFile();
            if (manifestFile != null) {
                mFiles.put(Scope.MANIFEST, Collections.<File>singletonList(manifestFile));
}
}

//Synthetic comment -- @@ -115,7 +180,7 @@
if (!applicable.isEmpty()) {
for (File file : files) {
Context context = new Context(driver, project, main, file);
                    for (Detector detector : mDetectors) {
detector.beforeCheckFile(context);
detector.run(context);
detector.afterCheckFile(context);








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Issue.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Issue.java
//Synthetic comment -- index e5856b9..ceca9ca 100644

//Synthetic comment -- @@ -335,6 +335,7 @@
* @param required the collection of scopes
* @return this, for constructor chaining
*/
public Issue setAnalysisScopes(@Nullable List<EnumSet<Scope>> required) {
mAnalysisScopes = required;









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/OverdrawDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/OverdrawDetector.java
//Synthetic comment -- index e73a4b5..6627411 100644

//Synthetic comment -- @@ -75,6 +75,7 @@
import lombok.ast.Expression;
import lombok.ast.ForwardingAstVisitor;
import lombok.ast.MethodInvocation;
import lombok.ast.Select;
import lombok.ast.StrictListAccessor;
import lombok.ast.VariableReference;
//Synthetic comment -- @@ -497,7 +498,12 @@
String packageName = "";
if (node.getParent() instanceof CompilationUnit) {
CompilationUnit compilationUnit = (CompilationUnit) node.getParent();
                    packageName = compilationUnit.astPackageDeclaration().getPackageName();
}
mClassFqn = (!packageName.isEmpty() ? (packageName + '.') : "") + name;








