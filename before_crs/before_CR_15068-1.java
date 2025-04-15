/*Prohibit Listening Ports on Devices

Bug 2732034

Check that devices do not have any listening ports open by
scanning files in the /proc/net directory.

Change-Id:Ic6204667809b3a0c136e38f35fe536bc6d79dcad*/
//Synthetic comment -- diff --git a/tests/tests/net/src/android/net/cts/ListeningPortsTest.java b/tests/tests/net/src/android/net/cts/ListeningPortsTest.java
new file mode 100644
//Synthetic comment -- index 0000000..16a3bea

//Synthetic comment -- @@ -0,0 +1,94 @@







