/*Missing from previous commit

Change-Id:Ib9e8b906c651b2a59ed10e159a8f8027e2845059*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidXmlEditor.java
//Synthetic comment -- index 2f3e0b1..6368783 100644

//Synthetic comment -- @@ -27,12 +27,14 @@
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
//Synthetic comment -- @@ -69,9 +71,12 @@
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.ide.IDEActionFactory;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.internal.browser.WorkbenchBrowserSupport;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
//Synthetic comment -- @@ -953,7 +958,7 @@
* the hooks should not perform edits on the model without acquiring
* a lock first.
*/
    protected void runEditHooks() {
if (!mIgnoreXmlUpdate) {
// Check for errors, if enabled
if (AdtPrefs.getPrefs().isLintOnSave()) {







