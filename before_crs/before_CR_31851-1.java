/*TextView vibration change

Vibration  plays twice when the call to super method is called and returns handle.
This was found while playing with the Launcher application. When longpressing an app icon in the full app list to add it as a shprtcut in the launcher, vibration is called twice.

Change-Id:I73a56af7529ff5680d36e170347c7f65a7adef23Signed-off-by: JF Dionne <jeanfrancoisbd@gmail.com>*/
//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index b9d3d43..214cd9a 100644

//Synthetic comment -- @@ -9221,6 +9221,7 @@
if (super.performLongClick()) {
mDiscardNextActionUp = true;
handled = true;
}

// Long press in empty space moves cursor and shows the Paste affordance if available.







