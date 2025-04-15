/*gltrace: Update trace control protocol.

The trace control protocol requires that the size of the command
be sent before the command itself.

Change-Id:I75aa26289fcdb8cd23e1ee3a8a088b85599e186c*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/TraceCommandWriter.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/TraceCommandWriter.java
//Synthetic comment -- index f8d97b7..dd5cd3c 100644

//Synthetic comment -- @@ -25,6 +25,8 @@
* single integer. Any changes to this protocol have to be updated on the device as well.
*/
public class TraceCommandWriter {
private static final int READ_FB_ON_EGLSWAP_BIT = 0;
private static final int READ_FB_ON_GLDRAW_BIT = 1;
private static final int READ_TEXTURE_DATA_ON_GLTEXIMAGE_BIT = 2;
//Synthetic comment -- @@ -43,6 +45,7 @@

int cmd = eglSwap | glDraw | tex;

mStream.writeInt(cmd);
mStream.flush();
}







