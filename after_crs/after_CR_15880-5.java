/*Sort the IME list before showing to user

The original list is unsorted so the order is random to users.
For users who installed two or more Chinese IMEs, they may see
Chinese IME, English IME, Chinese IME. That's odd to users.

Change-Id:Ibdc0cf1151a9f151aefeae0aa11c69d781a748dc*/




//Synthetic comment -- diff --git a/services/java/com/android/server/InputMethodManagerService.java b/services/java/com/android/server/InputMethodManagerService.java
//Synthetic comment -- index 5bf66e4..1b50a94 100644

//Synthetic comment -- @@ -78,9 +78,12 @@
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.Collator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
* This class provides a system service that manages input methods.
//Synthetic comment -- @@ -1513,20 +1516,21 @@
hideInputMethodMenuLocked();

int N = immis.size();

            final Map<CharSequence, InputMethodInfo> imMap =
                new TreeMap<CharSequence, InputMethodInfo>(Collator.getInstance());

for (int i = 0; i < N; ++i) {
InputMethodInfo property = immis.get(i);
if (property == null) {
continue;
}
                imMap.put(property.loadLabel(pm), property);
}

            N = imMap.size();
            mItems = imMap.keySet().toArray(new CharSequence[N]);
            mIms = imMap.values().toArray(new InputMethodInfo[N]);

int checkedItem = 0;
for (int i = 0; i < N; ++i) {







