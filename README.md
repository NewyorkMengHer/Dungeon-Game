# Project0 Dungeon
In Project0, a player enters the dungeon, and is given a series of moves to choose from as they engange in battle against many different monsters. 
The battle will continue till one runs out of hp and all information are stored in a database in MYSQL. Project0 implements all CRUD operations and parse data from 
a json file and persisted to MYSQL. User interaction will be through the Command Line Interface (CLI), and Entity Relationship Diagram(ERD) is included in the project.

## Technologies Used
- Java - version 11.0.10
- Scala - version 2.11.12
- MySQL Community Edition - version 8.0
- Git + GitHub

## Features
- Random number generator(RNG) attacks
- List of unique monsters (Skeleton, Minotaur, Slime, etc.)
- Chance of monsters with bonus stats
- Different abilities
- Chance of landing a crit attack
- Health potions
- Enticing turn base game

## Getting Started
- (Note, these instructions only support Windows 10 and above)
- First, download and install Git
  - Navigate to https://git-scm.com/download/win and install
- Run the following Git command to create a copy of the project repository using either Command Prompt or PowerShell:
  - git clone https://github.com/newyorkher/Project0
- Install Java
  - Navigate to https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html and install
- Install MySQL 8.0 Community Edition
  - Navigate to https://dev.mysql.com/downloads/windows/installer/8.0.html and install
- Install Database
- Once MySQL is installed, open up MySQL Workbench and connect as root

## Usage
- In the main menu, a prompt would present the following options and as the player continue in the dungeon, they will encounter a monster
- This particular monsters has a special stat for equiping a bow (+12 DMG)

![](Dungeon%20Images/1.PNG)

- In the turn base game, the player would always have the first move and the attacks dealt for both monsters and player are randomized between a set of numbers
- From the series of choices the player can choose one of the followings
- After defeating the monster, a prompt would be presented allowing the player to choose to leave or continue in the dungeon

![](Dungeon%20Images/2.PNG)

- If the player's ability is activated, it will have a 50% chance of landing a critical hit towards the enemy
- The monsters will get more difficults as the player continues into the dungeon 

![](Dungeon%20Images/3.PNG)
![](Dungeon%20Images/4.PNG)

- In this attempt to use the ability, the player missed and thus taking damage from the enemy

![](Dungeon%20Images/5.PNG)

- There are four potions given at the start of the dungeon. Player can choose to consume one in exchange for 100 health
- If player's hp is higher than 300. It would not consume a potion as the player would only need it if they are low in health

![](Dungeon%20Images/6.PNG)

- Here, the player took too much damage and had to leave the dungeon
- All monsters and health are stored in a SQL database that can be accessed and seen on MYSQL 

![](Dungeon%20Images/7.PNG)
 
