/*Made TextView non-editable when using the inputType="none" attribute in XML.

Fixes issue 2854. Since android:editable is deprecated, developers must use android:inputType="none" to make non-editable EditText widgets.
Calling setInputType(InputType.TYPE_NULL) worked, but declaring it in XML didn't.

Change-Id:I81aee2e790c27e9d3ec0c6b81be41e8e1de77ff0Signed-off-by: Janos Levai <digistyl3@gmail.com>*/
//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index b9d3d43..798bf25 100644

//Synthetic comment -- @@ -808,6 +808,9 @@

case com.android.internal.R.styleable.TextView_inputType:
inputType = a.getInt(attr, mInputType);
break;

case com.android.internal.R.styleable.TextView_imeOptions:







