/*Avoid system_server crash in viewroot surface null pointer

Change-Id:I12a0cd2413665de1b4092b473bf87f8e255b116b*/




//Synthetic comment -- diff --git a/core/java/android/view/ViewRoot.java b/core/java/android/view/ViewRoot.java
//Synthetic comment -- index 03efea9..a287f6be 100644

//Synthetic comment -- @@ -1428,7 +1428,9 @@
}

} finally {
               if (surface != null && canvas != null) {
		    surface.unlockCanvasAndPost(canvas);
		}
}

if (LOCAL_LOGV) {







