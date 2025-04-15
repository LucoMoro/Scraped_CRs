/*Fix case-sensitive alphabetic order in vCard listing

This patch fixes case sensitive alphabetic order (when
lower cases where placed at the end) for vCard xml listing.

Change-Id:Ia9fd4cbac4b0c8a2300193a731c1eefaf4bbf00fSigned-off-by: Christian Bejram <christian.bejram@stericsson.com>*/
//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapObexServer.java b/src/com/android/bluetooth/pbap/BluetoothPbapObexServer.java
//Synthetic comment -- index 6002263..6004dd9 100755

//Synthetic comment -- @@ -44,6 +44,7 @@
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.obex.ServerRequestHandler;
import javax.obex.ResponseCodes;
//Synthetic comment -- @@ -623,6 +624,17 @@
if (searchValue != null) {
compareValue = searchValue.trim();
}
for (int pos = listStartOffset; pos < listSize &&
itemsFound < requestSize; pos++) {
currentValue = nameList.get(pos);







