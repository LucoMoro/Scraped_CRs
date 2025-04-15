/*Fix a crash in DDMS when parsing an unexpected sync event format due to a change in froyo.

Change-Id:I878911b3167ff12b48fa3ea3a7bfb9129466b9bd*/
//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/log/event/DisplaySyncPerf.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/log/event/DisplaySyncPerf.java
//Synthetic comment -- index 9ce7045..b484f26 100644

//Synthetic comment -- @@ -185,6 +185,9 @@
mTooltips[HTTP_NETWORK].add(tip);
mTooltips[HTTP_PROCESSING].add(tip);
}
} catch (InvalidTypeException e) {
}
}







