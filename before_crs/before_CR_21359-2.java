/*We don't need these local references.

Change-Id:I227c88eb4eb5d2842124e1c944cc18d5b28cccc1*/
//Synthetic comment -- diff --git a/core/java/android/text/method/MultiTapKeyListener.java b/core/java/android/text/method/MultiTapKeyListener.java
//Synthetic comment -- index 6d94788..2a739fa 100644

//Synthetic comment -- @@ -116,7 +116,7 @@
content.replace(selStart, selEnd,
String.valueOf(current).toUpperCase());
removeTimeouts(content);
                    Timeout t = new Timeout(content);

return true;
}
//Synthetic comment -- @@ -124,7 +124,7 @@
content.replace(selStart, selEnd,
String.valueOf(current).toLowerCase());
removeTimeouts(content);
                    Timeout t = new Timeout(content);

return true;
}
//Synthetic comment -- @@ -140,7 +140,7 @@

content.replace(selStart, selEnd, val, ix, ix + 1);
removeTimeouts(content);
                    Timeout t = new Timeout(content);

return true;
}
//Synthetic comment -- @@ -206,7 +206,7 @@
}

removeTimeouts(content);
            Timeout t = new Timeout(content);

// Set up the callback so we can remove the timeout if the
// cursor moves.







