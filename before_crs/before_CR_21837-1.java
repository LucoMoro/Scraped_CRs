/*CTS AudioTrackTest: Changing audiotrack buffer size from minBuffSize to 2*minBuffSize

This patch modifies the audio track buffer size for some test cases.
If the buffer size is not multiple of framesize in track.write, the audio track write function
hangs as frame count * framesize doesn't match the buffer size sent.
eg: if minbuffersize = 7524, buffersizesent = minbuffersize/2 = 3762

framecount = 3762/4(frame_size) = 940.5 (as it is fractional number considers as 940)

framecount*frame_size = 940*4=3760 and the write function hangs to get the remaining 2 bytes.

As buffersize is always multiple of frame_size, it is good to sent the entire buffer to track.write
function to avoid this kind of issues.

Change-Id:I6fda86ad12dc3b1e1735caa3bb527de87c7b3586Signed-off-by: karimuddin sayed <sayed.karimuddin@ti.com>*/
//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/AudioTrackTest.java b/tests/tests/media/src/android/media/cts/AudioTrackTest.java
//Synthetic comment -- index 4adc582..0316ef9 100644

//Synthetic comment -- @@ -394,8 +394,8 @@
// -------- initialization --------------
int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize / 2];
// -------- test --------------
assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
track.write(data, OFFSET_DEFAULT, data.length);
//Synthetic comment -- @@ -453,8 +453,8 @@
// -------- initialization --------------
int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize / 2];
// -------- test --------------
assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
track.write(data, OFFSET_DEFAULT, data.length);
//Synthetic comment -- @@ -519,8 +519,8 @@
// -------- initialization --------------
int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize / 2];
// -------- test --------------
assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
track.write(data, OFFSET_DEFAULT, data.length);
//Synthetic comment -- @@ -586,8 +586,8 @@
// -------- initialization --------------
int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize / 2];
// -------- test --------------
assertTrue(TEST_NAME, track.getState() == AudioTrack.STATE_INITIALIZED);
track.write(data, OFFSET_DEFAULT, data.length);
//Synthetic comment -- @@ -661,8 +661,8 @@
// -------- initialization --------------
int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize / 2];
// -------- test --------------
track.write(data, OFFSET_DEFAULT, data.length);
track.write(data, OFFSET_DEFAULT, data.length);
//Synthetic comment -- @@ -728,8 +728,8 @@
// -------- initialization --------------
int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize / 2];
// -------- test --------------
track.write(data, OFFSET_DEFAULT, data.length);
track.write(data, OFFSET_DEFAULT, data.length);
//Synthetic comment -- @@ -790,8 +790,8 @@
// -------- initialization --------------
int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize / 2];
// -------- test --------------

track.write(data, OFFSET_DEFAULT, data.length);
//Synthetic comment -- @@ -848,8 +848,8 @@
// -------- initialization --------------
int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize / 2];
// -------- test --------------
track.write(data, OFFSET_DEFAULT, data.length);
track.write(data, OFFSET_DEFAULT, data.length);
//Synthetic comment -- @@ -964,8 +964,8 @@
// -------- initialization --------------
int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize / 2];
int outputSR = AudioTrack.getNativeOutputSampleRate(TEST_STREAM_TYPE);
// -------- test --------------
track.write(data, OFFSET_DEFAULT, data.length);
//Synthetic comment -- @@ -1028,8 +1028,8 @@
// -------- initialization --------------
int minBuffSize = AudioTrack.getMinBufferSize(TEST_SR, TEST_CONF, TEST_FORMAT);
AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT,
                minBuffSize, TEST_MODE);
        byte data[] = new byte[minBuffSize / 2];
// -------- test --------------
track.write(data, OFFSET_DEFAULT, data.length);
track.write(data, OFFSET_DEFAULT, data.length);







