# Game Character Management API - SOLID Architecture

A console-based Java application demonstrating SOLID principles, advanced OOP features, and clean architecture for managing game characters.



## Project Overview

This application manages game characters (Warriors, Mages, Rogues) with full CRUD operations, guild system, and character progression. Built using SOLID principles and advanced Java features including generics, lambdas, and reflection.


### Main Features
- ✅ Create and manage three character types
- ✅ Guild system with member management
- ✅ Experience and leveling system
- ✅ Combat simulation
- ✅ Character statistics and rankings
- ✅ Advanced sorting and filtering

---

## SOLID Principles

### 1. Single Responsibility Principle (SRP)

Each class has ONE clear responsibility:

**CharacterController**
- Responsibility: Handle user input/output only
- Does NOT: Contain validation or database logic

```java
public class CharacterController {
    private CharacterService characterService;
    
    public void createCharacter() {
        // Only handles user input
        String name = scanner.nextLine();
        int level = scanner.nextInt();
        
        // Delegates to service
        characterService.createCharacter(new Warrior(name, level, ...));
    }
}
```

**CharacterService**
- Responsibility: Business logic and validation only
- Does NOT: Handle database operations or user interaction

```java
public class CharacterService {
    private CrudRepository<GameEntity> characterRepository;
    
    public int createCharacter(GameEntity entity) {
        // Validation (SRP: business rules only)
        validateCharacter(entity);
        checkForDuplicates(entity);
        
        // Delegates to repository
        return characterRepository.create(entity);
    }
}
```

**CharacterRepository**
- Responsibility: Database operations only
- Does NOT: Contain validation or business logic

```java
public class CharacterRepository implements CrudRepository<GameEntity> {
    public int create(GameEntity entity) {
        // Only database operations
        PreparedStatement ps = conn.prepareStatement(sql);
        // ...
    }
}
```

---

### 2. Open-Closed Principle (OCP)

System is **open for extension, closed for modification**.

**Example: Adding new character types**

```java
// Abstract base - closed for modification
abstract class GameEntity {
    abstract int calculatePower();
}

// Open for extension - add new types without changing base
class Warrior extends GameEntity {
    int calculatePower() { return strength * 2 + armor; }
}

class Mage extends GameEntity {
    int calculatePower() { return intelligence * 3 + mana / 2; }
}

// NEW: Can add Paladin without modifying existing code
class Paladin extends GameEntity {
    int calculatePower() { return (strength + intelligence) * 2; }
}
```

No existing code needs to change when adding `Paladin`!

---

### 3. Liskov Substitution Principle (LSP)

Subclasses can replace parent class without breaking functionality.

```java
// Parent reference, child object
GameEntity warrior = new Warrior("Arthas", 10, 50, 30, "Sword");
GameEntity mage = new Mage("Gandalf", 15, 200, 45, "Fire");

// Works identically - LSP satisfied
warrior.displayInfo();  // Calls Warrior version
mage.displayInfo();     // Calls Mage version

warrior.calculatePower();  // Returns warrior calculation
mage.calculatePower();     // Returns mage calculation

// Can store in same collection
List<GameEntity> characters = Arrays.asList(warrior, mage);
for (GameEntity character : characters) {
    character.levelUp();  // Polymorphic behavior
}
```

All subclasses behave correctly when used through parent type.

---

### 4. Interface Segregation Principle (ISP)

Clients should not depend on interfaces they don't use.

**Good: Small, focused interfaces**

```java
// Small interface - only combat-related
interface Combatant {
    int attack();
    int defend();
}

// Small interface - only progression-related
interface Progressable {
    void gainExperience(int xp);
    boolean canLevelUp();
}

// Classes implement only what they need
class Warrior implements Combatant, Progressable {
    // Implements all methods because it needs them
}

// If we had non-combat NPCs:
class Merchant implements Progressable {
    // Only implements progression, not combat
    // ISP: Not forced to implement unused combat methods
}
```

**Bad example (fat interface):**
```java
// BAD: Forces all entities to implement everything
interface GameCharacter {
    void attack();
    void defend();
    void gainExperience();
    void trade();  // Not all characters trade!
    void craft();  // Not all characters craft!
}
```

---

### 5. Dependency Inversion Principle (DIP)

High-level modules depend on abstractions, not concrete classes.

**Service depends on Repository Interface (not concrete class)**

```java
public class CharacterService {
    // DIP: Depends on interface, not implementation
    private CrudRepository<GameEntity> repository;
    
    public CharacterService() {
        // Concrete class injected
        this.repository = new CharacterRepository();
    }
    
    public int createCharacter(GameEntity entity) {
        // Works with interface
        return repository.create(entity);
    }
}
```

**Benefits:**
- Easy to swap implementations (e.g., MockRepository for testing)
- Service doesn't know about database details
- Loose coupling between layers

---

## Advanced OOP Features

### 1. Generics

**Generic Repository Interface**

```java
public interface CrudRepository<T> {
    int create(T entity) throws DatabaseOperationException;
    List<T> getAll() throws DatabaseOperationException;
    T getById(int id) throws DatabaseOperationException, ResourceNotFoundException;
    void update(int id, T entity) throws DatabaseOperationException, ResourceNotFoundException;
    void delete(int id) throws DatabaseOperationException, ResourceNotFoundException;
}
```

**Usage:**
```java
// For characters
public class CharacterRepository implements CrudRepository<GameEntity> {
    // T = GameEntity
}

// For guilds
public class GuildRepository implements CrudRepository<Guild> {
    // T = Guild
}
```

**Benefits:**
- Type safety at compile time
- Code reuse across different entity types
- No need to cast objects

---

### 2. Lambda Expressions

**Sorting with Lambdas**

```java
public class SortingUtils {
    // Sort by name
    public static void sortByName(List<GameEntity> characters) {
        characters.sort((c1, c2) -> c1.getName().compareTo(c2.getName()));
    }
    
    // Sort by power (descending)
    public static void sortByPowerDesc(List<GameEntity> characters) {
        characters.sort((c1, c2) -> c2.calculatePower() - c1.calculatePower());
    }
    
    // Filter by minimum level
    public static List<GameEntity> filterByMinLevel(List<GameEntity> characters, int minLevel) {
        return characters.stream()
                .filter(c -> c.getLevel() >= minLevel)
                .toList();
    }
}
```

**Usage:**
```java
List<GameEntity> characters = characterService.getAllCharacters();

// Lambda sorting
SortingUtils.sortByName(characters);
characters.forEach(c -> System.out.println(c.getName()));

// Lambda filtering
List<GameEntity> highLevel = SortingUtils.filterByMinLevel(characters, 10);
```

---

### 3. Reflection (RTTI)

**ReflectionUtils class for runtime inspection**

```java
public class ReflectionUtils {
    public static void inspectObject(Object obj) {
        Class<?> clazz = obj.getClass();
        
        System.out.println("Class Name: " + clazz.getSimpleName());
        System.out.println("Package: " + clazz.getPackage().getName());
        
        // Get superclass
        if (clazz.getSuperclass() != null) {
            System.out.println("Superclass: " + clazz.getSuperclass().getSimpleName());
        }
        
        // Get interfaces
        for (Class<?> iface : clazz.getInterfaces()) {
            System.out.println("Interface: " + iface.getSimpleName());
        }
        
        // Get fields
        for (Field field : clazz.getDeclaredFields()) {
            System.out.println("Field: " + field.getType().getSimpleName() + " " + field.getName());
        }
        
        // Get methods
        for (Method method : clazz.getDeclaredMethods()) {
            System.out.println("Method: " + method.getName());
        }
    }
}
```

**Example output:**
```
========== REFLECTION INSPECTION ==========
Class Name: Warrior
Package: model
Superclass: GameEntity
Interfaces:
  - Combatant
  - Progressable
Fields:
  - int strength
  - int armor
  - String weaponType
Methods:
  - calculatePower
  - levelUp
  - attack
  - defend
==========================================
```

---

### 4. Interface Default and Static Methods

**Progressable Interface with default/static methods**

```java
public interface Progressable {
    // Abstract methods
    void gainExperience(int xp);
    boolean canLevelUp();
    
    // DEFAULT method - provides default implementation
    default int calculateRequiredXP(int level) {
        return level * 1000;  // Default formula
    }
    
    // STATIC method - utility
    static int getMaxLevel() {
        return 100;
    }
    
    static boolean isValidLevel(int level) {
        return level >= 1 && level <= getMaxLevel();
    }
}
```

**Usage:**
```java
// Static methods
int maxLevel = Progressable.getMaxLevel();  // 100
boolean valid = Progressable.isValidLevel(50);  // true

// Default method
Warrior warrior = new Warrior("Arthas", 10, 50, 30, "Sword");
int requiredXP = warrior.calculateRequiredXP(10);  // 10000
```

---

## Architecture

### Three-Layer Architecture

```
┌─────────────────────────────────┐
│  Controller Layer               │  User Interface
│  - CharacterController          │  - Handles I/O
│  - GuildController              │  - No business logic
└───────────┬─────────────────────┘
            │ delegates to
            ↓
┌─────────────────────────────────┐
│  Service Layer                  │  Business Logic
│  - CharacterService             │  - Validation
│  - GuildService                 │  - Business rules
│  - Uses repository interfaces   │  - Orchestration
└───────────┬─────────────────────┘
            │ uses
            ↓
┌─────────────────────────────────┐
│  Repository Layer               │  Data Access
│  - CharacterRepository          │  - CRUD operations
│  - GuildRepository              │  - SQL queries
│  - Implements CrudRepository<T> │  - JDBC
└───────────┬─────────────────────┘
            │
            ↓
┌─────────────────────────────────┐
│  Database (PostgreSQL)          │
│  - guilds                       │
│  - characters                   │
│  - character_attributes         │
│  - equipment                    │
└─────────────────────────────────┘
```

### Request Flow Example

**Creating a Character:**

```
1. User Input (Controller)
   └→ characterController.createCharacter()
       ├→ Collects: name, level, stats
       └→ Creates: new Warrior(...)

2. Validation (Service)
   └→ characterService.createCharacter(warrior)
       ├→ Validates: name length, level range
       ├→ Checks: duplicate names
       └→ Calls: repository.create()

3. Database (Repository)
   └→ characterRepository.create(warrior)
       ├→ INSERT INTO characters
       ├→ INSERT INTO character_attributes
       └→ Returns: generated ID

4. Response
   └→ Success message + character details
```

---

## Database Design

### Schema

**guilds table**
```sql
CREATE TABLE guilds (
    id SERIAL PRIMARY KEY,
    guild_name VARCHAR(100) NOT NULL UNIQUE,
    level INTEGER DEFAULT 1,
    member_count INTEGER DEFAULT 0,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**characters table**
```sql
CREATE TABLE characters (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    character_type VARCHAR(20) NOT NULL CHECK (character_type IN ('WARRIOR', 'MAGE', 'ROGUE')),
    level INTEGER DEFAULT 1 CHECK (level BETWEEN 1 AND 100),
    experience INTEGER DEFAULT 0,
    health_points INTEGER NOT NULL,
    guild_id INTEGER,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (guild_id) REFERENCES guilds(id) ON DELETE SET NULL
);
```

**character_attributes table** (Polymorphic storage)
```sql
CREATE TABLE character_attributes (
    character_id INTEGER PRIMARY KEY,
    -- Warrior
    strength INTEGER,
    armor INTEGER,
    weapon_type VARCHAR(50),
    -- Mage
    mana INTEGER,
    intelligence INTEGER,
    spell_school VARCHAR(50),
    -- Rogue
    agility INTEGER,
    stealth INTEGER,
    critical_chance NUMERIC(5,2),
    FOREIGN KEY (character_id) REFERENCES characters(id) ON DELETE CASCADE
);
```

### Entity Relationships

```
Guild (1) ────< (N) Character
         "has many"

Character (1) ────< (N) Equipment
           "owns"
```

---



---

## Usage Examples

### Create a Character

```
========== CREATE CHARACTER ==========
Select character type:
1. Warrior
2. Mage
3. Rogue
Choice: 1

Enter name: Arthas
Enter level: 10

--- Warrior Stats ---
Strength: 50
Armor: 30
Weapon Type: Great Sword

✓ SUCCESS: Character created with ID: 1
```

### Polymorphism Demo

```
========== POLYMORPHISM DEMONSTRATION ==========

Character: Arthas (WARRIOR)
Power: 180

Character: Gandalf (MAGE)
Power: 295

Character: Shadow (ROGUE)
Power: 156
```

### Reflection Output

```
========== REFLECTION INSPECTION ==========
Class Name: Warrior
Superclass: GameEntity
Interfaces:
  - Combatant
  - Progressable
Fields:
  - int strength
  - int armor
  - String weaponType
==========================================
```

### Lambda Sorting

```
Sorted by Name:
  Arthas
  Gandalf
  Shadow

Sorted by Power:
  Gandalf - Power: 295
  Arthas - Power: 180
  Shadow - Power: 156
```

---

## Key Features

### CRUD Operations
- ✅ Create characters with validation
- ✅ Read all or by ID
- ✅ Update character stats
- ✅ Delete with foreign key checks

### Character Types
- ⚔️ **Warrior** - Strength and armor based
- 🔮 **Mage** - Intelligence and mana based
- 🗡️ **Rogue** - Agility and stealth based

### Guild System
- Create and manage guilds
- Add/remove members
- Track member count automatically

### Advanced Features
- Generic repository pattern
- Lambda sorting and filtering
- Reflection inspection
- Interface default/static methods

---

## Project Structure

```
src/
├── controller/
│   ├── CharacterController.java
│   └── GuildController.java
├── service/
│   ├── CharacterService.java
│   └── GuildService.java
├── repository/
│   ├── interfaces/
│   │   └── CrudRepository.java       ← Generic interface
│   ├── CharacterRepository.java
│   └── GuildRepository.java
├── model/
│   ├── GameEntity.java (abstract)
│   ├── Warrior.java
│   ├── Mage.java
│   ├── Rogue.java
│   ├── Guild.java
│   └── Equipment.java
├── interfaces/
│   ├── Combatant.java
│   └── Progressable.java             ← With default/static methods
├── exception/
│   ├── InvalidInputException.java
│   ├── DuplicateResourceException.java
│   ├── ResourceNotFoundException.java
│   └── DatabaseOperationException.java
├── utils/
│   ├── DatabaseConnection.java
│   ├── ReflectionUtils.java          ← RTTI
│   └── SortingUtils.java             ← Lambdas
└── Main.java
```

---

## Exception Handling

### Exception Hierarchy

```
Exception
    ├── InvalidInputException
    │       └── DuplicateResourceException
    ├── ResourceNotFoundException
    └── DatabaseOperationException
```

### Usage Examples

```java
// Validation error
try {
    Warrior invalid = new Warrior("", 1, 10, 5, "Sword");
    characterService.createCharacter(invalid);
} catch (InvalidInputException e) {
    System.out.println("✗ " + e.getMessage());
    // Output: Character name cannot be empty
}

// Duplicate
try {
    characterService.createCharacter(existingWarrior);
} catch (DuplicateResourceException e) {
    System.out.println("✗ " + e.getMessage());
    // Output: Character 'Arthas' already exists
}

// Not found
try {
    characterService.getCharacterById(9999);
} catch (ResourceNotFoundException e) {
    System.out.println("✗ " + e.getMessage());
    // Output: Character with ID 9999 not found
}
```

---

## What I Learned

### SOLID Principles
- How to apply SRP to create focused, maintainable classes
- Using OCP to make code extensible without modification
- LSP ensures subclasses work correctly as parent types
- ISP creates small, focused interfaces
- DIP reduces coupling between layers

### Advanced Java Features
- **Generics** provide type safety and code reuse
- **Lambdas** make code more concise and functional
- **Reflection** enables runtime type inspection
- **Interface evolution** with default/static methods

### Architecture Benefits
- Layered architecture separates concerns clearly
- Each layer has a single, well-defined purpose
- Easy to test, modify, and extend
- Changes in one layer don't affect others

### Challenges Faced
- Understanding when to use generics vs inheritance
- Balancing between interfaces and abstract classes
- Managing database transactions with JDBC
- Implementing polymorphic database storage
