/*Typo fix in javadoc

@throw -> @throws
@Deprecated -> @deprecated*/
//Synthetic comment -- diff --git a/core/java/android/view/animation/Animation.java b/core/java/android/view/animation/Animation.java
//Synthetic comment -- index a662760..2f5e601 100644

//Synthetic comment -- @@ -319,7 +319,7 @@
* 
* @param durationMillis Duration in milliseconds
*
     * @throw java.lang.IllegalArgumentException if the duration is < 0
*
* @attr ref android.R.styleable#Animation_duration
*/








//Synthetic comment -- diff --git a/core/java/android/webkit/UrlInterceptHandler.java b/core/java/android/webkit/UrlInterceptHandler.java
//Synthetic comment -- index 9216413..766ed7e 100644

//Synthetic comment -- @@ -30,7 +30,7 @@
* @param url URL string.
* @param headers The headers associated with the request. May be null.
* @return The CacheResult containing the surrogate response.
     * @Deprecated Use PluginData getPluginData(String url,
* Map<String, String> headers); instead
*/
@Deprecated








//Synthetic comment -- diff --git a/core/java/android/webkit/UrlInterceptRegistry.java b/core/java/android/webkit/UrlInterceptRegistry.java
//Synthetic comment -- index 6051f29..31005bb 100644

//Synthetic comment -- @@ -89,7 +89,7 @@
* UrlInterceptHandler interested, or null if none are.
*
* @return A CacheResult containing surrogate content.
     * @Deprecated Use PluginData getPluginData( String url,
* Map<String, String> headers) instead.
*/
@Deprecated








//Synthetic comment -- diff --git a/core/java/android/webkit/gears/UrlInterceptHandlerGears.java b/core/java/android/webkit/gears/UrlInterceptHandlerGears.java
//Synthetic comment -- index 43104bf..887afc2 100644

//Synthetic comment -- @@ -331,7 +331,7 @@
* @param url URL string.
* @param headers The headers associated with the request. May be null.
* @return The CacheResult containing the surrogate response.
     * @Deprecated Use PluginData getPluginData(String url,
* Map<String, String> headers); instead
*/
@Deprecated








//Synthetic comment -- diff --git a/services/java/com/android/server/LocationManagerService.java b/services/java/com/android/server/LocationManagerService.java
//Synthetic comment -- index fab97b1..1d81f32 100644

//Synthetic comment -- @@ -1382,7 +1382,7 @@

/**
* @return null if the provider does not exits
     * @throw SecurityException if the provider is not allowed to be
* accessed by the caller
*/
public Bundle getProviderInfo(String provider) {








//Synthetic comment -- diff --git a/telephony/java/android/telephony/PhoneStateListener.java b/telephony/java/android/telephony/PhoneStateListener.java
//Synthetic comment -- index e113680..7d83ea8 100644

//Synthetic comment -- @@ -154,7 +154,7 @@
* @see ServiceState#STATE_IN_SERVICE
* @see ServiceState#STATE_OUT_OF_SERVICE
* @see ServiceState#STATE_POWER_OFF
     * @deprecated, @see #onSignalStrengthsChanged
*/
public void onSignalStrengthChanged(int asu) {
// default implementation empty







