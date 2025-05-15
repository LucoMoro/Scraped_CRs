//<Beginning of snippet n. 0>

mInitialSelection = 0; // Default initialization

if (Intent.ACTION_PICK.equals(intent.getAction())) {
    int index = intent.getIntExtra("index", -1); // Default to -1 if "index" not present
    if (index > 0) { // Only update if index is greater than 0
        mInitialSelection = index - 1;
    }
}

if (mInitialSelection < 0) { // Ensure non-negative selection
    mInitialSelection = 0;
}

return intent.getData();

//<End of snippet n. 0>