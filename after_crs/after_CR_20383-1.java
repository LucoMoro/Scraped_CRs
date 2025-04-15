/*Resource parser should strip unescaped " from string values.

Change-Id:I15cc5becdf139a7eeb01309d0d903a35ba773ed9*/




//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/rendering/ValueResourceParser.java b/ide_common/src/com/android/ide/common/rendering/ValueResourceParser.java
//Synthetic comment -- index 8c56a98..93303ba 100644

//Synthetic comment -- @@ -222,6 +222,14 @@
System.arraycopy(buffer, i+1, buffer, i, length - i - 1);
length--;
}
            } else if (buffer[i] == '"') {
                // if the " was escaped it would have been processed above.
                // offset the buffer to erase the "
                System.arraycopy(buffer, i+1, buffer, i, length - i - 1);
                length--;

                // unlike when unescaping, we want to process the next char too
                i--;
}
}








