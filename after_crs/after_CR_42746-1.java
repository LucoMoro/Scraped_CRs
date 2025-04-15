/*Document that Random.nextGaussian should use StrictMath.

Bug: 7146208
Change-Id:Ife58d868e9310de3f378500fdbf53b2f3a690f04*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Random.java b/luni/src/main/java/java/util/Random.java
//Synthetic comment -- index cc9de58..b0a92ff 100644

//Synthetic comment -- @@ -151,6 +151,8 @@
v2 = 2 * nextDouble() - 1;
s = v1 * v1 + v2 * v2;
} while (s >= 1 || s == 0);

        // The specification says this uses StrictMath.
double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s) / s);
nextNextGaussian = v2 * multiplier;
haveNextNextGaussian = true;







