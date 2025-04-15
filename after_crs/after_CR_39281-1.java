/*Fix wrong count return for getPhoneSignalStrengthCount

Test report for the time spent in each signal strength bin, along
with the number of times that bin was entered showed the result
as Bin=3 Time=3211926000 Count=0. With a non-zero Time, the Count
value 0 was wrong.

The cause of the problem was that getPhoneSignalStrengthCount() used
mPhoneDataConnectionsTimer, instead of mPhoneSignalStrengthsTimer,
to get the count.

Change-Id:I55ac1125abfcfdc105605d76d1c706ac315b90cc*/




//Synthetic comment -- diff --git a/core/java/com/android/internal/os/BatteryStatsImpl.java b/core/java/com/android/internal/os/BatteryStatsImpl.java
//Synthetic comment -- index 5157385..db752e9 100644

//Synthetic comment -- @@ -2302,8 +2302,8 @@
batteryRealtime, which);
}

    @Override public int getPhoneSignalStrengthCount(int strengthBin, int which) {
        return mPhoneSignalStrengthsTimer[strengthBin].getCountLocked(which);
}

@Override public long getPhoneDataConnectionTime(int dataType,







