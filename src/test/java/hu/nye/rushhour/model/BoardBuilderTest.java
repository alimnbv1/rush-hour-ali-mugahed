package hu.nye.rushhour.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class BoardBuilderTest {

    private static final Vehicle HERO_A = new Vehicle('A', new Position(0, 0), 2, Orientation.HORIZONTAL);
    private static final Vehicle HERO_B = new Vehicle('B', new Position(1, 2), 2, Orientation.HORIZONTAL);

    @Test
    void testBuildShouldCreateTheSameBoardAsTheConstructor() {
        Board result = Board.builder().rows(2).columns(4).vehicle(HERO_A).vehicle(HERO_B).build();
        assertEquals(new Board(2, 4, java.util.List.of(HERO_A, HERO_B)), result);
    }

    @Test
    void testBuildShouldThrowExceptionWhenHeroIsMissing() {
        assertThrows(IllegalArgumentException.class,
                () -> Board.builder().rows(2).columns(4).vehicle(HERO_A).build());
    }
}
