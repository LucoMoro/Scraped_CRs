/*Improve exception detail in Mac.update

Change-Id:I51667af9b054afe202d98474e219f04eb5267370*/
//Synthetic comment -- diff --git a/luni/src/main/java/javax/crypto/Mac.java b/luni/src/main/java/javax/crypto/Mac.java
//Synthetic comment -- index 46be141..c208456 100644

//Synthetic comment -- @@ -265,7 +265,9 @@
return;
}
if ((offset < 0) || (len < 0) || ((offset + len) > input.length)) {
            throw new IllegalArgumentException("Incorrect arguments");
}
spiImpl.engineUpdate(input, offset, len);
}







