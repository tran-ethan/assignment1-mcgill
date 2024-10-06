package assignment1;

import assignment1.items.*;

public class Store {
    private ItemList inventory;
    private double revenue;

    public Store(ItemList inventory) {
        this.inventory = inventory;
        revenue = 0;
    }

    public double getRevenue() {
        return revenue;
    }

    public ItemList getItems() {
        return inventory;
    }

    public void cleanUp() {
        for (StoreItem item : inventory.getAllItems()) {
            if (item instanceof Snack snack) {
                if (snack.isExpired()) {
                    inventory.removeItem(snack);
                }
            }
        }
    }

    public int completeSale(ItemList items) {
        // Reorganize the drinks in the inventory and remove empty bottles
        refillDrinkInventory(new Drink[0]);
        // Start count at 0 happiness
        int totalHappiness = 0;
        StoreItem[] requestedItems = items.getAllItems();

        // Loop over the items requested by the customer
        for (StoreItem requestedItem : requestedItems) {
            // For each item requested, find similar items in store inventory
            StoreItem[] inStock = inventory.findEqualItems(requestedItem);

            if (requestedItem instanceof Drink customerDrink) {
                // Handle drink purchase
                for (StoreItem storeItem : inStock) {
                    // Check instanceof to avoid null items
                    if (storeItem instanceof Drink storeDrink) {
                        int requestedBottles = customerDrink.getNumOfBottles();
                        Drink portion = storeDrink.getPortion(requestedBottles);
                        if (portion == null) {
                            // Handle case where not enough bottles are available
                            // Since we loop from the left, we know the first pack of bottles will be the one with most amount of bottles
                            totalHappiness += storeDrink.getHappinessIndex() * storeDrink.getNumOfBottles();
                            revenue += storeDrink.getPrice() * storeDrink.getNumOfBottles();
                            // Get the entire portion of the drink to remove it later
                            storeDrink.getPortion(storeDrink.getNumOfBottles());
                            // Reorganize inventory to remove empty drinks
                            refillDrinkInventory(new Drink[0]);
                        } else {
                            // Update happiness and revenue based on the portion sold
                            totalHappiness += portion.getHappinessIndex() * requestedBottles;
                            revenue += portion.getPrice() * requestedBottles;
                            // Reorganize inventory and remove empty bottles
                            refillDrinkInventory(new Drink[0]);
                            break;  // Stop after selling the requested drink
                        }
                    }
                }
            } else {
                // Non-drink items
                for (StoreItem storeItem : inStock) {
                    // Avoid null items
                    if (storeItem != null) {
                        totalHappiness += storeItem.getHappinessIndex();
                        revenue += storeItem.getPrice();
                        inventory.removeItem(storeItem);  // Remove the sold item from inventory
                        break;  // Stop after finding and selling one item
                    }
                }
            }
        }
        return totalHappiness;
    }

    public void refillDrinkInventory(Drink[] newDrinks) {
        // Squeeze drinks together as efficiently as possible, what will remain is multiple full packs of drinks,
        // and at most one pack with less than 6 bottles
        StoreItem[] storeItems = inventory.getAllItems();
        for (int i = 0; i < inventory.getSize() - 1; i++) {
            if (storeItems[i] instanceof Drink drink) {
                if (drink.getNumOfBottles() == Drink.MAX_PACK_SIZE) {
                    continue;
                }
                for (int j = i + 1; j < inventory.getSize(); j++) {
                    if (storeItems[j] instanceof Drink newDrink) {
                        if (drink.combine(newDrink)) {
                            if (drink.getNumOfBottles() == Drink.MAX_PACK_SIZE) {
                                break;
                            }
                        }
                    }
                }
            }
        }

        int size = inventory.getSize();
        // Remove all drinks from the inventory
        for (StoreItem item: inventory.getAllItems()) {
            if (item instanceof Drink) {
                inventory.removeItem(item);
            }
        }

        // Add back all drinks with bottles left
        for (int i = 0; i < size; i++) {
            if (storeItems[i] instanceof Drink drink) {
                // Note: only reason we do not use removeItem() here is because removeItem for Drink removes first instance of
                // same drink without comparing number of bottles, but here we want to remove only bottles with same number of drinks
                if (drink.getNumOfBottles() != 0) {
                    inventory.addItem(drink);
                }
            }
        }


        // Add new drinks to the inventory
        for (Drink newDrink : newDrinks) {
            boolean drinkAdded = false;
            for (StoreItem item: inventory.getAllItems()) {
                if (item instanceof Drink drink) {
                    if (drink.combine(newDrink) && newDrink.getNumOfBottles() == 0) {
                        // Becomes true if drink successfully combined and no leftover bottles to add
                        drinkAdded = true;
                        break;
                    }
                }
            }

            if (!drinkAdded) {
                inventory.addItem(newDrink);
            }
        }
    }
}
