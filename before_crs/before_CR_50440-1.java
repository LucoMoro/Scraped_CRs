/*Reset data stall watch dog after doing recovery

Without resetting data stall watch dog, one data
stall will lead to radio reset if there is no
data traffic after doing recovery.

Change-Id:Icc7b1e7f160cd0c8910f7132d7c2fea0d29803e5Signed-off-by: Bin Li <libin@marvell.com>*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/DataConnectionTracker.java b/src/java/com/android/internal/telephony/DataConnectionTracker.java
//Synthetic comment -- index f1855d2..ec83723 100644

//Synthetic comment -- @@ -1295,6 +1295,7 @@
throw new RuntimeException("doRecovery: Invalid recoveryAction=" +
recoveryAction);
}
}
}








