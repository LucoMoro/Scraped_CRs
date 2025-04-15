/*Telephony: Add tethering definitions

Change-Id:I286f6c763c5e95b69d7405f94ba9193737861e8c*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/DctConstants.java b/telephony/java/com/android/internal/telephony/DctConstants.java
//Synthetic comment -- index 10ac153..362d9c0 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2012 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -91,6 +92,7 @@
public static final int CMD_SET_DEPENDENCY_MET = BASE + 31;
public static final int CMD_SET_POLICY_DATA_ENABLE = BASE + 32;
public static final int EVENT_ICC_CHANGED = BASE + 33;

/***** Constants *****/









//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RILConstants.java b/telephony/java/com/android/internal/telephony/RILConstants.java
//Synthetic comment -- index f501b21..fb74b97 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -152,6 +153,12 @@
public static final int DATA_PROFILE_CBS       = 4;
public static final int DATA_PROFILE_OEM_BASE  = 1000;

int RIL_REQUEST_GET_SIM_STATUS = 1;
int RIL_REQUEST_ENTER_SIM_PIN = 2;
int RIL_REQUEST_ENTER_SIM_PUK = 3;
//Synthetic comment -- @@ -297,4 +304,5 @@
int RIL_UNSOL_EXIT_EMERGENCY_CALLBACK_MODE = 1033;
int RIL_UNSOL_RIL_CONNECTED = 1034;
int RIL_UNSOL_VOICE_RADIO_TECH_CHANGED = 1035;
}







