/*CHAR_SEQUENCE_CREATOR cannot handle null string

TextUtils.writeToParcel can handle null as input CharSequence
but createFromParcel will throw NullPointerException. Transforming
to and from parcel should handle null in the same way.

Change-Id:I707ea9be2091d2655c5f63eaa57c5674befa5ad3*/




//Synthetic comment -- diff --git a/core/java/android/text/TextUtils.java b/core/java/android/text/TextUtils.java
//Synthetic comment -- index 8675d05..0d7aa02 100644

//Synthetic comment -- @@ -627,10 +627,16 @@
public  CharSequence createFromParcel(Parcel p) {
int kind = p.readInt();

            String string = p.readString();
            if (string == null) {
                return null;
            }

            if (kind == 1) {
                return string;
            }

            SpannableString sp = new SpannableString(string);

while (true) {
kind = p.readInt();








//Synthetic comment -- diff --git a/core/tests/coretests/src/android/text/TextUtilsTest.java b/core/tests/coretests/src/android/text/TextUtilsTest.java
//Synthetic comment -- index 79d57f1..63dd0cc 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package android.text;

import android.graphics.Paint;
import android.os.Parcel;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.text.Spannable;
//Synthetic comment -- @@ -352,6 +353,51 @@
assertFalse(TextUtils.delimitedStringContains("network,mock,gpsx", ',', "gps"));
}

    @SmallTest
    public void testCharSequenceCreator() {
        Parcel p = Parcel.obtain();
        TextUtils.writeToParcel(null, p, 0);
        CharSequence text = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(p);
        assertNull("null CharSequence should generate null from parcel", text);
        p = Parcel.obtain();
        TextUtils.writeToParcel("test", p, 0);
        text = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(p);
        assertEquals("conversion to/from parcel failed", "test", text);
    }

    @SmallTest
    public void testCharSequenceCreatorNull() {
        Parcel p;
        CharSequence text;
        p = Parcel.obtain();
        TextUtils.writeToParcel(null, p, 0);
        p.setDataPosition(0);
        text = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(p);
        assertNull("null CharSequence should generate null from parcel", text);
    }

    @SmallTest
    public void testCharSequenceCreatorSpannable() {
        Parcel p;
        CharSequence text;
        p = Parcel.obtain();
        TextUtils.writeToParcel(new SpannableString("test"), p, 0);
        p.setDataPosition(0);
        text = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(p);
        assertEquals("conversion to/from parcel failed", "test", text.toString());
    }

    @SmallTest
    public void testCharSequenceCreatorString() {
        Parcel p;
        CharSequence text;
        p = Parcel.obtain();
        TextUtils.writeToParcel("test", p, 0);
        p.setDataPosition(0);
        text = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(p);
        assertEquals("conversion to/from parcel failed", "test", text.toString());
    }

/**
* CharSequence wrapper for testing the cases where text is copied into
* a char array instead of working from a String or a Spanned.







