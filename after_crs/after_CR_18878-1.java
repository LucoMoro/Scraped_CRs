/*RadioGroup now adds RadioButton children that aren't direct children, but can be children of child ViewGroups.

Change-Id:I9d843926403f364c3389fb45288f645b1818d6c7*/




//Synthetic comment -- diff --git a/core/java/android/widget/RadioGroup.java b/core/java/android/widget/RadioGroup.java
//Synthetic comment -- index 393346a..4528769 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;


/**
//Synthetic comment -- @@ -124,20 +125,48 @@

@Override
public void addView(View child, int index, ViewGroup.LayoutParams params) {
        lookForRadioButtonChildren(child);

super.addView(child, index, params);
}
    
    /**
     * Recursively find and handle all RadioButtons children
     * 
     * @param child the current child view to investigate
     */
    private void lookForRadioButtonChildren(View child) {
        if(child instanceof RadioButton) {
            setRadioChildChecked((RadioButton)child);
        } else if (child instanceof ViewGroup) {
            
            // Don't go into child RadioGroups.
            if(child instanceof RadioGroup)
                return;

            ViewGroup group = (ViewGroup)child;
            final int len = group.getChildCount();
            for(int i = 0; i < len; i++)
                lookForRadioButtonChildren(group.getChildAt(i));
    	}
    }
    
    /**
     * If this child view is a checked RadioButton, then set this
     * as the checked button for this group. 
     * 
     * @param child The RadioButton to determine whether checked
     */
    private void setRadioChildChecked(RadioButton button) {
        if (button.isChecked()) {
            mProtectFromCheckedChange = true;
            if (mCheckedId != -1) {
                setCheckedStateForView(mCheckedId, false);
            }
            mProtectFromCheckedChange = false;
            setCheckedId(button.getId());
        }
    }

/**
* <p>Sets the selection to the radio button whose identifier is passed in
//Synthetic comment -- @@ -356,28 +385,78 @@
* {@inheritDoc}
*/
public void onChildViewAdded(View parent, View child) {
            
            // Set check changed listeners on all child RadioButtons
            if(parent != null && parent instanceof ViewParent) {
                if(weAreTheParent((ViewParent)parent)) {
                    setAllRadioChildren(child, true);
}
}

if (mOnHierarchyChangeListener != null) {
mOnHierarchyChangeListener.onChildViewAdded(parent, child);
}
}
        
        /**
         * Determine whether we are the most immediate RadioGroup parent in
         * this branch of the tree
         */
        private boolean weAreTheParent(ViewParent parent) {
            // Look for this RadioGroup in the parent hierarchy
            while(parent != null && parent != RadioGroup.this && !(parent instanceof RadioGroup)) {
                parent = parent.getParent();
            }
            return parent == RadioGroup.this;
        }
        
        /**
         * Recurse through the tree, finding all immediate RadioButton children
         * that aren't under another RadioGroup.
         */
        private void setAllRadioChildren(View child, boolean isAdded) {
            if(child instanceof RadioButton) {
                setRadioChild((RadioButton)child, isAdded);
            } else if (child instanceof ViewGroup) {
                
                // Don't go into child RadioGroups.
                if(child instanceof RadioGroup)
                    return;
                
                ViewGroup group = (ViewGroup)child;
                final int len = group.getChildCount();
                for(int i = 0; i < len; i++)
                    setAllRadioChildren(group.getChildAt(i), isAdded);
            }
        }
        
        /**
         * Set the appropriate state for this RadioButton child.
         */
        private void setRadioChild(RadioButton button, boolean isAdded) {
            if(isAdded) {
                int id = button.getId();
                // generates an id if it's missing
                if (id == View.NO_ID) {
                    id = button.hashCode();
                    button.setId(id);
                }
                button.setOnCheckedChangeWidgetListener(
                        mChildOnCheckedChangeListener);
            } else {
                button.setOnCheckedChangeWidgetListener(null);
            }
        }
        
/**
* {@inheritDoc}
*/
public void onChildViewRemoved(View parent, View child) {
            // Clear check changed listeners on all child RadioButtons
            if(parent != null && parent instanceof ViewParent) {
                if(weAreTheParent((ViewParent)parent)) {
                    setAllRadioChildren(child, false);
                }
}

if (mOnHierarchyChangeListener != null) {







