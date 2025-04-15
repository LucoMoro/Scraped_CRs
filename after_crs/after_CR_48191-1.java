/*Correct check box visibility in Display / Brightness

The check box for "Automatic brightness" should not be visible in
Brightness screen if the product configuration
config_automatic_brightness_available in config.xml is set to false.

Change-Id:I0d81137723d0eacd97aa88bf08b69e12dd4d73faSigned-off-by: Benn Porscke <benn.porscke@stericsson.com>*/




//Synthetic comment -- diff --git a/src/com/android/settings/BrightnessPreference.java b/src/com/android/settings/BrightnessPreference.java
//Synthetic comment -- index 2cf6b43..62f716f 100644

//Synthetic comment -- @@ -120,6 +120,7 @@
mCheckBox.setChecked(mAutomaticMode);
mSeekBar.setEnabled(!mAutomaticMode || USE_SCREEN_AUTO_BRIGHTNESS_ADJUSTMENT);
} else {
            mCheckBox.setVisibility(View.GONE);
mSeekBar.setEnabled(true);
}
mSeekBar.setOnSeekBarChangeListener(this);







