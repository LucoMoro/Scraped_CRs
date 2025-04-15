/*Possible NumberFormatException of parseInt is outside try-catch

Change-Id:I77c1e41185a8d3a1724af848d6bbfbb1da816b55*/
//Synthetic comment -- diff --git a/src/com/android/settings/SoundSettings.java b/src/com/android/settings/SoundSettings.java
//Synthetic comment -- index a735268..2984684 100644

//Synthetic comment -- @@ -318,8 +318,8 @@
public boolean onPreferenceChange(Preference preference, Object objValue) {
final String key = preference.getKey();
if (KEY_EMERGENCY_TONE.equals(key)) {
            int value = Integer.parseInt((String) objValue);
try {
Settings.System.putInt(getContentResolver(),
Settings.System.EMERGENCY_TONE, value);
} catch (NumberFormatException e) {







