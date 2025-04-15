/*Adding null check to avoid NPE on ACTION_UP in TitleBar

When launching browser from google search and then holding on to the stop button
when backing out of the browser and then releasing it results in a crash.
ACTION_UP is triggered outside browser and getCurrentTab() is then null.
Adding null check to avoid the resulting NPE.

Change-Id:Ia5f26c64ddeb74b4269118ec21455f358bf98623*/
//Synthetic comment -- diff --git a/src/com/android/browser/TitleBar.java b/src/com/android/browser/TitleBar.java
//Synthetic comment -- index dc4979b..2fffd45 100644

//Synthetic comment -- @@ -206,37 +206,41 @@
break;
case MotionEvent.ACTION_UP:
if (button.isPressed()) {
                    if (mInVoiceMode) {
                        if (mBrowserActivity.getTabControl().getCurrentTab()
                                .voiceSearchSourceIsGoogle()) {
                            Intent i = new Intent(
                                    LoggingEvents.ACTION_LOG_EVENT);
                            i.putExtra(LoggingEvents.EXTRA_EVENT,
                                    LoggingEvents.VoiceSearch.RETRY);
                            mBrowserActivity.sendBroadcast(i);
}
                        mBrowserActivity.startActivity(mVoiceSearchIntent);
                    } else if (mInLoad) {
                        mBrowserActivity.stopLoading();
                    } else {
                        mBrowserActivity.bookmarksOrHistoryPicker(false);
}
button.setPressed(false);
} else if (mTitleBg.isPressed()) {
mHandler.removeMessages(LONG_PRESS);
                    if (mInVoiceMode) {
                        if (mBrowserActivity.getTabControl().getCurrentTab()
                                .voiceSearchSourceIsGoogle()) {
                            Intent i = new Intent(
                                    LoggingEvents.ACTION_LOG_EVENT);
                            i.putExtra(LoggingEvents.EXTRA_EVENT,
                                    LoggingEvents.VoiceSearch.N_BEST_REVEAL);
                            mBrowserActivity.sendBroadcast(i);
}
                        mBrowserActivity.showVoiceSearchResults(
                                mTitle.getText().toString().trim());
                    } else {
                        mBrowserActivity.editUrl();
}
mTitleBg.setPressed(false);
}







