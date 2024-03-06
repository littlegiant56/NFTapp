package nftspy.data;

public class Price {
    private final float value;
    private final DateTime time;

    public Price(float value, DateTime time) {
        this.value = value;
        this.time = time;
    }

    public float getValue() {
        return value;
    }

    public DateTime getTime() {
        return time;
    }
}
