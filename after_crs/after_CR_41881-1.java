/*Remove optional video encoder: MPEG-4 SP

Change-Id:I5b4594a3cb1d4f5f9bddf25cb8bbce27776c8328Signed-off-by: sammi_ms <Sammi_MS@asus.com>*/




//Synthetic comment -- diff --git a/tests/tests/media/src/android/media/cts/MediaCodecListTest.java b/tests/tests/media/src/android/media/cts/MediaCodecListTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index d89d992..e6abe31

//Synthetic comment -- @@ -182,7 +182,6 @@
list.add(new CodecType("video/3gpp", false));           // h263 decoder
list.add(new CodecType("video/3gpp", true));            // h263 encoder
list.add(new CodecType("video/mp4v-es", false));        // m4v decoder
list.add(new CodecType("video/x-vnd.on2.vp8", false));  // vp8 decoder

return list;







