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








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidPackageRenameParticipantTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidPackageRenameParticipantTest.java
new file mode 100644
//Synthetic comment -- index 0000000..178e119

//Synthetic comment -- @@ -0,0 +1,289 @@







