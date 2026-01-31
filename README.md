# Game Character Management API

A console-based Java application for managing game characters using OOP principles, JDBC, and PostgreSQL database.

## Project Overview

This application allows users to create and manage different types of game characters (Warriors, Mages, Rogues), organize them into guilds, and track their progression. All data is stored in a PostgreSQL database using JDBC.

### Main Features
- Create, read, update, and delete characters
- Three character types with unique attributes
- Guild system for character organization  
- Experience and leveling system
- Combat simulation between characters
- Character statistics and rankings

## OOP Design

### Inheritance Hierarchy
- **GameEntity (Abstract Class)** - Base class for all characters
  - Abstract methods: `calculatePower()`, `levelUp()`, `getCharacterType()`
  - Concrete method: `displayInfo()`
  
- **Warrior** - Extends GameEntity
  - Attributes: strength, armor, weaponType
  
- **Mage** - Extends GameEntity  
  - Attributes: mana, intelligence, spellSchool
  
- **Rogue** - Extends GameEntity
  - Attributes: agility, stealth, criticalChance

### Interfaces
- **Combatant** - Defines combat abilities
  - Methods: `attack()`, `defend()`, `calculateDamage()`
  
- **Progressable** - Defines progression mechanics
  - Methods: `gainExperience()`, `canLevelUp()`

### Composition
- **Guild** has many **Characters** (one-to-many relationship)
- **Character** can have multiple **Equipment** items

### Polymorphism
Different character types implement the same methods differently:
```java
List<GameEntity> characters = getAllCharacters();
for (GameEntity character : characters) {
    character.displayInfo();        // Calls different versions
    int power = character.calculatePower();  // Different calculations
}
```

## Database Schema

### Tables

**guilds**
- `id` (PRIMARY KEY)
- `guild_name` (UNIQUE)
- `level`
- `member_count`
- `created_date`

**characters**
- `id` (PRIMARY KEY)
- `name`
- `character_type` (WARRIOR/MAGE/ROGUE)
- `level`
- `experience`
- `health_points`
- `guild_id` (FOREIGN KEY → guilds)
- `created_date`

**character_attributes**
- `character_id` (PRIMARY KEY, FOREIGN KEY → characters)
- Warrior fields: `strength`, `armor`, `weapon_type`
- Mage fields: `mana`, `intelligence`, `spell_school`
- Rogue fields: `agility`, `stealth`, `critical_chance`

**equipment**
- `id` (PRIMARY KEY)
- `name`
- `equipment_type`
- `bonus_stats`
- `rarity`
- `character_id` (FOREIGN KEY → characters)

## Architecture

The project uses a three-layer architecture:

```
Controller Layer (User Interface)
    ↓
Service Layer (Business Logic)
    ↓
Repository Layer (Database Access)
    ↓
Database (PostgreSQL)
```

### Layers Explained

**Controller** - Handles user input and displays output
- CharacterController.java
- GuildController.java

**Service** - Contains business logic and validation
- CharacterService.java
- GuildService.java

**Repository** - Database operations using JDBC
- CharacterRepository.java
- GuildRepository.java

## Exception Handling

Custom exception hierarchy:
- `InvalidInputException` - For validation errors
  - `DuplicateResourceException` - For duplicate entries
- `ResourceNotFoundException` - When entity doesn't exist
- `DatabaseOperationException` - For database errors


## How to Use

### Create a Character
1. Select option 1 from menu
2. Choose character type (1=Warrior, 2=Mage, 3=Rogue)
3. Enter name and level
4. Enter character-specific stats

### View Characters
Select option 2 to see all characters with their stats

### Level Up Character
1. Select option 6
2. Enter character ID
3. Enter XP amount
4. Character automatically levels up when reaching threshold

### Create Guild
1. Select option 10
2. Enter guild name

### Add Character to Guild
1. Select option 15
2. Enter character ID and guild ID

### Combat Simulation
1. Select option 20
2. Enter two character IDs
3. See battle results

## Example Output

```
========== CREATE CHARACTER ==========
Select character type:
1. Warrior
2. Mage
3. Rogue
Choice: 1

Enter name: Thorin
Enter level: 10

--- Warrior Stats ---
Strength: 50
Armor: 30
Weapon Type: Great Sword

✓ SUCCESS: Character created with ID: 1

=================================
Character: Thorin
Type: WARRIOR
Level: 10
Power: 180
=================================
Strength: 50
Armor: 30
Weapon: Great Sword
```

## CRUD Operations Demonstrated

**CREATE** - Adding new characters and guilds
**READ** - Viewing all characters, searching by ID, filtering by type
**UPDATE** - Modifying character stats, adding XP, changing names
**DELETE** - Removing characters (with validation for guild membership)

## Business Rules

- Character names must be 3-50 characters
- Level must be between 1-100
- All stats must be positive numbers
- Cannot delete a guild with active members
- Duplicate character names not allowed
- Guild names must be unique

## Key OOP Concepts Demonstrated

1. **Abstraction** - GameEntity abstract class
2. **Inheritance** - Warrior/Mage/Rogue extend GameEntity
3. **Polymorphism** - Same method calls, different behavior
4. **Encapsulation** - Private fields with getters/setters
5. **Interfaces** - Combatant and Progressable contracts
6. **Composition** - Guild contains Characters

## Technologies Used

- **Java 11+** - Programming language
- **PostgreSQL** - Relational database
- **JDBC** - Database connectivity
- **PreparedStatements** - SQL injection prevention
- **IntelliJ IDEA** - Development environment
- **pgAdmin4** - Database management

## Project Structure

```
game-character-api/
├── src/
│   ├── controller/
│   │   ├── CharacterController.java
│   │   └── GuildController.java
│   ├── service/
│   │   ├── CharacterService.java
│   │   └── GuildService.java
│   ├── repository/
│   │   ├── CharacterRepository.java
│   │   └── GuildRepository.java
│   ├── model/
│   │   ├── GameEntity.java
│   │   ├── Warrior.java
│   │   ├── Mage.java
│   │   ├── Rogue.java
│   │   ├── Guild.java
│   │   └── Equipment.java
│   ├── interfaces/
│   │   ├── Combatant.java
│   │   └── Progressable.java
│   ├── exception/
│   │   ├── InvalidInputException.java
│   │   ├── DuplicateResourceException.java
│   │   ├── ResourceNotFoundException.java
│   │   └── DatabaseOperationException.java
│   ├── utils/
│   │   └── DatabaseConnection.java
│   └── Main.java
├── resources/
│   └── schema_postgres.sql
├── lib/
│   └── postgresql-42.6.0.jar
└── README.md
```






## What I Learned

- How to design class hierarchies with abstract classes
- Implementing and using interfaces effectively
- Working with JDBC and PreparedStatements
- Multi-layer architecture benefits
- Database design with constraints and relationships
- Polymorphism in real applications


