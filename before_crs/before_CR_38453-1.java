/*Fixed DDMS network statistics parsing

(cherry picked from commit 2c6188187236e0147423c4e0cdc2faaeaf40d32c)

Change-Id:I5d2f66da9a14696f663a4efa9836969e5cc7a60e*/
//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/net/NetworkPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/net/NetworkPanel.java
//Synthetic comment -- index ae28d07..15b8b56 100644

//Synthetic comment -- @@ -750,10 +750,11 @@
// iface and set are currently ignored, which groups those
// entries together.
final NetworkSnapshot.Entry entry = new NetworkSnapshot.Entry();
entry.iface = null; //cols[1];
                entry.uid = kernelToTag(cols[3]);
entry.set = -1; //Integer.parseInt(cols[4]);
                entry.tag = (int) (Long.decode(cols[2]) >> 32);
entry.rxBytes = Long.parseLong(cols[5]);
entry.rxPackets = Long.parseLong(cols[6]);
entry.txBytes = Long.parseLong(cols[7]);







