//<Beginning of snippet n. 0>
import android.database.DataSetObserver;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ArrayAdapterTest {
    private ArrayAdapter<String> adapter;
    private MockDataSetObserver mockDataSetObserver;

    @Before
    public void setUp() {
        mockDataSetObserver = new MockDataSetObserver();
        adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
        adapter.registerDataSetObserver(mockDataSetObserver);
    }

    @Test
    public void testAddMethodNotifiesObserver() {
        adapter.add("Item 1");
        assertEquals(1, mockDataSetObserver.getCalledOnChangedCount());
    }

    @Test
    public void testAddAllMethodNotifiesObserver() {
        adapter.addAll("Item 1", "Item 2", "Item 3");
        assertEquals(3, mockDataSetObserver.getCalledOnChangedCount());
    }

    @Test
    public void testAddNullValue() {
        adapter.add(null);
        assertEquals(1, mockDataSetObserver.getCalledOnChangedCount());
    }

    @Test
    public void testAddAllEmptyList() {
        adapter.addAll(new ArrayList<String>());
        assertEquals(0, mockDataSetObserver.getCalledOnChangedCount());
    }

    private static class MockDataSetObserver extends DataSetObserver {
        private int mCalledOnChangedCount = 0;

        @Override
        public void onChanged() {
            mCalledOnChangedCount++;
        }

        public int getCalledOnChangedCount() {
            return mCalledOnChangedCount;
        }
    }
}
//<End of snippet n. 0>