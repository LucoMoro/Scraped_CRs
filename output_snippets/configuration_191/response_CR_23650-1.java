//<Beginning of snippet n. 0>


if (Intent.ACTION_PICK.equals(intent.getAction())) {
    int index = intent.getIntExtra("index", -1);
    int maxContacts = getMaxContacts(); // Assume this method returns the total number of contacts
    if (index < 1 || index > maxContacts) {
        mInitialSelection = 0; // fallback to zero
    } else {
        mInitialSelection = index - 1; // "index" is 1-based
    }
} else {
    mInitialSelection = 0; // fallback for non-pick actions
}
return intent.getData();

//<End of snippet n. 0>