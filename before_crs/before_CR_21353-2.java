/*Removes unnecesary local variables and adds explanation for those suspicious statements.

Change-Id:Ib86f05b4c5a6f9d11960ddfa2b9db78246e58fc6*/
//Synthetic comment -- diff --git a/core/java/android/bluetooth/BluetoothDeviceProfileState.java b/core/java/android/bluetooth/BluetoothDeviceProfileState.java
//Synthetic comment -- index f6d7073..c721bb0 100644

//Synthetic comment -- @@ -177,8 +177,10 @@

mContext.registerReceiver(mBroadcastReceiver, filter);

        HeadsetServiceListener l = new HeadsetServiceListener();
        PbapServiceListener p = new PbapServiceListener();
}

private class HeadsetServiceListener implements BluetoothHeadset.ServiceListener {







