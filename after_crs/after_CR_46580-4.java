/*Fix rename package refactoring

This changeset fixes a couple of bugs in the package rename
refactoring code, including
34466: Android refactoring participant gives NPE

It also fixes a bug in the move type refactoring, and adds
unit tests for package rename, move type, and rename type.

Change-Id:I4f43aabbcf1aeddc6c27011bfcffbe5a49c42372*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index f8971dc..752bbc6 100644

//Synthetic comment -- @@ -1743,7 +1743,7 @@
(List<ITargetChangeListener>)mTargetChangeListeners.clone();

Display display = AdtPlugin.getDisplay();
        if (display == null || display.isDisposed()) {
return;
}
display.asyncExec(new Runnable() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidPackageRenameParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidPackageRenameParticipant.java
//Synthetic comment -- index 24a1fb1..bd3224d 100644

//Synthetic comment -- @@ -43,16 +43,9 @@
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.refactoring.changes.RenamePackageChange;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
//Synthetic comment -- @@ -111,6 +104,7 @@
return null;
}
CompositeChange result = new CompositeChange(getName());
        result.markAsSynthetic();
if (mAndroidManifest.exists()) {
if (mAndroidElements.size() > 0 || mIsPackage) {
getManifestDocument();
//Synthetic comment -- @@ -232,37 +226,25 @@
mIsPackage = true;
}
mAndroidElements = addAndroidElements();

try {
final IType type = javaProject.findType(SdkConstants.CLASS_VIEW);
final HashSet<IType> elements = new HashSet<IType>();
                    if (type != null) {
                        ITypeHierarchy hierarchy = type.newTypeHierarchy(
                                new NullProgressMonitor());
                        IType[] allSubtypes = hierarchy.getAllSubtypes(type);
                        for (IType subType : allSubtypes) {
                            IResource resource = subType.getResource();
                            // TODO: Handle library project downstream dependencies!
                            if (resource != null && project.equals(resource.getProject())) {
                                if (subType.getPackageFragment().equals(mPackageFragment)) {
                                    elements.add(subType);
}
}
}
                    }

List<String> views = new ArrayList<String>();
for (IType elem : elements) {
views.add(elem.getFullyQualifiedName());
//Synthetic comment -- @@ -516,7 +498,11 @@
}
} else {
if (fullName != null) {
                                String currentPackage = mPackageFragment.getElementName();
                                if (fullName.lastIndexOf('.') == currentPackage.length()
                                        && fullName.startsWith(currentPackage)) {
                                    androidElements.put(element, value);
                                }
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeMoveParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeMoveParticipant.java
//Synthetic comment -- index 2186486..287139a 100644

//Synthetic comment -- @@ -196,6 +196,19 @@
if (destination instanceof IPackageFragment) {
IPackageFragment packageFragment = (IPackageFragment) destination;
mNewName = packageFragment.getElementName() + "." + type.getElementName();
            } else if (destination instanceof IResource) {
                try {
                    IPackageFragment[] fragments = javaProject.getPackageFragments();
                    for (IPackageFragment fragment : fragments) {
                        IResource resource = fragment.getResource();
                        if (resource.equals(destination)) {
                            mNewName = fragment.getElementName() + "." + type.getElementName();
                            break;
                        }
                    }
                } catch (JavaModelException e) {
                    // pass
                }
}
if (mOldName == null || mNewName == null) {
return false;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidPackageRenameParticipantTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidPackageRenameParticipantTest.java
new file mode 100644
//Synthetic comment -- index 0000000..ec9d324

//Synthetic comment -- @@ -0,0 +1,216 @@
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
package com.android.ide.eclipse.adt.internal.refactorings.core;

import com.android.annotations.NonNull;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenamePackageProcessor;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;

/**
 * TODO: Test renaming a DIFFERENT package than the application package!
 */
@SuppressWarnings({"javadoc", "restriction"})
public class AndroidPackageRenameParticipantTest extends RefactoringTestBase {
    public void testRefactor1() throws Exception {
        renamePackage(
                TEST_PROJECT,
                false /*renameSubpackages*/,
                true /*updateReferences*/,
                "my.pkg.name",

                "CHANGES:\n" +
                "-------\n" +
                "* Rename package 'com.example.refactoringtest' to 'my.pkg.name'\n" +
                "\n" +
                "* MainActivity.java - /testRefactor1/src/com/example/refactoringtest/MainActivity.java\n" +
                "  @@ -3 +3\n" +
                "  + import com.example.refactoringtest.R;\n" +
                "  +\n" +
                "\n" +
                "\n" +
                "* AndroidManifest.xml - /testRefactor1/AndroidManifest.xml\n" +
                "  @@ -16 +16\n" +
                "  -             android:name=\"com.example.refactoringtest.MainActivity\"\n" +
                "  +             android:name=\"my.pkg.name.MainActivity\"");
    }

    public void testRefactor1_noreferences() throws Exception {
        renamePackage(
                TEST_PROJECT,
                false /*renameSubpackages*/,
                false /*updateReferences*/,
                "my.pkg.name",

                "CHANGES:\n" +
                "-------\n" +
                "* Rename package 'com.example.refactoringtest' to 'my.pkg.name'");
    }

    public void testRefactor2() throws Exception {
        // Tests custom view handling
        renamePackage(
                TEST_PROJECT2,
                false /*renameSubpackages*/,
                true /*updateReferences*/,
                "my.pkg.name",

                "CHANGES:\n" +
                "-------\n" +
                "* Rename package 'com.example.refactoringtest' to 'my.pkg.name'\n" +
                "\n" +
                "* MainActivity.java - /testRefactor2/src/com/example/refactoringtest/MainActivity.java\n" +
                "  @@ -3 +3\n" +
                "  + import com.example.refactoringtest.R;\n" +
                "  +\n" +
                "\n" +
                "\n" +
                "* customviews.xml - /testRefactor2/res/layout/customviews.xml\n" +
                "  @@ -9 +9\n" +
                "  -     <com.example.refactoringtest.CustomView1\n" +
                "  +     <my.pkg.name.CustomView1\n" +
                "\n" +
                "\n" +
                "* customviews.xml - /testRefactor2/res/layout-land/customviews.xml\n" +
                "  @@ -9 +9\n" +
                "  -     <com.example.refactoringtest.CustomView1\n" +
                "  +     <my.pkg.name.CustomView1\n" +
                "\n" +
                "\n" +
                "* AndroidManifest.xml - /testRefactor2/AndroidManifest.xml\n" +
                "  @@ -16 +16\n" +
                "  -             android:name=\"com.example.refactoringtest.MainActivity\"\n" +
                "  +             android:name=\"my.pkg.name.MainActivity\"");
    }

    public void testRefactor2_renamesub() throws Exception {
        // Tests custom view handling
        renamePackage(
                TEST_PROJECT2,
                true /*renameSubpackages*/,
                true /*updateReferences*/,
                "my.pkg.name",

                "CHANGES:\n" +
                "-------\n" +
                "* Rename package 'com.example.refactoringtest' and subpackages to 'my.pkg.name'\n" +
                "\n" +
                "* MainActivity.java - /testRefactor2_renamesub/src/com/example/refactoringtest/MainActivity.java\n" +
                "  @@ -3 +3\n" +
                "  + import com.example.refactoringtest.R;\n" +
                "  +\n" +
                "\n" +
                "\n" +
                "* customviews.xml - /testRefactor2_renamesub/res/layout/customviews.xml\n" +
                "  @@ -9 +9\n" +
                "  -     <com.example.refactoringtest.CustomView1\n" +
                "  +     <my.pkg.name.CustomView1\n" +
                "\n" +
                "\n" +
                "* customviews.xml - /testRefactor2_renamesub/res/layout-land/customviews.xml\n" +
                "  @@ -9 +9\n" +
                "  -     <com.example.refactoringtest.CustomView1\n" +
                "  +     <my.pkg.name.CustomView1\n" +
                "\n" +
                "\n" +
                "* AndroidManifest.xml - /testRefactor2_renamesub/AndroidManifest.xml\n" +
                "  @@ -16 +16\n" +
                "  -             android:name=\"com.example.refactoringtest.MainActivity\"\n" +
                "  +             android:name=\"my.pkg.name.MainActivity\"\n" +
                "\n" +
                "\n" +
                "* customviews.xml - /testRefactor2_renamesub/res/layout/customviews.xml\n" +
                "  @@ -15 +15\n" +
                "  -     <com.example.refactoringtest.subpackage.CustomView2\n" +
                "  +     <my.pkg.name.subpackage.CustomView2\n" +
                "\n" +
                "\n" +
                "* customviews.xml - /testRefactor2_renamesub/res/layout-land/customviews.xml\n" +
                "  @@ -15 +15\n" +
                "  -     <com.example.refactoringtest.subpackage.CustomView2\n" +
                "  +     <my.pkg.name.subpackage.CustomView2");
    }

    public void testRefactor2_renamesub_norefs() throws Exception {
        // Tests custom view handling
        renamePackage(
                TEST_PROJECT2,
                true /*renameSubpackages*/,
                false /*updateReferences*/,
                "my.pkg.name",

                "CHANGES:\n" +
                "-------\n" +
                "* Rename package 'com.example.refactoringtest' and subpackages to 'my.pkg.name'");
    }


    // ---- Test infrastructure ----

    protected void renamePackage(
            @NonNull Object[] testData,
            boolean renameSubpackages,
            boolean updateReferences,
            @NonNull String newName,
            @NonNull String expected) throws Exception {
        IProject project = createProject(testData);
        renamePackage(project, renameSubpackages, updateReferences, newName, expected);
    }

    protected void renamePackage(
            @NonNull IProject project,
            boolean renameSubpackages,
            boolean updateReferences,
            @NonNull String newName,
            @NonNull String expected) throws Exception {
        ManifestInfo info = ManifestInfo.get(project);
        String currentPackage = info.getPackage();
        assertNotNull(currentPackage);

        IPackageFragment pkgFragment = getPackageFragment(project, currentPackage);
        RenamePackageProcessor processor = new RenamePackageProcessor(pkgFragment);
        processor.setNewElementName(newName);
        processor.setRenameSubpackages(renameSubpackages);
        processor.setUpdateReferences(updateReferences);
        assertNotNull(processor);

        RenameRefactoring refactoring = new RenameRefactoring(processor);
        checkRefactoring(refactoring, expected);
    }

    private static IPackageFragment getPackageFragment(IProject project, String pkg)
            throws CoreException, JavaModelException {
        IPackageFragment pkgFragment = null;
        IJavaProject javaProject = BaseProjectHelper.getJavaProject(project);
        assertNotNull(javaProject);
        IPackageFragment[] fragments = javaProject.getPackageFragments();
        for (IPackageFragment fragment : fragments) {
            String name = fragment.getElementName();
            if (pkg.equals(name)) {
                pkgFragment = fragment;
                break;
            }
        }
        return pkgFragment;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/RefactoringTestBase.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/RefactoringTestBase.java
//Synthetic comment -- index 3ae8535..93c3489 100644

//Synthetic comment -- @@ -15,8 +15,11 @@
*/
package com.android.ide.eclipse.adt.internal.refactorings.core;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.refactoring.AdtProjectTest;
import com.android.ide.eclipse.adt.internal.refactorings.changes.AndroidDocumentChange;
import com.android.ide.eclipse.adt.internal.refactorings.changes.AndroidLayoutChange;
import com.android.ide.eclipse.adt.internal.refactorings.changes.AndroidPackageRenameChange;
import com.android.ide.eclipse.adt.internal.refactorings.changes.AndroidTypeRenameChange;
//Synthetic comment -- @@ -29,6 +32,7 @@
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.internal.corext.refactoring.changes.RenameCompilationUnitChange;
import org.eclipse.jdt.internal.corext.refactoring.changes.RenamePackageChange;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
//Synthetic comment -- @@ -38,11 +42,16 @@
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.ltk.core.refactoring.resource.MoveResourceChange;
import org.eclipse.ltk.core.refactoring.resource.RenameResourceChange;
import org.eclipse.text.edits.TextEdit;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings({"javadoc","restriction"})
public abstract class RefactoringTestBase extends AdtProjectTest {
//Synthetic comment -- @@ -114,81 +123,54 @@
indent(sb, indent);
sb.append("* ");
sb.append(changeName);

            IFile file = getFile(change);
            if (file != null) {
sb.append(" - ");
                sb.append(file.getFullPath());
sb.append('\n');
} else {
sb.append('\n');
}

            if (change instanceof TextFileChange
                    || change instanceof AndroidPackageRenameChange
                    || change instanceof AndroidTypeRenameChange
                    || change instanceof AndroidLayoutChange) {
                assertNotNull(file);
                if (file != null) {
                    TextChange tc = (TextChange) change;
                    TextEdit edit = tc.getEdit();
                    byte[] bytes = ByteStreams.toByteArray(file.getContents());
                    String before = new String(bytes, Charsets.UTF_8);
                    IDocument document = new Document();
                    document.replace(0, 0, before);
                    // Make a copy: edits are sometimes destructive when run repeatedly!
                    edit.copy().apply(document);
                    String after = document.get();

                    String diff = getDiff(before, after);
                    for (String line : Splitter.on('\n').split(diff)) {
                        if (!line.trim().isEmpty()) {
                            indent(sb, indent + 1);
                            sb.append(line);
                        }
                        sb.append('\n');
}
}
            } else if (change instanceof RenameCompilationUnitChange) {
                // Change name, appended above, is adequate
} else if (change instanceof RenameResourceChange) {
// Change name, appended above, is adequate
} else if (change instanceof RenamePackageChange) {
// Change name, appended above, is adequate
            } else if (change instanceof MoveResourceChange) {
                // Change name, appended above, is adequate
} else if (change instanceof CompositeChange) {
// Don't print details about children here; they'll be nested below
} else {
indent(sb, indent);
                sb.append("<UNKNOWN CHANGE TYPE " + change.getClass().getName() + ">");
}
sb.append('\n');
}
//Synthetic comment -- @@ -196,12 +178,66 @@
if (change instanceof CompositeChange) {
CompositeChange composite = (CompositeChange) change;
Change[] children = composite.getChildren();
            List<Change> sorted = Arrays.asList(children);
            // Process children in a fixed (output-alphabetical) order to ensure stable output
            Collections.sort(sorted, new Comparator<Change>() {
                @Override
                public int compare(Change change1, Change change2) {
                    try {
                        IFile file1 = getFile(change1);
                        IFile file2 = getFile(change2);
                        if (file1 != null && file2 != null) {
                            // Sort in decreasing order. This places the most interesting
                            // files first: res > src > gen
                            int fileDelta = file2.getFullPath().toOSString().compareToIgnoreCase(
                                    file1.getFullPath().toOSString());
                            if (fileDelta != 0) {
                                return fileDelta;
                            }
                        }

                        int nameDelta = change2.getName().compareTo(change1.getName());
                        if (nameDelta != 0) {
                            return nameDelta;
                        }

                        // This is pretty inefficient but ensures stable output
                        return describe(change2).compareTo(describe(change1));
                    } catch (Exception e) {
                        fail(e.getLocalizedMessage());
                        return 0;
                    }
                }

            });
            for (Change child : sorted) {
describe(sb, child, indent + (composite.isSynthetic() ? 0 : 1));
}
}
}

    @Nullable
    private static IFile getFile(@NonNull Change change) {
        if (change instanceof TextFileChange) {
            TextFileChange tfc = (TextFileChange) change;
            return tfc.getFile();
        } else if (change instanceof AndroidPackageRenameChange) {
            AndroidPackageRenameChange aprc = (AndroidPackageRenameChange) change;
            return aprc.getManifest();
        } else if (change instanceof AndroidTypeRenameChange) {
            AndroidTypeRenameChange aprc = (AndroidTypeRenameChange) change;
            return aprc.getManifest();
        } else if (change instanceof AndroidLayoutChange) {
            AndroidLayoutChange alc = (AndroidLayoutChange) change;
            return alc.getFile();
        } else if (change instanceof AndroidDocumentChange) {
            AndroidDocumentChange atmc = (AndroidDocumentChange) change;
            return atmc.getManifest();
        }

        return null;
    }

protected static void indent(StringBuilder sb, int indent) {
for (int i = 0; i < indent; i++) {
sb.append("  ");
//Synthetic comment -- @@ -473,4 +509,100 @@
"res/values/styles.xml",   // file 3
SAMPLE_STYLES,
};

    // More test data

    protected static final String CUSTOM_VIEW_1 =
            "package com.example.refactoringtest;\n" +
            "\n" +
            "import android.content.Context;\n" +
            "import android.widget.Button;\n" +
            "\n" +
            "public class CustomView1 extends Button {\n" +
            "    public CustomView1(Context context) {\n" +
            "        super(context);\n" +
            "    }\n" +
            "}\n";

    protected static final String CUSTOM_VIEW_2 =
            "package com.example.refactoringtest.subpackage;\n" +
            "\n" +
            "import android.content.Context;\n" +
            "import android.widget.Button;\n" +
            "\n" +
            "public class CustomView2 extends Button {\n" +
            "    public CustomView2(Context context) {\n" +
            "        super(context);\n" +
            "    }\n" +
            "}\n";

    protected static final String CUSTOM_VIEW_LAYOUT =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
            "    xmlns:tools=\"http://schemas.android.com/tools\"\n" +
            "    android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"match_parent\"\n" +
            "    android:orientation=\"vertical\"\n" +
            "    tools:ignore=\"HardcodedText\" >\n" +
            "\n" +
            "    <com.example.refactoringtest.CustomView1\n" +
            "        android:id=\"@+id/customView1\"\n" +
            "        android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:text=\"CustomView1\" />\n" +
            "\n" +
            "    <com.example.refactoringtest.subpackage.CustomView2\n" +
            "        android:id=\"@+id/customView2\"\n" +
            "        android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:text=\"CustomView2\" />\n" +
            "\n" +
            "</LinearLayout>";

    protected static final Object[] TEST_PROJECT2 = new Object[] {
        "AndroidManifest.xml",
        SAMPLE_MANIFEST,

        "src/com/example/refactoringtest/MainActivity.java",
        SAMPLE_MAIN_ACTIVITY,

        "src/com/example/refactoringtest/CustomView1.java",
        CUSTOM_VIEW_1,

        "src/com/example/refactoringtest/subpackage/CustomView2.java",
        CUSTOM_VIEW_2,

        "gen/com/example/refactoringtest/R.java",
        SAMPLE_R,

        "res/drawable-xhdpi/ic_launcher.png",
        new byte[] { 0 },
        "res/drawable-hdpi/ic_launcher.png",
        new byte[] { 0 },
        "res/drawable-ldpi/ic_launcher.png",
        new byte[] { 0 },
        "res/drawable-mdpi/ic_launcher.png",
        new byte[] { 0 },

        "res/layout/activity_main.xml",
        SAMPLE_LAYOUT,

        "res/layout-land/activity_main.xml",
        SAMPLE_LAYOUT_2,

        "res/layout/customviews.xml",
        CUSTOM_VIEW_LAYOUT,

        "res/layout-land/customviews.xml",
        CUSTOM_VIEW_LAYOUT,

        "res/menu/activity_main.xml",
        SAMPLE_MENU,

        "res/values/strings.xml",   // file 3
        SAMPLE_STRINGS,

        "res/values/styles.xml",   // file 3
        SAMPLE_STYLES,
    };
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/RenameResourceParticipantTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/RenameResourceParticipantTest.java
//Synthetic comment -- index 90b49fb..b783f8d 100644

//Synthetic comment -- @@ -42,15 +42,6 @@

"CHANGES:\n" +
"-------\n" +
"* strings.xml - /testRefactor1/res/values/strings.xml\n" +
"  @@ -4 +4\n" +
"  -     <string name=\"app_name\">RefactoringTest</string>\n" +
//Synthetic comment -- @@ -60,7 +51,16 @@
"* R.java - /testRefactor1/gen/com/example/refactoringtest/R.java\n" +
"  @@ -29 +29\n" +
"  -         public static final int app_name=0x7f040000;\n" +
                "  +         public static final int myname=0x7f040000;\n" +
                "\n" +
                "\n" +
                "* AndroidManifest.xml - /testRefactor1/AndroidManifest.xml\n" +
                "  @@ -13 +13\n" +
                "  -         android:label=\"@string/app_name\"\n" +
                "  +         android:label=\"@string/myname\"\n" +
                "  @@ -17 +17\n" +
                "  -             android:label=\"@string/app_name\" >\n" +
                "  +             android:label=\"@string/myname\" >");
}

public void testRefactor2() throws Exception {
//Synthetic comment -- @@ -158,10 +158,6 @@

"CHANGES:\n" +
"-------\n" +
"* MainActivity.java - /testRefactor5/src/com/example/refactoringtest/MainActivity.java\n" +
"  @@ -13 +13\n" +
"  -         setContentView(R.layout.activity_main);\n" +
//Synthetic comment -- @@ -171,7 +167,12 @@
"* R.java - /testRefactor5/gen/com/example/refactoringtest/R.java\n" +
"  @@ -23 +23\n" +
"  -         public static final int activity_main=0x7f030000;\n" +
                "  +         public static final int newlayout=0x7f030000;\n" +
                "\n" +
                "\n" +
                "* Rename 'testRefactor5/res/layout/activity_main.xml' to 'newlayout.xml'\n" +
                "\n" +
                "* Rename 'testRefactor5/res/layout-land/activity_main.xml' to 'newlayout.xml'");
}

public void testRefactor6() throws Exception {
//Synthetic comment -- @@ -183,24 +184,24 @@

"CHANGES:\n" +
"-------\n" +
"* R.java - /testRefactor6/gen/com/example/refactoringtest/R.java\n" +
"  @@ -14 +14\n" +
"  -         public static final int ic_launcher=0x7f020000;\n" +
                "  +         public static final int newlauncher=0x7f020000;\n" +
                "\n" +
                "\n" +
                "* Rename 'testRefactor6/res/drawable-xhdpi/ic_launcher.png' to 'newlauncher.png'\n" +
                "\n" +
                "* Rename 'testRefactor6/res/drawable-mdpi/ic_launcher.png' to 'newlauncher.png'\n" +
                "\n" +
                "* Rename 'testRefactor6/res/drawable-ldpi/ic_launcher.png' to 'newlauncher.png'\n" +
                "\n" +
                "* Rename 'testRefactor6/res/drawable-hdpi/ic_launcher.png' to 'newlauncher.png'\n" +
                "\n" +
                "* AndroidManifest.xml - /testRefactor6/AndroidManifest.xml\n" +
                "  @@ -12 +12\n" +
                "  -         android:icon=\"@drawable/ic_launcher\"\n" +
                "  +         android:icon=\"@drawable/newlauncher\"");
}

public void testRefactor7() throws Exception {
//Synthetic comment -- @@ -215,10 +216,6 @@

"CHANGES:\n" +
"-------\n" +
"* MainActivity.java - /testRefactor7/src/com/example/refactoringtest/MainActivity.java\n" +
"  @@ -13 +13\n" +
"  -         setContentView(R.layout.activity_main);\n" +
//Synthetic comment -- @@ -228,7 +225,12 @@
"* R.java - /testRefactor7/gen/com/example/refactoringtest/R.java\n" +
"  @@ -23 +23\n" +
"  -         public static final int activity_main=0x7f030000;\n" +
                "  +         public static final int newlayout=0x7f030000;\n" +
                "\n" +
                "\n" +
                "* Rename 'testRefactor7/res/layout-land/activity_main.xml' to 'newlayout.xml'\n" +
                "\n" +
                "* Rename 'testRefactor7/res/layout/activity_main.xml' to 'newlayout.xml'");
}

public void testRefactor8() throws Exception {
//Synthetic comment -- @@ -255,6 +257,10 @@

"CHANGES:\n" +
"-------\n" +
                "* Rename 'testRefactor8/res/layout/activity_main.xml' to 'newlauncher.xml'\n" +
                "\n" +
                "* Rename 'testRefactor8/res/layout-land/activity_main.xml' to 'newlauncher.xml'\n" +
                "\n" +
"* MainActivity.java - /testRefactor8/src/com/example/refactoringtest/MainActivity.java\n" +
"  @@ -13 +13\n" +
"  -         setContentView(R.layout.activity_main);\n" +
//Synthetic comment -- @@ -264,12 +270,7 @@
"* R.java - /testRefactor8/gen/com/example/refactoringtest/R.java\n" +
"  @@ -23 +23\n" +
"  -         public static final int activity_main=0x7f030000;\n" +
                "  +         public static final int newlauncher=0x7f030000;");
}

public void testInvalidName() throws Exception {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeMoveParticipantTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeMoveParticipantTest.java
new file mode 100644
//Synthetic comment -- index 0000000..aeedf20

//Synthetic comment -- @@ -0,0 +1,118 @@
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
package com.android.ide.eclipse.adt.internal.refactorings.core;

import com.android.annotations.NonNull;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.corext.refactoring.reorg.IReorgPolicy.IMovePolicy;
import org.eclipse.jdt.internal.corext.refactoring.reorg.JavaMoveProcessor;
import org.eclipse.jdt.internal.corext.refactoring.reorg.ReorgDestinationFactory;
import org.eclipse.jdt.internal.corext.refactoring.reorg.ReorgPolicyFactory;
import org.eclipse.jdt.internal.ui.refactoring.reorg.CreateTargetQueries;
import org.eclipse.jdt.internal.ui.refactoring.reorg.ReorgQueries;
import org.eclipse.ltk.core.refactoring.participants.MoveRefactoring;
import org.eclipse.swt.widgets.Shell;


@SuppressWarnings({"javadoc", "restriction"})
public class AndroidTypeMoveParticipantTest extends RefactoringTestBase {
    public void testRefactor1() throws Exception {
        moveType(
                TEST_PROJECT2,
                "com.example.refactoringtest.CustomView1",
                "src/com/example/refactoringtest/subpackage",
                true /*updateReferences*/,

                "CHANGES:\n" +
                "-------\n" +
                "* Move resource 'testRefactor1/src/com/example/refactoringtest/CustomView1.java' to 'subpackage'\n" +
                "\n" +
                "* Move resource 'testRefactor1/src/com/example/refactoringtest/CustomView1.java' to 'subpackage'\n" +
                "\n" +
                "* Android Type Move\n" +
                "\n" +
                "  * customviews.xml - /testRefactor1/res/layout/customviews.xml\n" +
                "    @@ -9 +9\n" +
                "    -     <com.example.refactoringtest.CustomView1\n" +
                "    +     <com.example.refactoringtest.subpackage.CustomView1\n" +
                "\n" +
                "\n" +
                "  * customviews.xml - /testRefactor1/res/layout-land/customviews.xml\n" +
                "    @@ -9 +9\n" +
                "    -     <com.example.refactoringtest.CustomView1\n" +
                "    +     <com.example.refactoringtest.subpackage.CustomView1");
    }

    public void testRefactor1_norefs() throws Exception {
        moveType(
                TEST_PROJECT2,
                "com.example.refactoringtest.CustomView1",
                "src/com/example/refactoringtest/subpackage",
                false /*updateReferences*/,

                "CHANGES:\n" +
                "-------\n" +
                "* Move resource 'testRefactor1_norefs/src/com/example/refactoringtest/CustomView1.java' to 'subpackage'\n" +
                "\n" +
                "* Move resource 'testRefactor1_norefs/src/com/example/refactoringtest/CustomView1.java' to 'subpackage'");
    }

    // ---- Test infrastructure ----

    protected void moveType(
            @NonNull Object[] testData,
            @NonNull String typeFqcn,
            @NonNull String destination,
            boolean updateReferences,
            @NonNull String expected) throws Exception {
        IProject project = createProject(testData);

        IFolder destinationFolder = project.getFolder(destination);

        IJavaProject javaProject = BaseProjectHelper.getJavaProject(project);
        assertNotNull(javaProject);
        IType type = javaProject.findType(typeFqcn);
        assertNotNull(typeFqcn, type);
        assertTrue(typeFqcn, type.exists());
        IResource resource = type.getResource();
        assertNotNull(typeFqcn, resource);
        assertTrue(typeFqcn, resource.exists());

        IResource[] resources = new IResource[] { resource };
        IJavaElement[] elements = new IJavaElement[] { type };
        IMovePolicy policy = ReorgPolicyFactory.createMovePolicy(resources, elements);
        JavaMoveProcessor processor = new JavaMoveProcessor(policy);
        processor.setUpdateReferences(updateReferences);
        processor.setUpdateQualifiedNames(true);
        assertTrue(policy.canEnable());
        processor.setDestination(ReorgDestinationFactory.createDestination(destinationFolder));
        Shell parent = AdtPlugin.getShell();
        assertNotNull(parent);
        processor.setCreateTargetQueries(new CreateTargetQueries(parent));
        processor.setReorgQueries(new ReorgQueries(parent));

        MoveRefactoring refactoring = new MoveRefactoring(processor);
        checkRefactoring(refactoring, expected);
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeRenameParticipantTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeRenameParticipantTest.java
new file mode 100644
//Synthetic comment -- index 0000000..8ab0a11

//Synthetic comment -- @@ -0,0 +1,112 @@
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
package com.android.ide.eclipse.adt.internal.refactorings.core;

import com.android.annotations.NonNull;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameTypeProcessor;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;


@SuppressWarnings({"javadoc", "restriction"})
public class AndroidTypeRenameParticipantTest extends RefactoringTestBase {
    public void testRefactor1() throws Exception {
        renameType(
                TEST_PROJECT,
                "com.example.refactoringtest.MainActivity",
                true /*updateReferences*/,
                "NewActivityName",

                "CHANGES:\n" +
                "-------\n" +
                "* Rename compilation unit 'MainActivity.java' to 'NewActivityName.java'\n" +
                "\n" +
                "* Android Type Rename\n" +
                "\n" +
                "  * AndroidManifest.xml - /testRefactor1/AndroidManifest.xml\n" +
                "    @@ -16 +16\n" +
                "    -             android:name=\"com.example.refactoringtest.MainActivity\"\n" +
                "    +             android:name=\"com.example.refactoringtest.NewActivityName\"");
    }

    public void testRefactor1_noreferences() throws Exception {
        renameType(
                TEST_PROJECT,
                "com.example.refactoringtest.MainActivity",
                false /*updateReferences*/,
                "NewActivityName",

                "CHANGES:\n" +
                "-------\n" +
                "* Rename compilation unit 'MainActivity.java' to 'NewActivityName.java'");
    }

    public void testRefactor2() throws Exception {
        renameType(
                TEST_PROJECT2,
                "com.example.refactoringtest.CustomView1",
                true /*updateReferences*/,
                "NewCustomViewName",

                "CHANGES:\n" +
                "-------\n" +
                "* Rename compilation unit 'CustomView1.java' to 'NewCustomViewName.java'\n" +
                "\n" +
                "* Android Type Rename\n" +
                "\n" +
                "  * customviews.xml - /testRefactor2/res/layout/customviews.xml\n" +
                "    @@ -9 +9\n" +
                "    -     <com.example.refactoringtest.CustomView1\n" +
                "    +     <com.example.refactoringtest.NewCustomViewName\n" +
                "\n" +
                "\n" +
                "  * customviews.xml - /testRefactor2/res/layout-land/customviews.xml\n" +
                "    @@ -9 +9\n" +
                "    -     <com.example.refactoringtest.CustomView1\n" +
                "    +     <com.example.refactoringtest.NewCustomViewName");
    }

    // ---- Test infrastructure ----

    protected void renameType(
            @NonNull Object[] testData,
            @NonNull String typeFqcn,
            boolean updateReferences,
            @NonNull String newName,
            @NonNull String expected) throws Exception {
        IProject project = createProject(testData);
        IJavaProject javaProject = BaseProjectHelper.getJavaProject(project);
        assertNotNull(javaProject);
        IType type = javaProject.findType(typeFqcn);
        assertNotNull(typeFqcn, type);
        assertTrue(typeFqcn, type.exists());
        RenameTypeProcessor processor = new RenameTypeProcessor(type);
        processor.setNewElementName(newName);
        processor.setUpdateQualifiedNames(true);
        processor.setUpdateSimilarDeclarations(false);
        //processor.setMatchStrategy(?);
        //processor.setFilePatterns(patterns);
        processor.setUpdateReferences(updateReferences);
        assertNotNull(processor);

        RenameRefactoring refactoring = new RenameRefactoring(processor);
        checkRefactoring(refactoring, expected);
    }
}







