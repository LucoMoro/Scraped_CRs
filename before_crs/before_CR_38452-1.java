/*Bring Random.nextGaussian in line with the specification.

Bug: 6126164
Change-Id:I36d5ccc776b15f7f4085da23c189f91507d57224*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/util/Random.java b/luni/src/main/java/java/util/Random.java
//Synthetic comment -- index 7ce74cc..cc9de58 100644

//Synthetic comment -- @@ -140,25 +140,21 @@
* section 3.4.1, subsection C, algorithm P.
*/
public synchronized double nextGaussian() {
        if (haveNextNextGaussian) { // if X1 has been returned, return the
                                    // second Gaussian
haveNextNextGaussian = false;
return nextNextGaussian;
}

double v1, v2, s;
do {
            v1 = 2 * nextDouble() - 1; // Generates two independent random
                                        // variables U1, U2
v2 = 2 * nextDouble() - 1;
s = v1 * v1 + v2 * v2;
        } while (s >= 1);
        double norm = Math.sqrt(-2 * Math.log(s) / s);
        nextNextGaussian = v2 * norm; // should that not be norm instead
                                        // of multiplier ?
haveNextNextGaussian = true;
        return v1 * norm; // should that not be norm instead of multiplier
                            // ?
}

/**







