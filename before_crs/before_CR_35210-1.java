/*RadioInfo: Update the network type listing

The dropdown for the preferred type in the INFO menu is outdated.
ril.h and RILConstants now go up to type 11, due to the addition of
the LTE combinations.
If a device has one of these new types set up as the default, going
into this menu causes a crash for com.android.phone, since it attempts
to use array position "type" without verifying if it is that long.

Change-Id:I0bf4bb70e21aef79f22e7258c8154e3ec569a215*/
//Synthetic comment -- diff --git a/src/com/android/settings/RadioInfo.java b/src/com/android/settings/RadioInfo.java
//Synthetic comment -- index aa3f2e9..3d35db0 100644

//Synthetic comment -- @@ -195,7 +195,7 @@
int type = ((int[])ar.result)[0];
preferredNetworkType.setSelection(type, true);
} else {
                        preferredNetworkType.setSelection(8, true);
}
break;
case EVENT_SET_PREFERRED_TYPE_DONE:
//Synthetic comment -- @@ -1027,5 +1027,9 @@
"CDMA only",
"EvDo only",
"GSM/CDMA auto (PRL)",
"Unknown"};
}







