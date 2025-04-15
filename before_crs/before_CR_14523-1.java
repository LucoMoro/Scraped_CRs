/*Should unescapeXML(src) before getPartBy*()

Change-Id:Icc5cb8ed8a4e6e3e08308d0ba8aac8e8e2b17dc5*/
//Synthetic comment -- diff --git a/src/com/android/mms/model/MediaModelFactory.java b/src/com/android/mms/model/MediaModelFactory.java
//Synthetic comment -- index 5bc73be..510b38e 100644

//Synthetic comment -- @@ -61,6 +61,7 @@
PduPart part = null;

if (src != null) {
if (src.startsWith("cid:")) {
part = pb.getPartByContentId("<" + src.substring("cid:".length()) + ">");
} else {
//Synthetic comment -- @@ -81,6 +82,14 @@
throw new IllegalArgumentException("No part found for the model.");
}

private static MediaModel getRegionMediaModel(Context context,
String tag, String src, SMILRegionMediaElement srme,
LayoutModel layouts, PduPart part) throws DrmException, IOException, MmsException {







