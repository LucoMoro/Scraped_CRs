/*Fix possible NPE in layout configuration UI.

Change-Id:Ib5974aea2a7e5e67133e1d195f569335bd224fed*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index ef426df..f5ad935 100644

//Synthetic comment -- @@ -823,7 +823,7 @@
if (configIndex != -1) {
String configName = mDeviceConfigCombo.getItem(configIndex);
FolderConfiguration currentConfig = mState.device.getFolderConfigByName(configName);
            if (mEditedConfig.isMatchFor(currentConfig)) {
currentConfigIsCompatible = true; // current config is compatible
if (needBestMatch == false || isCurrentFileBestMatchFor(currentConfig)) {
needConfigChange = false;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/FolderConfiguration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/FolderConfiguration.java
//Synthetic comment -- index aa0dc25..3a37634 100644

//Synthetic comment -- @@ -518,6 +518,10 @@
* @return true if the configuration matches.
*/
public boolean isMatchFor(FolderConfiguration referenceConfig) {
for (int i = 0 ; i < INDEX_COUNT ; i++) {
ResourceQualifier testQualifier = mQualifiers[i];
ResourceQualifier referenceQualifier = referenceConfig.mQualifiers[i];







