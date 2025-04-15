/*Remove error(tag, throwable) from LayoutLog.

We should never only log an exception with no associated
message.
Also, I've found several case in the layoutlib where there
is a message in place of the tag.

This new API makes it more clear that there's both a tag and a message
since both are required for error()

Change-Id:I6d5f45c07b1cb8df96311d930170526bb729fae6*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLogger.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLogger.java
//Synthetic comment -- index bc89b24..1dc5b78 100644

//Synthetic comment -- @@ -96,23 +96,6 @@
}

@Override
    public void error(String tag, Throwable throwable) {
        AdtPlugin.log(throwable, "%1$s: %2$s", mName, tag);
        assert throwable != null;
        mHaveExceptions = true;

        String message = throwable.getMessage();
        if (message == null) {
            message = throwable.getClass().getName();
        } else if (tag == null && throwable instanceof ClassNotFoundException
                && !message.contains(ClassNotFoundException.class.getSimpleName())) {
            tag = ClassNotFoundException.class.getSimpleName();
        }
        String description = describe(tag, message);
        addError(description);
    }

    @Override
public void error(String tag, String message, Throwable throwable) {
String description = describe(tag, message);
AdtPlugin.log(throwable, "%1$s: %2$s", mName, description);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java
//Synthetic comment -- index b75d6fd..216502c 100644

//Synthetic comment -- @@ -268,11 +268,6 @@
}

@Override
                        public void error(String tag, Throwable throwable) {
                            AdtPlugin.log(throwable, null);
                        }

                        @Override
public void warning(String tag, String message) {
AdtPlugin.log(IStatus.WARNING, message);
}








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java b/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java
//Synthetic comment -- index a961135..e1ce53b 100644

//Synthetic comment -- @@ -350,7 +350,7 @@
}

public void error(Throwable t) {
                log.error(null, t);
}

public void error(String message) {








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/LayoutLog.java b/layoutlib_api/src/com/android/ide/common/rendering/api/LayoutLog.java
//Synthetic comment -- index a735f81..979b873 100644

//Synthetic comment -- @@ -18,15 +18,15 @@

public class LayoutLog {

    public void error(String tag, String message) {
    }

    public void error(String tag, Throwable t) {
    }

public void warning(String tag, String message) {
}

/**
* Logs an error message and a {@link Throwable}.
* @param message the message to log.
//Synthetic comment -- @@ -36,7 +36,4 @@

}

    public void fidelityWarning(String tag, String message, Throwable throwable) {

    }
}







