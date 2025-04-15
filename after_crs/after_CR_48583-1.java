/*if the source is hls or rtsp,do not skip prepare when enter the full screen mode

Now, HLS and rtsp use nuplayer to play.Since nuplayer has not such function like reset display during playing,
when enter the full screen mode from inline mode in HTML5, the preparation should not be skipped.
If a new display needs to be setted, the old player must be resetted
Signed-off-by: ywan171 <yi.a.wang@intel.com>

Change-Id:I23de1cec9bff7d5f93839ccb118cc4d6477f02feAuthor: Yi Wang <yi.a.wang@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 52762*/




//Synthetic comment -- diff --git a/core/java/android/webkit/HTML5VideoViewProxy.java b/core/java/android/webkit/HTML5VideoViewProxy.java
//Synthetic comment -- index a3d62ae..cdf8741 100644

//Synthetic comment -- @@ -167,6 +167,15 @@
|| playerState == HTML5VideoView.STATE_PLAYING;
}
}
                if (canSkipPrepare) {
                    // since nuplayer can not support setting display after player starts,for
                    // HLS and RTSP which uses nuplayer,it should not skip prepare.
                    if (((url.startsWith("https://") || url.startsWith("http://"))
                         && url.endsWith(".m3u8")) || url.startsWith("rtsp://")) {
                         canSkipPrepare = false;
                         mHTML5VideoView.reset();
                    }
                }
mHTML5VideoView = new HTML5VideoFullScreen(proxy.getContext(),
layerId, savePosition, canSkipPrepare);
mHTML5VideoView.setStartWhenPrepared(forceStart);







