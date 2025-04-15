/*If you call setDrawingCacheEnabled(true), updateCursorControllerPosition is never called.
I've overrided getDrawingCache() to call updateCursorControllerPosition()

Change-Id:I5f79478da03f32e9c3b2f209dcb5355c79bce543*/




//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index 68600cf..002c1bb 100644

//Synthetic comment -- @@ -28,6 +28,7 @@
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
//Synthetic comment -- @@ -8404,6 +8405,13 @@
}
return mInBatchEditControllers;
}
    
    @Override
    public Bitmap getDrawingCache(boolean autoScale){
    	updateCursorControllerPositions();
    	return super.getDrawingCache(autoScale);
    }


@ViewDebug.ExportedProperty
private CharSequence            mText;







