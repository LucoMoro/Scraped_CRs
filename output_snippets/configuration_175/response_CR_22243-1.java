//<Beginning of snippet n. 0>
import com.android.ide.common.api.IMenuCallback;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
import com.android.ide.common.api.MenuAction;

import java.util.ArrayList;
import java.util.List;

public class TableLayout implements IViewRule {
    private List<TableRow> rows;

    public TableLayout() {
        this.rows = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            addRow(new TableRow());
        }
    }

    @Override
    protected boolean isVertical(INode node) {
        return true; // Tables are always vertical
    }

    public void addRow(TableRow row) {
        rows.add(row);
        // Notify listeners of change
    }

    public void removeRow(TableRow row) {
        rows.remove(row);
        // Update layout properly
    }

    // Other methods...
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
package com.android.ide.common.layout;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;

/**
* An {@link IViewRule} for android.widget.TableRow.
* respectively.
}
public class TableRow {
    // TableRow implementation
}
//<End of snippet n. 1>