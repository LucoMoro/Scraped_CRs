/*ndk: disable CDT's language settings provider.

Language Settings Provider is CDT's new scanner discovery
mechanism (http://wiki.eclipse.org/CDT/ScannerDiscovery61) introduced
with CDT 8.1.0. Until we migrate to that, this will allow scanner
discovery to work in CDT 8.0.x or with CDT 8.1.x without any user
intervention.

Fixeshttp://code.google.com/p/android/issues/detail?id=33788Change-Id:Ie26d60cd33ceb5442dafd3fa116b5e381bef680e*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/wizards/AddNativeWizard.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/wizards/AddNativeWizard.java
//Synthetic comment -- index a79eb31..b3675ed 100644

//Synthetic comment -- @@ -35,6 +35,7 @@
import org.eclipse.ui.WorkbenchException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

//Synthetic comment -- @@ -81,6 +82,26 @@
MakeCorePlugin.MAKE_PROJECT_ID);
// Set up build information
new NdkWizardHandler().convertProject(mProject, monitor1);
// Run the template
NdkManager.addNativeSupport(mProject, mTemplateArgs, monitor1);
}







