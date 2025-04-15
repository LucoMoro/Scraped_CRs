/*Don't always use wifi as mNetworkPreference. This value should be
updated according to user's networkAttributes setting from overlay
config.xml when connectivityservice start. Or if we add a higher
preference network type than wifi(like Ethernet), the policy won't
play its role to tear down wifi connection.

Change-Id:I9263c8675f76a1080ae6db5d7b5a9ef403699c4bSigned-off-by: Jianzheng Zhou <jianzheng.zhou@freescale.com>*/
//Synthetic comment -- diff --git a/services/java/com/android/server/ConnectivityService.java b/services/java/com/android/server/ConnectivityService.java
//Synthetic comment -- index ad1dfb2..ab866af 100644

//Synthetic comment -- @@ -489,6 +489,8 @@
continue;
}
mPriorityList[insertionPoint--] = na.type;
}
currentLowest = nextLowest;
nextLowest = 0;







