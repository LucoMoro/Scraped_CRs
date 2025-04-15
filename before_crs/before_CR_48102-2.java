/*Stk: Few STK fixes

1) Fix for GCF T.C Launch Browser, connect to default URL

GCF TC : 27.22.4.26.1.4.2, Seq 1.1, When launch browser
proactive command is issued with default URL , browser
should be launched with the default home page that it
is set to and not a harcoded home page.

2) StkDialog activity should not cancel timer when it is paused

During display text,  if any higher / lower priority events
such as incoming call, new sms, screen off intent,
notification alerts, user actions such as 'User moving to
another activtiy' etc.. occur during Display Text ongoing session,
this activity would receive 'onPause()' evt resulting in
cancellation of the timer. As a result no terminal response is
sent to the card

3) StkMenu/StkInput activity should not cancel timer when paused

During SelectItem / GetInkey / GetInput, if any
higher / lower priority events such as incoming call,
new sms, screen off intent,notification alerts,
user actions such as 'User moving to another activtiy' etc..
occur during SELECT ITEM / GETINKEY / GETINPUT ongoing session
respectively, the respective activity would receive
'onPause()' evt resulting in cancellation of the timer.
As a result no terminal response is sent to the card

4) Fix OPEN CHANNEL user confirmation

On receipt of OPEN CHANNEL , user confirmation needs to be set
if user acknowledges the message / command.

5) Show 'Toast indications' SEND DATA / RECV DATA and CLOSE CHANNEL

Displaying Toast indication yields better UI experience when there are
multiple SEND / RCV DATA proactive commands in a single OPEN CHANNEL
session.

Change-Id:I83ebaa33fa1e35781022cc04d57c5a5bd2a24f9d*/
//Synthetic comment -- diff --git a/src/com/android/stk/StkAppService.java b/src/com/android/stk/StkAppService.java
//Synthetic comment -- index 9dcd25b..4f14672 100644

//Synthetic comment -- @@ -462,7 +462,10 @@
break;
}
}
            launchTransientEventMessage();
break;
}

//Synthetic comment -- @@ -492,6 +495,7 @@

// set result code
boolean helpRequired = args.getBoolean(HELP, false);

switch(args.getInt(RES_ID)) {
case RES_ID_MENU_SELECTION:
//Synthetic comment -- @@ -529,7 +533,7 @@
break;
case RES_ID_CONFIRM:
CatLog.d(this, "RES_ID_CONFIRM");
            boolean confirmed = args.getBoolean(CONFIRMATION);
switch (mCurrentCmd.getCmdType()) {
case DISPLAY_TEXT:
resMsg.setResultCode(confirmed ? ResultCode.OK
//Synthetic comment -- @@ -583,12 +587,19 @@
switch (choice) {
case YES:
resMsg.setResultCode(ResultCode.OK);
break;
case NO:
resMsg.setResultCode(ResultCode.USER_NOT_ACCEPT);
break;
}
break;
default:
CatLog.d(this, "Unknown result id");
return;
//Synthetic comment -- @@ -698,7 +709,7 @@

Intent intent = new Intent(Intent.ACTION_VIEW);

        Uri data;
if (settings.url != null) {
CatLog.d(this, "settings.url = " + settings.url);
if ((settings.url.startsWith("http://") || (settings.url.startsWith("https://")))) {
//Synthetic comment -- @@ -708,18 +719,10 @@
CatLog.d(this, "modifiedUrl = " + modifiedUrl);
data = Uri.parse(modifiedUrl);
}
        } else {
            // If no URL specified, just bring up the "home page".
            //
            // (Note we need to specify *something* in the intent's data field
            // here, since if you fire off a VIEW intent with no data at all
            // you'll get an activity chooser rather than the browser.  There's
            // no specific URI that means "use the default home page", so
            // instead let's just explicitly bring up http://google.com.)
            data = Uri.parse("http://google.com/");
}
        intent.setData(data);

intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
switch (settings.mode) {
case USE_EXISTING_BROWSER:
//Synthetic comment -- @@ -729,6 +732,9 @@
intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
break;
case LAUNCH_IF_NOT_ALREADY_LAUNCHED:
intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
break;
}








//Synthetic comment -- diff --git a/src/com/android/stk/StkDialogActivity.java b/src/com/android/stk/StkDialogActivity.java
//Synthetic comment -- index 3fd3ef7..7409f79 100644

//Synthetic comment -- @@ -1,7 +1,7 @@
/*
* Copyright (C) 2007 The Android Open Source Project
*
 * Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
//Synthetic comment -- @@ -133,7 +133,15 @@
public void onPause() {
super.onPause();

        cancelTimeOut();
}

@Override








//Synthetic comment -- diff --git a/src/com/android/stk/StkInputActivity.java b/src/com/android/stk/StkInputActivity.java
//Synthetic comment -- index b6228fb..1088cec 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2007 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License"); you may not
* use this file except in compliance with the License. You may obtain a copy of
//Synthetic comment -- @@ -165,8 +166,7 @@
@Override
public void onPause() {
super.onPause();

        cancelTimeOut();
}

@Override








//Synthetic comment -- diff --git a/src/com/android/stk/StkMenuActivity.java b/src/com/android/stk/StkMenuActivity.java
//Synthetic comment -- index aac1a12..a2b3c96 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2007 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -169,8 +170,16 @@
public void onPause() {
super.onPause();

        appService.indicateMenuVisibility(false);
        cancelTimeOut();
}

@Override







