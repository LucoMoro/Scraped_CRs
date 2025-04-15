/*Phone: Fix to answer MT call when USSD session is on progress.

On receiving MMI Complete event for USSD cancel operation, InCallScreen
session was being ended. Changes done to move InCallScreen to
foreground after cancelling an USSD session if the call state is
not IDLE.

Change-Id:Id4c44152b64a6ff3334df31d27ccb64b303055a9*/




//Synthetic comment -- diff --git a/src/com/android/phone/InCallScreen.java b/src/com/android/phone/InCallScreen.java
//Synthetic comment -- index 2cc22a0..cf7cd6f 100755

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006 The Android Open Source Project
 * Copyright (c) 2012, Code Aurora Forum. All rights reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -1035,7 +1036,12 @@
*/
public void endInCallScreenSession() {
if (DBG) log("endInCallScreenSession()... phone state = " + mCM.getState());
        // Do not end the session if a call is on progress.
        if (mCM.getState() == PhoneConstants.State.IDLE) {
            endInCallScreenSession(false);
        } else {
            Log.i(LOG_TAG, "endInCallScreenSession(): Call in progress");
        }
}

/**







