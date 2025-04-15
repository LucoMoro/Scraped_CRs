/*Add missing logging for SET_TTY_MODE and QUERY_TTY_MODE

Change-Id:Ic794ef8258138b903f83c5ed77a0ee09e75ca094*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RIL.java b/telephony/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index 2833d56..d79a89e 100644

//Synthetic comment -- @@ -3406,6 +3406,8 @@
RILRequest rr = RILRequest.obtain(
RILConstants.RIL_REQUEST_QUERY_TTY_MODE, response);

send(rr);
}

//Synthetic comment -- @@ -3419,6 +3421,9 @@
rr.mp.writeInt(1);
rr.mp.writeInt(ttyMode);

send(rr);
}








