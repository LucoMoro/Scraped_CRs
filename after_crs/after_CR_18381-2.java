/*Disabled EditText now loses focus and closes onscreen keyboard. Fixes Issue 2771.

Change-Id:I44e3c0aff2a0ce1e6426818bfe16c1d19c7c18ac*/




//Synthetic comment -- diff --git a/core/java/android/widget/EditText.java b/core/java/android/widget/EditText.java
//Synthetic comment -- index 1532db1..b911247 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;


/*
//Synthetic comment -- @@ -111,4 +112,19 @@
}
super.setEllipsize(ellipsis);
}

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            setFocusableInTouchMode(true);
        } else {
            setFocusable(false);
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            if (imm.isActive(this)) {
                imm.hideSoftInputFromWindow(getWindowToken(), 0);
            }
        }
        super.setEnabled(enabled);
    }
}







