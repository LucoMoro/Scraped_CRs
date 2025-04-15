/*verifyCertificateChain should convert unknown exceptions to CertificateException

Bug:http://code.google.com/p/android/issues/detail?id=42533Change-Id:Id0e0eb8f007987decb4fee94135be8a92d2f8981*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLSocketImpl.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLSocketImpl.java
//Synthetic comment -- index 4cc16e6..2ca434d 100644

//Synthetic comment -- @@ -593,10 +593,8 @@

} catch (CertificateException e) {
throw e;
        } catch (RuntimeException e) {
            throw e;
} catch (Exception e) {
            throw new RuntimeException(e);
}
}








