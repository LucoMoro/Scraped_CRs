/*sysinfo: Remove unsupported views.

General cleanup of the SysInfo Panel:

- Remove support for displaying wakelocks/alarm/sync etc info since
  the existing parsers were woefully out of date and didn't work
  anyway.
- Move parsing datasets out of the SWT thread

Change-Id:Iac77c322ad1db715772f585212da2330a6f9d693*/




//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/SysinfoPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/SysinfoPanel.java
//Synthetic comment -- index dad57dd..32fdcd9 100644

//Synthetic comment -- @@ -35,7 +35,7 @@
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
//Synthetic comment -- @@ -74,26 +74,17 @@
private int mMode = 0;

private static final int MODE_CPU = 0;
    private static final int MODE_MEMINFO = 1;

// argument to dumpsys; section in the bugreport holding the data
private static final String DUMP_COMMAND[] = {
"dumpsys cpuinfo",
"cat /proc/meminfo ; procrank",
};

private static final String CAPTIONS[] = {
"CPU load",
"Memory usage",
};

/**
//Synthetic comment -- @@ -101,9 +92,7 @@
*
* @param file The bugreport file to process.
*/
    private void generateDataset(File file) {
if (file == null) {
return;
}
//Synthetic comment -- @@ -111,15 +100,10 @@
BufferedReader br = getBugreportReader(file);
if (mMode == MODE_CPU) {
readCpuDataset(br);
} else if (mMode == MODE_MEMINFO) {
readMeminfoDataset(br);
}
            br.close();
} catch (IOException e) {
Log.e("DDMS", e);
}
//Synthetic comment -- @@ -160,23 +144,30 @@
* Fetching is asynchronous.  See also addOutput, flush, and isCancelled.
*/
private void loadFromDevice() {
        clearDataSet();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    initShellOutputBuffer();
                    if (mMode == MODE_MEMINFO) {
                        // Hack to add bugreport-style section header for meminfo
                        mTempStream.write("------ MEMORY INFO ------\n".getBytes());
                    }
                    getCurrentDevice().executeShellCommand(DUMP_COMMAND[mMode], SysinfoPanel.this);
                } catch (IOException e) {
                    Log.e("DDMS", e);
                } catch (TimeoutException e) {
                    Log.e("DDMS", e);
                } catch (AdbCommandRejectedException e) {
                    Log.e("DDMS", e);
                } catch (ShellCommandUnresponsiveException e) {
                    Log.e("DDMS", e);
                }
}
        }, "Sysinfo Output Collector");
        t.start();
}

/**
//Synthetic comment -- @@ -261,22 +252,6 @@
}
});

mFetchButton = new Button(buttons, SWT.PUSH);
mFetchButton.setText("Update from Device");
mFetchButton.setEnabled(false);
//Synthetic comment -- @@ -689,29 +664,28 @@
}
}

private void readCpuDataset(BufferedReader br) throws IOException {
        updateDataSet(BugReportParser.readCpuDataset(br), "");
}

private void readMeminfoDataset(BufferedReader br) throws IOException {
        updateDataSet(BugReportParser.readMeminfoDataset(br), "PSS in kB");
}

    private void clearDataSet() {
        mLabel.setText("");
        mDataset.clear();
}

    private void updateDataSet(final List<BugReportParser.DataValue> data, final String label) {
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                mLabel.setText(label);
                for (BugReportParser.DataValue d : data) {
                    mDataset.setValue(d.name, d.value);
                }
            }
        });
}
}







