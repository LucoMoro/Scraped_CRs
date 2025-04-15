/*Migrate render target settings to released versions

When loading the project's render target setting (which as of ADT 11
is per project rather than per layout), check to see if it has a code
name, and if so, see if the user has the final version of the platform
installed as well. If so, migrate the render target to the final
version instead since the user probably did not intentionally want to
stay with the pre-release version after installing the final version.

Change-Id:Ic45232b5dc691b770045c3281f2c36b580347784*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index d304303..9f2ba98 100644

//Synthetic comment -- @@ -2400,6 +2400,24 @@
}
}
target = stringToTarget(values[1]);
}

return Pair.of(locale, target);







