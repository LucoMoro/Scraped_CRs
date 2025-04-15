/*Allow runtime loading of cpufreq driver

Android assumes that cpufreq driver is loaded during boot
time. Loading the driver after android is booted to UI
results in a crash in ActivityManager.The commit handles the
case when the driver is loaded after android is booted to UI.

This issue was observe on vexpress_rtsm platform:https://bugs.launchpad.net/linaro-big-little-integrated/+bug/1000374Change-Id:Ibf526d6c2722cea36d0b2075f100019989243c76Signed-off-by: Vishal Bhoj <vishal.bhoj@linaro.org>*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/os/ProcessStats.java b/core/java/com/android/internal/os/ProcessStats.java
//Synthetic comment -- index 9490437..762e1aa 100644

//Synthetic comment -- @@ -569,6 +569,10 @@
while (st.hasMoreElements()) {
String token = st.nextToken();
try {
long val = Long.parseLong(token);
tempSpeeds[speed] = val;
token = st.nextToken();
//Synthetic comment -- @@ -585,7 +589,7 @@
}
}
}
        if (out == null) {
out = new long[speed];
mCpuSpeeds = new long[speed];
System.arraycopy(tempSpeeds, 0, mCpuSpeeds, 0, speed);







