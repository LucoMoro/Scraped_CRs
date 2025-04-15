/*Added correct OrientationChange handling

Dialogs for Custom Labels in Contactedit are now handled correctly.
When you press Phonenumber Type and select Custom, a
Input Dialog pops up. There was an error when
changing Orientation (in example
open the Keyboard on the G1), the Dialog was gone
(with an internal WindowLeaked Exception).
Now the Dialogstate, the EditText state and the Type for the Custom Label
are preserved by storing them in state.
The CustomLabel Dialog now also has a onDismissListener, so the private
variable will always be null when there is no Dialog to save ressources
and we do not leak a Context.

Change-Id:I77bdadc9c245e42895af04356848b2ac850b19cb*/
//Synthetic comment -- diff --git a/src/com/android/contacts/ui/widget/GenericEditorView.java b/src/com/android/contacts/ui/widget/GenericEditorView.java
//Synthetic comment -- index 24262bb..d855876 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import android.content.Context;
import android.content.DialogInterface;
import android.content.Entity;
import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.PhoneNumberFormattingTextWatcher;
//Synthetic comment -- @@ -61,6 +62,7 @@
public class GenericEditorView extends RelativeLayout implements Editor, View.OnClickListener {
protected static final int RES_FIELD = R.layout.item_editor_field;
protected static final int RES_LABEL_ITEM = android.R.layout.simple_list_item_1;

protected LayoutInflater mInflater;

//Synthetic comment -- @@ -78,6 +80,8 @@
protected EntityDelta mState;
protected boolean mReadOnly;

protected boolean mHideOptional = true;

protected EditType mType;
//Synthetic comment -- @@ -285,6 +289,7 @@
*/
private Dialog createCustomDialog() {
final EditText customType = new EditText(mContext);
customType.setInputType(INPUT_TYPE_CUSTOM);
customType.requestFocus();

//Synthetic comment -- @@ -354,7 +359,9 @@
// Only when the custum value input in the next step is correct one.
// this method also set the type value to what the user requested here.
mPendingType = selected;
                    createCustomDialog().show();
} else {
// User picked type, and we're sure it's ok to actually write the entry.
mType = selected;
//Synthetic comment -- @@ -372,6 +379,13 @@
return builder.create();
}

/** {@inheritDoc} */
public void onClick(View v) {
switch (v.getId()) {
//Synthetic comment -- @@ -405,6 +419,9 @@
private static class SavedState extends BaseSavedState {
public boolean mHideOptional;
public int[] mVisibilities;

SavedState(Parcelable superState) {
super(superState);
//Synthetic comment -- @@ -414,6 +431,9 @@
super(in);
mVisibilities = new int[in.readInt()];
in.readIntArray(mVisibilities);
}

@Override
//Synthetic comment -- @@ -421,6 +441,9 @@
super.writeToParcel(out, flags);
out.writeInt(mVisibilities.length);
out.writeIntArray(mVisibilities);
}

public static final Parcelable.Creator<SavedState> CREATOR
//Synthetic comment -- @@ -445,6 +468,17 @@

ss.mHideOptional = mHideOptional;

final int numChildren = mFields.getChildCount();
ss.mVisibilities = new int[numChildren];
for (int i = 0; i < numChildren; i++) {
//Synthetic comment -- @@ -468,5 +502,15 @@
for (int i = 0; i < numChildren; i++) {
mFields.getChildAt(i).setVisibility(ss.mVisibilities[i]);
}
}
}







