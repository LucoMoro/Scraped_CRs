/*Clear binding when the current input method is dead.

Change-Id:I15010787ada1600b9e78d33b0be73a8c8344dc5e*/




//Synthetic comment -- diff --git a/core/java/android/view/inputmethod/InputMethodManager.java b/core/java/android/view/inputmethod/InputMethodManager.java
//Synthetic comment -- index e30687f..8ea1f5a 100644

//Synthetic comment -- @@ -1348,6 +1348,7 @@
callback.finishedEvent(seq, false);
} catch (RemoteException ex) {
}
                clearBindingLocked();
}
}
}
//Synthetic comment -- @@ -1377,6 +1378,7 @@
callback.finishedEvent(seq, false);
} catch (RemoteException ex) {
}
                clearBindingLocked();
}
}
}







