/*Change max/min level range to allow the equalizer to be replaced

Audioeffect framework has a mechanism to replace the effect with
the enhanced version. Some equalizer can have a solid equalizing
implementation with appropriate band gains. Max/min level range
should not be limited with a fixed value. According to the OpenSL
ES 1.0.1 specification(http://www.khronos.org/opensles/), the
range returned by GetBandLevelRange must at least include 0mB.
So this fix changes to check if the maximum level range is not
negative and minimum level range is not positive.

Change-Id:I0145545bddc35db119c82817882ea26a35268645*/
//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/EqualizerTest.java b/tests/tests/media/src/android/media/cts/EqualizerTest.java
//Synthetic comment -- index 4b63828..955986e 100644

//Synthetic comment -- @@ -33,8 +33,8 @@

private String TAG = "EqualizerTest";
private final static int MIN_NUMBER_OF_BANDS = 4;
    private final static int MAX_LEVEL_RANGE_LOW = -1200;         // -12dB
    private final static int MIN_LEVEL_RANGE_HIGH = 1200;         // +12dB
private final static int TEST_FREQUENCY_MILLIHERTZ = 1000000; // 1kHz
private final static int MIN_NUMBER_OF_PRESETS = 0;
private final static float TOLERANCE = 100;                   // +/-1dB
//Synthetic comment -- @@ -597,4 +597,4 @@
}
}

}
\ No newline at end of file







