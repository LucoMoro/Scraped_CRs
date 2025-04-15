/*NullPointerException in HeaderIterator

I have found this bug with the FindBugs tool

Change-Id:I42e6097e974b902a80c75142a42c8cea7e980f8cSigned-off-by: László Dávid <laszlo.david@gmail.com>*/
//Synthetic comment -- diff --git a/java/gov/nist/javax/sip/message/HeaderIterator.java b/java/gov/nist/javax/sip/message/HeaderIterator.java
//Synthetic comment -- index 58db98c..0cd0746 100644

//Synthetic comment -- @@ -82,8 +82,8 @@
if (this.sipHeader == null)
throw new IllegalStateException();
if (toRemove) {
            this.sipHeader = null;
this.sipMessage.removeHeader(sipHeader.getName());
} else {
throw new IllegalStateException();
}







