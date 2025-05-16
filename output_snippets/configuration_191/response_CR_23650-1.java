//<Beginning of snippet n. 0>
mInitialSelection = 0;
if (Intent.ACTION_PICK.equals(intent.getAction())) {
    int index = intent.getIntExtra("index", -1) - 1;
    if (index >= 0) {
        mInitialSelection = index;
    }
}
return intent.getData();
//<End of snippet n. 0>