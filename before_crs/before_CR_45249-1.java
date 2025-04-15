/*Telephony: Add group qcom_oncrpc and give permissions to ril-daemon and /dev/oncrpc

Change-Id:I8f891fc9a3ee48845b11b8b77dab33c8d12c0e51*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/os/ZygoteInit.java b/core/java/com/android/internal/os/ZygoteInit.java
//Synthetic comment -- index 4924326..049d3d3 100644

//Synthetic comment -- @@ -478,7 +478,7 @@
String args[] = {
"--setuid=1000",
"--setgid=1000",
            "--setgroups=1001,1002,1003,1004,1005,1006,1007,1008,1009,1010,1018,3001,3002,3003,3006,3007",
"--capabilities=130104352,130104352",
"--runtime-init",
"--nice-name=system_server",







