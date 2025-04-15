/*ADT: Fix NPE in ConfigComposite

For some reason the locale used by the ConfigState in the
ConfigurationComposite had null members. Failed to reproduce
it. One clue might be there was a non-selected GLE2 open
as an editor tab but the Java workspace was not the main
visible one. It's possible some event fired before the
composite controls got created.

Change-Id:I2c8b6c86842f3c772c2a7ec113941eb8fc01b9e4*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 89285bf..4b1d5f6 100644

//Synthetic comment -- @@ -189,9 +189,12 @@
sb.append(configName);
sb.append(SEP);
if (locale != null) {
                    sb.append(((LanguageQualifier) locale[0]).getValue());
                    sb.append(SEP_LOCALE);
                    sb.append(((RegionQualifier) locale[1]).getValue());
}
sb.append(SEP);
sb.append(theme);







