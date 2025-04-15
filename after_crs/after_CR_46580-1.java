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







