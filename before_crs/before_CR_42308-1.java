/*Initialize RIL with the correct CDMA subscription mode setting

Users choice of CDMA Subscription Source is stored in CDMA_SUBSCRIPTION_MODE
setting in database.
If telephony restarts after a crash, PhoneFactory sets the
CDMA subscription source value from PREFERRED_CDMA_SUBSCRIPTION
setting and that causes the mismatch with the value in the settings
database chosen by the user. The change is to use the
CDMA_SUBSCRIPTION_MODE setting instead when creating the RIL object.

Also, remove the Setting PREFERRED_CDMA_SUBSCRIPTION from the database.
With this change the special treatment for LTE on CDMA will not be needed.
The correct value can be set in the database for CDMA_SUBSCRIPTION_MODE
and that will be taken on power-up by this code.

Change-Id:I11fff596a5fe721c64f192c889672326517dc43d*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneFactory.java b/src/java/com/android/internal/telephony/PhoneFactory.java
//Synthetic comment -- index 2c85dc6..32d9028 100644

//Synthetic comment -- @@ -109,30 +109,11 @@
Settings.Secure.PREFERRED_NETWORK_MODE, preferredNetworkMode);
Log.i(LOG_TAG, "Network Mode set to " + Integer.toString(networkMode));

                // Get cdmaSubscription
                // TODO: Change when the ril will provides a way to know at runtime
                //       the configuration, bug 4202572. And the ril issues the
                //       RIL_UNSOL_CDMA_SUBSCRIPTION_SOURCE_CHANGED, bug 4295439.
int cdmaSubscription;
                int lteOnCdma = TelephonyManager.getLteOnCdmaModeStatic();
                switch (lteOnCdma) {
                    case PhoneConstants.LTE_ON_CDMA_FALSE:
                        cdmaSubscription = CdmaSubscriptionSourceManager.SUBSCRIPTION_FROM_NV;
                        Log.i(LOG_TAG, "lteOnCdma is 0 use SUBSCRIPTION_FROM_NV");
                        break;
                    case PhoneConstants.LTE_ON_CDMA_TRUE:
                        cdmaSubscription = CdmaSubscriptionSourceManager.SUBSCRIPTION_FROM_RUIM;
                        Log.i(LOG_TAG, "lteOnCdma is 1 use SUBSCRIPTION_FROM_RUIM");
                        break;
                    case PhoneConstants.LTE_ON_CDMA_UNKNOWN:
                    default:
                        //Get cdmaSubscription mode from Settings.System
                        cdmaSubscription = Settings.Secure.getInt(context.getContentResolver(),
                                Settings.Secure.PREFERRED_CDMA_SUBSCRIPTION,
preferredCdmaSubscription);
                        Log.i(LOG_TAG, "lteOnCdma not set, using PREFERRED_CDMA_SUBSCRIPTION");
                        break;
                }
Log.i(LOG_TAG, "Cdma Subscription set to " + cdmaSubscription);

//reads the system properties and makes commandsinterface







