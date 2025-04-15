/*ADT Refactoring: 2 minor fixes.

This fixes 2 minor issues in the "android type rename"
refactoring:
- the oldName vs newName was inverted in _some_ of the
  refactorings. It was still properly working due to
  pure luck.
- I've added an heuristic: if for example an <activity>
  uses the fully qualified name for its class name
  attribute, then the refactoring should not collapse
  it to the short notation (".MyClass"). Make it
  respect whether the developper choose to expand or
  not the class name.

This still does not fix SDK bug 21589.

Change-Id:I2f189508979710a6fab5c2f6db360c3776a5930e*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/changes/AndroidDocumentChange.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/changes/AndroidDocumentChange.java
//Synthetic comment -- index fe88b77..c0a9283 100644

//Synthetic comment -- @@ -230,10 +230,13 @@

if (attribute instanceof IDOMAttr) {
IDOMAttr domAttr = (IDOMAttr) attribute;
            // Get the current "region" (the current attribute, including its quotes.)
String region = domAttr.getValueRegionText();
int offset = domAttr.getValueRegionStartOffset();
if (region != null && region.length() >= 2) {
                // Skip the quotes when replacing.
                ReplaceEdit edit = new ReplaceEdit(offset + 1, region.length() - 2, newValue);
                return edit;
}
}
return null;
//Synthetic comment -- @@ -275,11 +278,16 @@
name = attr.getValue();
}
if (name != null) {
            String newValue = newName;
if (combinePackage) {
                String pkg = getAppPackage();
                if (oldName.startsWith(pkg) && newName.startsWith(pkg)) {
                    // Heuristic: if the old value is a fully qualified name, then
                    // assume that's how the user wants it in the manifest and
                    // do *not* shorten the new value. Keep it fully qualified too.
                } else {
                    newValue = AndroidManifest.extractActivityName(newName, pkg);
                }
}
if (newValue != null) {
TextEdit edit = createTextEdit(attr, newValue);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/changes/AndroidPackageRenameChange.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/changes/AndroidPackageRenameChange.java
//Synthetic comment -- index 63da563..08e4f01 100644

//Synthetic comment -- @@ -53,7 +53,7 @@
* @param isPackage is the application package
*/
public AndroidPackageRenameChange(IFile androidManifest, ITextFileBufferManager manager,
            IDocument document, Map<String, String> elements, String oldName, String newName,
boolean isPackage) {
super(document);
this.mDocument = document;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/changes/AndroidTypeMoveChange.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/changes/AndroidTypeMoveChange.java
//Synthetic comment -- index 7e5ea80..618500d 100644

//Synthetic comment -- @@ -38,8 +38,8 @@
* @param oldName the old name
*/
public AndroidTypeMoveChange(IFile androidManifest, ITextFileBufferManager manager,
            IDocument document, Map<String, String> elements, String oldName, String newName) {
        super(androidManifest, manager, document, elements, oldName, newName);
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/changes/AndroidTypeRenameChange.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/changes/AndroidTypeRenameChange.java
//Synthetic comment -- index a415c17..275d412 100644

//Synthetic comment -- @@ -49,12 +49,12 @@
* @param oldName the old name
*/
public AndroidTypeRenameChange(IFile androidManifest, ITextFileBufferManager manager,
            IDocument document, Map<String, String> elements, String oldName, String newName) {
super(document);
this.mDocument = document;
this.mElements = elements;
this.mOldName = oldName;
        this.mNewName = newName;
this.mManager = manager;
this.mAndroidManifest = androidManifest;
try {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidPackageRenameParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidPackageRenameParticipant.java
//Synthetic comment -- index 990a4bd..24a1fb1 100644

//Synthetic comment -- @@ -113,9 +113,9 @@
CompositeChange result = new CompositeChange(getName());
if (mAndroidManifest.exists()) {
if (mAndroidElements.size() > 0 || mIsPackage) {
                getManifestDocument();
Change change = new AndroidPackageRenameChange(mAndroidManifest, mManager,
                        mDocument, mAndroidElements, mOldName, mNewName, mIsPackage);
if (change != null) {
result.add(change);
}
//Synthetic comment -- @@ -438,7 +438,7 @@

IDocument document;
try {
            document = getManifestDocument();
} catch (CoreException e) {
RefactoringUtil.log(e);
if (mManager != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidRenameParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidRenameParticipant.java
//Synthetic comment -- index 22587d2..dca43a1 100644

//Synthetic comment -- @@ -59,15 +59,17 @@
}

/**
     * @return the document for the {@link #mAndroidManifest}
* @throws CoreException
*/
    public IDocument getManifestDocument() throws CoreException {
if (mDocument == null) {
mManager = FileBuffers.getTextFileBufferManager();
            mManager.connect(mAndroidManifest.getFullPath(),
                    LocationKind.NORMALIZE,
new NullProgressMonitor());
            ITextFileBuffer buffer = mManager.getTextFileBuffer(
                    mAndroidManifest.getFullPath(),
LocationKind.NORMALIZE);
mDocument = buffer.getDocument();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeRenameParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeRenameParticipant.java
//Synthetic comment -- index 393b803..6605394 100644

//Synthetic comment -- @@ -64,12 +64,12 @@
/**
* A participant to participate in refactorings that rename a type in an Android project.
* The class updates android manifest and the layout file
 * The user can suppress refactoring by disabling the "Update references" checkbox.
* <p>
* Rename participants are registered via the extension point <code>
* org.eclipse.ltk.core.refactoring.renameParticipants</code>.
 * Extensions to this extension point must therefore extend
 * <code>org.eclipse.ltk.core.refactoring.participants.RenameParticipant</code>.
*/
@SuppressWarnings("restriction")
public class AndroidTypeRenameParticipant extends AndroidRenameParticipant {
//Synthetic comment -- @@ -89,9 +89,9 @@
CompositeChange result = new CompositeChange(getName());
if (mAndroidManifest.exists()) {
if (mAndroidElements.size() > 0) {
                getManifestDocument();
Change change = new AndroidTypeRenameChange(mAndroidManifest, mManager, mDocument,
                        mAndroidElements, mOldName, mNewName);
if (change != null) {
result.add(change);
}
//Synthetic comment -- @@ -133,9 +133,10 @@

if (manifestResource == null || !manifestResource.exists()
|| !(manifestResource instanceof IFile)) {
                RefactoringUtil.logInfo(
                        String.format("Invalid or missing file %1$s in project %2$s",
                                SdkConstants.FN_ANDROID_MANIFEST_XML,
                                project.getName()));
return false;
}
mAndroidManifest = (IFile) manifestResource;
//Synthetic comment -- @@ -240,8 +241,7 @@
if (model != null) {
IDOMModel xmlModel = (IDOMModel) model;
IDOMDocument xmlDoc = xmlModel.getDocument();
                    NodeList nodes = xmlDoc.getElementsByTagName(SdkConstants.VIEW);
for (int i = 0; i < nodes.getLength(); i++) {
Node node = nodes.item(i);
NamedNodeMap attributes = node.getAttributes();
//Synthetic comment -- @@ -253,7 +253,7 @@
String value = attribute.getValue();
if (value != null && value.equals(className)) {
AndroidLayoutChangeDescription layoutChange =
                                       new AndroidLayoutChangeDescription(className, mLayoutNewName,
AndroidLayoutChangeDescription.VIEW_TYPE);
changes.add(layoutChange);
}
//Synthetic comment -- @@ -278,7 +278,8 @@
} finally {
if (lManager != null) {
try {
                    lManager.disconnect(file.getFullPath(),
                            LocationKind.NORMALIZE,
new NullProgressMonitor());
} catch (CoreException ignore) {
}
//Synthetic comment -- @@ -299,12 +300,13 @@

IDocument document;
try {
            document = getManifestDocument();
} catch (CoreException e) {
RefactoringUtil.log(e);
if (mManager != null) {
try {
                    mManager.disconnect(mAndroidManifest.getFullPath(),
                            LocationKind.NORMALIZE,
new NullProgressMonitor());
} catch (CoreException e1) {
RefactoringUtil.log(e1);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/ApplicationPackageNameRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/ApplicationPackageNameRefactoring.java
//Synthetic comment -- index d95ef20..ec40a6e 100644

//Synthetic comment -- @@ -100,8 +100,8 @@
IMarker.PROBLEM,
true,
IResource.DEPTH_INFINITE) == IMarker.SEVERITY_ERROR) {
            return
                RefactoringStatus.createFatalErrorStatus("Fix the errors in your project, first.");
}

return new RefactoringStatus();







