/*fix AudioTrackProperties test

the return value of the track.getNativeFrameCount() is dependent
on the chipset, and for chipsets which use 4K buffers, almost always
this test case fails as the native frame count ends up being 2048 which
happens to be less then 2400.

Change-Id:I78c2bf42e15942b58210bf44e8a7e6a2b4aa4eb9Signed-off-by: madan ankapura <mankapur@sta.samsung.com>*/
//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/AudioTrackTest.java b/tests/tests/media/src/android/media/cts/AudioTrackTest.java
//Synthetic comment -- index d3ace05..328b955 100644

//Synthetic comment -- @@ -2485,7 +2485,7 @@
assertEquals(TEST_NAME, AudioTrack.STATE_NO_STATIC_DATA, track.getState());
track.setState(AudioTrack.STATE_UNINITIALIZED);
assertEquals(TEST_NAME, AudioTrack.STATE_UNINITIALIZED, track.getState());
        final int nativeFrameCount = 2400;
assertTrue(TEST_NAME, track.getNativeFrameCount() >= nativeFrameCount);
}

//Synthetic comment -- @@ -2614,4 +2614,4 @@
}
}

}
\ No newline at end of file







