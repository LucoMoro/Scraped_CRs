/*Telephony: Initialize RIL with the correct CDMA subscription mode setting

If the telephony restarts after a crash, Android RIL sets the
CDMA subscription source value from the PREFERRED_CDMA_SUBSCRIPTION
setting and that causes the mismatch with the value in the settings
database chosen by the user. The change is to use the
CDMA_SUBSCRIPTION_MODE setting instead when creating the RIL object.

Also, remove the Setting PREFERRED_CDMA_SUBSCRIPTION from the database.

Change-Id:I0b5d9cf46b26a47c28f965f03d7b9e8abc3f2815*/
//Synthetic comment -- diff --git a/core/java/android/provider/Settings.java b/core/java/android/provider/Settings.java
//Synthetic comment -- index 7b0e560..c9391c5 100644

//Synthetic comment -- @@ -2698,14 +2698,6 @@
"cdma_cell_broadcast_sms";

/**
         * The cdma subscription 0 = Subscription from RUIM, when available
         *                       1 = Subscription from NV
         * @hide
         */
        public static final String PREFERRED_CDMA_SUBSCRIPTION =
                "preferred_cdma_subscription";

        /**
* Whether the enhanced voice privacy mode is enabled.
* 0 = normal voice privacy
* 1 = enhanced voice privacy








//Synthetic comment -- diff --git a/packages/SettingsProvider/src/com/android/providers/settings/DatabaseHelper.java b/packages/SettingsProvider/src/com/android/providers/settings/DatabaseHelper.java
//Synthetic comment -- index 2b4714d..013c4ec 100644

//Synthetic comment -- @@ -1119,10 +1119,6 @@
loadSetting(stmt, Settings.Secure.CDMA_CELL_BROADCAST_SMS,
RILConstants.CDMA_CELL_BROADCAST_SMS_DISABLED);

            // Set the preferred cdma subscription to 0 = Subscription from RUIM, when available
            loadSetting(stmt, Settings.Secure.PREFERRED_CDMA_SUBSCRIPTION,
                    RILConstants.PREFERRED_CDMA_SUBSCRIPTION);
    
// Don't do this.  The SystemServer will initialize ADB_ENABLED from a
// persistent system property instead.
//loadSetting(stmt, Settings.Secure.ADB_ENABLED, 0);








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneFactory.java b/telephony/java/com/android/internal/telephony/PhoneFactory.java
//Synthetic comment -- index 803b736..d26dd13 100644

//Synthetic comment -- @@ -99,9 +99,9 @@
Settings.Secure.PREFERRED_NETWORK_MODE, preferredNetworkMode);
Log.i(LOG_TAG, "Network Mode set to " + Integer.toString(networkMode));

                //Get preferredNetworkMode from Settings.System
int cdmaSubscription = Settings.Secure.getInt(context.getContentResolver(),
                        Settings.Secure.PREFERRED_CDMA_SUBSCRIPTION, preferredCdmaSubscription);
Log.i(LOG_TAG, "Cdma Subscription set to " + Integer.toString(cdmaSubscription));

//reads the system properties and makes commandsinterface







