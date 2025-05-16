//<Beginning of snippet n. 0>
if (attribute instanceof IDOMAttr) {
    IDOMAttr domAttr = (IDOMAttr) attribute;
    String region = domAttr.getValueRegionText();
    int offset = domAttr.getValueRegionStartOffset();
    if (region != null && region.length() >= 2) {
        return new ReplaceEdit(offset + 1, region.length() - 2, newValue);
    }
}
String name = attr.getValue();
if (name != null) {
    String newValue;
    if (combinePackage) {
        newValue = AndroidManifest.extractActivityName(newName, getAppPackage());
    } else {
        newValue = newName;
    }
    if (newValue != null) {
        TextEdit edit = createTextEdit(attr, newValue);
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public AndroidPackageRenameChange(IFile androidManifest, ITextFileBufferManager manager,
            IDocument document, Map<String, String> elements, String newName, String oldName,
            boolean isPackage) {
    super(document);
    this.mDocument = document;
    this.mElements = elements;
    this.mNewName = newName;
    this.mOldName = oldName;
    this.mIsPackage = isPackage;
    if (oldName == null || newName == null) {
        throw new IllegalArgumentException("oldName and newName must not be null");
    }
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
public AndroidTypeMoveChange(IFile androidManifest, ITextFileBufferManager manager,
            IDocument document, Map<String, String> elements, String newName, String oldName) {
    super(androidManifest, manager, document, elements, newName, oldName);
    if (oldName == null || newName == null) {
        throw new IllegalArgumentException("oldName and newName must not be null");
    }
}
//<End of snippet n. 2>

//<Beginning of snippet n. 3>
public AndroidTypeRenameChange(IFile androidManifest, ITextFileBufferManager manager,
            IDocument document, Map<String, String> elements, String newName, String oldName) {
    super(document);
    this.mDocument = document;
    this.mElements = elements;
    this.mNewName = newName;
    this.mOldName = oldName;
    this.mManager = manager;
    this.mAndroidManifest = androidManifest;
    if (oldName == null || newName == null) {
        throw new IllegalArgumentException("oldName and newName must not be null");
    }
    try {
        // Constructor logic here...
    } catch (Exception e) {
        RefactoringUtil.log(e);
    }
}
//<End of snippet n. 3>

//<Beginning of snippet n. 4>
CompositeChange result = new CompositeChange(getName());
if (mAndroidManifest.exists()) {
    if (mAndroidElements.size() > 0 || mIsPackage) {
        getDocument();
        Change change = new AndroidPackageRenameChange(mAndroidManifest, mManager,
                mDocument, mAndroidElements, mNewName, mOldName, mIsPackage);
        if (change != null) {
            result.add(change);
        }
    }
}
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
        }
    }
}
//<End of snippet n. 4>

//<Beginning of snippet n. 5>
public IDocument getDocument() throws CoreException {
    if (mDocument == null) {
        mManager = FileBuffers.getTextFileBufferManager();
        mManager.connect(mAndroidManifest.getFullPath(), LocationKind.NORMALIZE,
                new NullProgressMonitor());
        ITextFileBuffer buffer = mManager.getTextFileBuffer(mAndroidManifest.getFullPath(),
                LocationKind.NORMALIZE);
        mDocument = buffer.getDocument();
    }
    return mDocument;
}
//<End of snippet n. 5>

//<Beginning of snippet n. 6>
@SuppressWarnings("restriction")
public class AndroidTypeRenameParticipant extends AndroidRenameParticipant {
    CompositeChange result = new CompositeChange(getName());
    if (mAndroidManifest.exists()) {
        if (mAndroidElements.size() > 0) {
            getDocument();
            Change change = new AndroidTypeRenameChange(mAndroidManifest, mManager, mDocument,
                    mAndroidElements, mNewName, mOldName);
            if (change != null) {
                result.add(change);
            }
        }
    }
    if (manifestResource == null || !manifestResource.exists() || !(manifestResource instanceof IFile)) {
        RefactoringUtil.logInfo("Invalid or missing the " + SdkConstants.FN_ANDROID_MANIFEST_XML + " in the " + project.getName() + " project.");
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
            String value = attributes.getNamedItem("value").getNodeValue();
            if (value != null && value.equals(className)) {
                AndroidLayoutChangeDescription layoutChange =
                        new AndroidLayoutChangeDescription(className, mLayoutNewName,
                                AndroidLayoutChangeDescription.VIEW_TYPE);
                changes.add(layoutChange);
            }
        }
    }
}
//<End of snippet n. 6>

//<Beginning of snippet n. 7>
if (IMarker.PROBLEM,
        true,
        IResource.DEPTH_INFINITE) == IMarker.SEVERITY_ERROR) {
    return RefactoringStatus.createFatalErrorStatus("Fix the errors in your project, first.");
}
return new RefactoringStatus();
//<End of snippet n. 7>