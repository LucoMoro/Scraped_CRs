
//<Beginning of snippet n. 0>



import com.android.cts.stub.R;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;

import android.app.cts.CTSResult;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.Settings;
@TestTargets({
@TestTargetNew(
level = TestLevel.COMPLETE,
            method = "setStreamMute",
            args = {int.class, boolean.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setStreamSolo",
            args = {int.class, boolean.class}
        )
    })
    @BrokenTest("flaky")
    public void testMuteSolo() throws Exception {
        /**
         * this test must be run on screen unlocked model
         */
        AudioManagerStub.setCTSResult(this);
        Intent intent = new Intent();
        intent.setClass(mContext, AudioManagerStub.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        mSync.waitForResult();
        assertEquals(CTSResult.RESULT_OK, mResultCode);
    }

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
method = "setMicrophoneMute",
args = {boolean.class}
),

//<End of snippet n. 0>








