/*TextToSpeech memory leak

The callback object needs to be out of the class so that it won't
get a strong reference on the TextToSpeech object because it leads
to a memory leak due to the fact that the callback's lifecycle is
linked to the server and not to the TextToSpeech and then, keeps it
from being GCed.

Change-Id:I9cd5c7726d7cf2f60d6e25cc5caa87ad825e8f83Author: Jean-Christophe PINCE <jean-christophe.pince@intel.com>
Signed-off-by: Jean-Christophe PINCE <jean-christophe.pince@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 57869*/




//Synthetic comment -- diff --git a/core/java/android/speech/tts/TextToSpeech.java b/core/java/android/speech/tts/TextToSpeech.java
//Synthetic comment -- index 5e367cb..7c3e7bb 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import android.text.TextUtils;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
//Synthetic comment -- @@ -1188,6 +1189,7 @@
@Deprecated
public int setOnUtteranceCompletedListener(final OnUtteranceCompletedListener listener) {
mUtteranceProgressListener = UtteranceProgressListener.from(listener);
        mCallback.setUtteranceProgressListener(mUtteranceProgressListener);
return TextToSpeech.SUCCESS;
}

//Synthetic comment -- @@ -1203,6 +1205,7 @@
*/
public int setOnUtteranceProgressListener(UtteranceProgressListener listener) {
mUtteranceProgressListener = listener;
        mCallback.setUtteranceProgressListener(mUtteranceProgressListener);
return TextToSpeech.SUCCESS;
}

//Synthetic comment -- @@ -1253,34 +1256,8 @@
return mEnginesHelper.getEngines();
}

private class Connection implements ServiceConnection {
private ITextToSpeechService mService;

@Override
public void onServiceConnected(ComponentName name, IBinder service) {
//Synthetic comment -- @@ -1394,4 +1371,38 @@

}

    private final TextToSpeechCallback mCallback = new TextToSpeechCallback();
}

class TextToSpeechCallback extends ITextToSpeechCallback.Stub {

    private volatile WeakReference<UtteranceProgressListener> mUtteranceProgressListener = null;

    public void setUtteranceProgressListener(UtteranceProgressListener aUtteranceProgressListener) {
        mUtteranceProgressListener = new WeakReference<UtteranceProgressListener>(aUtteranceProgressListener);
    }

    @Override
    public void onDone(String utteranceId) {
        UtteranceProgressListener listener = mUtteranceProgressListener.get();
        if (listener != null) {
            listener.onDone(utteranceId);
        }
    }

    @Override
    public void onError(String utteranceId) {
        UtteranceProgressListener listener = mUtteranceProgressListener.get();
        if (listener != null) {
            listener.onError(utteranceId);
        }
    }

    @Override
    public void onStart(String utteranceId) {
        UtteranceProgressListener listener = mUtteranceProgressListener.get();
        if (listener != null) {
            listener.onStart(utteranceId);
        }
    }
}







