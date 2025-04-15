/*Fix typo of <p> end tag in appwidgets.xml

Change-Id:Ic1f4d8ade0d32f3b74decbe557d4275dc2e19466*/




//Synthetic comment -- diff --git a/services/java/com/android/server/AppWidgetService.java b/services/java/com/android/server/AppWidgetService.java
//Synthetic comment -- index 6bf7102..981cc93 100644

//Synthetic comment -- @@ -855,7 +855,7 @@
out.startTag(null, "p");
out.attribute(null, "pkg", p.info.provider.getPackageName());
out.attribute(null, "cl", p.info.provider.getClassName());
                    out.endTag(null, "p");
p.tag = providerIndex;
providerIndex++;
}







