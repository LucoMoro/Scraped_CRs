//<Beginning of snippet n. 0>

mHistory = mPersist.history;

mDisplay = (CalculatorDisplay) findViewById(R.id.display);
mDisplay.setFocusable(true); // Ensure mDisplay is focusable
mDisplay.setContentDescription("Calculator display"); // Accessibility property
mDisplay.requestFocus(); // Set initial focus

mLogic = new Logic(this, mHistory, mDisplay, (Button) findViewById(R.id.equal));
HistoryAdapter historyAdapter = new HistoryAdapter(this, mHistory, mLogic);

//<End of snippet n. 0>