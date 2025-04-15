/*OpenSSLCipher: Add DESede support

Change-Id:I81f1bec8e3562c3ed90b35a60829ca0dfc4d8341*/




//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLCipher.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLCipher.java
//Synthetic comment -- index c65cbdc..ce50553 100644

//Synthetic comment -- @@ -687,4 +687,131 @@
return AES_BLOCK_SIZE;
}
}

    public static class DESEDE extends OpenSSLCipher {
        private static int DES_BLOCK_SIZE = 8;

        public DESEDE(Mode mode, Padding padding) {
            super(mode, padding);
        }

        public static class CBC extends DESEDE {
            public CBC(Padding padding) {
                super(Mode.CBC, padding);
            }

            public static class NoPadding extends CBC {
                public NoPadding() {
                    super(Padding.NOPADDING);
                }
            }

            public static class PKCS5Padding extends CBC {
                public PKCS5Padding() {
                    super(Padding.PKCS5PADDING);
                }
            }
        }

        public static class CFB extends DESEDE {
            public CFB(Padding padding) {
                super(Mode.CFB, padding);
            }

            public static class NoPadding extends CFB {
                public NoPadding() {
                    super(Padding.NOPADDING);
                }
            }

            public static class PKCS5Padding extends CFB {
                public PKCS5Padding() {
                    super(Padding.PKCS5PADDING);
                }
            }
        }

        public static class ECB extends DESEDE {
            public ECB(Padding padding) {
                super(Mode.ECB, padding);
            }

            public static class NoPadding extends ECB {
                public NoPadding() {
                    super(Padding.NOPADDING);
                }
            }

            public static class PKCS5Padding extends ECB {
                public PKCS5Padding() {
                    super(Padding.PKCS5PADDING);
                }
            }
        }

        public static class OFB extends DESEDE {
            public OFB(Padding padding) {
                super(Mode.OFB, padding);
            }

            public static class NoPadding extends OFB {
                public NoPadding() {
                    super(Padding.NOPADDING);
                }
            }

            public static class PKCS5Padding extends OFB {
                public PKCS5Padding() {
                    super(Padding.PKCS5PADDING);
                }
            }
        }

        @Override
        protected String getCipherName(int keySize, Mode mode) {
            if (mode == Mode.ECB) {
                return "des-ede";
            } else {
                return "des-ede-" + mode.toString().toLowerCase(Locale.US);
            }
        }

        @Override
        protected void checkSupportedKeySize(int keySize) throws InvalidKeyException {
            if (keySize != 16 && keySize != 24) {
                throw new InvalidKeyException("key size must be 128 or 192 bits");
            }
        }

        @Override
        protected void checkSupportedMode(Mode mode) throws NoSuchAlgorithmException {
            switch (mode) {
                case CBC:
                case CFB:
                case CFB1:
                case CFB8:
                case ECB:
                case OFB:
                    return;
                default:
                    throw new NoSuchAlgorithmException("Unsupported mode " + mode.toString());
            }
        }

        @Override
        protected void checkSupportedPadding(Padding padding) throws NoSuchPaddingException {
            switch (padding) {
                case NOPADDING:
                case PKCS5PADDING:
                    return;
                default:
                    throw new NoSuchPaddingException("Unsupported padding " + padding.toString());
            }
        }

        @Override
        protected int getCipherBlockSize() {
            return DES_BLOCK_SIZE;
        }
    }
}








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLProvider.java b/luni/src/main/java/org/apache/harmony/xnet/provider/jsse/OpenSSLProvider.java
//Synthetic comment -- index 4a2f9b7..7e56b96 100644

//Synthetic comment -- @@ -140,5 +140,14 @@
put("Cipher.AES/CTR/PKCS5Padding", OpenSSLCipher.AES.CTR.PKCS5Padding.class.getName());
put("Cipher.AES/OFB/NoPadding", OpenSSLCipher.AES.OFB.NoPadding.class.getName());
put("Cipher.AES/OFB/PKCS5Padding", OpenSSLCipher.AES.OFB.PKCS5Padding.class.getName());

        put("Cipher.DESEDE/CBC/NoPadding", OpenSSLCipher.DESEDE.CBC.NoPadding.class.getName());
        put("Cipher.DESEDE/CBC/PKCS5Padding", OpenSSLCipher.DESEDE.CBC.PKCS5Padding.class.getName());
        put("Cipher.DESEDE/CFB/NoPadding", OpenSSLCipher.DESEDE.CFB.NoPadding.class.getName());
        put("Cipher.DESEDE/CFB/PKCS5Padding", OpenSSLCipher.DESEDE.CFB.PKCS5Padding.class.getName());
        put("Cipher.DESEDE/ECB/NoPadding", OpenSSLCipher.DESEDE.ECB.NoPadding.class.getName());
        put("Cipher.DESEDE/ECB/PKCS5Padding", OpenSSLCipher.DESEDE.ECB.PKCS5Padding.class.getName());
        put("Cipher.DESEDE/OFB/NoPadding", OpenSSLCipher.DESEDE.OFB.NoPadding.class.getName());
        put("Cipher.DESEDE/OFB/PKCS5Padding", OpenSSLCipher.DESEDE.OFB.PKCS5Padding.class.getName());
}
}







