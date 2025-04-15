/*Use flag for region, if specified

Change-Id:I2916a25d26ed3f9a5915c7c1de499a07b76b53d4*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 0a80b5e..670568a 100644

//Synthetic comment -- @@ -1632,16 +1632,17 @@
}

String country = region;
        if (country.isEmpty()) {
            // Special cases
            if (language.equals("ar")) {        //$NON-NLS-1$
                country = "AE";                 //$NON-NLS-1$
            } else if (language.equals("zh")) { //$NON-NLS-1$
                country = "CN";                 //$NON-NLS-1$
            } else if (language.equals("en")) { //$NON-NLS-1$
                country = "US";                 //$NON-NLS-1$
            } else if (language.equals("fa")) { //$NON-NLS-1$
                country = "IR";                 //$NON-NLS-1$
            }
}

if (country.isEmpty()) {







