/*SDK Manager: fix window shell disposed exception.

SDK Bug: 6710759

(cherry picked from commit 6263542fb2bc1819bd8aeac6fe73374f894503cb)

Change-Id:I28def31e3b58567a5022b9dd1808ec4bc2985434*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index cdf5e7b..521e70c 100755

//Synthetic comment -- @@ -235,7 +235,7 @@
example);

// We may not have any UI. Only display a dialog if there's a window shell available.
            if (mWindowShell != null && !mWindowShell.isDisposed()) {
MessageDialog.openError(mWindowShell,
"Android Virtual Devices Manager",
error);
//Synthetic comment -- @@ -1064,7 +1064,7 @@
* This can be called from any thread.
*/
public void broadcastOnSdkLoaded() {
        if (mWindowShell != null && !mWindowShell.isDisposed() && mListeners.size() > 0) {
mWindowShell.getDisplay().syncExec(new Runnable() {
@Override
public void run() {
//Synthetic comment -- @@ -1085,7 +1085,7 @@
* This can be called from any thread.
*/
private void broadcastOnSdkReload() {
        if (mWindowShell != null && !mWindowShell.isDisposed() && mListeners.size() > 0) {
mWindowShell.getDisplay().syncExec(new Runnable() {
@Override
public void run() {
//Synthetic comment -- @@ -1106,7 +1106,7 @@
* This can be called from any thread.
*/
private void broadcastPreInstallHook() {
        if (mWindowShell != null && !mWindowShell.isDisposed() && mListeners.size() > 0) {
mWindowShell.getDisplay().syncExec(new Runnable() {
@Override
public void run() {
//Synthetic comment -- @@ -1127,7 +1127,7 @@
* This can be called from any thread.
*/
private void broadcastPostInstallHook() {
        if (mWindowShell != null && !mWindowShell.isDisposed() && mListeners.size() > 0) {
mWindowShell.getDisplay().syncExec(new Runnable() {
@Override
public void run() {







