/*Fixed Context and Thread leaks in App

The reference to the Context passed in App's constructor was never
removed from the static map of Contexts and Apps, resulting
in a lot of leaked activites. Instead of mapping a Context to
null in shutdown() it's now actually removed.
The HandlerThread in App was never terminated, resulting in a lot
of leaked threads. The HandlerThread is now gracefully terminated
in shutdown().

Change-Id:I3fadf594f89acec2a7c87ec39ed30e570beb9dd8*/
//Synthetic comment -- diff --git a/src/com/cooliris/app/App.java b/src/com/cooliris/app/App.java
//Synthetic comment -- index 5281670..ced2689 100644

//Synthetic comment -- @@ -69,9 +69,10 @@
		
	public void shutdown() {
mReverseGeocoder.shutdown();

// unregister
        mMap.put(mContext, null);
	}
	
public Context getContext() {







