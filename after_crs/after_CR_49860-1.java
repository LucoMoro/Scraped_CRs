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

import static com.android.SdkConstants.ANDROID_MANIFEST_XML;
import static com.android.SdkConstants.DOT_CLASS;
import static com.android.SdkConstants.DOT_JAVA;
import static com.android.SdkConstants.DOT_XML;
import static com.android.SdkConstants.FD_ASSETS;
import static com.android.tools.lint.detector.api.Detector.OtherFileScanner;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Project;
import com.android.tools.lint.detector.api.Scope;
import com.android.utils.SdkUtils;
import com.google.common.collect.Lists;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

/**
* Visitor for "other" files: files that aren't java sources,
//Synthetic comment -- @@ -64,40 +74,95 @@
scopes.addAll(applicable);
}

        List<File> subset = project.getSubset();

if (scopes.contains(Scope.RESOURCE_FILE)) {
            if (subset != null && !subset.isEmpty()) {
                List<File> files = new ArrayList<File>(subset.size());
                for (File file : subset) {
                    if (SdkUtils.endsWith(file.getPath(), DOT_XML) &&
                            !file.getName().equals(ANDROID_MANIFEST_XML)) {
                        files.add(file);
                    }
                }
                if (!files.isEmpty()) {
                    mFiles.put(Scope.RESOURCE_FILE, files);
                }
            } else {
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
}

if (scopes.contains(Scope.JAVA_FILE)) {
            if (subset != null && !subset.isEmpty()) {
                List<File> files = new ArrayList<File>(subset.size());
                for (File file : subset) {
                    if (file.getPath().endsWith(DOT_JAVA)) {
                        files.add(file);
                    }
                }
                if (!files.isEmpty()) {
                    mFiles.put(Scope.JAVA_FILE, files);
                }
            } else {
                List<File> files = Lists.newArrayListWithExpectedSize(100);
                for (File srcFolder : project.getJavaSourceFolders()) {
                    collectFiles(files, srcFolder);
                }
                if (!files.isEmpty()) {
                    mFiles.put(Scope.JAVA_FILE, files);
                }
}
}

if (scopes.contains(Scope.CLASS_FILE)) {
            if (subset != null && !subset.isEmpty()) {
                List<File> files = new ArrayList<File>(subset.size());
                for (File file : subset) {
                    if (file.getPath().endsWith(DOT_CLASS)) {
                        files.add(file);
                    }
                }
                if (!files.isEmpty()) {
                    mFiles.put(Scope.CLASS_FILE, files);
                }
            } else {
                List<File> files = Lists.newArrayListWithExpectedSize(100);
                for (File classFolder : project.getJavaClassFolders()) {
                    collectFiles(files, classFolder);
                }
                if (!files.isEmpty()) {
                    mFiles.put(Scope.CLASS_FILE, files);
                }
}
}

if (scopes.contains(Scope.MANIFEST)) {
            if (subset != null && !subset.isEmpty()) {
                List<File> files = new ArrayList<File>(subset.size());
                for (File file : subset) {
                    if (file.getName().equals(ANDROID_MANIFEST_XML)) {
                        files.add(file);
                    }
                }
                if (!files.isEmpty()) {
                    mFiles.put(Scope.MANIFEST, files);
                }
            } else {
                File manifestFile = project.getManifestFile();
                if (manifestFile != null) {
                    mFiles.put(Scope.MANIFEST, Collections.<File>singletonList(manifestFile));
                }
}
}

//Synthetic comment -- @@ -115,7 +180,7 @@
if (!applicable.isEmpty()) {
for (File file : files) {
Context context = new Context(driver, project, main, file);
                    for (Detector detector : applicable) {
detector.beforeCheckFile(context);
detector.run(context);
detector.afterCheckFile(context);








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Issue.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Issue.java
//Synthetic comment -- index e5856b9..ceca9ca 100644

//Synthetic comment -- @@ -335,6 +335,7 @@
* @param required the collection of scopes
* @return this, for constructor chaining
*/
    @NonNull
public Issue setAnalysisScopes(@Nullable List<EnumSet<Scope>> required) {
mAnalysisScopes = required;









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/OverdrawDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/OverdrawDetector.java
//Synthetic comment -- index e73a4b5..6627411 100644

//Synthetic comment -- @@ -75,6 +75,7 @@
import lombok.ast.Expression;
import lombok.ast.ForwardingAstVisitor;
import lombok.ast.MethodInvocation;
import lombok.ast.PackageDeclaration;
import lombok.ast.Select;
import lombok.ast.StrictListAccessor;
import lombok.ast.VariableReference;
//Synthetic comment -- @@ -497,7 +498,12 @@
String packageName = "";
if (node.getParent() instanceof CompilationUnit) {
CompilationUnit compilationUnit = (CompilationUnit) node.getParent();
                    PackageDeclaration packageDeclaration = compilationUnit.astPackageDeclaration();
                    if (packageDeclaration == null) {
                        // No package declaration: ignore this one
                        return true;
                    }
                    packageName = packageDeclaration.getPackageName();
}
mClassFqn = (!packageName.isEmpty() ? (packageName + '.') : "") + name;








