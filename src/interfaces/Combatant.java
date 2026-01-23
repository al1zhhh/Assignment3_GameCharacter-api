package interfaces;

public class Combatant {
    /**
     * Perform an attack action
     * @return damage dealt
     */
    int attack();

    /**
     * Perform a defencive action
     * @return defense value
     */
    int defend();

    /**
     * Calculate total damage output
     * @return calculated damage
     */
    int calculateDamage();

}
