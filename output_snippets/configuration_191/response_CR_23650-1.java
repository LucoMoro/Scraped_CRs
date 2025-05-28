//<Beginning of snippet n. 0>

mInitialSelection = 0;
if (intent.hasExtra("index")) {
    mInitialSelection = intent.getIntExtra("index", -1);
}
int totalContacts = getTotalContacts(); // Assuming this method exists
if (mInitialSelection < 0 || mInitialSelection >= totalContacts) {
    mInitialSelection = 0;
}
return intent.getData();

//<End of snippet n. 0>