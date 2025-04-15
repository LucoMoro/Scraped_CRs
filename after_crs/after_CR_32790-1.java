/*MediaPlayerTest: Improve timestamp logic in testVideoSurfaceResetting

In android.media.cts.MediaPlayerTest#testVideoSurfaceResetting, the
code was assuming that:

   1. The media position is not updated between invocations of
      MediaPlayer.getCurrentPosition and MediaPlayer.setDisplay().

   2. Seeks happen immediately.

These are both false assumptions.

This patch sets a tolerance when comparing the results of
getCurrentPosition().  The tolerance is set to the time for 1.5 frames
of a 15 fps video to elapse.

This patch also waits for the seek complete signal from the media
before continuing with the test.  As a consequence, it is able to use
a lower tolerance when comparing the results after the seek.

Change-Id:I528577b5b92162b4c1a1ba81af801846bacd89d2Signed-off-by: Gabriel M. Beddingfield <gabrbedd@ti.com>*/




//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/MediaPlayerTest.java b/tests/tests/media/src/android/media/cts/MediaPlayerTest.java
//Synthetic comment -- index 9947592..d922d71 100644

//Synthetic comment -- @@ -36,6 +36,8 @@
* Attribution 3.0 License at http://creativecommons.org/licenses/by/3.0/us/.
*/
public class MediaPlayerTest extends MediaPlayerTestBase {
    private boolean mSeekDone;

public void testPlayNullSource() throws Exception {
try {
mMediaPlayer.setDataSource((String) null);
//Synthetic comment -- @@ -109,9 +111,16 @@
* from the time setDisplay() was called
*/
public void testVideoSurfaceResetting() throws Exception {
        final int tolerance = 66 * 3 / 2; /* Test video is 15fps... 66 ms per frame */
final int seekPos = 1500;

        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                mSeekDone = true;
            }
        });

playVideoTest(R.raw.testvideo, 352, 288);

mMediaPlayer.start();
//Synthetic comment -- @@ -121,19 +130,23 @@
mMediaPlayer.setDisplay(getActivity().getSurfaceHolder2());
int posAfter = mMediaPlayer.getCurrentPosition();

        assertEquals(posAfter, posBefore, tolerance);
assertTrue(mMediaPlayer.isPlaying());

Thread.sleep(SLEEP_TIME);

        mSeekDone = false;
mMediaPlayer.seekTo(seekPos);
        while (!mSeekDone) {
            /* busy wait */
        }
Thread.sleep(SLEEP_TIME / 2);

posBefore = mMediaPlayer.getCurrentPosition();
mMediaPlayer.setDisplay(null);
posAfter = mMediaPlayer.getCurrentPosition();

        assertEquals(posAfter, posBefore, tolerance);
assertEquals(seekPos + SLEEP_TIME / 2, posBefore, tolerance);
assertTrue(mMediaPlayer.isPlaying());

//Synthetic comment -- @@ -143,7 +156,7 @@
mMediaPlayer.setDisplay(getActivity().generateSurfaceHolder());
posAfter = mMediaPlayer.getCurrentPosition();

        assertEquals(posAfter, posBefore, tolerance);
assertTrue(mMediaPlayer.isPlaying());

Thread.sleep(SLEEP_TIME);







