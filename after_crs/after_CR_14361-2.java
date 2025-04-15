/*Fix broken logic in SettingsProvider.parseProviderList.

We were accidentally stripping both leading and trailing commas
when removing a provider from the enabled provider list.

Signed-off-by: Mike Lockwood <lockwood@android.com>*/




//Synthetic comment -- diff --git a/packages/SettingsProvider/src/com/android/providers/settings/SettingsProvider.java b/packages/SettingsProvider/src/com/android/providers/settings/SettingsProvider.java
//Synthetic comment -- index 9877342..ab186cf 100644

//Synthetic comment -- @@ -305,9 +305,12 @@
}
} else if (prefix == '-' && index >= 0) {
// remove the provider from the list if present
                    // remove leading or trailing comma
                    if (index > 0) {
                        index--;
                    } else if (end < providers.length()) {
                        end++;
                    }

newProviders = providers.substring(0, index);
if (end < providers.length()) {







