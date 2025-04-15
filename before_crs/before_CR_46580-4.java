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
        if (display == null) {
return;
}
display.asyncExec(new Runnable() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidPackageRenameParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidPackageRenameParticipant.java
//Synthetic comment -- index 24a1fb1..bd3224d 100644

//Synthetic comment -- @@ -43,16 +43,9 @@
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jdt.internal.corext.refactoring.changes.RenamePackageChange;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
//Synthetic comment -- @@ -111,6 +104,7 @@
return null;
}
CompositeChange result = new CompositeChange(getName());
if (mAndroidManifest.exists()) {
if (mAndroidElements.size() > 0 || mIsPackage) {
getManifestDocument();
//Synthetic comment -- @@ -232,37 +226,25 @@
mIsPackage = true;
}
mAndroidElements = addAndroidElements();
try {
final IType type = javaProject.findType(SdkConstants.CLASS_VIEW);
                    SearchPattern pattern = SearchPattern.createPattern("*",
                            IJavaSearchConstants.TYPE, IJavaSearchConstants.DECLARATIONS,
                            SearchPattern.R_REGEXP_MATCH);
                    IJavaSearchScope scope =SearchEngine.createJavaSearchScope(
                            new IJavaElement[] { mPackageFragment });
final HashSet<IType> elements = new HashSet<IType>();
                    SearchRequestor requestor = new SearchRequestor() {

                        @Override
                        public void acceptSearchMatch(SearchMatch match) throws CoreException {
                            Object elem = match.getElement();
                            if (elem instanceof IType) {
                                IType eType = (IType) elem;
                                IType[] superTypes = JavaModelUtil.getAllSuperTypes(eType,
                                        new NullProgressMonitor());
                                for (int i = 0; i < superTypes.length; i++) {
                                    if (superTypes[i].equals(type)) {
                                        elements.add(eType);
                                        break;
                                    }
}
}

}
                    };
                    SearchEngine searchEngine = new SearchEngine();
                    searchEngine.search(pattern, new SearchParticipant[] {
                        SearchEngine.getDefaultSearchParticipant()
                    }, scope, requestor, null);
List<String> views = new ArrayList<String>();
for (IType elem : elements) {
views.add(elem.getFullyQualifiedName());
//Synthetic comment -- @@ -516,7 +498,11 @@
}
} else {
if (fullName != null) {
                                androidElements.put(element, value);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeMoveParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeMoveParticipant.java
//Synthetic comment -- index 2186486..287139a 100644

//Synthetic comment -- @@ -196,6 +196,19 @@
if (destination instanceof IPackageFragment) {
IPackageFragment packageFragment = (IPackageFragment) destination;
mNewName = packageFragment.getElementName() + "." + type.getElementName();
}
if (mOldName == null || mNewName == null) {
return false;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidPackageRenameParticipantTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidPackageRenameParticipantTest.java
new file mode 100644
//Synthetic comment -- index 0000000..ec9d324

//Synthetic comment -- @@ -0,0 +1,216 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/RefactoringTestBase.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/RefactoringTestBase.java
//Synthetic comment -- index 3ae8535..93c3489 100644

//Synthetic comment -- @@ -15,8 +15,11 @@
*/
package com.android.ide.eclipse.adt.internal.refactorings.core;

import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.refactoring.AdtProjectTest;
import com.android.ide.eclipse.adt.internal.refactorings.changes.AndroidLayoutChange;
import com.android.ide.eclipse.adt.internal.refactorings.changes.AndroidPackageRenameChange;
import com.android.ide.eclipse.adt.internal.refactorings.changes.AndroidTypeRenameChange;
//Synthetic comment -- @@ -29,6 +32,7 @@
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.internal.corext.refactoring.changes.RenamePackageChange;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
//Synthetic comment -- @@ -38,11 +42,16 @@
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.ltk.core.refactoring.resource.RenameResourceChange;
import org.eclipse.text.edits.TextEdit;

import java.io.File;
import java.io.IOException;

@SuppressWarnings({"javadoc","restriction"})
public abstract class RefactoringTestBase extends AdtProjectTest {
//Synthetic comment -- @@ -114,81 +123,54 @@
indent(sb, indent);
sb.append("* ");
sb.append(changeName);
            if (change instanceof TextFileChange) {
                TextFileChange tfc = (TextFileChange) change;
sb.append(" - ");
                sb.append(tfc.getFile().getFullPath());
sb.append('\n');
} else {
sb.append('\n');
}
            if (change instanceof TextFileChange
                    || change instanceof AndroidLayoutChange
                    || change instanceof AndroidTypeRenameChange
                    || change instanceof AndroidPackageRenameChange) {
                TextChange tc = (TextChange) change;
                TextEdit edit = tc.getEdit();
                IFile file = null;
                if (change instanceof TextFileChange) {
                    TextFileChange tfc = (TextFileChange) change;
                    file = tfc.getFile();
                } else if (change instanceof AndroidPackageRenameChange) {
                    AndroidPackageRenameChange aprc = (AndroidPackageRenameChange) change;
                    file = aprc.getManifest();
                } else if (change instanceof AndroidTypeRenameChange) {
                    AndroidTypeRenameChange aprc = (AndroidTypeRenameChange) change;
                    file = aprc.getManifest();
                } else {
                    assert change instanceof AndroidLayoutChange;
                    AndroidLayoutChange alc = (AndroidLayoutChange) change;
                    file = alc.getFile();
                }
                byte[] bytes = ByteStreams.toByteArray(file.getContents());
                String before = new String(bytes, Charsets.UTF_8);
                IDocument document = new Document();
                document.replace(0, 0, before);
                edit.apply(document);
                String after = document.get();

                String diff = getDiff(before, after);
                for (String line : Splitter.on('\n').split(diff)) {
                    if (!line.trim().isEmpty()) {
                        indent(sb, indent + 1);
                        sb.append(line);
}
                    sb.append('\n');
}
} else if (change instanceof RenameResourceChange) {
// Change name, appended above, is adequate
} else if (change instanceof RenamePackageChange) {
// Change name, appended above, is adequate
            } else if (change instanceof AndroidPackageRenameChange) {
                AndroidPackageRenameChange aprc = (AndroidPackageRenameChange) change;
                aprc.getEdit();
            } else if (change instanceof AndroidLayoutChange) {
                AndroidLayoutChange tfc = (AndroidLayoutChange) change;
                TextEdit edit = tfc.getEdit();
                tfc.getModifiedElement();
                IFile file = tfc.getFile();
                byte[] bytes = ByteStreams.toByteArray(file.getContents());
                String before = new String(bytes, Charsets.UTF_8);
                IDocument document = new Document();
                document.replace(0, 0, before);
                edit.apply(document);
                String after = document.get();
                String diff = getDiff(before, after);
                for (String line : Splitter.on('\n').split(diff)) {
                    if (!line.trim().isEmpty()) {
                        indent(sb, indent + 1);
                        sb.append(line);
                    }
                    sb.append('\n');
                }
} else if (change instanceof CompositeChange) {
// Don't print details about children here; they'll be nested below
} else {
indent(sb, indent);
                sb.append("<unknown change type " + change.getClass().getName() + ">");
}
sb.append('\n');
}
//Synthetic comment -- @@ -196,12 +178,66 @@
if (change instanceof CompositeChange) {
CompositeChange composite = (CompositeChange) change;
Change[] children = composite.getChildren();
            for (Change child : children) {
describe(sb, child, indent + (composite.isSynthetic() ? 0 : 1));
}
}
}

protected static void indent(StringBuilder sb, int indent) {
for (int i = 0; i < indent; i++) {
sb.append("  ");
//Synthetic comment -- @@ -473,4 +509,100 @@
"res/values/styles.xml",   // file 3
SAMPLE_STYLES,
};
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/RenameResourceParticipantTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/RenameResourceParticipantTest.java
//Synthetic comment -- index 90b49fb..b783f8d 100644

//Synthetic comment -- @@ -42,15 +42,6 @@

"CHANGES:\n" +
"-------\n" +
                "* AndroidManifest.xml - /testRefactor1/AndroidManifest.xml\n" +
                "  @@ -13 +13\n" +
                "  -         android:label=\"@string/app_name\"\n" +
                "  +         android:label=\"@string/myname\"\n" +
                "  @@ -17 +17\n" +
                "  -             android:label=\"@string/app_name\" >\n" +
                "  +             android:label=\"@string/myname\" >\n" +
                "\n" +
                "\n" +
"* strings.xml - /testRefactor1/res/values/strings.xml\n" +
"  @@ -4 +4\n" +
"  -     <string name=\"app_name\">RefactoringTest</string>\n" +
//Synthetic comment -- @@ -60,7 +51,16 @@
"* R.java - /testRefactor1/gen/com/example/refactoringtest/R.java\n" +
"  @@ -29 +29\n" +
"  -         public static final int app_name=0x7f040000;\n" +
                "  +         public static final int myname=0x7f040000;");
}

public void testRefactor2() throws Exception {
//Synthetic comment -- @@ -158,10 +158,6 @@

"CHANGES:\n" +
"-------\n" +
                "* Rename 'testRefactor5/res/layout/activity_main.xml' to 'newlayout.xml'\n" +
                "\n" +
                "* Rename 'testRefactor5/res/layout-land/activity_main.xml' to 'newlayout.xml'\n" +
                "\n" +
"* MainActivity.java - /testRefactor5/src/com/example/refactoringtest/MainActivity.java\n" +
"  @@ -13 +13\n" +
"  -         setContentView(R.layout.activity_main);\n" +
//Synthetic comment -- @@ -171,7 +167,12 @@
"* R.java - /testRefactor5/gen/com/example/refactoringtest/R.java\n" +
"  @@ -23 +23\n" +
"  -         public static final int activity_main=0x7f030000;\n" +
                "  +         public static final int newlayout=0x7f030000;");
}

public void testRefactor6() throws Exception {
//Synthetic comment -- @@ -183,24 +184,24 @@

"CHANGES:\n" +
"-------\n" +
                "* AndroidManifest.xml - /testRefactor6/AndroidManifest.xml\n" +
                "  @@ -12 +12\n" +
                "  -         android:icon=\"@drawable/ic_launcher\"\n" +
                "  +         android:icon=\"@drawable/newlauncher\"\n" +
                "\n" +
                "\n" +
                "* Rename 'testRefactor6/res/drawable-hdpi/ic_launcher.png' to 'newlauncher.png'\n" +
                "\n" +
                "* Rename 'testRefactor6/res/drawable-ldpi/ic_launcher.png' to 'newlauncher.png'\n" +
                "\n" +
                "* Rename 'testRefactor6/res/drawable-mdpi/ic_launcher.png' to 'newlauncher.png'\n" +
                "\n" +
                "* Rename 'testRefactor6/res/drawable-xhdpi/ic_launcher.png' to 'newlauncher.png'\n" +
                "\n" +
"* R.java - /testRefactor6/gen/com/example/refactoringtest/R.java\n" +
"  @@ -14 +14\n" +
"  -         public static final int ic_launcher=0x7f020000;\n" +
                "  +         public static final int newlauncher=0x7f020000;");
}

public void testRefactor7() throws Exception {
//Synthetic comment -- @@ -215,10 +216,6 @@

"CHANGES:\n" +
"-------\n" +
                "* Rename 'testRefactor7/res/layout/activity_main.xml' to 'newlayout.xml'\n" +
                "\n" +
                "* Rename 'testRefactor7/res/layout-land/activity_main.xml' to 'newlayout.xml'\n" +
                "\n" +
"* MainActivity.java - /testRefactor7/src/com/example/refactoringtest/MainActivity.java\n" +
"  @@ -13 +13\n" +
"  -         setContentView(R.layout.activity_main);\n" +
//Synthetic comment -- @@ -228,7 +225,12 @@
"* R.java - /testRefactor7/gen/com/example/refactoringtest/R.java\n" +
"  @@ -23 +23\n" +
"  -         public static final int activity_main=0x7f030000;\n" +
                "  +         public static final int newlayout=0x7f030000;");
}

public void testRefactor8() throws Exception {
//Synthetic comment -- @@ -255,6 +257,10 @@

"CHANGES:\n" +
"-------\n" +
"* MainActivity.java - /testRefactor8/src/com/example/refactoringtest/MainActivity.java\n" +
"  @@ -13 +13\n" +
"  -         setContentView(R.layout.activity_main);\n" +
//Synthetic comment -- @@ -264,12 +270,7 @@
"* R.java - /testRefactor8/gen/com/example/refactoringtest/R.java\n" +
"  @@ -23 +23\n" +
"  -         public static final int activity_main=0x7f030000;\n" +
                "  +         public static final int newlauncher=0x7f030000;\n" +
                "\n" +
                "\n" +
                "* Rename 'testRefactor8/res/layout/activity_main.xml' to 'newlauncher.xml'\n" +
                "\n" +
                "* Rename 'testRefactor8/res/layout-land/activity_main.xml' to 'newlauncher.xml'");
}

public void testInvalidName() throws Exception {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeMoveParticipantTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeMoveParticipantTest.java
new file mode 100644
//Synthetic comment -- index 0000000..aeedf20

//Synthetic comment -- @@ -0,0 +1,118 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeRenameParticipantTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeRenameParticipantTest.java
new file mode 100644
//Synthetic comment -- index 0000000..8ab0a11

//Synthetic comment -- @@ -0,0 +1,112 @@







