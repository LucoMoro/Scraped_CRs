/*Sort the IME list before showing to user

The original list is unsorted so the order is random to users.
For users who installed two or more Chinese IMEs, they may see
Chinese IME, English IME, Chinese IME. That's odd to users.*/
//Synthetic comment -- diff --git a/services/java/com/android/server/InputMethodManagerService.java b/services/java/com/android/server/InputMethodManagerService.java
//Synthetic comment -- index 5bf66e4..0e74169 100644

//Synthetic comment -- @@ -78,9 +78,12 @@
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* This class provides a system service that manages input methods.
//Synthetic comment -- @@ -1513,21 +1516,22 @@
hideInputMethodMenuLocked();

int N = immis.size();
    
            mItems = new CharSequence[N];
            mIms = new InputMethodInfo[N];
    
            int j = 0;
for (int i = 0; i < N; ++i) {
InputMethodInfo property = immis.get(i);
if (property == null) {
continue;
}
                mItems[j] = property.loadLabel(pm);
                mIms[j] = property;
                j++;
}
    
int checkedItem = 0;
for (int i = 0; i < N; ++i) {
if (mIms[i].getId().equals(lastInputMethodId)) {







