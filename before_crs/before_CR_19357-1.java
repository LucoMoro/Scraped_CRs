/*Cleanup refactoring.

Change-Id:I5641a9238129558b269c3f8a27a5b4de54e9db44*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java
//Synthetic comment -- index 2a7f5f3..1bc511b 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.common.layout;

/**
* A bunch of constants that map to either:
* <ul>
//Synthetic comment -- @@ -25,6 +27,13 @@
* </ul>
*/
public class LayoutConstants {
public static final String RELATIVE_LAYOUT = "RelativeLayout";      //$NON-NLS-1$
public static final String LINEAR_LAYOUT   = "LinearLayout";        //$NON-NLS-1$
public static final String ABSOLUTE_LAYOUT = "AbsoluteLayout";      //$NON-NLS-1$
//Synthetic comment -- @@ -63,7 +72,7 @@
* Namespace for the Android resource XML, i.e.
* "http://schemas.android.com/apk/res/android"
*/
    public static String ANDROID_URI = "http://schemas.android.com/apk/res/android"; //$NON-NLS-1$

/** The fully qualified class name of an EditText view */
public static final String FQCN_EDIT_TEXT = "android.widget.EditText"; //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/changes/AndroidDocumentChange.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/changes/AndroidDocumentChange.java
//Synthetic comment -- index 542a090..f6677f8 100755

//Synthetic comment -- @@ -18,7 +18,6 @@

import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.internal.project.XmlErrorHandler.BasicXmlErrorListener;
import com.android.ide.eclipse.adt.internal.refactoring.core.IConstants;
import com.android.ide.eclipse.adt.internal.refactoring.core.RefactoringUtil;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.xml.AndroidManifest;
//Synthetic comment -- @@ -44,13 +43,13 @@
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.Map;

/**
* A text change that operates on android manifest using WTP SSE model.
* It is base class for Rename Package and Rename Type changes
*/
public class AndroidDocumentChange extends DocumentChange {

protected IFile mAndroidManifest;
//Synthetic comment -- @@ -79,10 +78,7 @@
super(SdkConstants.FN_ANDROID_MANIFEST_XML , document);
}

    /**
     * {@inheritDoc}
     */
    @Override
public RefactoringStatus isValid(IProgressMonitor pm) throws CoreException,
OperationCanceledException {
RefactoringStatus status = super.isValid(pm);
//Synthetic comment -- @@ -174,8 +170,7 @@
* @return the model
*
*/

    protected IStructuredModel getModel(IDocument document) throws IOException, CoreException {
if (mModel != null) {
return mModel;
}
//Synthetic comment -- @@ -191,9 +186,6 @@
return model;
}

    /**
     * {@inheritDoc}
     */
@Override
public void setTextType(String type) {
super.setTextType(mAndroidManifest.getFileExtension());
//Synthetic comment -- @@ -268,8 +260,8 @@
*
* @param elementName the element name
* @param attributeName the attribute name
     * @param oldValue the old value
     * @param newValue the new value
* @param combinePackage combine package ?
*
* @return the text change








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/changes/AndroidLayoutChange.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/changes/AndroidLayoutChange.java
//Synthetic comment -- index c7c684d..cfa4250 100755

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt.internal.refactoring.changes;

import com.android.ide.eclipse.adt.internal.refactoring.core.IConstants;
import com.android.ide.eclipse.adt.internal.refactoring.core.RefactoringUtil;

import org.eclipse.core.filebuffers.ITextFileBufferManager;
//Synthetic comment -- @@ -33,6 +33,7 @@
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
//Synthetic comment -- @@ -45,8 +46,6 @@
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
//Synthetic comment -- @@ -55,6 +54,7 @@
* A text change that operates on android layout using WTP SSE model.
* It is base class for Rename Package and Rename Type changes
*/
public class AndroidLayoutChange extends DocumentChange {

private IDocument mDocument;
//Synthetic comment -- @@ -74,15 +74,14 @@
* @param document the document
* @param manager the buffer manager
* @param changes the list of changes
     * @param androidLayoutChangeDescription
*/
public AndroidLayoutChange(IFile file, IDocument document, ITextFileBufferManager manager,
Set<AndroidLayoutChangeDescription> changes) {
super("", document); //$NON-NLS-1$
        this.mFile = file;
        this.mDocument = document;
        this.mManager = manager;
        this.mChanges = changes;
try {
this.mModel = getModel(document);
} catch (Exception ignore) {
//Synthetic comment -- @@ -92,6 +91,43 @@
}
}

/**
* Adds text edits for this change
*/
//Synthetic comment -- @@ -99,8 +135,9 @@
MultiTextEdit multiEdit = new MultiTextEdit();
for (AndroidLayoutChangeDescription change : mChanges) {
if (!change.isStandalone()) {
                TextEdit edit = createTextEdit(IConstants.ANDROID_LAYOUT_VIEW_ELEMENT,
                        IConstants.ANDROID_LAYOUT_CLASS_ARGUMENT, change.getClassName(),
change.getNewName());
if (edit != null) {
multiEdit.addChild(edit);
//Synthetic comment -- @@ -117,7 +154,7 @@
}

/**
     * (non-Javadoc) Returns the text changes which change class (custom layout viewer) in layout file
*
* @param className the class name
* @param newName the new class name
//Synthetic comment -- @@ -153,7 +190,7 @@
*
* @return the attribute value
*/
    protected IDOMDocument getDOMDocument() {
IDOMModel xmlModel = (IDOMModel) mModel;
IDOMDocument xmlDoc = xmlModel.getDocument();
return xmlDoc;
//Synthetic comment -- @@ -167,7 +204,7 @@
*
* @return the text change
*/
    protected TextEdit createTextEdit(Attr attribute, String newValue) {
if (attribute == null)
return null;

//Synthetic comment -- @@ -187,13 +224,13 @@
* Returns the text change that change the value of attribute from oldValue to newValue
*
* @param elementName the element name
     * @param attributeName the attribute name
     * @param oldValue the old value
     * @param newValue the new value
*
* @return the text change
*/
    protected TextEdit createTextEdit(String elementName, String argumentName, String oldName,
String newName) {
IDOMDocument xmlDoc = getDOMDocument();
String name = null;
//Synthetic comment -- @@ -209,7 +246,7 @@
}

/**
     * (non-Javadoc) Finds the attribute with values oldName
*
* @param xmlDoc the document
* @param element the element
//Synthetic comment -- @@ -218,7 +255,7 @@
*
* @return the attribute
*/
    public Attr findAttribute(IDOMDocument xmlDoc, String element, String attributeName,
String oldValue) {
NodeList nodes = xmlDoc.getElementsByTagName(element);
for (int i = 0; i < nodes.getLength(); i++) {
//Synthetic comment -- @@ -238,70 +275,19 @@
}

/**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return mFile.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RefactoringStatus isValid(IProgressMonitor pm) throws CoreException,
            OperationCanceledException {
        RefactoringStatus status = super.isValid(pm);
        if (mModel == null) {
            status.addFatalError("Invalid the " + getName() + " file.");
        }
        return status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTextType(String type) {
        super.setTextType(mFile.getFileExtension());
    }

    /**
* Returns the SSE model for a document
*
* @param document the document
     *
* @return the model
     *
*/
    protected IStructuredModel getModel(IDocument document) throws IOException, CoreException {
        if (mModel != null) {
            return mModel;
}
        IStructuredModel model;
        model = StructuredModelManager.getModelManager().getExistingModelForRead(document);
        if (model == null) {
            if (document instanceof IStructuredDocument) {
                IStructuredDocument structuredDocument = (IStructuredDocument) document;
                model = StructuredModelManager.getModelManager()
                        .getModelForRead(structuredDocument);
            }
        }
return model;
}

    @Override
    public void dispose() {
        super.dispose();
        RefactoringUtil.fixModel(mModel, mDocument);

        if (mManager != null) {
            try {
                mManager.disconnect(mFile.getFullPath(), LocationKind.NORMALIZE,
                        new NullProgressMonitor());
            } catch (CoreException e) {
                RefactoringUtil.log(e);
            }
        }
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/changes/AndroidLayoutChangeDescription.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/changes/AndroidLayoutChangeDescription.java
//Synthetic comment -- index cde4533..b96c412 100755

//Synthetic comment -- @@ -79,9 +79,6 @@
return mType == STANDALONE_TYPE;
}

    /**
     * {@inheritDoc}
     */
@Override
public int hashCode() {
final int prime = 31;
//Synthetic comment -- @@ -92,9 +89,6 @@
return result;
}

    /**
     * {@inheritDoc}
     */
@Override
public boolean equals(Object obj) {
if (this == obj)
//Synthetic comment -- @@ -119,9 +113,6 @@
return true;
}

    /**
     * {@inheritDoc}
     */
@Override
public String toString() {
return "AndroidLayoutChangeDescription [className=" + mClassName + ", newName=" + mNewName








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/changes/AndroidPackageRenameChange.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/changes/AndroidPackageRenameChange.java
//Synthetic comment -- index 23a3a26..1723087 100755

//Synthetic comment -- @@ -17,7 +17,6 @@
package com.android.ide.eclipse.adt.internal.refactoring.changes;

import com.android.ide.eclipse.adt.internal.refactoring.core.FixImportsJob;
import com.android.ide.eclipse.adt.internal.refactoring.core.IConstants;
import com.android.ide.eclipse.adt.internal.refactoring.core.RefactoringUtil;
import com.android.sdklib.xml.AndroidManifest;

//Synthetic comment -- @@ -108,9 +107,6 @@
setEdit(multiEdit);
}

    /**
     * {@inheritDoc}
     */
@Override
public Change perform(IProgressMonitor pm) throws CoreException {
super.perform(pm);
//Synthetic comment -- @@ -118,9 +114,6 @@
mOldName, mNewName, mIsPackage);
}

    /**
     * {@inheritDoc}
     */
@Override
public void dispose() {
super.dispose();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/changes/AndroidTypeMoveChange.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/changes/AndroidTypeMoveChange.java
//Synthetic comment -- index 9d57114..e7f2d1a 100755

//Synthetic comment -- @@ -36,7 +36,6 @@
* @param elements the elements
* @param newName the new name
* @param oldName the old name
     * @param isPackage is the application package
*/
public AndroidTypeMoveChange(IFile androidManifest, ITextFileBufferManager manager,
IDocument document, Map<String, String> elements, String newName, String oldName) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/changes/AndroidTypeRenameChange.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/changes/AndroidTypeRenameChange.java
//Synthetic comment -- index f87674a..0f56442 100755

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.ide.eclipse.adt.internal.refactoring.changes;

import com.android.ide.eclipse.adt.internal.refactoring.core.IConstants;
import com.android.ide.eclipse.adt.internal.refactoring.core.RefactoringUtil;
import com.android.sdklib.xml.AndroidManifest;

//Synthetic comment -- @@ -48,7 +47,6 @@
* @param elements the elements
* @param newName the new name
* @param oldName the old name
     * @param isPackage is the application package
*/
public AndroidTypeRenameChange(IFile androidManifest, ITextFileBufferManager manager,
IDocument document, Map<String, String> elements, String newName, String oldName) {
//Synthetic comment -- @@ -69,7 +67,7 @@
}

/**
     * (non-Javadoc) Adds text edits for this change
*/
private void addEdits() {
MultiTextEdit multiEdit = new MultiTextEdit();
//Synthetic comment -- @@ -97,9 +95,6 @@
setEdit(multiEdit);
}

    /**
     * {@inheritDoc}
     */
@Override
public Change perform(IProgressMonitor pm) throws CoreException {
super.perform(pm);
//Synthetic comment -- @@ -107,9 +102,6 @@
mOldName, mNewName);
}

    /**
     * {@inheritDoc}
     */
@Override
public void dispose() {
super.dispose();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/AndroidPackageRenameParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/AndroidPackageRenameParticipant.java
//Synthetic comment -- index d9343cc..43cb09d 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.refactoring.core;

import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.internal.refactoring.changes.AndroidLayoutChange;
//Synthetic comment -- @@ -84,6 +85,7 @@
* <code>org.eclipse.ltk.core.refactoring.participants.RenameParticipant</code>.
* </p>
*/
public class AndroidPackageRenameParticipant extends AndroidRenameParticipant {

private IPackageFragment mPackageFragment;
//Synthetic comment -- @@ -92,12 +94,6 @@

private Set<AndroidLayoutFileChanges> mFileChanges = new HashSet<AndroidLayoutFileChanges>();

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#
     * createChange(org.eclipse.core.runtime.IProgressMonitor)
     */
@Override
public Change createChange(IProgressMonitor pm) throws CoreException,
OperationCanceledException {
//Synthetic comment -- @@ -170,8 +166,8 @@
return null;
}

    /*
     * (non-Javadoc) return the gen package fragment
*
*/
private IPackageFragment getGenPackageFragment() throws JavaModelException {
//Synthetic comment -- @@ -191,23 +187,11 @@
return null;
}

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#
     * getName()
     */
@Override
public String getName() {
return "Android Package Rename";
}

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#
     * initialize(java.lang.Object)
     */
@Override
protected boolean initialize(final Object element) {
mIsPackage = false;
//Synthetic comment -- @@ -300,11 +284,10 @@
}

/**
     * (non-Javadoc) Adds layout changes for project
*
* @param project the Android project
* @param classNames the layout classes
     *
*/
private void addLayoutChanges(IProject project, String[] classNames) {
try {
//Synthetic comment -- @@ -341,11 +324,10 @@
}

/**
     * (non-Javadoc) Searches the layout file for classes
*
* @param file the Android layout file
* @param classNames the layout classes
     *
*/
private Set<AndroidLayoutChangeDescription> parse(IFile file, String[] classNames) {
Set<AndroidLayoutChangeDescription> changes =
//Synthetic comment -- @@ -372,14 +354,13 @@
if (model != null) {
IDOMModel xmlModel = (IDOMModel) model;
IDOMDocument xmlDoc = xmlModel.getDocument();
                    NodeList nodes = xmlDoc
                            .getElementsByTagName(IConstants.ANDROID_LAYOUT_VIEW_ELEMENT);
for (int i = 0; i < nodes.getLength(); i++) {
Node node = nodes.item(i);
NamedNodeMap attributes = node.getAttributes();
if (attributes != null) {
Node attributeNode = attributes
                                    .getNamedItem(IConstants.ANDROID_LAYOUT_CLASS_ARGUMENT);
if (attributeNode != null || attributeNode instanceof Attr) {
Attr attribute = (Attr) attributeNode;
String value = attribute.getValue();
//Synthetic comment -- @@ -432,11 +413,10 @@
}

/**
     * (non-Javadoc) Returns the new class name
*
* @param className the class name
* @return the new class name
     *
*/
private String getNewClassName(String className) {
int lastDot = className.lastIndexOf("."); //$NON-NLS-1$
//Synthetic comment -- @@ -449,11 +429,10 @@
}

/**
     * (non-Javadoc) Returns the elements (activity, receiver, service ...)
* which have to be renamed
*
* @return the android elements
     *
*/
private Map<String, String> addAndroidElements() {
Map<String, String> androidElements = new HashMap<String, String>();
//Synthetic comment -- @@ -509,7 +488,7 @@
}

/**
     * (non-Javadoc) Adds the element  (activity, receiver, service ...) to the map
*
* @param xmlDoc the document
* @param androidElements the map








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/AndroidRenameParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/AndroidRenameParticipant.java
//Synthetic comment -- index 2f4ceed..dd99a98 100755

//Synthetic comment -- @@ -52,13 +52,6 @@

protected Map<String, String> mAndroidElements;

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#
     * checkConditions(org.eclipse.core.runtime.IProgressMonitor,
     * org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext)
     */
@Override
public RefactoringStatus checkConditions(IProgressMonitor pm, CheckConditionsContext context)
throws OperationCanceledException {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/AndroidTypeMoveParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/AndroidTypeMoveParticipant.java
//Synthetic comment -- index 9498421..e7c3e1a 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.refactoring.core;

import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.internal.refactoring.changes.AndroidLayoutChange;
//Synthetic comment -- @@ -75,6 +76,7 @@
* Extensions to this extension point must therefore extend <code>org.eclipse.ltk.core.refactoring.participants.MoveParticipant</code>.
* </p>
*/
public class AndroidTypeMoveParticipant extends MoveParticipant {

protected IFile mAndroidManifest;
//Synthetic comment -- @@ -93,25 +95,12 @@

private Set<AndroidLayoutFileChanges> mFileChanges = new HashSet<AndroidLayoutFileChanges>();

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#
     * checkConditions(org.eclipse.core.runtime.IProgressMonitor,
     * org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext)
     */
@Override
public RefactoringStatus checkConditions(IProgressMonitor pm, CheckConditionsContext context)
throws OperationCanceledException {
return new RefactoringStatus();
}

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#
     * createChange(org.eclipse.core.runtime.IProgressMonitor)
     */
@Override
public Change createChange(IProgressMonitor pm) throws CoreException,
OperationCanceledException {
//Synthetic comment -- @@ -173,23 +162,11 @@
return mAndroidManifest;
}

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#
     * getName()
     */
@Override
public String getName() {
return "Android Type Move";
}

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#
     * initialize(java.lang.Object)
     */
@Override
protected boolean initialize(Object element) {

//Synthetic comment -- @@ -246,10 +223,10 @@
}

/**
     * (non-Javadoc) Adds layout changes for project
*
* @param project the Android project
     * @param classNames the layout classes
*
*/
private void addLayoutChanges(IProject project, String className) {
//Synthetic comment -- @@ -275,10 +252,10 @@
}

/**
     * (non-Javadoc) Searches the layout file for classes
*
* @param file the Android layout file
     * @param classNames the layout classes
*
*/
private Set<AndroidLayoutChangeDescription> parse(IFile file, String className) {
//Synthetic comment -- @@ -303,14 +280,13 @@
if (model != null) {
IDOMModel xmlModel = (IDOMModel) model;
IDOMDocument xmlDoc = xmlModel.getDocument();
                    NodeList nodes = xmlDoc
                            .getElementsByTagName(IConstants.ANDROID_LAYOUT_VIEW_ELEMENT);
for (int i = 0; i < nodes.getLength(); i++) {
Node node = nodes.item(i);
NamedNodeMap attributes = node.getAttributes();
if (attributes != null) {
                            Node attributeNode = attributes
                                    .getNamedItem(IConstants.ANDROID_LAYOUT_CLASS_ARGUMENT);
if (attributeNode != null || attributeNode instanceof Attr) {
Attr attribute = (Attr) attributeNode;
String value = attribute.getValue();
//Synthetic comment -- @@ -351,11 +327,10 @@
}

/**
     * (non-Javadoc) Returns the elements (activity, receiver, service ...)
* which have to be renamed
*
* @return the android elements
     *
*/
private Map<String, String> addAndroidElements() {
Map<String, String> androidElements = new HashMap<String, String>();
//Synthetic comment -- @@ -411,7 +386,7 @@
}

/**
     * (non-Javadoc) Adds the element  (activity, receiver, service ...) to the map
*
* @param xmlDoc the document
* @param androidElements the map








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/AndroidTypeRenameParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/AndroidTypeRenameParticipant.java
//Synthetic comment -- index acf8caa..dc393e3 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.refactoring.core;

import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.internal.refactoring.changes.AndroidLayoutChange;
//Synthetic comment -- @@ -71,18 +72,13 @@
* Extensions to this extension point must therefore extend <code>org.eclipse.ltk.core.refactoring.participants.RenameParticipant</code>.
* </p>
*/
public class AndroidTypeRenameParticipant extends AndroidRenameParticipant {

private Set<AndroidLayoutFileChanges> mFileChanges = new HashSet<AndroidLayoutFileChanges>();

private String mLayoutNewName;

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#
     * createChange(org.eclipse.core.runtime.IProgressMonitor)
     */
@Override
public Change createChange(IProgressMonitor pm) throws CoreException,
OperationCanceledException {
//Synthetic comment -- @@ -121,23 +117,11 @@

}

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#
     * getName()
     */
@Override
public String getName() {
return "Android Type Rename";
}

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant#
     * initialize(java.lang.Object)
     */
@Override
protected boolean initialize(Object element) {

//Synthetic comment -- @@ -200,10 +184,10 @@
}

/**
     * (non-Javadoc) Adds layout changes for project
*
* @param project the Android project
     * @param classNames the layout classes
*
*/
private void addLayoutChanges(IProject project, String className) {
//Synthetic comment -- @@ -229,10 +213,10 @@
}

/**
     * (non-Javadoc) Searches the layout file for classes
*
* @param file the Android layout file
     * @param classNames the layout classes
*
*/
private Set<AndroidLayoutChangeDescription> parse(IFile file, String className) {
//Synthetic comment -- @@ -258,13 +242,13 @@
IDOMModel xmlModel = (IDOMModel) model;
IDOMDocument xmlDoc = xmlModel.getDocument();
NodeList nodes = xmlDoc
                            .getElementsByTagName(IConstants.ANDROID_LAYOUT_VIEW_ELEMENT);
for (int i = 0; i < nodes.getLength(); i++) {
Node node = nodes.item(i);
NamedNodeMap attributes = node.getAttributes();
if (attributes != null) {
                            Node attributeNode = attributes
                                    .getNamedItem(IConstants.ANDROID_LAYOUT_CLASS_ARGUMENT);
if (attributeNode != null || attributeNode instanceof Attr) {
Attr attribute = (Attr) attributeNode;
String value = attribute.getValue();
//Synthetic comment -- @@ -305,7 +289,7 @@
}

/**
     * (non-Javadoc) Returns the elements (activity, receiver, service ...)
* which have to be renamed
*
* @return the android elements








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/FixImportsJob.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/FixImportsJob.java
//Synthetic comment -- index 4a84bc2..ae40790 100755

//Synthetic comment -- @@ -50,6 +50,7 @@
* The helper class which fixes the import errors after refactoring
*
*/
public class FixImportsJob extends WorkspaceJob {

private IFile mAndroidManifest;
//Synthetic comment -- @@ -69,12 +70,6 @@
this.mJavaPackage = javaPackage;
}

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.core.resources.WorkspaceJob#runInWorkspace(org.eclipse.core
     * .runtime.IProgressMonitor)
     */
@Override
public IStatus runInWorkspace(final IProgressMonitor monitor) throws CoreException {
if (mJavaPackage == null || mAndroidManifest == null || !mAndroidManifest.exists()) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/IConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/IConstants.java
deleted file mode 100755
//Synthetic comment -- index 218ed99..0000000

//Synthetic comment -- @@ -1,35 +0,0 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.android.ide.eclipse.adt.internal.refactoring.core;

/**
 * Declares some constants used in refactoring
 *
 */
public interface IConstants {

    /**
     * the android view argument
     */
    static final String ANDROID_LAYOUT_VIEW_ELEMENT = "view"; //$NON-NLS-1$

    /**
     * the android class argument
     */
    static final String ANDROID_LAYOUT_CLASS_ARGUMENT = "class"; //$NON-NLS-1$

}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/RefactoringUtil.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/RefactoringUtil.java
//Synthetic comment -- index c75dd0f..409cf72 100644

//Synthetic comment -- @@ -21,7 +21,6 @@

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.IDocument;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
//Synthetic comment -- @@ -36,6 +35,7 @@
* The utility class for android refactoring
*
*/
public class RefactoringUtil {

private static boolean sRefactorAppPackage = false;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifest.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifest.java
//Synthetic comment -- index c888d17..4012300 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
public final static String NODE_MANIFEST = "manifest";
public final static String NODE_APPLICATION = "application";
public final static String NODE_ACTIVITY = "activity";
public final static String NODE_SERVICE = "service";
public final static String NODE_RECEIVER = "receiver";
public final static String NODE_PROVIDER = "provider";
//Synthetic comment -- @@ -62,7 +63,6 @@
public final static String ATTRIBUTE_TARGET_PACKAGE = "targetPackage";
public final static String ATTRIBUTE_TARGET_ACTIVITY = "targetActivity";
public final static String ATTRIBUTE_MANAGE_SPACE_ACTIVITY = "manageSpaceActivity";
    public final static String NODE_ACTIVITY_ALIAS = "activity-alias";
public final static String ATTRIBUTE_EXPORTED = "exported";
public final static String ATTRIBUTE_RESIZEABLE = "resizeable";
public final static String ATTRIBUTE_ANYDENSITY = "anyDensity";







