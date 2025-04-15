/*Removing array copying with no real effect.

Change-Id:Ia07aa265a6e684a38e89d7697481dd1d18afd752*/




//Synthetic comment -- diff --git a/media/java/android/media/audiofx/AudioEffect.java b/media/java/android/media/audiofx/AudioEffect.java
//Synthetic comment -- index 3e54627..d6dda9b 100644

//Synthetic comment -- @@ -587,13 +587,7 @@
checkState("getParameter()");
int[] vSize = new int[1];
vSize[0] = value.length;
        return native_getParameter(param.length, param, vSize, value);
}

/**
//Synthetic comment -- @@ -749,15 +743,8 @@
int[] replySize = new int[1];
replySize[0] = reply.length;

        return native_command(cmdCode, command.length, command,
replySize, reply);
}

// --------------------------------------------------------------------------







