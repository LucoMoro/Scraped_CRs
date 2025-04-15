/*removed calls to deprecated APIs

Change-Id:Iaaee3e770bf65d4f041b0786d0cf6c683feebea6*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/NumberPickerDialog.java b/src/com/android/mms/ui/NumberPickerDialog.java
//Synthetic comment -- index bc48555..0f5193b 100644

//Synthetic comment -- @@ -24,7 +24,6 @@
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.android.internal.widget.NumberPicker;

//Synthetic comment -- @@ -87,8 +86,8 @@

setTitle(title);

        setButton(DialogInterface.BUTTON_POSITIVE, context.getText(R.string.set), this);
        setButton(DialogInterface.BUTTON_NEGATIVE, context.getText(R.string.no), (OnClickListener) null);

LayoutInflater inflater =
(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);







