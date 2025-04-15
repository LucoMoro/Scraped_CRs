/*BT: Power cycle issue

Tab out of range access.

“String[] properties” is a table transmitted
by the remote BT device that describes
its properties (name, profile, uuid …)

This table structure, is
   property name: name
   property value: Headset Motorola
   …

but in case of UUID:
   property name: UUIDs
   property Length: number of UUIDs transmitted
   property value: UUID1
   property value: UUID2
   property value: UUID3

If the UUDI property length doesn’t fit
the table length, when we try to access memory
out of the table range, causing a crash.
Since this table is transmitted by remote BT device
we have to control it on reception.

Change-Id:If36fa359f622e715aed03c13c83dbde3bfdc6f08Author: Cedric Bondier <cedricx.bondier@intel.com>
Signed-off-by: Cedric Bondier <cedricx.bondier@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 44346*/
//Synthetic comment -- diff --git a/core/java/android/server/BluetoothDeviceProperties.java b/core/java/android/server/BluetoothDeviceProperties.java
//Synthetic comment -- index fe3ef79..c0d7695 100644

//Synthetic comment -- @@ -58,16 +58,31 @@
}
if (name.equals("UUIDs") || name.equals("Nodes")) {
StringBuilder str = new StringBuilder();
                    len = Integer.valueOf(properties[++i]);
for (int j = 0; j < len; j++) {
                        str.append(properties[++i]);
                        str.append(",");
}
if (len > 0) {
newValue = str.toString();
}
} else {
                    newValue = properties[++i];
}

propertyValues.put(name, newValue);







