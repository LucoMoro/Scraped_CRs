
//<Beginning of snippet n. 0>



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

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


* @param isPackage is the application package
*/
public AndroidPackageRenameChange(IFile androidManifest, ITextFileBufferManager manager,
            IDocument document, Map<String, String> elements, String oldName, String newName,
boolean isPackage) {
super(document);
this.mDocument = document;

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


* @param oldName the old name
*/
public AndroidTypeMoveChange(IFile androidManifest, ITextFileBufferManager manager,
            IDocument document, Map<String, String> elements, String oldName, String newName) {
        super(androidManifest, manager, document, elements, oldName, newName);
}

}

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


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

//<End of snippet n. 3>










//<Beginning of snippet n. 4>


CompositeChange result = new CompositeChange(getName());
if (mAndroidManifest.exists()) {
if (mAndroidElements.size() > 0 || mIsPackage) {
                getManifestDocument();
Change change = new AndroidPackageRenameChange(mAndroidManifest, mManager,
                        mDocument, mAndroidElements, mOldName, mNewName, mIsPackage);
if (change != null) {
result.add(change);
}

IDocument document;
try {
            document = getManifestDocument();
} catch (CoreException e) {
RefactoringUtil.log(e);
if (mManager != null) {

//<End of snippet n. 4>










//<Beginning of snippet n. 5>


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

//<End of snippet n. 5>










//<Beginning of snippet n. 6>


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
CompositeChange result = new CompositeChange(getName());
if (mAndroidManifest.exists()) {
if (mAndroidElements.size() > 0) {
                getManifestDocument();
Change change = new AndroidTypeRenameChange(mAndroidManifest, mManager, mDocument,
                        mAndroidElements, mOldName, mNewName);
if (change != null) {
result.add(change);
}

if (manifestResource == null || !manifestResource.exists()
|| !(manifestResource instanceof IFile)) {
                RefactoringUtil.logInfo(
                        String.format("Invalid or missing file %1$s in project %2$s",
                                SdkConstants.FN_ANDROID_MANIFEST_XML,
                                project.getName()));
return false;
}
mAndroidManifest = (IFile) manifestResource;
if (model != null) {
IDOMModel xmlModel = (IDOMModel) model;
IDOMDocument xmlDoc = xmlModel.getDocument();
                    NodeList nodes = xmlDoc.getElementsByTagName(SdkConstants.VIEW);
for (int i = 0; i < nodes.getLength(); i++) {
Node node = nodes.item(i);
NamedNodeMap attributes = node.getAttributes();
String value = attribute.getValue();
if (value != null && value.equals(className)) {
AndroidLayoutChangeDescription layoutChange =
                                       new AndroidLayoutChangeDescription(className, mLayoutNewName,
AndroidLayoutChangeDescription.VIEW_TYPE);
changes.add(layoutChange);
}
} finally {
if (lManager != null) {
try {
                    lManager.disconnect(file.getFullPath(),
                            LocationKind.NORMALIZE,
new NullProgressMonitor());
} catch (CoreException ignore) {
}

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

//<End of snippet n. 6>










//<Beginning of snippet n. 7>


IMarker.PROBLEM,
true,
IResource.DEPTH_INFINITE) == IMarker.SEVERITY_ERROR) {
            return
                RefactoringStatus.createFatalErrorStatus("Fix the errors in your project, first.");
}

return new RefactoringStatus();

//<End of snippet n. 7>








