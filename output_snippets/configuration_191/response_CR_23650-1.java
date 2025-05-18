//<Beginning of snippet n. 0>

int index = intent.getIntExtra("index", -1);
int maxContacts = getMaxContacts(); // Assume this method returns the total number of contacts

if (index < 0 || index >= maxContacts) {
    mInitialSelection = 0; // fallback to zero for invalid indices
} else {
    mInitialSelection = index - 1; // "index" is 1-based
}

if (!Intent.ACTION_PICK.equals(intent.getAction())) {
    mInitialSelection = 0; // fallback for non-pick actions
}

return intent.getData();

//<End of snippet n. 0>