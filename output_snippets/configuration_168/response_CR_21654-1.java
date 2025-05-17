//<Beginning of snippet n. 0>
mHistory = mPersist.history;

mDisplay = (CalculatorDisplay) findViewById(R.id.display);
mDisplay.setFocusable(true);
mDisplay.setFocusableInTouchMode(true);

mLogic = new Logic(this, mHistory, mDisplay, (Button) findViewById(R.id.equal));
Button equalButton = (Button) findViewById(R.id.equal);
equalButton.setContentDescription("Equal button");
equalButton.setOnClickListener(v -> {
    // Implement action for button press
    // Trigger accessibility event here if necessary
});

HistoryAdapter historyAdapter = new HistoryAdapter(this, mHistory, mLogic);
equalButton.setContentDescription("Equal button");
mDisplay.requestFocus();
//<End of snippet n. 0>