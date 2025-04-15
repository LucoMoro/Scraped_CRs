/*Phone: Shows the list of the status PLMNs

Shows all the list of the PLMNs available but does not
highlight the status (unknown,available,current,forbidden)
PLMNs amongst them.

Shows the list of status (unknown,available,current,forbidden)
PLMNs.

Change-Id:If9986634116d3ebea8bf4a12882ac0b7adb8e420*/




//Synthetic comment -- diff --git a/src/com/android/phone/NetworkSetting.java b/src/com/android/phone/NetworkSetting.java
//Synthetic comment -- index 87bd05a..3c69cc2 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2006 The Android Open Source Project
 * Copyright (c) 2012, Code Aurora Forum. All rights reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -407,7 +408,8 @@
// confusing mcc/mnc.
for (OperatorInfo ni : result) {
Preference carrier = new Preference(this, null);
                    carrier.setTitle(getNetworkTitle(ni) + "("
                            + ni.getState().toString().toLowerCase() + ")");
carrier.setPersistent(false);
mNetworkList.addPreference(carrier);
mNetworkMap.put(carrier, ni);







