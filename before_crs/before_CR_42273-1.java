/*Make AudioGroup.add locale safe

Explicitly use Locale.US in AudioGroup.add to avoid
unexpected results in some locales.

Change-Id:Ifb477ca590f630747e09e38ac2246d284b5c5bfc*/
//Synthetic comment -- diff --git a/voip/java/android/net/rtp/AudioGroup.java b/voip/java/android/net/rtp/AudioGroup.java
//Synthetic comment -- index 8c19062..8faeb88 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import android.media.AudioManager;

import java.util.HashMap;
import java.util.Map;

/**
//Synthetic comment -- @@ -146,7 +147,7 @@
if (!mStreams.containsKey(stream)) {
try {
AudioCodec codec = stream.getCodec();
                String codecSpec = String.format("%d %s %s", codec.type,
codec.rtpmap, codec.fmtp);
int id = nativeAdd(stream.getMode(), stream.getSocket(),
stream.getRemoteAddress().getHostAddress(),







