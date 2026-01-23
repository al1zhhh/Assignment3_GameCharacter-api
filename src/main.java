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
        System.out.println(" ==========================================");
        System.out.println("||  GAME CHARACTER MANAGEMENT API         ||");
        System.out.println("||  Console-based CRUD Application        ||");
        System.out.println(" ==========================================\n");

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
            System.out.println("\n========================================");
            System.out.println("||            MAIN MENU                   ||");
            System.out.println("||========================================||");
            System.out.println("-- CHARACTER OPERATIONS ------------------");
            System.out.println("| 1.  Create Character                   |");
            System.out.println("| 2.  View All Characters                |");
            System.out.println("| 3.  View Character by ID               |");
            System.out.println("| 4.  Update Character                   |");
            System.out.println("| 5.  Delete Character                   |");
            System.out.println("| 6.  Add Experience to Character        |");
            System.out.println("| 7.  Level Up Character                 |");
            System.out.println("| 8.  Filter Characters by Type          |");
            System.out.println("| 9.  Character Statistics               |");
            System.out.println("------------------------------------------");
            System.out.println("-- GUILD OPERATIONS ----------------------");
            System.out.println("| 10. Create Guild                       |");
            System.out.println("| 11. View All Guilds                    |");
            System.out.println("| 12. View Guild by ID                   |");
            System.out.println("| 13. Update Guild                       |");
            System.out.println("| 14. Delete Guild                       |");
            System.out.println("| 15. Add Character to Guild             |");
            System.out.println("| 16. Remove Character from Guild        |");
            System.out.println("| 17. Level Up Guild                     |");
            System.out.println("| 18. Guild Statistics                   |");
            System.out.println("------------------------------------------");
            System.out.println("-- DEMONSTRATIONS ------------------------");
            System.out.println("| 20. Simulate Combat (Interface Demo)   |");
            System.out.println("| 21. Polymorphism Demonstration         |");
            System.out.println("------------------------------------------");
            System.out.println("| 0.  Exit Application                   |");
            System.out.println("------------------------------------------");
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

    private static void pause() {
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
}
