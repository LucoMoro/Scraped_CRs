/*Document per-implementation behaviors of native get* methods

Change-Id:I5d20d7796b85ce62750dd50dd79e47d76716b24c*/
//Synthetic comment -- diff --git a/core/java/android/database/CursorWindow.java b/core/java/android/database/CursorWindow.java
//Synthetic comment -- index c756825..76b91f5 100644

//Synthetic comment -- @@ -245,6 +245,15 @@
}
}

private native byte[] getBlob_native(int row, int col);

/**
//Synthetic comment -- @@ -332,6 +341,19 @@
}
}

private native String getString_native(int row, int col);

/**
//Synthetic comment -- @@ -383,6 +405,17 @@
}
}

private native long getLong_native(int row, int col);

/**
//Synthetic comment -- @@ -402,6 +435,17 @@
}
}

private native double getDouble_native(int row, int col);

/**







