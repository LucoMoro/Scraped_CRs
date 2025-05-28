
//<Beginning of snippet n. 0>


}

/**
* Returns the selected view info. Cannot be null.
*/
public CanvasViewInfo getViewInfo() {

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


boolean insideSelection = false;

for (CanvasSelection cs : mSelections) {
                    if (cs.getRect().contains(x, y)) {
insideSelection = true;
break;
}
// We should now have a proper selection that matches the cursor.
// Let's use this one. We make a copy of it since the "sanitize" pass
// below might remove some of the selected objects.
                    mDragSelection.addAll(mSelections);
}
}


//<End of snippet n. 1>








