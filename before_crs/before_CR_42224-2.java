/*Make addAddress locale safe

Using regular string concatenation to avoid unexpected
results in some locales.

Change-Id:I47dd5e174c4a2e88dc18e014002820cdbf63fcad*/
//Synthetic comment -- diff --git a/core/java/android/net/VpnService.java b/core/java/android/net/VpnService.java
//Synthetic comment -- index fb5263d..65d3f2b 100644

//Synthetic comment -- @@ -329,7 +329,7 @@
throw new IllegalArgumentException("Bad address");
}

            mAddresses.append(String.format(" %s/%d", address.getHostAddress(), prefixLength));
return this;
}








