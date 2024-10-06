package assignment1.items;

public class FizzWiz extends Drink {
    public FizzWiz(double price, int happiness, int numBottles) throws IllegalArgumentException {
        super(price, happiness, numBottles, true);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FizzWiz)) return false;
        return super.equals(o);
    }

    @Override
    public Drink getPortion(int numOfBottles) {
        if (super.getNumOfBottles() >= numOfBottles) {
            super.numOfBottles -= numOfBottles;
            return new FizzWiz(super.getPrice(), super.getHappinessIndex(), numOfBottles);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "FizzWiz{Bottles = " + getNumOfBottles() + "}";
    }
}
