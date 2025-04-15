/*Update KeyListener classes for AZERTY hardware

Need to check META key state in lookup function in KeyListener
classes to take care of case where one hardware key is used for
alpha & digit with function key.

Change-Id:Iea0e09630349130f3b0438cf3bfd62969e68cbe6*/
//Synthetic comment -- diff --git a/core/java/android/text/method/DateTimeKeyListener.java b/core/java/android/text/method/DateTimeKeyListener.java
//Synthetic comment -- index f8ebc40..f99c47e 100644

//Synthetic comment -- @@ -44,6 +44,19 @@
}

/**
* The characters that are used.
*
* @see KeyEvent#getMatch








//Synthetic comment -- diff --git a/core/java/android/text/method/DigitsKeyListener.java b/core/java/android/text/method/DigitsKeyListener.java
//Synthetic comment -- index f0f072c..1b3e79e 100644

//Synthetic comment -- @@ -40,6 +40,19 @@
}

/**
* The characters that are used.
*
* @see KeyEvent#getMatch








//Synthetic comment -- diff --git a/core/java/android/text/method/TimeKeyListener.java b/core/java/android/text/method/TimeKeyListener.java
//Synthetic comment -- index 3fbfd8c..01a3d8e 100644

//Synthetic comment -- @@ -18,6 +18,9 @@

import android.view.KeyEvent;
import android.text.InputType;

/**
* For entering times in a text field.
//Synthetic comment -- @@ -44,6 +47,19 @@
}

/**
* The characters that are used.
*
* @see KeyEvent#getMatch







