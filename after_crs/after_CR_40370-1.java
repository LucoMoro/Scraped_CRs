/*Do not set MMS proxy for LTE/EHRPD

Change-Id:I36f3e9e19183703972645d0666f11c884c218a66Signed-off-by: yc1024.choi <yc1024.choi@samsung.com>
Signed-off-by: Madan Ankapura <mankapur@sta.samsung.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/transaction/TransactionSettings.java b/src/com/android/mms/transaction/TransactionSettings.java
old mode 100644
new mode 100755
//Synthetic comment -- index 25e2b5c..460c70a

//Synthetic comment -- @@ -28,6 +28,7 @@

import android.net.NetworkUtils;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

//Synthetic comment -- @@ -94,17 +95,24 @@
sawValidApn = true;
mServiceCenter = NetworkUtils.trimV4AddrZeros(
cursor.getString(COLUMN_MMSC).trim());

                   TelephonyManager telephonyMgr = (TelephonyManager)context
                            .getSystemService(Context.TELEPHONY_SERVICE);
                    int networkType = telephonyMgr.getNetworkType();
                    if (!((networkType == TelephonyManager.NETWORK_TYPE_EHRPD)
                        || (networkType == TelephonyManager.NETWORK_TYPE_LTE))) {
                        mProxyAddress = NetworkUtils.trimV4AddrZeros(
                                cursor.getString(COLUMN_MMSPROXY));
                        if (isProxySet()) {
                            String portString = cursor.getString(COLUMN_MMSPORT);
                            try {
                                mProxyPort = Integer.parseInt(portString);
                            } catch (NumberFormatException e) {
                                if (TextUtils.isEmpty(portString)) {
                                    Log.w(TAG, "mms port not set!");
                                } else {
                                    Log.e(TAG, "Bad port number format: " + portString, e);
                                }
}
}
}







