package assignment1.items;

public abstract class Drink extends StoreItem {
    public static int MAX_PACK_SIZE = 6;
    public static int BUZZY_HAPPINESS_BOOST = 1;
    protected int numOfBottles;
    private boolean isBuzzy;

    public Drink(double price, int happiness, int numOfBottles, boolean isBuzzy) throws IllegalArgumentException {
        super(price, happiness);
        this.numOfBottles = numOfBottles;
        this.isBuzzy = isBuzzy;
    }

    public int getNumOfBottles() {
        return numOfBottles;
    }

    public int getHappinessIndex() {
        return isBuzzy ? super.getHappinessIndex() + BUZZY_HAPPINESS_BOOST : super.getHappinessIndex();
    }

    public boolean combine(Drink drink) {
        // Cannot combine with a full pack or drink is empty
        if (this.numOfBottles == MAX_PACK_SIZE || drink.getNumOfBottles() == 0) {
            return false;
        }
        // Combine with the same drink if sizes are possible
        if (this.equals(drink)) {
            int totalBottles = this.numOfBottles + drink.numOfBottles;
            if (totalBottles <= MAX_PACK_SIZE) {
                this.numOfBottles = totalBottles;
                drink.numOfBottles = 0;
            } else {
                this.numOfBottles = MAX_PACK_SIZE;
                drink.numOfBottles = totalBottles - MAX_PACK_SIZE;
            }
            return true;
        }
        // Cannot combine with a different drink, return false since no change is made
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        if (!(o instanceof Drink drink)) return false;
        return isBuzzy == drink.isBuzzy;
    }

    public abstract Drink getPortion(int numOfBottles);
}
