/*SDK Manager: fix window shell disposed exception.

SDK Bug: 6710759

Change-Id:Iffecdd16b7ef52ffbe858e065716a7a6125a6982*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterData.java
//Synthetic comment -- index cdf5e7b..521e70c 100755

//Synthetic comment -- @@ -235,7 +235,7 @@
example);

// We may not have any UI. Only display a dialog if there's a window shell available.
            if (mWindowShell != null) {
MessageDialog.openError(mWindowShell,
"Android Virtual Devices Manager",
error);
//Synthetic comment -- @@ -1064,7 +1064,7 @@
* This can be called from any thread.
*/
public void broadcastOnSdkLoaded() {
        if (mWindowShell != null && mListeners.size() > 0) {
mWindowShell.getDisplay().syncExec(new Runnable() {
@Override
public void run() {
//Synthetic comment -- @@ -1085,7 +1085,7 @@
* This can be called from any thread.
*/
private void broadcastOnSdkReload() {
        if (mWindowShell != null && mListeners.size() > 0) {
mWindowShell.getDisplay().syncExec(new Runnable() {
@Override
public void run() {
//Synthetic comment -- @@ -1106,7 +1106,7 @@
* This can be called from any thread.
*/
private void broadcastPreInstallHook() {
        if (mWindowShell != null && mListeners.size() > 0) {
mWindowShell.getDisplay().syncExec(new Runnable() {
@Override
public void run() {
//Synthetic comment -- @@ -1127,7 +1127,7 @@
* This can be called from any thread.
*/
private void broadcastPostInstallHook() {
        if (mWindowShell != null && mListeners.size() > 0) {
mWindowShell.getDisplay().syncExec(new Runnable() {
@Override
public void run() {







