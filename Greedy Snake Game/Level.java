public enum Level {
    LEVEL_0(0, 200),  // Level 0 - slowest speed (200ms per move)
    LEVEL_1(1, 150),  // Level 1
    LEVEL_2(2, 100),  // Level 2
    LEVEL_3(3, 70);   // Level 3 - fastest speed (70ms per move)

    // Instance variables
    private final int levelNumber;  // Level identifier (0-3)
    private final int speed;    // Speed in milliseconds (lower = faster)

    // Constructor (enum constructors are always private)
    Level(int levelNumber, int speed) {
        this.levelNumber = levelNumber;
        this.speed = speed;
    }

    public int getLevelNumber() {
        return levelNumber;
    }
    public int getSpeed() {
        return speed;
    }

    public static Level getLevelByNumber(int number) {
        for (Level level : values()) {
            if (level.levelNumber == number) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid level number: " + number);
    }

    @Override
    public String toString() {
        return "Level " + levelNumber + " (Speed: " + speed + "ms)";
    }
}
