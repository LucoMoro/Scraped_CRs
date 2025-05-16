//<Beginning of snippet n. 0>
} catch (Exception e) {
    Log.e(TAG, "Fail to show the PopupWindow.");
}

if (null != mSkbContainer && mSkbContainer.isShown()) {
    mSkbContainer.toggleCandidateMode(false);
    if (isSoftKeyboardVisible()) {
        try {
            mSkbContainer.setVisibility(View.GONE);
        } catch (Exception ex) {
            Log.e(TAG, "Failed to hide the input bar.");
        }
    }
}
//<End of snippet n. 0>