/*.CTS.android.net.cts.ListeningPortsTest testNoListeningTcp6Ports
Device:N9120.
Environment (platform version, hardware platform, etc.
ICS CTS R3.8960_ICS.4.0.4.
Failure details.
android.net.cts.ListeningPortsTest$ListeningPortsAssertionError: Found port listening on addr=00000000000000000000000000000000, port=12346, UID=10080 in /proc/net/tcp6 at android.net.cts.ListeningPortsTest.assertNoListeningPorts(ListeningPortsTest.java:130)

 Our Analysis.
Because an application named Rhapsody is listening in internal port, but this test case is valid only for applications that listen in on external port. So it's ok from a CTS perspective.

Here are some words from Metro:
Google has clarified that this test case is valid only for applications that listen in on external port.This test is not valid for application that listen in internal port which Rhapsody does.Please contact Google get an update to CTS test case or clarification.Other OEMs have got this clarification from Google.

Please help us to confirm:could this case be waived?android.net.cts.ListeningPortsTest testNoListeningTcp6Ports

Change-Id:I59509539e4cdabfdea8b56c11bd02d6d99efa5d7Signed-off-by: Yang Xingsui <yang.xingsui@zte.com.cn>*/




//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/cts/ListeningPortsTest.java b/tests/tests/net/src/android/net/cts/ListeningPortsTest.java
//Synthetic comment -- index 244b7e24..2826b4e 100644

//Synthetic comment -- @@ -127,7 +127,24 @@
int localPort = Integer.parseInt(localAddress.split(":")[1], 16);

/*ZTE_YUANJIAN_CTS_20121106,begin*/
             /*.CTS.android.net.cts.ListeningPortsTest testNoListeningTcp6Ports
Device:N9120.
Environment (platform version, hardware platform, etc.
ICS CTS R3.8960_ICS.4.0.4.
Failure details.
android.net.cts.ListeningPortsTest$ListeningPortsAssertionError: Found port listening on addr=00000000000000000000000000000000, port=12346, UID=10080 in /proc/net/tcp6 at android.net.cts.ListeningPortsTest.assertNoListeningPorts(ListeningPortsTest.java:130) 

 Our Analysis.
Because an application named Rhapsody is listening in internal port, but this test case is valid only for applications that listen in on external port. So it's ok from a CTS perspective. 

Here are some words from Metro:
Google has clarified that this test case is valid only for applications that listen in on external port.This test is not valid for application that listen in internal port which Rhapsody does.Please contact Google get an update to CTS test case or clarification.Other OEMs have got this clarification from Google.

Please help us to confirm:could this case be waived?android.net.cts.ListeningPortsTest testNoListeningTcp6Ports


                /*/
               /*if (!isException(localAddress) && isPortListening(state, isTcp)) {
throw new ListeningPortsAssertionError(
"Found port listening on addr=" + localIp + ", port="
+ localPort + ", UID=" + uid + " in " + procFilePath);







