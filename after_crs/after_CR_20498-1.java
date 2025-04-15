/*Fix memory leak when repeatedly playing a video file.

When a video file is played a reference to a MovieView is used in a
HashMap as key. When stopped playing the value in the hashmap is set
to null. The key is never removed from the hashmap and the MovieView
never removed by the GC. Now the entire instance in the HashMap is
removed enabeling the GC to clean not used MovieViews.

Change-Id:Ibafdcbf05861b2b3c9e30371b227eb3afcd4145bSigned-off-by: Christian Bejram <christian.bejram@stericsson.com>*/




//Synthetic comment -- diff --git a/src/com/cooliris/app/App.java b/src/com/cooliris/app/App.java
//Synthetic comment -- index 5281670..fe239cc 100644

//Synthetic comment -- @@ -71,7 +71,7 @@
mReverseGeocoder.shutdown();

// unregister
        mMap.remove(mContext);
	}
	
public Context getContext() {







