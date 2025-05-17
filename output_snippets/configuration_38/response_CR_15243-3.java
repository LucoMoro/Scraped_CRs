<<Beginning of snippet n. 0>>
assertEquals(2, mockDataSetObserver.getCalledOnChangedCount());
assertEquals(2, arrayAdapter.getCount());
assertTrue(arrayAdapter.contains(item1));
assertTrue(arrayAdapter.contains(item2));
assertFalse(arrayAdapter.contains(null));
}

private static class MockDataSetObserver extends DataSetObserver {

private int mCalledOnChangedCount;

@Override
public void onChanged() {
    mCalledOnChangedCount++;
}

public int getCalledOnChangedCount() {
    return mCalledOnChangedCount;
}
}

// Add comprehensive test cases for add and addAll methods

@Test
public void testAddSingleItem() {
    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>();
    MockDataSetObserver mockDataSetObserver = new MockDataSetObserver();
    arrayAdapter.registerDataSetObserver(mockDataSetObserver);

    String item = "Item1";
    arrayAdapter.add(item);
    
    assertEquals(1, arrayAdapter.getCount());
    assertTrue(arrayAdapter.contains(item));
    assertEquals(1, mockDataSetObserver.getCalledOnChangedCount());
}

@Test
public void testAddAllItems() {
    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>();
    MockDataSetObserver mockDataSetObserver = new MockDataSetObserver();
    arrayAdapter.registerDataSetObserver(mockDataSetObserver);

    List<String> items = Arrays.asList("Item1", "Item2");
    arrayAdapter.addAll(items);
    
    assertEquals(2, arrayAdapter.getCount());
    assertTrue(arrayAdapter.contains("Item1"));
    assertTrue(arrayAdapter.contains("Item2"));
    assertEquals(1, mockDataSetObserver.getCalledOnChangedCount());
}

@Test
public void testAddNullItem() {
    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>();
    MockDataSetObserver mockDataSetObserver = new MockDataSetObserver();
    arrayAdapter.registerDataSetObserver(mockDataSetObserver);

    arrayAdapter.add(null);
    
    assertEquals(1, arrayAdapter.getCount());
    assertNull(arrayAdapter.getItem(0));
    assertEquals(1, mockDataSetObserver.getCalledOnChangedCount());
}

@Test
public void testAddDuplicateItem() {
    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>();
    MockDataSetObserver mockDataSetObserver = new MockDataSetObserver();
    arrayAdapter.registerDataSetObserver(mockDataSetObserver);

    String item = "Item1";
    arrayAdapter.add(item);
    arrayAdapter.add(item);

    assertEquals(1, arrayAdapter.getCount());
    assertTrue(arrayAdapter.contains(item));
    assertEquals(1, mockDataSetObserver.getCalledOnChangedCount());
}

@Test
public void testAddAllWithEmptyList() {
    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>();
    MockDataSetObserver mockDataSetObserver = new MockDataSetObserver();
    arrayAdapter.registerDataSetObserver(mockDataSetObserver);

    arrayAdapter.addAll(Collections.emptyList());

    assertEquals(0, arrayAdapter.getCount());
    assertEquals(0, mockDataSetObserver.getCalledOnChangedCount());
}

@Test
public void testAddExceedingCapacity() {
    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(1); // Assuming max capacity is set to 1
    MockDataSetObserver mockDataSetObserver = new MockDataSetObserver();
    arrayAdapter.registerDataSetObserver(mockDataSetObserver);

    arrayAdapter.add("Item1");
    
    try {
        arrayAdapter.add("Item2");
    } catch (IllegalStateException e) {
        // Handle the exceeding capacity error
    }

    assertEquals(1, arrayAdapter.getCount());
    assertTrue(arrayAdapter.contains("Item1"));
    assertEquals(1, mockDataSetObserver.getCalledOnChangedCount());
}

// Verification of notifications and internal state after modifications
<<End of snippet n. 0>>