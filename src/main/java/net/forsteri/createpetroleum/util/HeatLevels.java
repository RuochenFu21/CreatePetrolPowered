package net.forsteri.createpetroleum.util;

public enum HeatLevels {
    NONE(0),
    HOT(1),
    HEAT(2),
    SUPERHEAT(3);

    HeatLevels(int i) {
        this.level = i;
    }

    final int level;

    public int getLevel() {
        return level;
    }

    public static HeatLevels fromInt(int i) {
        return switch (i) {
            case 0 -> NONE;
            case 1 -> HOT;
            case 2 -> HEAT;
            case 3 -> SUPERHEAT;
            default -> NONE;
        };
    }
}
