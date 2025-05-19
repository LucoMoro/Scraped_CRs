//<Beginning of snippet n. 0>
mHistory = mPersist.history;

mDisplay = (CalculatorDisplay) findViewById(R.id.display);
mDisplay.setContentDescription("Calculator display for showing results");

Logic mLogic = new Logic(this, mHistory, mDisplay, (Button) findViewById(R.id.equal));
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

    @Override
    public void onPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
        super.onPopulateAccessibilityEvent(host, event);
        event.setContentDescription(mDisplay.getText());
    }
});

// Enable keyboard navigation
mDisplay.setOnKeyListener(new View.OnKeyListener() {
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_ENTER:
                    // Handle enter key press for calculation
                    return true;
                case KeyEvent.KEYCODE_DPAD_UP:
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    // Handle directional navigation
                    return true;
            }
        }
        return false;
    }
});

@Override
protected void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    if (hasFocus) {
        mDisplay.requestFocus();
    }
}

//<End of snippet n. 0>