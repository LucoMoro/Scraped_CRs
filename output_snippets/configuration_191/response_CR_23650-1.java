//<Beginning of snippet n. 0>

mInitialSelection = 0;
int totalContacts = getTotalContacts(); // Assuming this method exists
if (intent.hasExtra("index")) {
    int index = intent.getIntExtra("index", -1);
    if (index >= 0 && index < totalContacts) {
        mInitialSelection = index;
    }
}
return intent.getData();

//<End of snippet n. 0>