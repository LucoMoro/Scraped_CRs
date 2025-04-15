/*Improved event handling in DrmManagerClient

Event threads were created for each instance in the constructor.
That consumed resources and time.
Event threads will now only be created when listeners are used.
This will also reduce the risk of running out of file descriptors
since each event thread will used some FDs.

Change-Id:Ide3b45e0c36c36f22fdbc5d9ce1d3de07f3b69e3*/
//Synthetic comment -- diff --git a/drm/java/android/drm/DrmManagerClient.java b/drm/java/android/drm/DrmManagerClient.java
//Synthetic comment -- index 2907f10..c82b871 100644

//Synthetic comment -- @@ -245,7 +245,6 @@
public DrmManagerClient(Context context) {
mContext = context;
mReleased = false;
        createEventThreads();

// save the unique id
mUniqueId = _initialize();
//Synthetic comment -- @@ -296,6 +295,7 @@
public synchronized void setOnInfoListener(OnInfoListener infoListener) {
mOnInfoListener = infoListener;
if (null != infoListener) {
createListeners();
}
}
//Synthetic comment -- @@ -309,6 +309,7 @@
public synchronized void setOnEventListener(OnEventListener eventListener) {
mOnEventListener = eventListener;
if (null != eventListener) {
createListeners();
}
}
//Synthetic comment -- @@ -322,6 +323,7 @@
public synchronized void setOnErrorListener(OnErrorListener errorListener) {
mOnErrorListener = errorListener;
if (null != errorListener) {
createListeners();
}
}
//Synthetic comment -- @@ -489,6 +491,7 @@
throw new IllegalArgumentException("Given drmInfo is invalid/null");
}
int result = ERROR_UNKNOWN;
if (null != mEventHandler) {
Message msg = mEventHandler.obtainMessage(ACTION_PROCESS_DRM_INFO, drmInfo);
result = (mEventHandler.sendMessage(msg)) ? ERROR_NONE : result;
//Synthetic comment -- @@ -716,6 +719,7 @@
*/
public int removeAllRights() {
int result = ERROR_UNKNOWN;
if (null != mEventHandler) {
Message msg = mEventHandler.obtainMessage(ACTION_REMOVE_ALL_RIGHTS);
result = (mEventHandler.sendMessage(msg)) ? ERROR_NONE : result;
//Synthetic comment -- @@ -889,7 +893,7 @@

private native DrmSupportInfo[] _getAllSupportInfo(int uniqueId);

    private void createEventThreads() {
if (mEventHandler == null && mInfoHandler == null) {
mInfoThread = new HandlerThread("DrmManagerClient.InfoHandler");
mInfoThread.start();







