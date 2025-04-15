/*systrace: Update template to match systrace.py

Change-Id:I0757c58061aeb932b7f1c21d0cadea5d5d25024c*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceOutputParser.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/systrace/SystraceOutputParser.java
//Synthetic comment -- index 19e8471..7963a30 100644

//Synthetic comment -- @@ -35,6 +35,18 @@
+ "<title>Android System Trace</title>\n"
+ "%s\n"
+ "%s\n"
            + "<script language=\"javascript\">\n"
            + "document.addEventListener('DOMContentLoaded', function() {\n"
            + "  if (!linuxPerfData)\n"
            + "    return;\n"
            + "  var m = new tracing.TimelineModel(linuxPerfData);\n"
            + "  var timelineViewEl = document.querySelector('.view');\n"
            + "  base.ui.decorate(timelineViewEl, tracing.TimelineView);\n"
            + "  timelineViewEl.model = m;\n"
            + "  timelineViewEl.tabIndex = 1;\n"
            + "  timelineViewEl.timeline.focusElement = timelineViewEl;\n"
            + "});\n"
            + "</script>\n"
+ "<style>\n"
+ "  .view {\n"
+ "    overflow: hidden;\n"







