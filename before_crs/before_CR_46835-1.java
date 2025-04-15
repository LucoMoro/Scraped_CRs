/*Fix InnerNodeImpl.insertBefore.

As the submitter said, an insertBefore with a null refChild should be taken as
a request to append.

Bug:http://code.google.com/p/android/issues/detail?id=24530Change-Id:I26141e2d4dd7a7805209a455a7c16bf0f26e7acf*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xml/dom/InnerNodeImpl.java b/luni/src/main/java/org/apache/harmony/xml/dom/InnerNodeImpl.java
//Synthetic comment -- index 418789d..4bea1b4 100644

//Synthetic comment -- @@ -85,6 +85,10 @@
public Node insertBefore(Node newChild, Node refChild) throws DOMException {
LeafNodeImpl refChildImpl = (LeafNodeImpl) refChild;

if (refChildImpl.document != document) {
throw new DOMException(DOMException.WRONG_DOCUMENT_ERR, null);
}








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/xml/DomTest.java b/luni/src/test/java/libcore/xml/DomTest.java
//Synthetic comment -- index 88da565..e4d4ecd 100644

//Synthetic comment -- @@ -1655,6 +1655,19 @@
assertNull(text.getNextSibling());
}

public void testBomAndByteInput() throws Exception {
byte[] xml = {
(byte) 0xef, (byte) 0xbb, (byte) 0xbf,







