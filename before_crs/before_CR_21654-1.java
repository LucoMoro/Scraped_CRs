/*Set initial focus to improve accessibility.

Change-Id:If0e841c20235268236d124aa60a4a5d47309f35c*/
//Synthetic comment -- diff --git a/src/com/android/calculator2/Calculator.java b/src/com/android/calculator2/Calculator.java
//Synthetic comment -- index 768a317..4bf3921 100644

//Synthetic comment -- @@ -61,6 +61,7 @@
mHistory = mPersist.history;

mDisplay = (CalculatorDisplay) findViewById(R.id.display);

mLogic = new Logic(this, mHistory, mDisplay, (Button) findViewById(R.id.equal));
HistoryAdapter historyAdapter = new HistoryAdapter(this, mHistory, mLogic);







