/*Only format Android projects with our formatter

When formatting, peek at the model base location, if known, and if a
corresponding file is found which isn't in an Android project, use the
builtin Eclipse formatter instead. This should reduce the impact of
the Android XML editor registering itself for the xml content type and
affecting unrelated projects, at least in terms of formatting (which
is more severe than say additional completion options or extra go to
declaration targets.)

See issue 38747: Android Tools XML formatter with pom.xml

Change-Id:Ifa0e8527b7020c2ca4f477d6e0be6397d2dbdeff*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/AndroidXmlFormattingStrategy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/AndroidXmlFormattingStrategy.java
//Synthetic comment -- index 35735dc..407a8a8 100644

//Synthetic comment -- @@ -15,6 +15,8 @@
*/
package com.android.ide.eclipse.adt.internal.editors.formatting;

import static com.android.SdkConstants.ANDROID_MANIFEST_XML;
import static com.android.SdkConstants.FD_RESOURCES;
import static com.android.ide.eclipse.adt.internal.editors.AndroidXmlAutoEditStrategy.findLineStart;
import static com.android.ide.eclipse.adt.internal.editors.AndroidXmlAutoEditStrategy.findTextStart;
import static com.android.ide.eclipse.adt.internal.editors.color.ColorDescriptors.SELECTOR_TAG;
//Synthetic comment -- @@ -32,8 +34,14 @@
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.resources.ResourceType;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
//Synthetic comment -- @@ -84,6 +92,8 @@
private final Queue<IDocument> mDocuments = new LinkedList<IDocument>();
private final LinkedList<TypedPosition> mPartitions = new LinkedList<TypedPosition>();
private ContextBasedFormattingStrategy mDelegate = null;
    /** False if document is known not to be in an Android project, null until initialized */
    private Boolean mIsAndroid;

/**
* Creates a new {@link AndroidXmlFormattingStrategy}
//Synthetic comment -- @@ -92,7 +102,8 @@
}

private ContextBasedFormattingStrategy getDelegate() {
        if (!AdtPrefs.getPrefs().getUseCustomXmlFormatter()
                || mIsAndroid != null && !mIsAndroid.booleanValue()) {
if (mDelegate == null) {
mDelegate = new XMLFormattingStrategy();
}
//Synthetic comment -- @@ -159,7 +170,7 @@
* @param length the length of the text range to be formatted
* @return a {@link TextEdit} which edits the model into a formatted document
*/
    private static TextEdit format(IStructuredModel model, int start, int length) {
int end = start + length;

TextEdit edit = new MultiTextEdit();
//Synthetic comment -- @@ -545,6 +556,43 @@
return style;
}

    private Boolean isAndroid(IDocument document) {
        if (mIsAndroid == null) {
            // Look up the corresponding IResource for this document. This isn't
            // readily available, so take advantage of the structured model's base location
            // string and convert it to an IResource to look up its project.
            if (document instanceof IStructuredDocument) {
                IStructuredDocument structuredDocument = (IStructuredDocument) document;
                IModelManager modelManager = StructuredModelManager.getModelManager();

                IStructuredModel model = modelManager.getModelForRead(structuredDocument);
                if (model != null) {
                    String location = model.getBaseLocation();
                    model.releaseFromRead();
                    if (!location.endsWith(ANDROID_MANIFEST_XML) &&
                            !location.contains(FD_RESOURCES)) {
                        // See if it looks like a foreign document
                        IWorkspace workspace = ResourcesPlugin.getWorkspace();
                        IWorkspaceRoot root = workspace.getRoot();
                        IResource member = root.findMember(location);
                        if (member.exists()) {
                            IProject project = member.getProject();
                            if (project.isAccessible() &&
                                    !BaseProjectHelper.isAndroidProject(project)) {
                                mIsAndroid = false;
                                return false;
                            }
                        }
                    }
                }
            }

            mIsAndroid = true;
        }

        return mIsAndroid.booleanValue();
    }

@Override
public void formatterStarts(final IFormattingContext context) {
// Use Eclipse XML formatter instead?
//Synthetic comment -- @@ -567,6 +615,15 @@
IDocument document = (IDocument) context.getProperty(CONTEXT_MEDIUM);
mPartitions.offer(partition);
mDocuments.offer(document);

        if (!isAndroid(document)) {
            // It's some foreign type of project: use default
            // formatter
            delegate = getDelegate();
            if (delegate != null) {
                delegate.formatterStarts(context);
            }
        }
}

@Override







