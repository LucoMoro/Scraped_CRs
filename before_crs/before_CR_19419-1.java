/*Telephony: Initialize RIL with the correct CDMA subscription mode setting

If the telephony restarts after a crash, Android RIL sets the
CDMA subscription source value from the PREFERRED_CDMA_SUBSCRIPTION
setting and that causes the mismatch with the value in the settings
database chosen by the user. The change is to use the
CDMA_SUBSCRIPTION_MODE setting instead when creating the RIL object.

Change-Id:I0b5d9cf46b26a47c28f965f03d7b9e8abc3f2815*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneFactory.java b/telephony/java/com/android/internal/telephony/PhoneFactory.java
//Synthetic comment -- index 803b736..b369b38 100644

//Synthetic comment -- @@ -101,7 +101,7 @@

//Get preferredNetworkMode from Settings.System
int cdmaSubscription = Settings.Secure.getInt(context.getContentResolver(),
                        Settings.Secure.PREFERRED_CDMA_SUBSCRIPTION, preferredCdmaSubscription);
Log.i(LOG_TAG, "Cdma Subscription set to " + Integer.toString(cdmaSubscription));

//reads the system properties and makes commandsinterface







