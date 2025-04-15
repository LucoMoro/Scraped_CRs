/*Fix broken logic in SettingsProvider.parseProviderList.

We were accidentally stripping both leading and trailing commas
when removing a provider from the enabled provider list.

Signed-off-by: Mike Lockwood <lockwood@android.com>*/




//Synthetic comment -- diff --git a/packages/SettingsProvider/src/com/android/providers/settings/SettingsProvider.java b/packages/SettingsProvider/src/com/android/providers/settings/SettingsProvider.java
//Synthetic comment -- index 9877342..b9710e7 100644

//Synthetic comment -- @@ -306,8 +306,11 @@
} else if (prefix == '-' && index >= 0) {
// remove the provider from the list if present
// remove leading and trailing commas
                    if (index > 0) {
                        index--;
                    } else if (end < providers.length()) {
                        end++;
                    }

newProviders = providers.substring(0, index);
if (end < providers.length()) {







