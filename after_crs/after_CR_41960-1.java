/*ndk: disable CDT's language settings provider.

Language Settings Provider is CDT's new scanner discovery
mechanism (http://wiki.eclipse.org/CDT/ScannerDiscovery61) introduced
with CDT 8.1.0. Until we migrate to that, this will allow scanner
discovery to work in CDT 8.0.x or with CDT 8.1.x without any user
intervention.

Fixeshttp://code.google.com/p/android/issues/detail?id=33788Change-Id:Ie26d60cd33ceb5442dafd3fa116b5e381bef680e*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/wizards/AddNativeWizard.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/wizards/AddNativeWizard.java
//Synthetic comment -- index a79eb31..8aef5d7 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.ide.eclipse.ndk.internal.NdkManager;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.language.settings.providers.ScannerDiscoveryLegacySupport;
import org.eclipse.cdt.make.core.MakeCorePlugin;
import org.eclipse.cdt.ui.CUIPlugin;
import org.eclipse.core.resources.IProject;
//Synthetic comment -- @@ -81,6 +82,8 @@
MakeCorePlugin.MAKE_PROJECT_ID);
// Set up build information
new NdkWizardHandler().convertProject(mProject, monitor1);
                        ScannerDiscoveryLegacySupport
                                .setLanguageSettingsProvidersFunctionalityEnabled(mProject, false);
// Run the template
NdkManager.addNativeSupport(mProject, mTemplateArgs, monitor1);
}







