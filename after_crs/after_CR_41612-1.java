/*Fix for updating TCP buffer size when switching network type

When PDP connects in GSM network, the TCP buffer window size is
set to max 8760. If the phone swicthes to UMTS, the TCP buffer
size is not updated to UMTS, i.e. GPRS buffer size is still
used with limited data transfer spead as a result.
This fix makes sure the TCP buffer size is updated when
switching network type.

Change-Id:Iaa527c84f56cae108f4c7276bbed10552faaff6c*/




//Synthetic comment -- diff --git a/core/java/android/net/MobileDataStateTracker.java b/core/java/android/net/MobileDataStateTracker.java
//Synthetic comment -- index 5612943..ff02920 100644

//Synthetic comment -- @@ -184,8 +184,17 @@
if (!TextUtils.equals(apnType, mApnType)) {
return;
}

                int oldSubtype = mNetworkInfo.getSubtype();
                int newSubType = TelephonyManager.getDefault().getNetworkType();
                String subTypeName = TelephonyManager.getDefault().getNetworkTypeName();
                mNetworkInfo.setSubtype(newSubType, subTypeName);
                if (newSubType != oldSubtype && mNetworkInfo.isConnected()) {
                    Message msg = mTarget.obtainMessage(EVENT_NETWORK_SUBTYPE_CHANGED,
                                                        oldSubtype, 0, mNetworkInfo);
                    msg.sendToTarget();
                }

PhoneConstants.DataState state = Enum.valueOf(PhoneConstants.DataState.class,
intent.getStringExtra(PhoneConstants.STATE_KEY));
String reason = intent.getStringExtra(PhoneConstants.STATE_CHANGE_REASON_KEY);








//Synthetic comment -- diff --git a/core/java/android/net/NetworkStateTracker.java b/core/java/android/net/NetworkStateTracker.java
//Synthetic comment -- index 7df0193..0d6dcd6 100644

//Synthetic comment -- @@ -69,6 +69,12 @@
public static final int EVENT_RESTORE_DEFAULT_NETWORK = 6;

/**
     * msg.what = EVENT_NETWORK_SUBTYPE_CHANGED
     * msg.obj = NetworkInfo object
     */
    public static final int EVENT_NETWORK_SUBTYPE_CHANGED = 7;

    /**
* -------------------------------------------------------------
* Control Interface
* -------------------------------------------------------------








//Synthetic comment -- diff --git a/services/java/com/android/server/ConnectivityService.java b/services/java/com/android/server/ConnectivityService.java
//Synthetic comment -- index 230f07b..86ada40 100644

//Synthetic comment -- @@ -2490,6 +2490,11 @@
//       @see bug/4455071
handleConnectivityChange(info.getType(), false);
break;
                case NetworkStateTracker.EVENT_NETWORK_SUBTYPE_CHANGED:
                    info = (NetworkInfo) msg.obj;
                    type = info.getType();
                    updateNetworkSettings(mNetTrackers[type]);
                    break;
case EVENT_CLEAR_NET_TRANSITION_WAKELOCK:
String causedBy = null;
synchronized (ConnectivityService.this) {







