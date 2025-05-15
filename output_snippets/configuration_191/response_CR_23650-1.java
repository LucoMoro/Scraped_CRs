//<Beginning of snippet n. 0>

if (Intent.ACTION_PICK.equals(intent.getAction())) {
    int index = intent.getIntExtra("index", 0);
    if (index < 1) {
        mInitialSelection = 0;
    } else {
        mInitialSelection = index - 1;
    }
} else {
    mInitialSelection = 0; // Default initialization when action is not ACTION_PICK
}
return intent.getData();

//<End of snippet n. 0>