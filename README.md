# Snake-Battle-MP
Multiplayer Snake Battle is a fast-paced Java Swing game where two players compete head-to-head in a modern twist on the classic Snake. Featuring real-time movement, dynamic speed scaling, competitive biting mechanics, and smooth UI transitions; it’s a fun showcase of object-oriented and event-driven programming.

# 🐍 Snake Battle (Java Mini Project)

A competitive two-player **Snake Battle Game** built using **Java Swing and AWT**, designed to combine classic snake mechanics with real-time multiplayer interaction.  
This mini-project demonstrates Java GUI design, event-driven programming, and object-oriented principles through an engaging head-to-head gameplay experience.

---

## 🎯 Problem Statement
Traditional Snake games are single-player and focus only on survival by avoiding collisions.  
However, they lack competitive interaction and multiplayer engagement.  

The proposed project aims to design and implement a **two-player Snake Battle game** using **Java Swing**, where players compete in real time to eat food, grow their snakes, and outlast each other.  
The game features an interactive GUI, smooth animations, and seamless transitions between screens.

---

## 🎯 Objectives
1. Design a GUI-based multiplayer Snake game using **Java Swing and AWT**.  
2. Implement **event handling** for real-time keyboard inputs, button clicks, and timers.  
3. Demonstrate **object-oriented concepts** — encapsulation, inheritance, classes, objects, and packages.  
4. Provide a **user-friendly interface** with intro, gameplay, and end screens.  
5. Enhance understanding of **Java GUI programming**, **event-driven programming**, and **modular design** through this mini project.

---

## 🧩 Features

### 🏁 Intro Screen
- Player name input fields for both players.  
- Speed selection option (0.5× to 2×).  
  - Speed automatically increases by **0.1× every 10 seconds**, capped at **1.6×**.  
- **Start** button to begin the game.  
- **Quit** button to exit the application.

---

### 🎮 Game Screen
- Two snakes are controlled **simultaneously by two players** on the same grid.  
- Snakes can **wrap around the map boundaries**.  
  - **Player 1 Controls:** `W`, `A`, `S`, `D`  
  - **Player 2 Controls:** Arrow Keys (`↑`, `↓`, `←`, `→`)
- Randomly generated **food** appears on the board.  
  - Each food item increases the snake’s length and adds to the player’s score.  
- **Restart round:** Press `R`  
- **Return to main menu:** Press `Backspace`  

---

### ⚔️ Battle Mechanics
- Snakes can **collide with themselves**, resulting in instant death.  
- Once a player reaches **25 points**, they unlock the **biting ability**:
  - Each **bite**:
    - Reduces **5 points** from the biter’s score.  
    - **Shortens** the opponent’s snake.  
  - After **3 bites**, the opponent snake dies.  

- When one snake dies:
  - A **15-second timer** appears at the bottom-right corner.  
  - The surviving snake must reach a **green window** (spawned randomly at a corner) to win.  
  - If it fails within 15 seconds, the game **restarts automatically**.

---

### 🏆 End Screen
- Displays the **winner’s name**.  
- Offers options to **restart** the game or **quit**.
- A file is also created (Winners.txt) which contains the winner's names.  

---

## 🧠 Concepts Demonstrated
- **Object-Oriented Programming (OOP)**  
  - Classes and Objects  
  - Inheritance and Encapsulation  
- **Event-Driven Programming**  
  - KeyListener, ActionListener, and Timer Events  
- **Java Swing GUI Development**  
  - JFrame, JPanel, JButton, JLabel Components  
- **Multithreading Concepts** (for timers and game loop)
- **Collision Detection and Game Logic**

---

## 🖥️ Technologies Used
- **Java (JDK 17 or later)**
- **Java Swing / AWT**
- **Event Handling Frameworks**
- **Timer and Thread Utilities**

---

## 🧰 How to Run

1. **Clone the repository**
   ```bash
   git clone https://github.com/ROGHET/SnakeBattle.git
   cd SnakeBattle
2. **Compile and run**
```bash
javac MultiplayerSnakeBattle.java
java MultiplayerSnakeBattle


---

## 📂 Project Structure

SnakeBattle/
│
src/
    Main.java                → Entry point  
    GameFrame.java           → Main game window (JFrame)  
    GamePanel.java           → Handles rendering, logic, collisions  
    Snake.java               → Snake object class  
    Food.java                → Food generation logic  
    Player.java              → Player data (name, score, bites)  
    IntroScreen.java         → Menu and setup screen  
    EndScreen.java           → Game over screen  
    Utils.java               → Helper utilities 
    FileHandler.java         → Handles reading/writing scores to file

assets/                      → (optional) images, icons, sounds  
README.md  
LICENSE


---

## 👨‍💻 Authors
**Harshit Rawat
Neal Wadekar**

📧 **Email : harshitrawat3125@gmail.com
 | neal.wadekar@gmail.com**

💼 GitHub: github.com/ROGHET
 | github.com/neal-wadekar
 
---

✨ **Future Enhancements**
- Add sound effects and background music.
- Implement AI bot mode for single-player practice.
- Add leaderboard or local high-score saving.
- Include custom map skins or theme selector.
