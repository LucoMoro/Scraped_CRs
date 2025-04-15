/*Fix large file download failure issue

The root cause is int overflow as integer can only hold size
Integer.MAX_VALUE = 0x7FFFFFFF. It is about 2G. So, when a file size
is greater than 2G, it throws a "NumberFormatException", and then
mark this download "unsuccessfully".
The progress bar in notification for the large file downloading was
not correct either. The total file size was converted from long to
int value when in builder.setProgress().

Change-Id:Ib038860e26cf8cade2c423403585c207f8b8979b*/




//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadNotification.java b/src/com/android/providers/downloads/DownloadNotification.java
//Synthetic comment -- index f5778e7..da2ce2a 100644

//Synthetic comment -- @@ -192,8 +192,13 @@
if (hasPausedText) {
builder.setContentText(item.mPausedText);
} else {
                long max = item.mTotalTotal;
                long progress = item.mTotalCurrent;
                while(max > Integer.MAX_VALUE) {
                    max = max / 1024;
                    progress = progress / 1024;
                }
                builder.setProgress((int) max, (int) progress, item.mTotalTotal == -1);
if (hasContentText) {
builder.setContentInfo(
buildPercentageLabel(mContext, item.mTotalTotal, item.mTotalCurrent));








//Synthetic comment -- diff --git a/src/com/android/providers/downloads/DownloadThread.java b/src/com/android/providers/downloads/DownloadThread.java
//Synthetic comment -- index e74d5c7..3a61357 100644

//Synthetic comment -- @@ -488,7 +488,7 @@
mContext.getContentResolver().update(mInfo.getAllDownloadsUri(), values, null, null);

boolean lengthMismatched = (innerState.mHeaderContentLength != null)
                && (state.mCurrentBytes != Long.parseLong(innerState.mHeaderContentLength));
if (lengthMismatched) {
if (cannotResume(state)) {
throw new StopRequestException(Downloads.Impl.STATUS_CANNOT_RESUME,







