/*Fix null warning from eclipse (passing nullable to non-null)

Change-Id:I080b2f4f4668b6bc09383523966b2bbd10e7eece*/




//Synthetic comment -- diff --git a/manifmerger/src/com/android/manifmerger/XmlUtils.java b/manifmerger/src/com/android/manifmerger/XmlUtils.java
//Synthetic comment -- index b17c7d4..1fe62a0 100755

//Synthetic comment -- @@ -617,8 +617,10 @@
// We want to add or replace the attribute.
if (attr == null) {
attr = doc.createAttributeNS(attrNsUri, attrName);
                        if (attrNsUri != null) {
                            attr.setPrefix(com.android.utils.XmlUtils.lookupNamespacePrefix(
                                    element, attrNsUri));
                        }
attrs.setNamedItemNS(attr);
}
attr.setNodeValue(value);
//Synthetic comment -- @@ -703,7 +705,7 @@
hasText = true;
}
} else if (t == Node.ELEMENT_NODE) {
                children.add(printElement(child, nsPrefix, prefix));
if (!nextSiblings) {
break;
}







