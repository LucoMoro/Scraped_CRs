//<Beginning of snippet n. 0>
import android.media.AudioManager;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AudioGroup {
    private Map<Integer, Stream> mStreams = new HashMap<>();

    public void add(Stream stream) {
        if (!mStreams.containsKey(stream)) {
            try {
                AudioCodec codec = stream.getCodec();
                String codecSpec = String.format(Locale.US, "%d %s %s", codec.type, codec.rtpmap, codec.fmtp);
                int id = nativeAdd(stream.getMode(), stream.getSocket(), stream.getRemoteAddress().getHostAddress(), codecSpec);
                mStreams.put(id, stream);
            } catch (Exception e) {
                // Handle exception
            }
        }
    }

    private native int nativeAdd(int mode, Socket socket, String remoteAddress, String codecSpec);
}
//<End of snippet n. 0>