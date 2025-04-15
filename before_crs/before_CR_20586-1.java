/*100ms does not gurantee trigerring the audio. the test always passed with 200ms*/
//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/AudioTrackTest.java b/tests/tests/media/src/android/media/cts/AudioTrackTest.java
//Synthetic comment -- index 12cfcad..4adc582 100644

//Synthetic comment -- @@ -29,7 +29,7 @@
@TestTargetClass(AudioTrack.class)
public class AudioTrackTest extends AndroidTestCase {
private String TAG = "AudioTrackTest";
    private final long WAIT_MSEC = 100;
private final int OFFSET_DEFAULT = 0;
private final int OFFSET_NEGATIVE = -10;

//Synthetic comment -- @@ -2620,4 +2620,4 @@
}
}

}
\ No newline at end of file







