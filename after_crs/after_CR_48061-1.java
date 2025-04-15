/*telephony: Fix typo in SignalStrength/CellSignalStrengthLte.java

getLteSignalStrenght() -> getLteSignalStrength()

Change-Id:I4288557b3c4bcb4bacfe00109f6b2d94b6301d7e*/




//Synthetic comment -- diff --git a/telephony/java/android/telephony/CellSignalStrengthLte.java b/telephony/java/android/telephony/CellSignalStrengthLte.java
//Synthetic comment -- index 55680c8..b456bb3 100644

//Synthetic comment -- @@ -94,7 +94,7 @@
* @hide
*/
public void initialize(SignalStrength ss, int timingAdvance) {
        mSignalStrength = ss.getLteSignalStrength();
mRsrp = ss.getLteRsrp();
mRsrq = ss.getLteRsrq();
mRssnr = ss.getLteRssnr();








//Synthetic comment -- diff --git a/telephony/java/android/telephony/SignalStrength.java b/telephony/java/android/telephony/SignalStrength.java
//Synthetic comment -- index e2da53e..85e2d57 100644

//Synthetic comment -- @@ -411,7 +411,7 @@
}

/** @hide */
    public int getLteSignalStrength() {
return mLteSignalStrength;
}








