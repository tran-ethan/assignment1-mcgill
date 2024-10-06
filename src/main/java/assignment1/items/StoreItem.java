package assignment1.items;

public abstract class StoreItem {
    private double price;
    private int happiness;

    public StoreItem(double price, int happiness) throws IllegalArgumentException {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        if (happiness < 0) {
            throw new IllegalArgumentException("Happiness cannot be negative.");
        }
        this.price = price;
        this.happiness = happiness;
    }

    public final double getPrice() {
        return price;
    }

    public int getHappinessIndex() {
        return happiness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoreItem storeItem)) return false;
        return (Math.abs(getPrice() - storeItem.getPrice()) < 0.001) && (getHappinessIndex() == storeItem.getHappinessIndex());
    }
}
