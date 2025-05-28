
//<Beginning of snippet n. 0>


}
}

/**
* This manages the execution of the main thread in an
* application process, scheduling and executing activities,
}

private void handleLaunchActivity(ActivityClientRecord r, Intent customIntent) {
// If we are getting ready to gc after going to the background, well
// we are back active so skip it.
unscheduleGcIdler();
info.activity = r;
info.state = r.state;
mH.post(info);
}

final void performRestartActivity(IBinder token) {
// If the system process has died, it's game over for everyone.
}
}
}

public final void requestRelaunchActivity(IBinder token,
}

private void handleRelaunchActivity(ActivityClientRecord tmp) {
// If we are getting ready to gc after going to the background, well
// we are back active so skip it.
unscheduleGcIdler();

//<End of snippet n. 0>








