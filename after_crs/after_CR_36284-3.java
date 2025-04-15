/*Open hprof files if hprof content type is supported.

When hprof files are obtained from the device, they are saved by
default. This patch changes the default to open the hprof file if
it is a registered content type implying that there is an editor
for it.

This fixeshttp://code.google.com/p/android/issues/detail?id=21297Change-Id:Ic89eb59855888daa4775852181d2af245c8893d9*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/preferences/PreferenceInitializer.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/preferences/PreferenceInitializer.java
//Synthetic comment -- index fa73dbc..cce470f 100644

//Synthetic comment -- @@ -23,6 +23,8 @@
import com.android.ddmlib.DdmPreferences;
import com.android.ddmuilib.DdmUiPreferences;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -106,7 +108,15 @@
store.setDefault(ATTR_LOGCAT_FONT,
new FontData("Courier", 10, SWT.NORMAL).toString()); //$NON-NLS-1$

        // When obtaining hprof files from the device, default to opening the file
        // only if there is a registered content type for the hprof extension.
store.setDefault(ATTR_HPROF_ACTION, HProfHandler.ACTION_SAVE);
        for (IContentType contentType: Platform.getContentTypeManager().getAllContentTypes()) {
            if (contentType.isAssociatedWith(HProfHandler.DOT_HPROF)) {
                store.setDefault(ATTR_HPROF_ACTION, HProfHandler.ACTION_OPEN);
                break;
            }
        }

store.setDefault(ATTR_TIME_OUT, DdmPreferences.DEFAULT_TIMEOUT);








