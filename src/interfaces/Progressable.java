package interfaces;

/**
 * Progressable interface for entities that can gain experience and level up
 */

public class Progressable {
    /**
     * Gain experience points
     * @param Xp amount of experience to gain
     */
    void gainExperience(int xp);

    /**
     * Check if entity can level up
     * @return true if enough experience for next level
     */
    boolean canLevelUp();
}
