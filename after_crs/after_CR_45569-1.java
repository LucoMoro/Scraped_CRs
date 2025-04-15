/*Regression test for 38195

Change-Id:I4fd6b6aa994e4ccce39bc68f069d0ef2075531b8*/




//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/ApiDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/ApiDetectorTest.java
//Synthetic comment -- index af7c3d5..963ea7c 100644

//Synthetic comment -- @@ -657,4 +657,52 @@
"apicheck/ApiCallTest9.class.data=>bin/classes/test/pkg/ApiCallTest9.class"
));
}

    public void test38195() throws Exception {
        // See http://code.google.com/p/android/issues/detail?id=38195
        assertEquals(
            "bin/classes/TestLint.class: Error: Call requires API level 16 (current min is 4): new android.database.SQLException [NewApi]\n" +
            "bin/classes/TestLint.class: Error: Call requires API level 9 (current min is 4): java.lang.String#isEmpty [NewApi]\n" +
            "bin/classes/TestLint.class: Error: Call requires API level 9 (current min is 4): new java.sql.SQLException [NewApi]\n" +
            "3 errors, 0 warnings\n",

            lintProject(
                    "apicheck/classpath=>.classpath",
                    "apicheck/minsdk4.xml=>AndroidManifest.xml",
                    /*
                        Compiled from "TestLint.java"
                        public class test.pkg.TestLint extends java.lang.Object{
                        public test.pkg.TestLint();
                          Code:
                           0:   aload_0
                           1:   invokespecial   #8; //Method java/lang/Object."<init>":()V
                           4:   return

                        public void test(java.lang.Exception)   throws java.lang.Exception;
                          Code:
                           0:   ldc #19; //String
                           2:   invokevirtual   #21; //Method java/lang/String.isEmpty:()Z
                           5:   istore_2
                           6:   new #27; //class java/sql/SQLException
                           9:   dup
                           10:  ldc #29; //String error on upgrade:
                           12:  aload_1
                           13:  invokespecial   #31; //Method java/sql/SQLException."<init>":
                                                       (Ljava/lang/String;Ljava/lang/Throwable;)V
                           16:  athrow

                        public void test2(java.lang.Exception)   throws java.lang.Exception;
                          Code:
                           0:   new #39; //class android/database/SQLException
                           3:   dup
                           4:   ldc #29; //String error on upgrade:
                           6:   aload_1
                           7:   invokespecial   #41; //Method android/database/SQLException.
                                               "<init>":(Ljava/lang/String;Ljava/lang/Throwable;)V
                           10:  athrow
                        }
                     */
                    "apicheck/TestLint.class.data=>bin/classes/TestLint.class"
                ));
    }
}







