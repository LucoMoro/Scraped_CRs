/*Disable hostname verification with client certificates when requested by user

Bug: 6838215
Change-Id:I82227720d512d8fc8dd20bdb0bf449c4a82d1372*/




//Synthetic comment -- diff --git a/emailcommon/src/com/android/emailcommon/utility/EmailClientConnectionManager.java b/emailcommon/src/com/android/emailcommon/utility/EmailClientConnectionManager.java
//Synthetic comment -- index 15d1cca..33419f8 100644

//Synthetic comment -- @@ -98,11 +98,9 @@
}
KeyManager keyManager =
KeyChainKeyManager.fromAlias(context, hostAuth.mClientCertAlias);
            boolean insecure = hostAuth.shouldTrustAllServerCerts();
            SSLSocketFactory ssf = SSLUtils.getHttpSocketFactory(insecure, keyManager);
            registry.register(new Scheme(schemeName, ssf, hostAuth.mPort));
}
}








