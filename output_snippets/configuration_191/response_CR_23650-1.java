//<Beginning of snippet n. 0>

mInitialSelection = 0; 
if (Intent.ACTION_PICK.equals(intent.getAction())) {
    // "index" is 1-based
    int index = intent.getIntExtra("index", -1);
    if (index > 0) {
        mInitialSelection = index - 1; 
        if (mInitialSelection >= numberOfContacts) {
            mInitialSelection = 0; 
        }
    }
}
return intent.getData();

//<End of snippet n. 0>