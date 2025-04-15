/*Fix Cut & Paste in value files for Eclipse 4.x

This changeset works around the following bug in Eclipse 4.2:

* Open some arbitrary file, like main.xml.
* Open strings.xml for the first time. (If it has already been opened,
  switch to the graphical tab, close it, and start again).
* Place the caret in strings.xml and paste something from the
  clipboard. In Eclipse 4.x, strings.xml will not change, but the
  *other* file is edited to include the new paste!

This appears to be related to some sort of change in the defaults for
action bar registrations in Eclipse 4. The workaround is to explicitly
set these actions on each opened editor, which seems to fix the
problem in 4.2 (and things still work in 3.7).

Change-Id:I86035d7921102ff684adf319c3bf5f94ce416cb7*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index 396e172..2f3e0b1 100644

//Synthetic comment -- @@ -27,14 +27,12 @@
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.Sdk.TargetChangeListener;
import com.android.sdklib.IAndroidTarget;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
//Synthetic comment -- @@ -71,11 +69,9 @@
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.ide.IDEActionFactory;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.internal.browser.WorkbenchBrowserSupport;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
//Synthetic comment -- @@ -365,6 +361,21 @@
action = mTextEditor.getAction(ActionFactory.REDO.getId());
bars.setGlobalActionHandler(ActionFactory.REDO.getId(), action);

            bars.setGlobalActionHandler(ActionFactory.DELETE.getId(),
                    mTextEditor.getAction(ActionFactory.DELETE.getId()));
            bars.setGlobalActionHandler(ActionFactory.CUT.getId(),
                    mTextEditor.getAction(ActionFactory.CUT.getId()));
            bars.setGlobalActionHandler(ActionFactory.COPY.getId(),
                    mTextEditor.getAction(ActionFactory.COPY.getId()));
            bars.setGlobalActionHandler(ActionFactory.PASTE.getId(),
                    mTextEditor.getAction(ActionFactory.PASTE.getId()));
            bars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(),
                    mTextEditor.getAction(ActionFactory.SELECT_ALL.getId()));
            bars.setGlobalActionHandler(ActionFactory.FIND.getId(),
                    mTextEditor.getAction(ActionFactory.FIND.getId()));
            bars.setGlobalActionHandler(IDEActionFactory.BOOKMARK.getId(),
                    mTextEditor.getAction(IDEActionFactory.BOOKMARK.getId()));

bars.updateActionBars();
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/common/CommonXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/common/CommonXmlEditor.java
//Synthetic comment -- index 5ee76c2..a7b3660 100755

//Synthetic comment -- @@ -42,7 +42,6 @@
import org.eclipse.jface.text.source.ISourceViewerExtension2;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IShowEditorInput;







