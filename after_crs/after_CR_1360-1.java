/*Add support for returning the most recent result as a string to the calling activity.*/




//Synthetic comment -- diff --git a/src/com/android/calculator2/Calculator.java b/src/com/android/calculator2/Calculator.java
//Synthetic comment -- index eb7453d..2be5025 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.content.Intent;
import android.content.res.Configuration;

public class Calculator extends Activity {
//Synthetic comment -- @@ -57,8 +58,11 @@
mHistory = mPersist.history;

mDisplay = (CalculatorDisplay) findViewById(R.id.display);
        
        Intent returnResult = new Intent(""); // Result return to calling activity
        setResult(RESULT_OK, returnResult);
        
        mLogic = new Logic(this, mHistory, mDisplay, (Button) findViewById(R.id.equal), returnResult);
HistoryAdapter historyAdapter = new HistoryAdapter(this, mHistory, mLogic);
mHistory.setObserver(historyAdapter);
View view;








//Synthetic comment -- diff --git a/src/com/android/calculator2/Logic.java b/src/com/android/calculator2/Logic.java
//Synthetic comment -- index 244f438..34cc24d 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import android.widget.Button;
import android.widget.EditText;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import org.javia.arity.Symbols;
//Synthetic comment -- @@ -33,6 +34,7 @@
private History mHistory;
private String  mResult = "";
private Button mEqualButton;
    private Intent mReturnResult;
private final String mEnterString;
private boolean mIsError = false;
private final boolean mOrientationPortrait;
//Synthetic comment -- @@ -51,7 +53,7 @@

private final String mErrorString;

    Logic(Context context, History history, CalculatorDisplay display, Button equalButton, Intent returnResult) {
mErrorString = context.getResources().getString(R.string.error);
mOrientationPortrait = context.getResources().getConfiguration().orientation
== Configuration.ORIENTATION_PORTRAIT;
//Synthetic comment -- @@ -69,6 +71,8 @@
mDisplay.setLogic(this);
mEqualButton = equalButton;
mEnterString = context.getText(R.string.enter).toString();
        
        mReturnResult = returnResult;

clearWithHistory(false);
}
//Synthetic comment -- @@ -142,6 +146,7 @@
mIsError = true;
mResult = mErrorString;
}
            mReturnResult.setAction(mResult);
if (text.equals(mResult)) {
//no need to show result, it is exactly what the user entered
clearWithHistory(true);







