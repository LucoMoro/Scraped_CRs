/*Corrected return value for updatePreferenceToSpecificActivityOrRemove method

Returned value should be false if preference is removed or was already deleted/missing.

Change-Id:I2a48bd8e0f5a82b76b60b2d11fac2305f0e27eedSigned-off-by: Shuhrat Dehkanov <uzbmaster@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/settings/Utils.java b/src/com/android/settings/Utils.java
//Synthetic comment -- index f28500e..582891d 100644

//Synthetic comment -- @@ -124,7 +124,7 @@
// Did not find a matching activity, so remove the preference
parentPreferenceGroup.removePreference(preference);

        return false;
}

/**







