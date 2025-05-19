//<Beginning of snippet n. 0>
} catch (Exception e) {
    Log.e(TAG, "Fail to show the PopupWindow.", e);
    Toast.makeText(context, "An error occurred while showing the popup.", Toast.LENGTH_SHORT).show();
}

private void switchInputMode(boolean candidateMode) {
    if (mSkbContainer != null) {
        mSkbContainer.toggleCandidateMode(candidateMode);
    }
    hideInputBar();
}

private boolean isKeyboardVisible(Context context) {
    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    return imm != null && imm.isAcceptingText();
}

private void hideInputBar() {
    if (mSkbContainer != null) {
        mSkbContainer.setVisibility(View.GONE);
    }
}
//<End of snippet n. 0>