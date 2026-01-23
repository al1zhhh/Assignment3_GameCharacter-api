package interfaces;

/**
 * Progressable interface for entities that can gain experience and level up
 */

public interface Progressable {
    /**
     * Gain experience points
     * @param xp amount of experience to gain
     */
    void gainExperience(int xp);

    /**
     * Check if entity can level up
     * @return true if enough experience for next level
     */
    boolean canLevelUp();
}
