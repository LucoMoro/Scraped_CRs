//<Beginning of snippet n. 0>
} catch (Exception e) {
    Log.e(TAG, "Error switching input mode: " + e.getMessage(), e);
}

if (null != mSkbContainer && mSkbContainer.isShown()) {
    mSkbContainer.toggleCandidateMode(false);
    mInputBar.setVisibility(View.GONE);
}
//<End of snippet n. 0>