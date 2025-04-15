/*StkMenu/StkInput/StkDialog activities should not cancel timer when paused

During SelectItem / GetInkey / GetInput, if any higher priority events
such as incoming call, new sms, screen off intent,notification alerts,
user actions such as 'User moving to another activtiy' etc. occur during
SELECT ITEM / GETINKEY / GETINPUT ongoing session respectively, the
respective activity would receive 'onPause()' evt resulting in
cancellation of the timer. As a result no terminal response is sent to
the card

Change-Id:I9b96b4304c96ce8dceda4c398e0e6e24de11f870*/
//Synthetic comment -- diff --git a/src/com/android/stk/StkDialogActivity.java b/src/com/android/stk/StkDialogActivity.java
//Synthetic comment -- index 3fd3ef7..22e373c 100644

//Synthetic comment -- @@ -132,8 +132,6 @@
@Override
public void onPause() {
super.onPause();

        cancelTimeOut();
}

@Override








//Synthetic comment -- diff --git a/src/com/android/stk/StkInputActivity.java b/src/com/android/stk/StkInputActivity.java
//Synthetic comment -- index b6228fb..1b7659b 100644

//Synthetic comment -- @@ -165,8 +165,6 @@
@Override
public void onPause() {
super.onPause();

        cancelTimeOut();
}

@Override








//Synthetic comment -- diff --git a/src/com/android/stk/StkMenuActivity.java b/src/com/android/stk/StkMenuActivity.java
//Synthetic comment -- index aac1a12..8c18cfd 100644

//Synthetic comment -- @@ -170,7 +170,6 @@
super.onPause();

appService.indicateMenuVisibility(false);
        cancelTimeOut();
}

@Override







