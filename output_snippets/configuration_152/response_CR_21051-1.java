//<Beginning of snippet n. 0>

import com.android.cts.stub.R;

import android.app.cts.CTSResult;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.Settings;

@TestTargets({
    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "setMicrophoneMute",
        args = {boolean.class}
    )
})

//<End of snippet n. 0>