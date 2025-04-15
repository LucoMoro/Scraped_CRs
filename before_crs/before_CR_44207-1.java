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
String region = domAttr.getValueRegionText();
int offset = domAttr.getValueRegionStartOffset();
if (region != null && region.length() >= 2) {
                return new ReplaceEdit(offset + 1, region.length() - 2, newValue);
}
}
return null;
//Synthetic comment -- @@ -275,11 +278,16 @@
name = attr.getValue();
}
if (name != null) {
            String newValue;
if (combinePackage) {
                newValue = AndroidManifest.extractActivityName(newName, getAppPackage());
            } else {
                newValue = newName;
}
if (newValue != null) {
TextEdit edit = createTextEdit(attr, newValue);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/changes/AndroidPackageRenameChange.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/changes/AndroidPackageRenameChange.java
//Synthetic comment -- index 63da563..08e4f01 100644

//Synthetic comment -- @@ -53,7 +53,7 @@
* @param isPackage is the application package
*/
public AndroidPackageRenameChange(IFile androidManifest, ITextFileBufferManager manager,
            IDocument document, Map<String, String> elements, String newName, String oldName,
boolean isPackage) {
super(document);
this.mDocument = document;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/changes/AndroidTypeMoveChange.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/changes/AndroidTypeMoveChange.java
//Synthetic comment -- index 7e5ea80..618500d 100644

//Synthetic comment -- @@ -38,8 +38,8 @@
* @param oldName the old name
*/
public AndroidTypeMoveChange(IFile androidManifest, ITextFileBufferManager manager,
            IDocument document, Map<String, String> elements, String newName, String oldName) {
        super(androidManifest, manager, document, elements, newName, oldName);
}

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/changes/AndroidTypeRenameChange.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/changes/AndroidTypeRenameChange.java
//Synthetic comment -- index a415c17..275d412 100644

//Synthetic comment -- @@ -49,12 +49,12 @@
* @param oldName the old name
*/
public AndroidTypeRenameChange(IFile androidManifest, ITextFileBufferManager manager,
            IDocument document, Map<String, String> elements, String newName, String oldName) {
super(document);
this.mDocument = document;
this.mElements = elements;
        this.mNewName = newName;
this.mOldName = oldName;
this.mManager = manager;
this.mAndroidManifest = androidManifest;
try {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidPackageRenameParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidPackageRenameParticipant.java
//Synthetic comment -- index 990a4bd..24a1fb1 100644

//Synthetic comment -- @@ -113,9 +113,9 @@
CompositeChange result = new CompositeChange(getName());
if (mAndroidManifest.exists()) {
if (mAndroidElements.size() > 0 || mIsPackage) {
                getDocument();
Change change = new AndroidPackageRenameChange(mAndroidManifest, mManager,
                        mDocument, mAndroidElements, mNewName, mOldName, mIsPackage);
if (change != null) {
result.add(change);
}
//Synthetic comment -- @@ -438,7 +438,7 @@

IDocument document;
try {
            document = getDocument();
} catch (CoreException e) {
RefactoringUtil.log(e);
if (mManager != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidRenameParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidRenameParticipant.java
//Synthetic comment -- index 22587d2..dca43a1 100644

//Synthetic comment -- @@ -59,15 +59,17 @@
}

/**
     * @return the document
* @throws CoreException
*/
    public IDocument getDocument() throws CoreException {
if (mDocument == null) {
mManager = FileBuffers.getTextFileBufferManager();
            mManager.connect(mAndroidManifest.getFullPath(), LocationKind.NORMALIZE,
new NullProgressMonitor());
            ITextFileBuffer buffer = mManager.getTextFileBuffer(mAndroidManifest.getFullPath(),
LocationKind.NORMALIZE);
mDocument = buffer.getDocument();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeRenameParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/core/AndroidTypeRenameParticipant.java
//Synthetic comment -- index 393b803..6605394 100644

//Synthetic comment -- @@ -64,12 +64,12 @@
/**
* A participant to participate in refactorings that rename a type in an Android project.
* The class updates android manifest and the layout file
 * The user can suppress refactoring by disabling the "Update references" checkbox
* <p>
* Rename participants are registered via the extension point <code>
* org.eclipse.ltk.core.refactoring.renameParticipants</code>.
 * Extensions to this extension point must therefore extend <code>org.eclipse.ltk.core.refactoring.participants.RenameParticipant</code>.
 * </p>
*/
@SuppressWarnings("restriction")
public class AndroidTypeRenameParticipant extends AndroidRenameParticipant {
//Synthetic comment -- @@ -89,9 +89,9 @@
CompositeChange result = new CompositeChange(getName());
if (mAndroidManifest.exists()) {
if (mAndroidElements.size() > 0) {
                getDocument();
Change change = new AndroidTypeRenameChange(mAndroidManifest, mManager, mDocument,
                        mAndroidElements, mNewName, mOldName);
if (change != null) {
result.add(change);
}
//Synthetic comment -- @@ -133,9 +133,10 @@

if (manifestResource == null || !manifestResource.exists()
|| !(manifestResource instanceof IFile)) {
                RefactoringUtil.logInfo("Invalid or missing the "
                        + SdkConstants.FN_ANDROID_MANIFEST_XML + " in the " + project.getName()
                        + " project.");
return false;
}
mAndroidManifest = (IFile) manifestResource;
//Synthetic comment -- @@ -240,8 +241,7 @@
if (model != null) {
IDOMModel xmlModel = (IDOMModel) model;
IDOMDocument xmlDoc = xmlModel.getDocument();
                    NodeList nodes = xmlDoc
                            .getElementsByTagName(SdkConstants.VIEW);
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
                    lManager.disconnect(file.getFullPath(), LocationKind.NORMALIZE,
new NullProgressMonitor());
} catch (CoreException ignore) {
}
//Synthetic comment -- @@ -299,12 +300,13 @@

IDocument document;
try {
            document = getDocument();
} catch (CoreException e) {
RefactoringUtil.log(e);
if (mManager != null) {
try {
                    mManager.disconnect(mAndroidManifest.getFullPath(), LocationKind.NORMALIZE,
new NullProgressMonitor());
} catch (CoreException e1) {
RefactoringUtil.log(e1);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/ApplicationPackageNameRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/ApplicationPackageNameRefactoring.java
//Synthetic comment -- index d95ef20..ec40a6e 100644

//Synthetic comment -- @@ -100,8 +100,8 @@
IMarker.PROBLEM,
true,
IResource.DEPTH_INFINITE) == IMarker.SEVERITY_ERROR) {
            return RefactoringStatus
            .createFatalErrorStatus("Fix the errors in your project, first.");
}

return new RefactoringStatus();







