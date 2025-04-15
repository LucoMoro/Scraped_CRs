/*Removed offensive music genre

The genre is a bad joke, not a real thing.

Seehttp://en.wikipedia.org/wiki/NegerpunkChange-Id:I073c89e286ef4b8f9d678e1b137c657b26066099*/
//Synthetic comment -- diff --git a/media/java/android/media/MediaScanner.java b/media/java/android/media/MediaScanner.java
//Synthetic comment -- index 6f8b809..a37d8b3 100644

//Synthetic comment -- @@ -283,7 +283,7 @@
"Terror",
"Indie",
"Britpop",
        "Negerpunk",
"Polsk Punk",
"Beat",
"Christian Gangsta",
//Synthetic comment -- @@ -683,7 +683,7 @@
try {
short genreIndex = Short.parseShort(number.toString());
if (genreIndex >= 0) {
                            if (genreIndex < ID3_GENRES.length) {
return ID3_GENRES[genreIndex];
} else if (genreIndex == 0xFF) {
return null;







