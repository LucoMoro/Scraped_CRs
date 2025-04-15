/*SDK: Fix ADT build.

Change-Id:Ib779e8e692f285e7a5afa5d3385954b73b63d28a*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/VersionCheck.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/VersionCheck.java
//Synthetic comment -- index b468c5e..0dde05f 100644

//Synthetic comment -- @@ -57,7 +57,7 @@
private final static Pattern sPluginVersionPattern = Pattern.compile(
"^plugin.version=(\\d+)\\.(\\d+)\\.(\\d+).*$"); //$NON-NLS-1$
private final static Pattern sSourcePropPattern = Pattern.compile(
            "^" + PkgProps.PKG_REVISION + "=(\\d+).*$"); //$NON-NLS-1$

/**
* Checks the plugin and the SDK have compatible versions.







