package hu.nye.rushhour.model;

/**
 * The two players. Each one controls a dedicated hero car.
 */
public enum PlayerType {
    HUMAN(Vehicle.HUMAN_HERO_ID),
    AI(Vehicle.AI_HERO_ID);

    private final char heroId;

    PlayerType(char heroId) {
        this.heroId = heroId;
    }

    public char getHeroId() {
        return heroId;
    }

    /**
     * Returns the other player.
     */
    public PlayerType opponent() {
        return this == HUMAN ? AI : HUMAN;
    }
}
