//<Beginning of snippet n. 0>
mHistory = mPersist.history;

mDisplay = (CalculatorDisplay) findViewById(R.id.display);
mDisplay.setContentDescription("Calculator display for showing results"); 
mDisplay.requestFocus();

mLogic = new Logic(this, mHistory, mDisplay, (Button) findViewById(R.id.equal));
Button equalButton = (Button) findViewById(R.id.equal);
equalButton.setContentDescription("Equal button to calculate the result");
equalButton.setFocusable(true);

HistoryAdapter historyAdapter = new HistoryAdapter(this, mHistory, mLogic);
historyAdapter.setAccessibilityEnabled(true);

// Ensure dynamic content updates notify accessibility service
mDisplay.setAccessibilityDelegate(new View.AccessibilityDelegate() {
    @Override
    public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(host, info);
        info.setText(mDisplay.getText());
    }
});

// Enable keyboard navigation
mDisplay.setOnKeyListener(new View.OnKeyListener() {
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
            // Handle keyboard input accordingly
            return true;
        }
        return false;
    }
});

//<End of snippet n. 0>