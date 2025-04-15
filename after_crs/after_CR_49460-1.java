/*Telephony: The settings values aren't taken in account correctly.

Just after a phone flash, the phone receives and
displays the Ambert Alert SMS CB, even if the
"Show Amber Alert" is disabled in the setting
of the CellBroadcastReceiver application by
default. This default value is to false in the
preference.xml file of the CellBroadcastReceiver
application in the vendor/intel/common/overlays
folder.

This patch updates the preference
TwoStatePreference.java file of the telephony
framework, with which after a phone flash the
Amber Alert for SMS CB is not received and
displayed, when the "Show Amber Alert" setting
disabled. Also With this patch the xml files are
correctly initialized after a phone flash.

Change-Id:I5c428c2b283ff6cc715a5eb22376f74ea98f9cb5Author: Delaude Emmanuel <emmanuelx.delaude@intel.com>
Signed-off-by: Delaude Emmanuel <emmanuelx.delaude@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 66630*/




//Synthetic comment -- diff --git a/core/java/android/preference/TwoStatePreference.java b/core/java/android/preference/TwoStatePreference.java
//Synthetic comment -- index c649879..03a9a584 100644

//Synthetic comment -- @@ -189,6 +189,12 @@
protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
setChecked(restoreValue ? getPersistedBoolean(mChecked)
: (Boolean) defaultValue);
        if (restoreValue == false && defaultValue != null
                && false == (Boolean)defaultValue) {
            persistBoolean((Boolean)defaultValue);
            notifyDependencyChange(shouldDisableDependents());
            notifyChanged();
        }
}

void sendAccessibilityEvent(View view) {







