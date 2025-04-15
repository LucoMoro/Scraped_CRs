/*Monkey: Fix to avoid generating bug report for every event input

bugreport is generated once an app crashes and monkey is intimated
by activity manager service. From this point, bugreport is generated
for every event input as mRequestAppCrashBugreport stays true. Fix
is to reset the same.

Change-Id:I29b5f138c5862c27a0f2c3fbe4e5398e091cac67*/




//Synthetic comment -- diff --git a/cmds/monkey/src/com/android/commands/monkey/Monkey.java b/cmds/monkey/src/com/android/commands/monkey/Monkey.java
//Synthetic comment -- index 68fb2a5..9f87f80 100644

//Synthetic comment -- @@ -615,7 +615,7 @@
}
if (mRequestAppCrashBugreport){
getBugreport("app_crash" + mReportProcessName + "_");
                mRequestAppCrashBugreport = false;
}
if (mRequestDumpsysMemInfo) {
reportDumpsysMemInfo();
//Synthetic comment -- @@ -1003,7 +1003,7 @@
}
if (mRequestAppCrashBugreport){
getBugreport("app_crash" + mReportProcessName + "_");
                    mRequestAppCrashBugreport = false;
}
if (mRequestDumpsysMemInfo) {
mRequestDumpsysMemInfo = false;







