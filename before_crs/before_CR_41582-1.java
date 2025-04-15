/*Light sensor does not 'guess' good enough: 23095

As described in Issue 23095 (and some other duplicates) users have problem with automatic brightness too dim and me personally have to use manual settings changing that 2 times per day. This patch enables user adjustemt to lux sensor's value solving the problem efficiantly.

Change-Id:Ie967a4cf128319f9676f87e9467068cd10c885b6Signed-off-by: Alexander Voloshyn <a.voloshyn@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/settings/BrightnessPreference.java b/src/com/android/settings/BrightnessPreference.java
//Synthetic comment -- index eff5c50..1d4ae9b 100644

//Synthetic comment -- @@ -113,7 +113,6 @@
mOldAutomatic = getBrightnessMode(0);
mAutomaticMode = mOldAutomatic == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
mCheckBox.setChecked(mAutomaticMode);
            mSeekBar.setEnabled(!mAutomaticMode);
} else {
mSeekBar.setEnabled(true);
}
//Synthetic comment -- @@ -136,8 +135,6 @@
public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
setMode(isChecked ? Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
: Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        mSeekBar.setProgress(getBrightness());
        mSeekBar.setEnabled(!mAutomaticMode);
setBrightness(mSeekBar.getProgress(), false);
}

//Synthetic comment -- @@ -180,7 +177,6 @@
== Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
mCheckBox.setChecked(checked);
mSeekBar.setProgress(getBrightness());
        mSeekBar.setEnabled(!checked);
}

@Override
//Synthetic comment -- @@ -211,25 +207,29 @@
}

private void setBrightness(int brightness, boolean write) {
if (mAutomaticMode) {
            if (false) {
                float valf = (((float)brightness*2)/SEEK_BAR_RANGE) - 1.0f;
                try {
                    IPowerManager power = IPowerManager.Stub.asInterface(
                            ServiceManager.getService("power"));
                    if (power != null) {
                        power.setAutoBrightnessAdjustment(valf);
                    }
                    if (write) {
                        final ContentResolver resolver = getContext().getContentResolver();
                        Settings.System.putFloat(resolver,
                                Settings.System.SCREEN_AUTO_BRIGHTNESS_ADJ, valf);
                    }
                } catch (RemoteException doe) {
}
}
} else {
            int range = (MAXIMUM_BACKLIGHT - mScreenBrightnessDim);
brightness = (brightness*range)/SEEK_BAR_RANGE + mScreenBrightnessDim;
try {
IPowerManager power = IPowerManager.Stub.asInterface(







