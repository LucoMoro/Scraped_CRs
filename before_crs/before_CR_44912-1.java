/*Phone: Fix for emergency call display issue.

The callerInfo is not updated with connection address in case of
emergency and voice mail numbers.

Change-Id:If7187b5667dd469b96b4d30c25a2638e865d628c*/
//Synthetic comment -- diff --git a/src/com/android/phone/CallCard.java b/src/com/android/phone/CallCard.java
//Synthetic comment -- index 77702d1..96516fe 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -40,6 +41,7 @@
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallManager;
//Synthetic comment -- @@ -597,6 +599,20 @@
if (DBG) log("- displayMainCallStatus: using data we already have...");
if (o instanceof CallerInfo) {
CallerInfo ci = (CallerInfo) o;
// Update CNAP information if Phone state change occurred
ci.cnapName = conn.getCnapName();
ci.numberPresentation = conn.getNumberPresentation();







