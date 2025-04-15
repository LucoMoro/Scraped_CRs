/*noo
Signed-off-by: Yang Xingsui <yang.xingsui@zte.com.cn>

Change-Id:I4eb5b4e264138156afbda51bb43f83bdf458e512*/




//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/cts/ListeningPortsTest.java b/tests/tests/net/src/android/net/cts/ListeningPortsTest.java
//Synthetic comment -- index 2826b4e..b2f2469 100644

//Synthetic comment -- @@ -126,30 +126,14 @@
String localIp = localAddress.split(":")[0];
int localPort = Integer.parseInt(localAddress.split(":")[1], 16);

                /*ZTE__20121106,begin*/
/*/
/*if (!isException(localAddress) && isPortListening(state, isTcp)) {
throw new ListeningPortsAssertionError(
"Found port listening on addr=" + localIp + ", port="
+ localPort + ", UID=" + uid + " in " + procFilePath);
}*/
 
}
} catch (FileNotFoundException notFound) {
fail("Could not open file " + procFilePath + " to check for listening ports.");







