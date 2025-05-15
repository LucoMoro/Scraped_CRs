//<Beginning of snippet n. 0>


import android.media.AudioManager;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
if (!mStreams.containsKey(stream)) {
try {
    AudioCodec codec = stream.getCodec();
    String codecSpec = String.format(Locale.US, "%d %s %s", codec.type,
            codec.rtpmap, codec.fmtp);
    int id = nativeAdd(stream.getMode(), stream.getSocket(),
            stream.getRemoteAddress().getHostAddress(),
//<End of snippet n. 0>
