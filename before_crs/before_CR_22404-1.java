/*Re-assingning local variable if mStringBlocks was initialized here. NPE is thrown otherwise.

Change-Id:Ic8648aa5ae6a61348803171243c1c217feb058ec*/
//Synthetic comment -- diff --git a/core/java/android/content/res/AssetManager.java b/core/java/android/content/res/AssetManager.java
//Synthetic comment -- index 73d9458..4702327 100644

//Synthetic comment -- @@ -234,6 +234,7 @@
StringBlock[] blocks = mStringBlocks;
if (blocks == null) {
ensureStringBlocks();
}
outValue.string = blocks[block].get(outValue.data);
return true;







