import controller.CharacterController;
import controller.GuildController;
import model.*;
import service.CharacterService;
import service.GuildService;
import exceptions.*;
import utils.DatabaseConnection;

import java.util.Scanner;

/**
 * Main class - Console-based API demonstration
 * Uses Controller pattern to handle all operations
 */
public class main {
    private static CharacterController characterController;
    private static GuildController guildController;
    private static CharacterService characterService;
    private static GuildService guildService;
    private static Scanner scanner;

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║  GAME CHARACTER MANAGEMENT API         ║");
        System.out.println("║  Console-based CRUD Application        ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        // Initialize scanner
        scanner = new Scanner(System.in);

        // Test database connection
        if (!DatabaseConnection.testConnection()) {
            System.err.println("✗ Failed to connect to database.");
            System.err.println("Please check DatabaseConnection.java configuration:");
            System.err.println("  - URL: jdbc:mysql://localhost:3306/game_character_db");
            System.err.println("  - Username and password");
            System.err.println("  - MySQL server is running");
            return;
        }

        // Initialize controllers and services
        characterController = new CharacterController(scanner);
        guildController = new GuildController(scanner);
        characterService = new CharacterService();
        guildService = new GuildService();

        // Run automated demonstration first
        System.out.println("Would you like to see an automated demonstration? (yes/no): ");
        String demo = scanner.nextLine();
        if (demo.equalsIgnoreCase("yes")) {
            runAutomatedDemo();
        }

        // Show interactive menu
        showMainMenu();

        // Cleanup
        DatabaseConnection.closeConnection();
        scanner.close();
        System.out.println("\n✓ Application closed. Goodbye!");
    }

    /**
     * Main menu system
     */
    private static void showMainMenu() {
        while (true) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║           MAIN MENU                    ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("┌─ CHARACTER OPERATIONS ─────────────────┐");
            System.out.println("│ 1.  Create Character                   │");
            System.out.println("│ 2.  View All Characters                │");
            System.out.println("│ 3.  View Character by ID               │");
            System.out.println("│ 4.  Update Character                   │");
            System.out.println("│ 5.  Delete Character                   │");
            System.out.println("│ 6.  Add Experience to Character        │");
            System.out.println("│ 7.  Level Up Character                 │");
            System.out.println("│ 8.  Filter Characters by Type          │");
            System.out.println("│ 9.  Character Statistics               │");
            System.out.println("└────────────────────────────────────────┘");
            System.out.println("┌─ GUILD OPERATIONS ─────────────────────┐");
            System.out.println("│ 10. Create Guild                       │");
            System.out.println("│ 11. View All Guilds                    │");
            System.out.println("│ 12. View Guild by ID                   │");
            System.out.println("│ 13. Update Guild                       │");
            System.out.println("│ 14. Delete Guild                       │");
            System.out.println("│ 15. Add Character to Guild             │");
            System.out.println("│ 16. Remove Character from Guild        │");
            System.out.println("│ 17. Level Up Guild                     │");
            System.out.println("│ 18. Guild Statistics                   │");
            System.out.println("└────────────────────────────────────────┘");
            System.out.println("┌─ DEMONSTRATIONS ───────────────────────┐");
            System.out.println("│ 20. Simulate Combat (Interface Demo)  │");
            System.out.println("│ 21. Polymorphism Demonstration         │");
            System.out.println("│ 22. Run Full Automated Demo            │");
            System.out.println("└────────────────────────────────────────┘");
            System.out.println("│ 0.  Exit Application                   │");
            System.out.println("└────────────────────────────────────────┘");
            System.out.print("\nEnter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    // Character operations
                    case 1: characterController.createCharacter(); break;
                    case 2: characterController.getAllCharacters(); break;
                    case 3: characterController.getCharacterById(); break;
                    case 4: characterController.updateCharacter(); break;
                    case 5: characterController.deleteCharacter(); break;
                    case 6: characterController.addExperience(); break;
                    case 7: characterController.levelUpCharacter(); break;
                    case 8: characterController.getCharactersByType(); break;
                    case 9: characterController.showStatistics(); break;

                    // Guild operations
                    case 10: guildController.createGuild(); break;
                    case 11: guildController.getAllGuilds(); break;
                    case 12: guildController.getGuildById(); break;
                    case 13: guildController.updateGuild(); break;
                    case 14: guildController.deleteGuild(); break;
                    case 15: guildController.addCharacterToGuild(); break;
                    case 16: guildController.removeCharacterFromGuild(); break;
                    case 17: guildController.levelUpGuild(); break;
                    case 18: guildController.showGuildStatistics(); break;

                    // Demonstrations
                    case 20: characterController.simulateCombat(); break;
                    case 21: characterController.demonstratePolymorphism(); break;
                    case 22: runAutomatedDemo(); break;

                    case 0:
                        System.out.println("\nExiting application...");
                        return;

                    default:
                        System.out.println("✗ Invalid option! Please try again.");
                }

                // Pause before showing menu again
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();

            } catch (Exception e) {
                System.err.println("✗ Input error: " + e.getMessage());
                scanner.nextLine(); // Clear buffer
            }
        }
    }

    /**
     * Automated demonstration of all features
     */
    private static void runAutomatedDemo() {
        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║     AUTOMATED FEATURE DEMONSTRATION                ║");
        System.out.println("║  Showcasing OOP, JDBC, and Exception Handling      ║");
        System.out.println("╚════════════════════════════════════════════════════╝\n");

        try {
            // ==========================================
            // STEP 1: INHERITANCE & POLYMORPHISM
            // ==========================================
            System.out.println("┌─ STEP 1: Creating Characters (Inheritance) ───────┐");
            Warrior warrior = new Warrior("Thorin Ironshield", 10, 50, 30, "Great Sword");
            Mage mage = new Mage("Gandalf the Wise", 15, 200, 45, "Fire Magic");
            Rogue rogue = new Rogue("Shadow Blade", 8, 40, 35, 0.25);

            int warriorId = characterService.createCharacter(warrior);
            int mageId = characterService.createCharacter(mage);
            int rogueId = characterService.createCharacter(rogue);
            System.out.println("└────────────────────────────────────────────────────┘\n");

            pause();

            // ==========================================
            // STEP 2: POLYMORPHISM
            // ==========================================
            System.out.println("┌─ STEP 2: Polymorphism Demonstration ──────────────┐");
            System.out.println("│ Calling abstract methods on different subclasses  │");
            characterService.demonstratePolymorphism();
            System.out.println("└────────────────────────────────────────────────────┘\n");

            pause();

            // ==========================================
            // STEP 3: INTERFACE USAGE - Combatant
            // ==========================================
            System.out.println("┌─ STEP 3: Interface Demo (Combatant) ──────────────┐");
            System.out.println("│ Warrior vs Mage combat simulation                 │");
            characterService.demonstrateCombat(warriorId, mageId);
            System.out.println("└────────────────────────────────────────────────────┘\n");

            pause();

            // ==========================================
            // STEP 4: INTERFACE USAGE - Progressable
            // ==========================================
            System.out.println("┌─ STEP 4: Interface Demo (Progressable) ───────────┐");
            System.out.println("│ Adding experience and triggering level up         │");
            characterService.addExperience(warriorId, 500);
            characterService.addExperience(mageId, 1500);
            System.out.println("└────────────────────────────────────────────────────┘\n");

            pause();

            // ==========================================
            // STEP 5: UPDATE OPERATION
            // ==========================================
            System.out.println("┌─ STEP 5: UPDATE Operation (CRUD) ─────────────────┐");
            GameEntity updatedWarrior = characterService.getCharacterById(warriorId);
            updatedWarrior.setName("Thorin the Mighty");
            if (updatedWarrior instanceof Warrior) {
                ((Warrior) updatedWarrior).setStrength(55);
            }
            characterService.updateCharacter(warriorId, updatedWarrior);
            System.out.println("└────────────────────────────────────────────────────┘\n");

            pause();

            // ==========================================
            // STEP 6: COMPOSITION - Guild & Characters
            // ==========================================
            System.out.println("┌─ STEP 6: Composition (Guild + Characters) ────────┐");
            Guild guild1 = new Guild("Dragon Slayers");
            Guild guild2 = new Guild("Shadow Brotherhood");

            int guild1Id = guildService.createGuild(guild1);
            int guild2Id = guildService.createGuild(guild2);

            System.out.println("\n│ Adding characters to guilds...                    │");
            guildService.addCharacterToGuild(warriorId, guild1Id);
            guildService.addCharacterToGuild(mageId, guild1Id);
            guildService.addCharacterToGuild(rogueId, guild2Id);

            Guild updatedGuild = guildService.getGuildById(guild1Id);
            updatedGuild.displayInfo();
            System.out.println("└────────────────────────────────────────────────────┘\n");

            pause();

            // ==========================================
            // STEP 7: EXCEPTION HANDLING
            // ==========================================
            System.out.println("┌─ STEP 7: Exception Handling Demonstration ────────┐");

            // Test 1: DuplicateResourceException
            System.out.println("│ Test 1: Creating duplicate character              │");
            try {
                Warrior duplicate = new Warrior("Thorin the Mighty", 1, 20, 10, "Sword");
                characterService.createCharacter(duplicate);
            } catch (DuplicateResourceException e) {
                System.out.println("│ ✓ DuplicateResourceException: " + e.getMessage());
            }

            // Test 2: InvalidInputException
            System.out.println("│                                                    │");
            System.out.println("│ Test 2: Creating character with invalid name      │");
            try {
                Warrior invalid = new Warrior("", 1, 20, 10, "Sword");
                characterService.createCharacter(invalid);
            } catch (InvalidInputException e) {
                System.out.println("│ ✓ InvalidInputException: " + e.getMessage());
            }

            // Test 3: ResourceNotFoundException
            System.out.println("│                                                    │");
            System.out.println("│ Test 3: Getting non-existent character            │");
            try {
                characterService.getCharacterById(9999);
            } catch (ResourceNotFoundException e) {
                System.out.println("│ ✓ ResourceNotFoundException: " + e.getMessage());
            }

            // Test 4: DatabaseOperationException (Business Rule)
            System.out.println("│                                                    │");
            System.out.println("│ Test 4: Deleting guild with members               │");
            try {
                guildService.deleteGuild(guild1Id);
            } catch (DatabaseOperationException e) {
                System.out.println("│ ✓ DatabaseOperationException: Cannot delete guild");
                System.out.println("│   with active members (Business rule enforced)   │");
            }

            System.out.println("└────────────────────────────────────────────────────┘\n");

            pause();

            // ==========================================
            // STEP 8: READ OPERATIONS
            // ==========================================
            System.out.println("┌─ STEP 8: READ Operations (Get All) ───────────────┐");
            characterController.getAllCharacters();
            System.out.println("└────────────────────────────────────────────────────┘\n");

            pause();

            // ==========================================
            // STEP 9: DELETE OPERATION
            // ==========================================
            System.out.println("┌─ STEP 9: DELETE Operation ────────────────────────┐");
            System.out.println("│ Removing rogue from guild first...                │");
            guildService.removeCharacterFromGuild(rogueId);
            System.out.println("│                                                    │");
            System.out.println("│ Now deleting the rogue character...               │");
            characterService.deleteCharacter(rogueId);
            System.out.println("└────────────────────────────────────────────────────┘\n");

            // ==========================================
            // FINAL SUMMARY
            // ==========================================
            System.out.println("\n╔════════════════════════════════════════════════════╗");
            System.out.println("║          DEMONSTRATION COMPLETE!                   ║");
            System.out.println("╚════════════════════════════════════════════════════╝");
            System.out.println("Demonstrated:");
            System.out.println("  ✓ Abstract classes and inheritance (GameEntity → Warrior/Mage/Rogue)");
            System.out.println("  ✓ Polymorphism (calculatePower, levelUp methods)");
            System.out.println("  ✓ Interfaces (Combatant, Progressable)");
            System.out.println("  ✓ Composition (Guild contains Characters)");
            System.out.println("  ✓ Encapsulation (private fields, validation in setters)");
            System.out.println("  ✓ CRUD operations with JDBC PreparedStatements");
            System.out.println("  ✓ Custom exception hierarchy");
            System.out.println("  ✓ Business logic validation");
            System.out.println("  ✓ Multi-layer architecture (Controller → Service → Repository)");
            System.out.println();

        } catch (Exception e) {
            System.err.println("\n✗ Error during demonstration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Pause for user to read output
     */
    private static void pause() {
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
}
