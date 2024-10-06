package assignment1;

import assignment1.items.StoreItem;

// Helper class to test StoreItem
public class StoreItemTest extends StoreItem {

    // Constructor to call the super constructor
    public StoreItemTest(double price, int happinessIndex) {
        super(price, happinessIndex);  // Call the abstract class's constructor
    }

    // No need to add any new functionality for testing the inherited methods
}
