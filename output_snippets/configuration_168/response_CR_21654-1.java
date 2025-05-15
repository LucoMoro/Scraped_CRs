//<Beginning of snippet n. 0>
mHistory = mPersist.history;

mDisplay = (CalculatorDisplay) findViewById(R.id.display);
mDisplay.setContentDescription("Calculator display"); // Accessibility attribute
mDisplay.setFocusable(true); // Ensuring mDisplay is focusable
mDisplay.requestFocus(); // Set initial focus immediately

mLogic = new Logic(this, mHistory, mDisplay, (Button) findViewById(R.id.equal));
HistoryAdapter historyAdapter = new HistoryAdapter(this, mHistory, mLogic);
//<End of snippet n. 0>