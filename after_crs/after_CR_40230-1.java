/*Throw on finalizer timeout so it can be reported by crash reporters

Bug: 6894375
Change-Id:I94664f8bb54dc7647b4d3666484d88b1c8428c4b*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/Daemons.java b/luni/src/main/java/java/lang/Daemons.java
//Synthetic comment -- index a7d0964..3653359 100644

//Synthetic comment -- @@ -103,14 +103,6 @@
}
}
}
}

/**
//Synthetic comment -- @@ -229,12 +221,10 @@
}

// The current object has exceeded the finalization deadline; abort!
                    String message = object.getClass().getName() + ".finalize() timed out after "
                            + elapsedMillis + " ms; limit is " + MAX_FINALIZE_MILLIS + " ms";
                    System.logE(message);
                    throw new RuntimeException(message);
} catch (InterruptedException ignored) {
}
}







