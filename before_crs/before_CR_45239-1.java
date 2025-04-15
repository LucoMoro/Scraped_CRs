/*Phone: Display IncallScreen when calling using AT command

Display IncallScreen when calling using AT command

Change-Id:Ia5cf3a54eebcc75677c8acdd4a21505ef0c86faf*/
//Synthetic comment -- diff --git a/src/com/android/phone/NotificationMgr.java b/src/com/android/phone/NotificationMgr.java
//Synthetic comment -- index 530389c..dab6da1 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -1031,7 +1032,7 @@

// Activate a couple of special Notification features if an
// incoming call is ringing:
        if (hasRingingCall) {
if (DBG) log("- Using hi-pri notification for ringing call!");

// This is a high-priority event that should be shown even if the







