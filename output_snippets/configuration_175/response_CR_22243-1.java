//<Beginning of snippet n. 0>
import com.android.ide.common.api.IMenuCallback;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
import com.android.ide.common.api.MenuAction;

import java.util.ArrayList;
import java.util.List;

public class TableLayout extends ViewGroup implements IViewRule {
    private List<TableRow> rows;

    public TableLayout() {
        rows = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            addRow(new TableRow());
        }
    }

    public void addRow(TableRow row) {
        rows.add(row);
        // Logic to visually add the row to the layout
        // Assuming a method to represent rows visually exists
        this.addView(row); // Pseudo-method for UI update
    }

    public void removeRow(TableRow row) {
        rows.remove(row);
        // Logic to visually remove the row from the layout
        // Assuming a method to remove visual representation exists
        this.removeView(row); // Pseudo-method for UI update
    }

    @Override
    protected boolean isVertical(INode node) {
        return true; // Tables are always vertical
    }
    // Other methods
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
package com.android.ide.common.layout;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;

import java.util.ArrayList;
import java.util.List;

public class TableRow {
    // Other properties and methods

    public TableRow() {
        // Constructor logic
    }

    public void addRow(TableRow row) {
        // Logic for adding a new row
    }

    public void removeRow() {
        // Logic for removing an existing row
    }
}
//<End of snippet n. 1>