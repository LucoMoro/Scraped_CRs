/*RadioGroup now adds RadioButton children that aren't direct children, but can be children of child ViewGroups.

Change-Id:I9d843926403f364c3389fb45288f645b1818d6c7*/
//Synthetic comment -- diff --git a/core/java/android/widget/RadioGroup.java b/core/java/android/widget/RadioGroup.java
//Synthetic comment -- index 393346a..4528769 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


/**
//Synthetic comment -- @@ -124,20 +125,48 @@

@Override
public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof RadioButton) {
            final RadioButton button = (RadioButton) child;
            if (button.isChecked()) {
                mProtectFromCheckedChange = true;
                if (mCheckedId != -1) {
                    setCheckedStateForView(mCheckedId, false);
                }
                mProtectFromCheckedChange = false;
                setCheckedId(button.getId());
            }
        }

super.addView(child, index, params);
}

/**
* <p>Sets the selection to the radio button whose identifier is passed in
//Synthetic comment -- @@ -356,28 +385,78 @@
* {@inheritDoc}
*/
public void onChildViewAdded(View parent, View child) {
            if (parent == RadioGroup.this && child instanceof RadioButton) {
                int id = child.getId();
                // generates an id if it's missing
                if (id == View.NO_ID) {
                    id = child.hashCode();
                    child.setId(id);
}
                ((RadioButton) child).setOnCheckedChangeWidgetListener(
                        mChildOnCheckedChangeListener);
}

if (mOnHierarchyChangeListener != null) {
mOnHierarchyChangeListener.onChildViewAdded(parent, child);
}
}

/**
* {@inheritDoc}
*/
public void onChildViewRemoved(View parent, View child) {
            if (parent == RadioGroup.this && child instanceof RadioButton) {
                ((RadioButton) child).setOnCheckedChangeWidgetListener(null);
}

if (mOnHierarchyChangeListener != null) {







