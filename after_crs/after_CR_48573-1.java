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
    /** Generated manifest class name */
    public static final String FN_MANIFEST_BASE = "Manifest";          //$NON-NLS-1$
    /** Generated BuildConfig class name */
    public static final String FN_BUILD_CONFIG_BASE = "BuildConfig";   //$NON-NLS-1$
/** Manifest java class filename, i.e. "Manifest.java" */
    public static final String FN_MANIFEST_CLASS = FN_MANIFEST_BASE + DOT_JAVA;
    /** BuildConfig java class filename, i.e. "BuildConfig.java" */
    public static final String FN_BUILD_CONFIG = FN_BUILD_CONFIG_BASE + DOT_JAVA;

public static final String DRAWABLE_FOLDER = "drawable";           //$NON-NLS-1$
public static final String DRAWABLE_XHDPI = "drawable-xhdpi";      //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/ApplicationPackageNameRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/ApplicationPackageNameRefactoring.java
//Synthetic comment -- index 872d18e..f6fa6ee 100644

//Synthetic comment -- @@ -16,6 +16,10 @@

package com.android.ide.eclipse.adt.internal.refactorings.renamepackage;

import static com.android.SdkConstants.FN_BUILD_CONFIG_BASE;
import static com.android.SdkConstants.FN_MANIFEST_BASE;
import static com.android.SdkConstants.FN_RESOURCE_BASE;

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

            UsageVisitor usageVisitor = new UsageVisitor();
            cu.accept(usageVisitor);

            if (usageVisitor.seenAny()) {
                ImportRewrite irw = ImportRewrite.create(cu, true);
                if (usageVisitor.hasSeenR()) {
                    irw.addImport(mNewPackageName.getFullyQualifiedName() + '.'
                            + FN_RESOURCE_BASE);
                }
                if (usageVisitor.hasSeenBuildConfig()) {
                    irw.addImport(mNewPackageName.getFullyQualifiedName() + '.'
                            + FN_BUILD_CONFIG_BASE);
                }
                if (usageVisitor.hasSeenManifest()) {
                    irw.addImport(mNewPackageName.getFullyQualifiedName() + '.'
                            + FN_MANIFEST_BASE);
                }

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
}

//Synthetic comment -- @@ -478,7 +496,43 @@
}
}

    private static class UsageVisitor extends ASTVisitor {
        private boolean mSeenManifest;
        private boolean mSeenR;
        private boolean mSeenBuildConfig;

        @Override
        public boolean visit(QualifiedName node) {
            Name qualifier = node.getQualifier();
            if (qualifier.isSimpleName()) {
                String name = qualifier.toString();
                if (name.equals(FN_RESOURCE_BASE)) {
                    mSeenR = true;
                } else if (name.equals(FN_BUILD_CONFIG_BASE)) {
                    mSeenBuildConfig = true;
                } else if (name.equals(FN_MANIFEST_BASE)) {
                    mSeenManifest = true;
                }
            }
            return super.visit(node);
        };

        public boolean seenAny() {
            return mSeenR || mSeenBuildConfig || mSeenManifest;
        }

        public boolean hasSeenBuildConfig() {
            return mSeenBuildConfig;
        }
        public boolean hasSeenManifest() {
            return mSeenManifest;
        }
        public boolean hasSeenR() {
            return mSeenR;
        }
    }

    private class ImportVisitor extends ASTVisitor {

final AST mAst;
final ASTRewrite mRewriter;
//Synthetic comment -- @@ -508,8 +562,19 @@
if (importName.isQualifiedName()) {
QualifiedName qualifiedImportName = (QualifiedName) importName;

                String identifier = qualifiedImportName.getName().getIdentifier();
                if (identifier.equals(FN_RESOURCE_BASE)) {
                    mRewriter.replace(qualifiedImportName.getQualifier(), mNewPackageName,
                            null);
                } else if (identifier.equals(FN_BUILD_CONFIG_BASE)
                        && mOldPackageName.toString().equals(
                                qualifiedImportName.getQualifier().toString())) {
                    mRewriter.replace(qualifiedImportName.getQualifier(), mNewPackageName,
                            null);

                } else if (identifier.equals(FN_MANIFEST_BASE)
                        && mOldPackageName.toString().equals(
                                qualifiedImportName.getQualifier().toString())) {
mRewriter.replace(qualifiedImportName.getQualifier(), mNewPackageName,
null);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/RefactoringTestBase.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/RefactoringTestBase.java
//Synthetic comment -- index dd0d68d..41838e2 100644

//Synthetic comment -- @@ -689,4 +689,69 @@
SAMPLE_MANIFEST,

};


    protected static final String MANIFEST =
            "/* AUTO-GENERATED FILE.  DO NOT MODIFY.\n" +
            " *\n" +
            " * This class was automatically generated by the\n" +
            " * aapt tool from the resource data it found.  It\n" +
            " * should not be modified by hand.\n" +
            " */\n" +
            "\n" +
            "package com.example.refactoringtest;\n" +
            "\n" +
            "public final class Manifest {\n" +
            "    public static final class permission {\n" +
            "        public static final String WRITE_SCHEDULE=\"com.example.refactoringtest.permission.WRITE_SCHEDULE\";\n" +
            "    }\n" +
            "}";

    protected static final String BUILD_CONFIG =
            "/** Automatically generated file. DO NOT MODIFY */\n" +
            "package com.example.refactoringtest;\n" +
            "\n" +
            "public final class BuildConfig {\n" +
            "    public final static boolean DEBUG = true;\n" +
            "}";

    protected static final String MORE_CODE_JAVA =
            "package com.example.refactoringtest.subpkg;\n" +
            "\n" +
            "import android.os.Bundle;\n" +
            "import android.app.Activity;\n" +
            "import android.view.Menu;\n" +
            "import android.view.View;\n" +
            "import com.example.refactoringtest.BuildConfig;\n" +
            "import com.example.refactoringtest.Manifest;\n" +
            "import com.example.refactoringtest.R;\n" +
            "\n" +
            "public class MoreCode extends Activity {\n" +
            "\n" +
            "    protected void code() {\n" +
            "        if (BuildConfig.DEBUG) {\n" +
            "            System.out.println(Manifest.permission);\n" +
            "        }" +
            "        System.out.println(com.example.refactoringtest.BuildConfig.DEBUG);\n" +
            "    }\n" +
            "\n" +
            "}\n";

    /** Project which includes references to BuildConfig, Manifest, and R */
    protected static final Object[] TEST_PROJECT3;
    static {
        Object[] additional = new Object[] {
                "src/com/example/refactoringtest/subpkg/MoreCode.java",
                MORE_CODE_JAVA,

                "gen/com/example/refactoringtest/BuildConfig.java",
                BUILD_CONFIG,

                "gen/com/example/refactoringtest/Manifest.java",
                MANIFEST,
        };
        TEST_PROJECT3 = new Object[TEST_PROJECT2.length + additional.length];
        System.arraycopy(TEST_PROJECT2, 0, TEST_PROJECT3, 0, TEST_PROJECT2.length);
        System.arraycopy(additional, 0, TEST_PROJECT3, TEST_PROJECT2.length, additional.length);
    };
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/ApplicationPackageNameRefactoringTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/ApplicationPackageNameRefactoringTest.java
//Synthetic comment -- index 438906d..0ac0344 100644

//Synthetic comment -- @@ -32,11 +32,6 @@

"CHANGES:\n" +
"-------\n" +
"[x] MainActivity.java - /testRefactor1/src/com/example/refactoringtest/MainActivity.java\n" +
"  @@ -7 +7\n" +
"  + import my.pkg.name.R;\n" +
//Synthetic comment -- @@ -59,22 +54,44 @@

"CHANGES:\n" +
"-------\n" +
"[x] MainActivity.java - /testRefactor2/src/com/example/refactoringtest/MainActivity.java\n" +
"  @@ -7 +7\n" +
"  + import my.pkg.name.R;\n" +
"\n" +
"\n" +
                "[x] Make Manifest edits - /testRefactor2/AndroidManifest.xml\n" +
                "  @@ -3 +3\n" +
                "  -     package=\"com.example.refactoringtest\"\n" +
                "  +     package=\"my.pkg.name\"\n" +
                "  @@ -25 +25\n" +
                "  -             android:name=\".MainActivity2\"\n" +
                "  +             android:name=\"com.example.refactoringtest.MainActivity2\"");
    }

    public void testRefactor3() throws Exception {
        // Tests BuildConfig imports and updates
        renamePackage(
                TEST_PROJECT3,
                "my.pkg.name",

                "CHANGES:\n" +
                "-------\n" +
                "[x] MoreCode.java - /testRefactor3/src/com/example/refactoringtest/subpkg/MoreCode.java\n" +
                "  @@ -7 +7\n" +
                "  - import com.example.refactoringtest.BuildConfig;\n" +
                "  - import com.example.refactoringtest.Manifest;\n" +
                "  - import com.example.refactoringtest.R;\n" +
                "  + import my.pkg.name.BuildConfig;\n" +
                "  + import my.pkg.name.Manifest;\n" +
"  + import my.pkg.name.R;\n" +
"\n" +
"\n" +
                "[x] MainActivity.java - /testRefactor3/src/com/example/refactoringtest/MainActivity.java\n" +
                "  @@ -7 +7\n" +
                "  + import my.pkg.name.R;\n" +
                "\n" +
                "\n" +
                "[x] Make Manifest edits - /testRefactor3/AndroidManifest.xml\n" +
"  @@ -3 +3\n" +
"  -     package=\"com.example.refactoringtest\"\n" +
"  +     package=\"my.pkg.name\"\n" +







