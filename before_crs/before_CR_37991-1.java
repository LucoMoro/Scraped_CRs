/*Update accessibility service fragment title

When an accessibility service fragment (e.g., Settings >
Accessibility > Talkback) is launched, breadcrumb title is
not correctly updated in tablets. This will fix that bug.

Please see the following screenshots for comparison.
 Current view:http://goo.gl/ebufBModified view:http://goo.gl/KeUORChange-Id:Ia0a97946eda99d79c4c14b6e9de8e8b071a8dff1Signed-off-by: Shuhrat Dehkanov <uzbmaster@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/settings/AccessibilitySettings.java b/src/com/android/settings/AccessibilitySettings.java
//Synthetic comment -- index 827af13..8df0916 100644

//Synthetic comment -- @@ -334,7 +334,7 @@

PreferenceScreen preference = getPreferenceManager().createPreferenceScreen(
getActivity());
            String title = info.getResolveInfo().loadLabel(getPackageManager()).toString();

ServiceInfo serviceInfo = info.getResolveInfo().serviceInfo;
ComponentName componentName = new ComponentName(serviceInfo.packageName,
//Synthetic comment -- @@ -352,10 +352,9 @@
}

preference.setOrder(i);
            preference.setFragment(ToggleAccessibilityServiceFragment.class.getName());
preference.setPersistent(true);

            Bundle extras = preference.getExtras();
extras.putString(EXTRA_PREFERENCE_KEY, preference.getKey());
extras.putBoolean(EXTRA_CHECKED, serviceEnabled);
extras.putString(EXTRA_TITLE, title);
//Synthetic comment -- @@ -388,6 +387,26 @@
new ComponentName(info.getResolveInfo().serviceInfo.packageName,
settingsClassName).flattenToString());
}

mServicesCategory.addPreference(preference);
}







