//<Beginning of snippet n. 0>
if (Intent.ACTION_PICK.equals(intent.getAction())) {
    mInitialSelection = intent.getIntExtra("index", 0) - 1;
} else {
    mInitialSelection = 0;
}

if (mInitialSelection < 0) {
    mInitialSelection = 0;
}

return intent.getData();
//<End of snippet n. 0>