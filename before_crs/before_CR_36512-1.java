/*gldebugger: Check for zero sized framebuffer

Change-Id:Ic6e98ddf98ec212eaff273177c0910456b9dba9e*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/ProtoBufUtils.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/ProtoBufUtils.java
//Synthetic comment -- index 7165e63..ed6a277 100644

//Synthetic comment -- @@ -30,6 +30,10 @@
int width = glMsg.getFb().getWidth();
int height = glMsg.getFb().getHeight();

byte[] compressed = glMsg.getFb().getContents(0).toByteArray();
byte[] uncompressed = new byte[width * height * 4];

//Synthetic comment -- @@ -59,7 +63,12 @@
return null;
}

        return new Image(display, getImageData(glMsg));
}

/**
//Synthetic comment -- @@ -72,6 +81,10 @@
}

ImageData imageData = getImageData(glMsg);
return new Image(display, imageData.scaledTo(width, height));
}
}







