/*Signed-off-by: Yang Xingsui <yang.xingsui@zte.com.cn>

solve AudioRecord_BufferSizeTest.testGetMinBufferSize failed

In AudioRecord_BufferSizeTest, AudioRecord instances were constructed and de-constructed for 4 times continuously with no delay. This requires jni and native system to response quickly. If sometimes, someone can’t response in time, there will be more than one AudioRecord/AudioStreamInMSM72xx instances. But it’s not supported on our MSM7x27A platform and raises an “Could not get audio input for record source x” error.

Change-Id:Icbbec693b1fe0852ee4cc97c2136aacaf4a5bccc*/




//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/AudioRecord_BufferSizeTest.java b/tests/tests/media/src/android/media/cts/AudioRecord_BufferSizeTest.java
//Synthetic comment -- index e597827..ed87622 100644

//Synthetic comment -- @@ -50,6 +50,7 @@
for (int i = 0; i < SAMPLE_RATES_IN_HZ.length; i++) {
try {
record(SAMPLE_RATES_IN_HZ[i]);
                Thread.sleep(1000); // insert 1000ms delay
} catch (Throwable e) {
Log.e(TAG, "Sample rate: " + SAMPLE_RATES_IN_HZ[i], e);
failedSampleRates.add(SAMPLE_RATES_IN_HZ[i]);
//Synthetic comment -- @@ -76,6 +77,7 @@
checkRecordingState(AudioRecord.RECORDSTATE_STOPPED);

mAudioRecord.release();
        mAudioRecord = null;
}

private void createAudioRecord(final int sampleRateInHz, final int bufferSize) {







