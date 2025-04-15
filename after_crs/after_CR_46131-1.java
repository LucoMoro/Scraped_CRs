/*Make /sdcard detector also flag /data/data/ references

Change-Id:If08b99370a32eaa256e9cb63939428480d563bfe*/




//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/SdCardDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/SdCardDetector.java
//Synthetic comment -- index e7545a1..32f8676 100644

//Synthetic comment -- @@ -46,7 +46,11 @@
"Looks for hardcoded references to /sdcard",

"Your code should not reference the `/sdcard` path directly; instead use " +
            "`Environment.getExternalStorageDirectory().getPath()`.\n" +
            "\n" +
            "Similarly, do not reference the `/data/data/` path directly; it can vary " +
            "in multi-user scenarios. Instead, use " +
            "`Context.getFilesDir().getPath()`.",

Category.CORRECTNESS,
6,
//Synthetic comment -- @@ -91,16 +95,25 @@
@Override
public boolean visitStringLiteral(StringLiteral node) {
String s = node.astValue();
            if (s.isEmpty() || s.charAt(0) != '/') {
                return false;
            }

            if (s.startsWith("/sdcard")                        //$NON-NLS-1$
                    || s.startsWith("/mnt/sdcard/")            //$NON-NLS-1$
                    || s.startsWith("/system/media/sdcard")    //$NON-NLS-1$
                    || s.startsWith("file://sdcard//")          //$NON-NLS-1$
                    || s.startsWith("file:///sdcard/")) {      //$NON-NLS-1$
String message = "Do not hardcode \"/sdcard/\"; " +
"use Environment.getExternalStorageDirectory().getPath() instead";
Location location = mContext.getLocation(node);
mContext.report(ISSUE, node, location, message, s);
            } else if (s.startsWith("/data/data/")    //$NON-NLS-1$
                    || s.startsWith("/data/user/")) { //$NON-NLS-1$
                String message = "Do not hardcode \"/data/\"; " +
                        "use Context.getFilesDir().getPath() instead";
                    Location location = mContext.getLocation(node);
                    mContext.report(ISSUE, node, location, message, s);
}

return false;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/SdCardDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/SdCardDetectorTest.java
//Synthetic comment -- index 1acbb87..1e628ec 100644

//Synthetic comment -- @@ -54,8 +54,13 @@
"src/test/pkg/SdCardTest.java:31: Warning: Do not hardcode \"/sdcard/\"; use Environment.getExternalStorageDirectory().getPath() instead [SdCardPath]\n" +
"  intent.putExtra(\"start-dir\", \"/sdcard\");\n" +
"                               ~~~~~~~~~\n" +
            "src/test/pkg/SdCardTest.java:32: Warning: Do not hardcode \"/data/\"; use Context.getFilesDir().getPath() instead [SdCardPath]\n" +
            "  String mypath = \"/data/data/foo\";\n" +
            "                  ~~~~~~~~~~~~~~~~\n" +
            "src/test/pkg/SdCardTest.java:33: Warning: Do not hardcode \"/data/\"; use Context.getFilesDir().getPath() instead [SdCardPath]\n" +
            "  String base = \"/data/data/foo.bar/test-profiling\";\n" +
            "                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
            "0 errors, 11 warnings\n",

lintProject("src/test/pkg/SdCardTest.java.txt=>src/test/pkg/SdCardTest.java"));
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/SdCardTest.java.txt b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/data/src/test/pkg/SdCardTest.java.txt
//Synthetic comment -- index e2719e9..d4fe9b6 100644

//Synthetic comment -- @@ -29,5 +29,7 @@
		intent.setDataAndType(Uri.parse("file://sdcard/foo.json"), "application/bar-json");
		intent.putExtra("path-filter", "/sdcard(/.+)*");
		intent.putExtra("start-dir", "/sdcard");
		String mypath = "/data/data/foo";
		String base = "/data/data/foo.bar/test-profiling";
	}
}







