/*Adds command "force_platform_scheduling" to GPS provider

This patch allow applications to force the Android platform to schedule GPS
refreshes, instead of solely relying on the native code to properly
report its GPS scheduling capabilities.  This is implemented through
the addition of a "force_platform_scheduling" command for the GPS
provider.

This feature is discussed in detail in posts to the Android Platform
(http://goo.gl/Br5us) and Android Contributors (http://goo.gl/yEHwS) groups.

In summary, in the majority of Android devices I’ve tested (~10-15 devices),
when an application passes in a "minTime" parameter (i.e., interval of time
between location updates) to the GPS location provider via
LocationManager.requestLocationUpdates() (http://goo.gl/XGgZy), the GPS
provider typically ignores this value and proceeds to update the application
via callbacks to the LocationListener.onLocationChanged() (http://goo.gl/1BVAV)
method every second (i.e., 1Hz update rate).  A stock build of AOSP ICS 4.0.3
on a Nexus S 4G has the same behavior of ignoring the minTime parameter, so
the behavior is not due to an OEM modification of the platform source.

I believe I've narrowed down the problem to faulty capability reporting from
native code to the GpsLocationProvider in the Android platform.  In a custom
AOSP build on the same Nexus S 4G, I hard-coded values in the GpsLocationProvider
to indicate that the native code was NOT capable of handling GPS scheduling.
Voila, the platform took over and properly followed the minTime parameter
(60 seconds in this case) and delivered location updates to the app 60 seconds
apart.  So, it seems that the native code is telling the platform “I can handle
GPS scheduling”, but then it doesn’t, resulting in a 1Hz update rate no matter
the minTime interval requested by the application.

This patch implements a workaround for the native GPS scheduling issue by allowing
an app developer to override native GPS scheduling capability reporting.  Via the
"force_platform_scheduling" command, the developer can request that the GPS provider
use the GPS scheduling implementation in the Android platform, instead of native code.
The only other workaround option for an app developer on faulty devices is implementing
their own GPS scheduling solution at the app level.  This workaround patch is preferred
because a) platform scheduling will be more consistent across devices, b) platform
scheduling will be more efficient than most app-level scheduling implementations,
and c) it’s much less work for developers to send a command to the platform than
to implement their own scheduling logic.

This patch is compatible with devices that properly communicate native capabilities, as
“force_platform_scheduling” is turned off by default and app developers don’t need to do
anything to get the expected behavior out of the GPS provider.

This patch was successfully tested on a Nexus S 4G with Android 4.0.3.

Change-Id:I25460da77a002b6b7db65f257a33f26f5e49a10dSigned-off-by: Sean Barbeau <sjbarbeau@gmail.com>*/




//Synthetic comment -- diff --git a/services/java/com/android/server/location/GpsLocationProvider.java b/services/java/com/android/server/location/GpsLocationProvider.java
//Synthetic comment -- index 588fa93..ea74ac8 100755

//Synthetic comment -- @@ -214,6 +214,14 @@

// requested frequency of fixes, in milliseconds
private int mFixInterval = 1000;
    
    // by default, we allow the native response in hasCapability() define if 
    // native or platform GPS scheduling occurs.  However, due to faulty native
    // reporting on devices that say they natively handle scheduling but then
    // don't (effectively ignoring the app's mFixInterval request), we allow
    // the app to force the platform to handle GPS scheduling, which sets
    // this value to true.
    private boolean mForcePlatformScheduling = false;

// true if we started navigation
private boolean mStarted;
//Synthetic comment -- @@ -932,6 +940,8 @@
xtraDownloadRequest();
result = true;
}
        } else if ("force_platform_scheduling".equals(command)) {
            result = forcePlatformScheduling(extras);
} else {
Log.w(TAG, "sendExtraCommand: unknown command " + command);
}
//Synthetic comment -- @@ -969,6 +979,23 @@

return false;
}
    
    private boolean forcePlatformScheduling(Bundle extras){
    	if (extras == null) {
    		Log.w(TAG, "Extras bundle for force_platform_scheduling command must " +
    				"include key/boolean_value pair 'force_platform_scheduling' and true or false");
    		return false;    		
    	} else {
    		if (extras.getBoolean("force_platform_scheduling")) {
    			mForcePlatformScheduling = true;
    			Log.d(TAG, "force_platform_scheduling = true");
    		} else {
    			mForcePlatformScheduling = false;
    			Log.d(TAG, "force_platform_scheduling = false");
    		}
    		return true;
    	}    	
    }

private void startNavigating(boolean singleShot) {
if (!mStarted) {
//Synthetic comment -- @@ -1039,7 +1066,14 @@
}

private boolean hasCapability(int capability) {
    	if(capability == GPS_CAPABILITY_SCHEDULING && mForcePlatformScheduling){
    		//app has requested that the platform handle GPS scheduling, 
    		//presumably due to faulty native reporting
    		return false;
    	}else{
    		//return native-reported capabilities
    		return ((mEngineCapabilities & capability) != 0);
    	}
}

/**







