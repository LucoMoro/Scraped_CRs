//<Beginning of snippet n. 0>
} catch (Exception e) {
    Log.e(TAG, "Error switching input mode at " + System.currentTimeMillis() + ": " + e.getMessage(), e);
}

mSkbContainer.toggleCandidateMode(false);
mInputBar.setVisibility(View.GONE);
//<End of snippet n. 0>