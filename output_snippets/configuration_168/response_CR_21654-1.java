//<Beginning of snippet n. 0>
mHistory = mPersist.history;

mDisplay = (CalculatorDisplay) findViewById(R.id.display);
mDisplay.setContentDescription("Calculator display");

mLogic = new Logic(this, mHistory, mDisplay, (Button) findViewById(R.id.equal));
HistoryAdapter historyAdapter = new HistoryAdapter(this, mHistory, mLogic);

mDisplay.requestFocus(); // Ensures initial focus on mDisplay
//<End of snippet n. 0>