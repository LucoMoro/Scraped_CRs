//<Beginning of snippet n. 0>
} catch (Exception e) {
    Log.e(TAG, "Fail to show the PopupWindow.");
}

if (null != mSkbContainer && mSkbContainer.isShown()) {
    // Hide the input bar
    mSkbContainer.setVisibility(View.GONE);

    // Check and dismiss the soft keyboard if it is displayed
    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    if (imm.isAcceptingText()) {
        View view = ((Activity) context).getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    
    mSkbContainer.toggleCandidateMode(false);

//<End of snippet n. 0>