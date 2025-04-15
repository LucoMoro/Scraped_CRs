/*PowerManagerService: make debug and info messages controllable at runtime.

This patch adds the possibility to enable/disable debug and info messages from PowerManagerService at runtime.
Previously, the source code had to be recompiled with mSpew boolean set to true. By default, debug messages are
not enabled, so there's no performance or power impact. To enable them, set property log.tag.PowerManagerService
to level "DEBUG" or "VERBOSE".

Improve existing wakelock debug messages. Add UID/PID to wakelock acquire debug message.

Change-Id:I8577961664667a21fffdd743d993c2f59dbbeef8Author: Catalin Popescu <catalinx.popescu@intel.com>
Signed-off-by: Catalin Popescu <catalinx.popescu@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 38727, 42701*/




//Synthetic comment -- diff --git a/services/java/com/android/server/PowerManagerService.java b/services/java/com/android/server/PowerManagerService.java
//Synthetic comment -- index 888ec69..195cabe 100644

//Synthetic comment -- @@ -301,11 +301,6 @@
private long mLastTouchDown;
private int mTouchCycles;


private native void nativeInit();
private native void nativeSetPowerState(boolean screenOn, boolean screenBright);
//Synthetic comment -- @@ -850,8 +845,11 @@

public void acquireWakeLockLocked(int flags, IBinder lock, int uid, int pid, String tag,
WorkSource ws) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Slog.d(TAG, "acquireWakeLock flags=0x" + Integer.toHexString(flags)
                + " tag=" + tag
                + " uid=" + Integer.toString(uid)
                + " pid=" + Integer.toString(pid));
}

if (ws != null && ws.size() == 0) {
//Synthetic comment -- @@ -939,7 +937,7 @@
mProximitySensorActive = false;
}

                    if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "wakeup here mUserState=0x" + Integer.toHexString(mUserState)
+ " mWakeLockState=0x"
+ Integer.toHexString(mWakeLockState)
//Synthetic comment -- @@ -947,7 +945,7 @@
+ Integer.toHexString(oldWakeLockState));
}
} else {
                    if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "here mUserState=0x" + Integer.toHexString(mUserState)
+ " mLocks.gatherState()=0x"
+ Integer.toHexString(mLocks.gatherState())
//Synthetic comment -- @@ -1017,7 +1015,7 @@
return;
}

        if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "releaseWakeLock flags=0x"
+ Integer.toHexString(wl.flags) + " tag=" + wl.tag);
}
//Synthetic comment -- @@ -1029,7 +1027,7 @@
if (mProximitySensorActive &&
((flags & PowerManager.WAIT_FOR_PROXIMITY_NEGATIVE) != 0)) {
// wait for proximity sensor to go negative before disabling sensor
                        if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "waiting for proximity sensor to go negative");
}
} else {
//Synthetic comment -- @@ -1333,7 +1331,7 @@
nextState = SCREEN_BRIGHT;
}
}
                if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "setTimeoutLocked now=" + now
+ " timeoutOverride=" + timeoutOverride
+ " nextState=" + nextState + " when=" + when);
//Synthetic comment -- @@ -1363,7 +1361,7 @@
public void run()
{
synchronized (mLocks) {
                if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "user activity timeout timed out nextState=" + this.nextState);
}

//Synthetic comment -- @@ -1518,7 +1516,7 @@
// ignore it
}

                    if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "mBroadcastWakeLock=" + mBroadcastWakeLock);
}
if (mContext != null && ActivityManagerNative.isSystemReady()) {
//Synthetic comment -- @@ -1663,7 +1661,7 @@
// handles the case where the screen is currently off because of
// a prior preventScreenOn(true) call.)
if (!mProximitySensorActive && (mPowerState & SCREEN_ON_BIT) != 0) {
                    if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG,
"preventScreenOn: turning on after a prior preventScreenOn(true)!");
}
//Synthetic comment -- @@ -1683,7 +1681,7 @@
public void setScreenBrightnessOverride(int brightness) {
mContext.enforceCallingOrSelfPermission(android.Manifest.permission.DEVICE_POWER, null);

        if (Log.isLoggable(TAG, Log.DEBUG)) Slog.d(TAG, "setScreenBrightnessOverride " + brightness);
synchronized (mLocks) {
if (mScreenBrightnessOverride != brightness) {
mScreenBrightnessOverride = brightness;
//Synthetic comment -- @@ -1697,7 +1695,7 @@
public void setButtonBrightnessOverride(int brightness) {
mContext.enforceCallingOrSelfPermission(android.Manifest.permission.DEVICE_POWER, null);

        if (Log.isLoggable(TAG, Log.DEBUG)) Slog.d(TAG, "setButtonBrightnessOverride " + brightness);
synchronized (mLocks) {
if (mButtonBrightnessOverride != brightness) {
mButtonBrightnessOverride = brightness;
//Synthetic comment -- @@ -1795,7 +1793,7 @@
synchronized (mLocks) {
int err;

            if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "setPowerState: mPowerState=0x" + Integer.toHexString(mPowerState)
+ " newState=0x" + Integer.toHexString(newState)
+ " noChangeLights=" + noChangeLights
//Synthetic comment -- @@ -1826,7 +1824,7 @@
boolean oldScreenOn = (mPowerState & SCREEN_ON_BIT) != 0;
boolean newScreenOn = (newState & SCREEN_ON_BIT) != 0;

            if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "setPowerState: mPowerState=" + mPowerState
+ " newState=" + newState + " noChangeLights=" + noChangeLights);
Slog.d(TAG, "  oldKeyboardBright=" + ((mPowerState & KEYBOARD_BRIGHT_BIT) != 0)
//Synthetic comment -- @@ -1845,7 +1843,7 @@

if (stateChanged && reason == WindowManagerPolicy.OFF_BECAUSE_OF_TIMEOUT) {
if (mPolicy != null && mPolicy.isScreenSaverEnabled()) {
                    if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "setPowerState: running screen saver instead of turning off screen");
}
if (mPolicy.startScreenSaver()) {
//Synthetic comment -- @@ -1872,13 +1870,13 @@
// seconds to prevent a buggy app from disabling the
// screen forever; see forceReenableScreen().)
boolean reallyTurnScreenOn = true;
                    if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "- turning screen on...  mPreventScreenOn = "
+ mPreventScreenOn);
}

if (mPreventScreenOn) {
                        if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "- PREVENTING screen from really turning on!");
}
reallyTurnScreenOn = false;
//Synthetic comment -- @@ -2142,7 +2140,7 @@
}
}

        if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "offMask=0x" + Integer.toHexString(offMask)
+ " dimMask=0x" + Integer.toHexString(dimMask)
+ " onMask=0x" + Integer.toHexString(onMask)
//Synthetic comment -- @@ -2153,7 +2151,7 @@
}

if (offMask != 0) {
            if (Log.isLoggable(TAG, Log.INFO)) Slog.i(TAG, "Setting brightess off: " + offMask);
setLightBrightness(offMask, PowerManager.BRIGHTNESS_OFF);
}
if (dimMask != 0) {
//Synthetic comment -- @@ -2162,7 +2160,7 @@
brightness > PowerManager.BRIGHTNESS_LOW_BATTERY) {
brightness = PowerManager.BRIGHTNESS_LOW_BATTERY;
}
            if (Log.isLoggable(TAG, Log.INFO)) Slog.i(TAG, "Setting brightess dim " + brightness + ": " + dimMask);
setLightBrightness(dimMask, brightness);
}
if (onMask != 0) {
//Synthetic comment -- @@ -2171,7 +2169,7 @@
brightness > PowerManager.BRIGHTNESS_LOW_BATTERY) {
brightness = PowerManager.BRIGHTNESS_LOW_BATTERY;
}
            if (Log.isLoggable(TAG, Log.INFO)) Slog.i(TAG, "Setting brightess on " + brightness + ": " + onMask);
setLightBrightness(onMask, brightness);
}
}
//Synthetic comment -- @@ -2210,7 +2208,7 @@
int value = msg.arg2;
long tStart = SystemClock.uptimeMillis();
if ((mask & SCREEN_BRIGHT_BIT) != 0) {
                            if (Log.isLoggable(TAG, Log.DEBUG)) Slog.d(TAG, "Set brightness: " + value);
mLcdLight.setBrightness(value, brightnessMode);
}
long elapsed = SystemClock.uptimeMillis() - tStart;
//Synthetic comment -- @@ -2272,8 +2270,8 @@
}
}

                    if (Log.isLoggable(TAG, Log.DEBUG)) {
                        Slog.d(TAG, "Animating light: " + "start:" + startValue
+ ", end:" + endValue + ", elapsed:" + elapsed
+ ", duration:" + duration + ", current:" + currentValue
+ ", newValue:" + newValue
//Synthetic comment -- @@ -2284,8 +2282,8 @@
if (turningOff && !mHeadless && !mAnimateScreenLights) {
int mode = mScreenOffReason == OFF_BECAUSE_OF_PROX_SENSOR
? 0 : mAnimationSetting;
                        if (Log.isLoggable(TAG, Log.DEBUG)) {
                            Slog.d(TAG, "Doing power-off anim, mode=" + mode);
}
mScreenBrightnessHandler.obtainMessage(ANIMATE_POWER_OFF, mode, 0)
.sendToTarget();
//Synthetic comment -- @@ -2345,8 +2343,8 @@
duration = (int) (mWindowScaleAnimation * animationDuration);
startTimeMillis = SystemClock.elapsedRealtime();

                if (Log.isLoggable(TAG, Log.DEBUG)) {
                    Slog.d(TAG, "animateTo(target=" + target
+ ", sensor=" + sensorTarget
+ ", mask=" + mask
+ ", duration=" + animationDuration +")"
//Synthetic comment -- @@ -2537,7 +2535,7 @@
}

synchronized (mLocks) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "userActivity mLastEventTime=" + mLastEventTime + " time=" + time
+ " mUserActivityAllowed=" + mUserActivityAllowed
+ " mUserState=0x" + Integer.toHexString(mUserState)
//Synthetic comment -- @@ -2687,13 +2685,13 @@
}

private void lightSensorChangedLocked(int value, boolean immediate) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "lightSensorChangedLocked value=" + value + " immediate=" + immediate);
}

// Don't do anything if the screen is off.
if ((mPowerState & SCREEN_ON_BIT) == 0) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "dropping lightSensorChangedLocked because screen is off");
}
return;
//Synthetic comment -- @@ -2717,7 +2715,7 @@
mLightSensorButtonBrightness = buttonValue;
mLightSensorKeyboardBrightness = keyboardValue;

                if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "lcdValue " + lcdValue);
Slog.d(TAG, "buttonValue " + buttonValue);
Slog.d(TAG, "keyboardValue " + keyboardValue);
//Synthetic comment -- @@ -2826,7 +2824,7 @@
}

private void goToSleepLocked(long time, int reason) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
Exception ex = new Exception();
ex.fillInStackTrace();
Slog.d(TAG, "goToSleep mLastEventTime=" + mLastEventTime + " time=" + time
//Synthetic comment -- @@ -2854,7 +2852,7 @@
}
if (!proxLock) {
mProxIgnoredBecauseScreenTurnedOff = true;
                if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "setting mProxIgnoredBecauseScreenTurnedOff");
}
}
//Synthetic comment -- @@ -2877,7 +2875,7 @@

public void setKeyboardVisibility(boolean visible) {
synchronized (mLocks) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "setKeyboardVisibility: " + visible);
}
if (mKeyboardVisible != visible) {
//Synthetic comment -- @@ -2906,7 +2904,7 @@
* short screen timeout when keyguard is unhidden.
*/
public void enableUserActivity(boolean enabled) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "enableUserActivity " + enabled);
}
synchronized (mLocks) {
//Synthetic comment -- @@ -2967,7 +2965,7 @@
mDimDelay = -1;
}
}
        if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "setScreenOffTimeouts mKeylightDelay=" + mKeylightDelay
+ " mDimDelay=" + mDimDelay + " mScreenOffDelay=" + mScreenOffDelay
+ " mDimScreen=" + mDimScreen);
//Synthetic comment -- @@ -3043,7 +3041,7 @@
result |= wl.minState;
}
}
            if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "reactivateScreenLocksLocked mProxIgnoredBecauseScreenTurnedOff="
+ mProxIgnoredBecauseScreenTurnedOff);
}
//Synthetic comment -- @@ -3181,7 +3179,7 @@
}

private void enableProximityLockLocked() {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "enableProximityLockLocked");
}
if (!mProximitySensorEnabled) {
//Synthetic comment -- @@ -3198,7 +3196,7 @@
}

private void disableProximityLockLocked() {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "disableProximityLockLocked");
}
if (mProximitySensorEnabled) {
//Synthetic comment -- @@ -3216,7 +3214,7 @@
}
if (mProximitySensorActive) {
mProximitySensorActive = false;
                if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "disableProximityLockLocked mProxIgnoredBecauseScreenTurnedOff="
+ mProxIgnoredBecauseScreenTurnedOff);
}
//Synthetic comment -- @@ -3228,7 +3226,7 @@
}

private void proximityChangedLocked(boolean active) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "proximityChangedLocked, active: " + active);
}
if (!mProximitySensorEnabled) {
//Synthetic comment -- @@ -3236,7 +3234,7 @@
return;
}
if (active) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "b mProxIgnoredBecauseScreenTurnedOff="
+ mProxIgnoredBecauseScreenTurnedOff);
}
//Synthetic comment -- @@ -3250,7 +3248,7 @@
// temporarily set mUserActivityAllowed to true so this will work
// even when the keyguard is on.
mProximitySensorActive = false;
            if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "b mProxIgnoredBecauseScreenTurnedOff="
+ mProxIgnoredBecauseScreenTurnedOff);
}
//Synthetic comment -- @@ -3266,7 +3264,7 @@
}

private void enableLightSensorLocked(boolean enable) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "enableLightSensorLocked enable=" + enable
+ " mLightSensorEnabled=" + mLightSensorEnabled
+ " mAutoBrightessEnabled=" + mAutoBrightessEnabled
//Synthetic comment -- @@ -3317,7 +3315,7 @@
boolean active = (distance >= 0.0 && distance < PROXIMITY_THRESHOLD &&
distance < mProximitySensor.getMaximumRange());

                if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "mProximityListener.onSensorChanged active: " + active);
}
if (timeSinceLastEvent < PROXIMITY_SENSOR_DELAY) {
//Synthetic comment -- @@ -3379,7 +3377,7 @@
SensorEventListener mLightListener = new SensorEventListener() {
@Override
public void onSensorChanged(SensorEvent event) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "onSensorChanged: light value: " + event.values[0]);
}
synchronized (mLocks) {
//Synthetic comment -- @@ -3389,7 +3387,7 @@
}
handleLightSensorValue((int)event.values[0], mWaitingForFirstLightSensor);
if (mWaitingForFirstLightSensor && !mPreparingForScreenOn) {
                    if (Log.isLoggable(TAG, Log.DEBUG)) {
Slog.d(TAG, "onSensorChanged: Clearing mWaitingForFirstLightSensor.");
}
mWaitingForFirstLightSensor = false;







