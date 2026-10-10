import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BowlingTest {
    @Test
    public void zeroRolls() {
        Bowling bowling = new Bowling();
        assertEquals(0, bowling.getScore());
        assertEquals(1, bowling.getCurrentFrame());
    }
    @Test
    public void oneRoll() {
        Bowling bowling = new Bowling();
        bowling.roll(3);
        assertEquals(3, bowling.getScore());
        assertEquals(1, bowling.getCurrentFrame());
    }
    @Test
    public void threeRolls() {
        Bowling bowling = new Bowling();
        bowling.roll(6);
        bowling.roll(3);
        bowling.roll(5);
        assertEquals(2, bowling.getCurrentFrame());
        assertEquals(9, bowling.getScore());
        assertEquals(9, bowling.getFrameScore(1));
        assertEquals(14, bowling.getFrameScore(2));
    }
    @Test
    public void spare() {
        Bowling bowling = new Bowling();
        bowling.roll(7);
        bowling.roll(3);
        bowling.roll(8);
        bowling.roll(1);
        assertEquals(18, bowling.getFrameScore(1));
        assertEquals(27, bowling.getFrameScore(2));
        assertEquals(27, bowling.getScore());
        assertEquals(3, bowling.getCurrentFrame());
    }
    @Test
    public void strike() {
        Bowling bowling = new Bowling();
        bowling.roll(10);
        bowling.roll(3);
        bowling.roll(6);
        assertEquals(19, bowling.getFrameScore(1));
        assertEquals(28, bowling.getScore());
        assertEquals(3, bowling.getCurrentFrame());
    }
    @Test
    public void allStrikesExceptLastRoll() {
        Bowling bowling = new Bowling();
        for (int i = 0; i < 11; i++)
            bowling.roll(10);
        bowling.roll(9);
        assertEquals(299, bowling.getScore());
    }
    @Test
    public void allStrikesSpareLastFrame() {
        Bowling bowling = new Bowling();
        for (int i = 0; i < 10; i++)
            bowling.roll(10);
        bowling.roll(9);
        bowling.roll(1);
        assertEquals(289, bowling.getScore());
    }
    @Test
    public void perfectGame() {
        Bowling bowling = new Bowling();
        for (int i = 0; i < 12; i++)
            bowling.roll(10);
        assertEquals(10, bowling.getCurrentFrame());
        assertEquals(270, bowling.getFrameScore(9));
        assertEquals(300, bowling.getFrameScore(10));
        assertEquals(300, bowling.getScore());
    }
    @Test
    public void worstGame() {
        Bowling bowling = new Bowling();
        for (int i = 0; i < 12; i++)
            bowling.roll(0);
        assertEquals(0, bowling.getScore());
    }
    @Test
    public void onlySpareGameWithFives() {
        Bowling bowling = new Bowling();
        for (int i = 0; i < 21; i++)
            bowling.roll(5);
        assertEquals(150, bowling.getScore());
    }
    @Test
    public void randomRollsGame() {
        Bowling bowling = new Bowling();
        bowling.roll(8);
        bowling.roll(2);
        bowling.roll(3);
        bowling.roll(5);
        bowling.roll(1);
        bowling.roll(8);
        bowling.roll(10);
        bowling.roll(5);
        bowling.roll(5);
        bowling.roll(7);
        bowling.roll(1);
        bowling.roll(6);
        bowling.roll(3);
        bowling.roll(8);
        bowling.roll(0);
        bowling.roll(3);
        bowling.roll(7);
        bowling.roll(10);
        bowling.roll(5);
        bowling.roll(3);
        assertEquals(130, bowling.getScore());
    }
}