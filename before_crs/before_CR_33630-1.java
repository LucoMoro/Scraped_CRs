/*Added preference tree click handler for internal storage category preferences

On devices with separate external storage (non-emulated external storage) Storage screen shows
separate category for internal storage (see onCreate()). Pressing preferences on this category
does not launch respective activities as is the case in the other categories (e.g., pressing
Applications preference does not launch Manage applications activity).
This will add similar behavior to internal storage category preferences.
Signed-off-by: Shuhrat Dehkanov <uzbmaster@gmail.com>

Change-Id:I6195d62087caba7108c0c71d03264f46a2222639*/
//Synthetic comment -- diff --git a/src/com/android/settings/deviceinfo/Memory.java b/src/com/android/settings/deviceinfo/Memory.java
//Synthetic comment -- index 728e558..442701e 100644

//Synthetic comment -- @@ -200,6 +200,13 @@

@Override
public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
for (int i = 0; i < mStorageVolumePreferenceCategories.length; i++) {
StorageVolumePreferenceCategory svpc = mStorageVolumePreferenceCategories[i];
Intent intent = svpc.intentForClick(preference);







