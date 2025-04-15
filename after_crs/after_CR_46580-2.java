/*Fix rename package refactoring

This changeset fixes a couple of bugs in the package rename
refactoring code, including
34466: Android refactoring participant gives NPE

Change-Id:I4f43aabbcf1aeddc6c27011bfcffbe5a49c42372*/




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








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidPackageRenameParticipantTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidPackageRenameParticipantTest.java
new file mode 100644
//Synthetic comment -- index 0000000..178e119

//Synthetic comment -- @@ -0,0 +1,289 @@
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
                "* MainActivity.java - /testRefactor1/src/com/example/refactoringtest/MainActivity.java\n" +
                "  @@ -3 +3\n" +
                "  + import com.example.refactoringtest.R;\n" +
                "  +\n" +
                "\n" +
                "\n" +
                "* Rename package 'com.example.refactoringtest' to 'my.pkg.name'\n" +
                "\n" +
                "* AndroidManifest.xml\n" +
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
                "* MainActivity.java - /testRefactor2/src/com/example/refactoringtest/MainActivity.java\n" +
                "  @@ -3 +3\n" +
                "  + import com.example.refactoringtest.R;\n" +
                "  +\n" +
                "\n" +
                "\n" +
                "* Rename package 'com.example.refactoringtest' to 'my.pkg.name'\n" +
                "\n" +
                "* AndroidManifest.xml\n" +
                "  @@ -16 +16\n" +
                "  -             android:name=\"com.example.refactoringtest.MainActivity\"\n" +
                "  +             android:name=\"my.pkg.name.MainActivity\"\n" +
                "\n" +
                "\n" +
                "* customviews.xml\n" +
                "  @@ -9 +9\n" +
                "  -     <com.example.refactoringtest.CustomView1\n" +
                "  +     <my.pkg.name.CustomView1");
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
                "* MainActivity.java - /testRefactor2_renamesub/src/com/example/refactoringtest/MainActivity.java\n" +
                "  @@ -3 +3\n" +
                "  + import com.example.refactoringtest.R;\n" +
                "  +\n" +
                "\n" +
                "\n" +
                "* Rename package 'com.example.refactoringtest' and subpackages to 'my.pkg.name'\n" +
                "\n" +
                "* AndroidManifest.xml\n" +
                "  @@ -16 +16\n" +
                "  -             android:name=\"com.example.refactoringtest.MainActivity\"\n" +
                "  +             android:name=\"my.pkg.name.MainActivity\"\n" +
                "\n" +
                "\n" +
                "* customviews.xml\n" +
                "  @@ -9 +9\n" +
                "  -     <com.example.refactoringtest.CustomView1\n" +
                "  +     <my.pkg.name.CustomView1\n" +
                "\n" +
                "\n" +
                "* customviews.xml\n" +
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

        "res/menu/activity_main.xml",
        SAMPLE_MENU,

        "res/values/strings.xml",   // file 3
        SAMPLE_STRINGS,

        "res/values/styles.xml",   // file 3
        SAMPLE_STYLES,
    };
}







