/*Just adding TODO; someone should review this.

Change-Id:If9d53677d3d297dbf4c095fe3afc971cc9a3ac5c*/




//Synthetic comment -- diff --git a/media/java/android/media/audiofx/AudioEffect.java b/media/java/android/media/audiofx/AudioEffect.java
//Synthetic comment -- index 3e54627..615a57f 100644

//Synthetic comment -- @@ -755,7 +755,7 @@
if (reply.length > replySize[0]) {
byte[] resizedReply = new byte[replySize[0]];
System.arraycopy(reply, 0, resizedReply, 0, replySize[0]);
            reply = resizedReply; // TODO: THIS IS LIKELY A BUG - IT DOES NOT WORK LIKE INTENDED. IN REALITY, THIS HAS _NO_ EFFECT.
}
return status;
}







