//<Beginning of snippet n. 0>
mHistory = mPersist.history;

mDisplay = (CalculatorDisplay) findViewById(R.id.display);
mDisplay.setContentDescription("Calculator display showing current value");

mLogic = new Logic(this, mHistory, mDisplay, (Button) findViewById(R.id.equal));
HistoryAdapter historyAdapter = new HistoryAdapter(this, mHistory, mLogic);

mDisplay.requestFocus();
mDisplay.setFocusable(true);
mDisplay.setFocusableInTouchMode(true);

// Optional: Add visual focus indicators in the layout XML or programmatically
// This can be done by setting background colors or drawables on focus
// Assuming mDisplay has a method to change appearance on focus
mDisplay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            mDisplay.setBackgroundColor(Color.LTGRAY); // Highlight color
        } else {
            mDisplay.setBackgroundColor(Color.TRANSPARENT); // Default color
        }
    }
});

//<End of snippet n. 0>