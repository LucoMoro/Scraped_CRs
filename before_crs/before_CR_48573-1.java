/*Fix package renaming for Manifest and BuildConfig classes. DO NOT MERGE

Fixes 41685: Rename Package Breaks BuildConfig and Manifest.

It also cleans up the refactoring in that it now only adds
import R statements to classes that actually contain an R
reference.

Change-Id:Icdd7db2d6473eccd57a992ee406a0b0b527517a2*/
//Synthetic comment -- diff --git a/common/src/main/java/com/android/SdkConstants.java b/common/src/main/java/com/android/SdkConstants.java
//Synthetic comment -- index 48cecc1..a0fdc39 100644

//Synthetic comment -- @@ -914,8 +914,14 @@
public static final String FN_COMPILED_RESOURCE_CLASS = FN_RESOURCE_BASE + DOT_CLASS;
/** Resource text filename, i.e. "R.txt" */
public static final String FN_RESOURCE_TEXT = FN_RESOURCE_BASE + DOT_TXT;
/** Manifest java class filename, i.e. "Manifest.java" */
    public static final String FN_MANIFEST_CLASS = "Manifest.java"; //$NON-NLS-1$

public static final String DRAWABLE_FOLDER = "drawable";           //$NON-NLS-1$
public static final String DRAWABLE_XHDPI = "drawable-xhdpi";      //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/ApplicationPackageNameRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/ApplicationPackageNameRefactoring.java
//Synthetic comment -- index 872d18e..f6fa6ee 100644

//Synthetic comment -- @@ -16,6 +16,10 @@

package com.android.ide.eclipse.adt.internal.refactorings.renamepackage;

import com.android.SdkConstants;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -74,7 +78,6 @@
*/
@SuppressWarnings("restriction")
class ApplicationPackageNameRefactoring extends Refactoring {

private final IProject mProject;
private final Name mOldPackageName;
private final Name mNewPackageName;
//Synthetic comment -- @@ -146,18 +149,33 @@
if (cu.getPackage().getName().getFullyQualifiedName()
.equals(mOldPackageName.getFullyQualifiedName())) {

            ImportRewrite irw = ImportRewrite.create(cu, true);
            irw.addImport(mNewPackageName.getFullyQualifiedName() + '.'
                    + SdkConstants.FN_RESOURCE_BASE);

            try {
                rewrittenImports.addChild( irw.rewriteImports(null) );
            } catch (MalformedTreeException e) {
                Status s = new Status(Status.ERROR, AdtPlugin.PLUGIN_ID, e.getMessage(), e);
                AdtPlugin.getDefault().getLog().log(s);
            } catch (CoreException e) {
                Status s = new Status(Status.ERROR, AdtPlugin.PLUGIN_ID, e.getMessage(), e);
                AdtPlugin.getDefault().getLog().log(s);
}
}

//Synthetic comment -- @@ -478,7 +496,43 @@
}
}

    class ImportVisitor extends ASTVisitor {

final AST mAst;
final ASTRewrite mRewriter;
//Synthetic comment -- @@ -508,8 +562,19 @@
if (importName.isQualifiedName()) {
QualifiedName qualifiedImportName = (QualifiedName) importName;

                if (qualifiedImportName.getName().getIdentifier()
                        .equals(SdkConstants.FN_RESOURCE_BASE)) {
mRewriter.replace(qualifiedImportName.getQualifier(), mNewPackageName,
null);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/RefactoringTestBase.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/RefactoringTestBase.java
//Synthetic comment -- index dd0d68d..41838e2 100644

//Synthetic comment -- @@ -689,4 +689,69 @@
SAMPLE_MANIFEST,

};
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/ApplicationPackageNameRefactoringTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/ApplicationPackageNameRefactoringTest.java
//Synthetic comment -- index 438906d..0ac0344 100644

//Synthetic comment -- @@ -32,11 +32,6 @@

"CHANGES:\n" +
"-------\n" +
                "[x] MainActivity2.java - /testRefactor1/src/com/example/refactoringtest/MainActivity2.java\n" +
                "  @@ -7 +7\n" +
                "  + import my.pkg.name.R;\n" +
                "\n" +
                "\n" +
"[x] MainActivity.java - /testRefactor1/src/com/example/refactoringtest/MainActivity.java\n" +
"  @@ -7 +7\n" +
"  + import my.pkg.name.R;\n" +
//Synthetic comment -- @@ -59,22 +54,44 @@

"CHANGES:\n" +
"-------\n" +
                "[x] MyFragment.java - /testRefactor2/src/com/example/refactoringtest/MyFragment.java\n" +
                "  @@ -3 +3\n" +
                "  + import my.pkg.name.R;\n" +
                "\n" +
                "\n" +
"[x] MainActivity.java - /testRefactor2/src/com/example/refactoringtest/MainActivity.java\n" +
"  @@ -7 +7\n" +
"  + import my.pkg.name.R;\n" +
"\n" +
"\n" +
                "[x] CustomView1.java - /testRefactor2/src/com/example/refactoringtest/CustomView1.java\n" +
                "  @@ -5 +5\n" +
"  + import my.pkg.name.R;\n" +
"\n" +
"\n" +
                "[x] Make Manifest edits - /testRefactor2/AndroidManifest.xml\n" +
"  @@ -3 +3\n" +
"  -     package=\"com.example.refactoringtest\"\n" +
"  +     package=\"my.pkg.name\"\n" +







