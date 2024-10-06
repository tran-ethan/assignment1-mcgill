package assignment1.items;

public class SnoozeJuice extends Drink {
    private double temperature; // Celsius
    public static int HOT_COLD_BOOST = 2;

    public SnoozeJuice(double price, int happiness, int numBottles, double temperature) throws IllegalArgumentException {
        super(price, happiness, numBottles, false);
        this.temperature = temperature;
    }

    @Override
    public int getHappinessIndex() {
        if (temperature < 4 || temperature > 65) {
            return 0;
        } else {
            MyDate today = MyDate.today();
            if (temperature <= 10 &&  today.isSummer()) {
                return super.getHappinessIndex() + HOT_COLD_BOOST;
            } else if (temperature >= 55 && today.isWinter()) {
                return super.getHappinessIndex() + HOT_COLD_BOOST;
            } else {
                return super.getHappinessIndex();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SnoozeJuice snoozeJuice)) return false;
        if (!super.equals(o)) return false;
        return Math.abs(temperature - snoozeJuice.temperature) < 0.001;
    }

    @Override
    public Drink getPortion(int numOfBottles) {
        if (super.getNumOfBottles() >= numOfBottles) {
            super.numOfBottles -= numOfBottles;
            return new SnoozeJuice(super.getPrice(), super.getHappinessIndex(), numOfBottles, temperature);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "SnoozeJuice{Bottles = " + getNumOfBottles() + "}";
    }
}
