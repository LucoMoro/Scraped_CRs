/*gltrace: Skip framebuffers that cannot be retrieved.

Rather than failing to parse the entire file, just ignore the
fb image if it can't be parsed. This should allow viewing the rest
of the data in the trace except for just the image.

Change-Id:I0d875cf45e09a29022b5b9a18d33601bb92225a5*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/ProtoBufUtils.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/ProtoBufUtils.java
//Synthetic comment -- index ed6a277..da0b502 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.gltrace;

import com.android.ide.eclipse.gldebugger.GlTracePlugin;
import com.android.ide.eclipse.gltrace.GLProtoBuf.GLMessage;

import org.eclipse.swt.graphics.Image;
//Synthetic comment -- @@ -80,7 +81,14 @@
return null;
}

        ImageData imageData = null;
        try {
            imageData = getImageData(glMsg);
        } catch (Exception e) {
            GlTracePlugin.getDefault().logMessage(
                    "Unexpected error while retrieving framebuffer image " + e.getMessage());
        }

if (imageData == null) {
return null;
}







