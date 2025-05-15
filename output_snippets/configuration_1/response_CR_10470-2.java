//<Beginning of snippet n. 0>
} catch (Exception e) {
    Log.e(TAG, "Fail to show the PopupWindow.");
}

if (null != mSkbContainer && mSkbContainer.isShown()) {
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            mSkbContainer.setVisibility(View.GONE);
        }
    });
    mSkbContainer.toggleCandidateMode(false);
//<End of snippet n. 0>