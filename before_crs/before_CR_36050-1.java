/*Avoid crash system_server when loading invalid batterystats.bin at boot time.

A batterystats.bin file is loaded at BatteryStatsService.
OutOfMemoryError or NegativeArraySizeException may throw
if invalid value is assigned to sNumSpeedSteps or NSB variable.
And also check negative another variables in BatteryStatsImpl.readSummaryFromParcel(Parcel in).

Change-Id:Id01b740084229a29d279f738c21377029828b0f1*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/os/BatteryStatsImpl.java b/core/java/com/android/internal/os/BatteryStatsImpl.java
//Synthetic comment -- index fec4cbc..01200eb 100644

//Synthetic comment -- @@ -5085,8 +5085,8 @@
mBluetoothOnTimer.readSummaryFromParcelLocked(in);

int NKW = in.readInt();
        if (NKW > 10000) {
            Slog.w(TAG, "File corrupt: too many kernel wake locks " + NKW);
return;
}
for (int ikw = 0; ikw < NKW; ikw++) {
//Synthetic comment -- @@ -5097,10 +5097,15 @@
}

sNumSpeedSteps = in.readInt();

final int NU = in.readInt();
        if (NU > 10000) {
            Slog.w(TAG, "File corrupt: too many uids " + NU);
return;
}
for (int iu = 0; iu < NU; iu++) {
//Synthetic comment -- @@ -5143,8 +5148,8 @@
}

int NW = in.readInt();
            if (NW > 100) {
                Slog.w(TAG, "File corrupt: too many wake locks " + NW);
return;
}
for (int iw = 0; iw < NW; iw++) {
//Synthetic comment -- @@ -5161,8 +5166,8 @@
}

int NP = in.readInt();
            if (NP > 1000) {
                Slog.w(TAG, "File corrupt: too many sensors " + NP);
return;
}
for (int is = 0; is < NP; is++) {
//Synthetic comment -- @@ -5174,8 +5179,8 @@
}

NP = in.readInt();
            if (NP > 1000) {
                Slog.w(TAG, "File corrupt: too many processes " + NP);
return;
}
for (int ip = 0; ip < NP; ip++) {
//Synthetic comment -- @@ -5185,8 +5190,8 @@
p.mSystemTime = p.mLoadedSystemTime = in.readLong();
p.mStarts = p.mLoadedStarts = in.readInt();
int NSB = in.readInt();
                if (NSB > 100) {
                    Slog.w(TAG, "File corrupt: too many speed bins " + NSB);
return;
}
p.mSpeedBins = new SamplingCounter[NSB];
//Synthetic comment -- @@ -5202,8 +5207,8 @@
}

NP = in.readInt();
            if (NP > 10000) {
                Slog.w(TAG, "File corrupt: too many packages " + NP);
return;
}
for (int ip = 0; ip < NP; ip++) {
//Synthetic comment -- @@ -5211,8 +5216,8 @@
Uid.Pkg p = u.getPackageStatsLocked(pkgName);
p.mWakeups = p.mLoadedWakeups = in.readInt();
final int NS = in.readInt();
                if (NS > 1000) {
                    Slog.w(TAG, "File corrupt: too many services " + NS);
return;
}
for (int is = 0; is < NS; is++) {







