/*Prevent IndexOutOfBoundsException on toString() if vibrate array is empty (non null)
example: notification.vibrate = new long[0];*/




//Synthetic comment -- diff --git a/core/java/android/app/Notification.java b/core/java/android/app/Notification.java
//Synthetic comment -- index 51fddb1..f222640 100644

//Synthetic comment -- @@ -460,7 +460,9 @@
sb.append(this.vibrate[i]);
sb.append(',');
}
	    if (N != -1) {
        	sb.append(this.vibrate[N]);
	    }
sb.append("]");
} else if ((this.defaults & DEFAULT_VIBRATE) != 0) {
sb.append("default");







