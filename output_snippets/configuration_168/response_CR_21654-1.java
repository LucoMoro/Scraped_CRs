<<Beginning of snippet n. 0>>
mHistory = mPersist.history;

mDisplay = (CalculatorDisplay) findViewById(R.id.display);
mDisplay.setContentDescription("Calculator display");

mLogic = new Logic(this, mHistory, mDisplay, (Button) findViewById(R.id.equal));
Button equalButton = (Button) findViewById(R.id.equal);
equalButton.setContentDescription("Equal button");

HistoryAdapter historyAdapter = new HistoryAdapter(this, mHistory, mLogic);

// Set initial focus for accessibility
mDisplay.requestFocus();
<<End of snippet n. 0>>