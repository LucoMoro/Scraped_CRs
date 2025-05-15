
//<Beginning of snippet n. 0>


if (Intent.ACTION_PICK.equals(intent.getAction())) {
// "index" is 1-based
mInitialSelection = intent.getIntExtra("index", 0) - 1;
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            mInitialSelection = 0;
}
return intent.getData();
}

//<End of snippet n. 0>








