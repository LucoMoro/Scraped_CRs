/*Fix self assignment in MediaArtistNativeHelper

Change-Id:Ibd3e9940c25c183b0e61b58f0959af0270571866Signed-off-by: You Kim <you.kim72@gmail.com>*/




//Synthetic comment -- diff --git a/media/java/android/media/videoeditor/MediaArtistNativeHelper.java b/media/java/android/media/videoeditor/MediaArtistNativeHelper.java
//Synthetic comment -- index 4756078..9ac22ce 100644

//Synthetic comment -- @@ -2191,7 +2191,7 @@
&& ((lEffect.startTime + lEffect.duration) <= endCutTime)) {
effectStartTime = lEffect.startTime - beginCutTime;
lEffect.startTime = effectStartTime;
            lEffect.duration = effectDuration;
return;
}








