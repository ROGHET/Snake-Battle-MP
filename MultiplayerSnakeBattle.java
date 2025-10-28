import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MultiplayerSnakeBattle extends JFrame {
    public MultiplayerSnakeBattle() {
        setTitle("Snake Battle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(900, 700);
        setLocationRelativeTo(null);

        CardLayout cards = new CardLayout();
        JPanel cardPanel = new JPanel(cards);

        IntroPanel intro = new IntroPanel(cards, cardPanel);
        GamePanel game = new GamePanel(cards, cardPanel, intro);
        EndPanel end = new EndPanel(cards, cardPanel, game);

        // Wire panels
        intro.setGamePanel(game);
        game.setEndPanel(end);

        cardPanel.add(intro, "INTRO");
        cardPanel.add(game, "GAME");
        cardPanel.add(end, "END");

        add(cardPanel);
        cards.show(cardPanel, "INTRO");
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MultiplayerSnakeBattle::new);
    }
}

/* ----- Intro screen ----- */
class IntroPanel extends JPanel {
    private CardLayout cards;
    private JPanel cardPanel;
    private JTextField p1NameField, p2NameField;
    private JComboBox<String> speedBox;
    private GamePanel gamePanel;

    IntroPanel(CardLayout cards, JPanel cardPanel) {
        this.cards = cards;
        this.cardPanel = cardPanel;
        setLayout(null);
        setBackground(new Color(8, 10, 20));

        // credit (bottom-left)
        JLabel credit = new JLabel("Java (OOP) Mini Project by Roll no.59 & 53");
        credit.setFont(new Font("Serif", Font.ITALIC, 16));
        credit.setForeground(new Color(170, 140, 255));
        credit.setBounds(10, 640, 400, 30);
        add(credit);

        // Animated title & byLabel
        JLabel byLabel = new JLabel("WELCOME TO");
        byLabel.setFont(new Font("Serif", Font.ITALIC, 20));
        byLabel.setForeground(new Color(170, 140, 255, 0));
        byLabel.setBounds(180, 70, 460, 30);
        add(byLabel);

        JLabel title = new JLabel("SNAKE BATTLE MP");
        title.setFont(new Font("Verdana", Font.BOLD, 48));
        title.setForeground(new Color(120, 200, 255, 0));
        title.setBounds(210, 90, 500, 60);
        add(title);

        // Speed rules (green section under title)
        JLabel speedRules = new JLabel("Choose 0.5x - 2.0x. Speed +0.1x every 10s, capped at 1.6x.");
        speedRules.setForeground(new Color(140, 255, 160));
        speedRules.setFont(new Font("SansSerif", Font.PLAIN, 14));
        speedRules.setBounds(450, 280, 600, 25);
        add(speedRules);

        // player fields
        JLabel p1Label = new JLabel("Player 1 Name (W A S D):");
        p1Label.setForeground(Color.WHITE);
        p1Label.setBounds(220, 200, 200, 25);
        add(p1Label);

        p1NameField = new JTextField("Player1");
        p1NameField.setBounds(420, 200, 200, 28);
        add(p1NameField);

        JLabel p2Label = new JLabel("Player 2 Name (Arrow keys):");
        p2Label.setForeground(Color.WHITE);
        p2Label.setBounds(220, 240, 200, 25);
        add(p2Label);

        p2NameField = new JTextField("Player2");
        p2NameField.setBounds(420, 240, 200, 28);
        add(p2NameField);

        // speed selector
        JLabel speedLabel = new JLabel("Select speed:");
        speedLabel.setForeground(Color.WHITE);
        speedLabel.setBounds(220, 280, 100, 25);
        add(speedLabel);

        speedBox = new JComboBox<>(new String[]{"0.5x","0.75x","1.0x","1.25x","1.5x","1.75x","2.0x"});
        speedBox.setSelectedItem("1.0x");
        speedBox.setBounds(320, 280, 120, 26);
        add(speedBox);

        // Start and Quit
        JButton startBtn = new JButton("Start Game");
        startBtn.setBounds(360, 320, 140, 40);
        add(startBtn);

        JButton quitBtn = new JButton("Quit");
        quitBtn.setBounds(520, 320, 100, 40);
        add(quitBtn);

        // Rules area - red section on right
        JTextArea rulesArea = new JTextArea(
            "#Rules:\n" +
            "1. Player 1 uses WASD, Player 2 uses Arrow Keys.\n" +
            "2. Food gives +5 points.\n" +
            "3. Biting only possible if score >= 25.\n" +
            "4. Each bite shortens the opponent. 3 bites = death.\n" +
            "5. Snakes wrap around the screen.\n" +
            "6. If one player dies, surviving player must reach green gate in 15s."
        );
        rulesArea.setEditable(false);
        rulesArea.setOpaque(false);
        rulesArea.setForeground(Color.WHITE);
        rulesArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        rulesArea.setBounds(80, 400, 430, 300);
	rulesArea.setLineWrap(true);
	rulesArea.setWrapStyleWord(true);

        add(rulesArea);

        // animation for title and byLabel
        javax.swing.Timer anim = new javax.swing.Timer(30, null);
        anim.addActionListener(new ActionListener() {
            float alpha = 0f;
            public void actionPerformed(ActionEvent e) {
                alpha += 0.02f;
                if (alpha >= 1f) {
                    alpha = 1f;
                    anim.stop();
                }
                int a = Math.min(255, (int)(alpha * 255));
                byLabel.setForeground(new Color(170, 140, 255, a));
                title.setForeground(new Color(120, 200, 255, a));
            }
        });
        anim.start();

        // start action
        startBtn.addActionListener(e -> {
            String n1 = p1NameField.getText().trim();
            String n2 = p2NameField.getText().trim();
            if (n1.isEmpty()) n1 = "Player1";
            if (n2.isEmpty()) n2 = "Player2";
            double sp = parseSpeed((String)speedBox.getSelectedItem());
            if (gamePanel != null) {
                gamePanel.setPlayerNames(n1, n2);
                gamePanel.setBaseSpeed(sp);
            }
            cards.show(cardPanel, "GAME");
            SwingUtilities.invokeLater(() -> {
                if (gamePanel != null) gamePanel.requestFocusInWindow();
                if (gamePanel != null) gamePanel.startMatch();
            });
        });

        quitBtn.addActionListener(e -> System.exit(0));
    }

    private double parseSpeed(String s) {
        try {
            return Double.parseDouble(s.replace("x",""));
        } catch (Exception ex) {
            return 1.0;
        }
    }

    void setGamePanel(GamePanel gp) { this.gamePanel = gp; }

    void getNames(String[] out) {
        out[0] = p1NameField.getText().trim();
        out[1] = p2NameField.getText().trim();
    }
}

/* ----- End screen ----- */
class EndPanel extends JPanel {
    private CardLayout cards;
    private JPanel cardPanel;
    private GamePanel gamePanel;

    EndPanel(CardLayout cards, JPanel cardPanel, GamePanel gamePanel) {
        this.cards = cards;
        this.cardPanel = cardPanel;
        this.gamePanel = gamePanel;
        setLayout(null);
        setBackground(new Color(6, 6, 12));
    }

    void showResult(String resultText) {
        removeAll();
        JLabel result = new JLabel(resultText, SwingConstants.CENTER);
        result.setFont(new Font("Verdana", Font.BOLD, 36));
        result.setForeground(new Color(255, 200, 80));
        result.setBounds(50, 80, 800, 60);
        add(result);

        JButton restart = new JButton("Restart Match");
        restart.setBounds(300, 220, 140, 40);
        add(restart);

        JButton quit = new JButton("Quit");
        quit.setBounds(460, 220, 100, 40);
        add(quit);

        restart.addActionListener(e -> {
            // send back to intro
            cards.show(cardPanel, "INTRO");
            // reset game
            gamePanel.resetEntireMatch();
        });

        quit.addActionListener(e -> {
            System.exit(0);
        });

        revalidate();
        repaint();
    }
}

/* ----- Main game panel ----- */
class GamePanel extends JPanel implements ActionListener {
    // Board
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final int UNIT = 20;
    private final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT * UNIT);
    private final int BASE_DELAY = 90; // base ms per tick for 1.0x speed

    // Player arrays
    private final int[] x1 = new int[GAME_UNITS];
    private final int[] y1 = new int[GAME_UNITS];
    private int len1, score1;
    private char dir1;
    private boolean alive1;
    private int bitesOn1; // how many times player1 has been bitten this round

    private final int[] x2 = new int[GAME_UNITS];
    private final int[] y2 = new int[GAME_UNITS];
    private int len2, score2;
    private char dir2;
    private boolean alive2;
    private int bitesOn2;

    // Food and door
    private int appleX, appleY;
    private final Color appleColor = Color.CYAN;
    private boolean doorVisible = false;
    private int doorX, doorY;
    private long doorSpawnTime = 0L;
    private final int DOOR_TIMEOUT = 15; // seconds to reach door

    // Rounds/match
    private int matchWins1 = 0;
    private int matchWins2 = 0;
    private java.util.List<String> roundHistory = new ArrayList<>();
    private boolean matchOver = false;

    // Player names
    private String player1Name = "Player1";
    private String player2Name = "Player2";

    // Timers
    private javax.swing.Timer timer; // main loop, uses delay scaled by speed
    private javax.swing.Timer doorTimer; // used to repaint door countdown regularly

    // Speed
    private double baseSpeed = 1.0;    // chosen on intro (0.5 - 2.0)
    private double currentSpeed = 1.0; // will ramp up but capped at 1.5
    private long roundStartMillis = 0L;

    // State
    private boolean running = false;
    private boolean roundActive = false;

    // UI helper
    private Font small = new Font("Monospaced", Font.PLAIN, 12);
    private Font big = new Font("Verdana", Font.BOLD, 22);

    private CardLayout cards;
    private JPanel cardPanel;
    private EndPanel endPanel;
    private IntroPanel introPanel;

    GamePanel(CardLayout cards, JPanel cardPanel, IntroPanel intro) {
        this.cards = cards;
        this.cardPanel = cardPanel;
        this.introPanel = intro;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        setLayout(null);

        addKeyListener(new MyKeyAdapter());

        // default timers (started when round begins)
        timer = new javax.swing.Timer(BASE_DELAY, this);
        doorTimer = new javax.swing.Timer(500, e -> repaint());
    }

    void setEndPanel(EndPanel ep) { this.endPanel = ep; }
    void setPlayerNames(String n1, String n2) {
        if (n1 != null && !n1.isEmpty()) player1Name = n1;
        if (n2 != null && !n2.isEmpty()) player2Name = n2;
    }
    void setBaseSpeed(double sp) {
        // keep chosen base but cap effective start at 1.5 as per requirement
        baseSpeed = sp;
    }

    void startMatch() {
        matchWins1 = 0;
        matchWins2 = 0;
        roundHistory.clear();
        matchOver = false;
        startNewRound();
    }

    void startNewRound() {
        // reset round variables
        running = true;
        roundActive = true;
        doorVisible = false;
        doorSpawnTime = 0L;
        bitesOn1 = 0; bitesOn2 = 0;

        // initialize players
        len1 = 3; len2 = 3;
        score1 = 0; score2 = 0;
        dir1 = 'R'; dir2 = 'L';
        alive1 = true; alive2 = true;

        // player 1 starts left, player 2 right
        int midY = HEIGHT / 2;
        x1[0] = 2 * UNIT; y1[0] = midY;
        x1[1] = UNIT; y1[1] = midY;
        x1[2] = 0; y1[2] = midY;

        x2[0] = WIDTH - 3 * UNIT; y2[0] = midY;
        x2[1] = WIDTH - 2 * UNIT; y2[1] = midY;
        x2[2] = WIDTH - UNIT; y2[2] = midY;

        // spawn first apple
        spawnApple();

        // speed init
        currentSpeed = Math.min(baseSpeed, 1.5); // cap start speed at 1.5
        roundStartMillis = System.currentTimeMillis();
        updateTimerDelay();

        timer.restart();
        doorTimer.restart();
        requestFocusInWindow();
        repaint();
    }

    void resetEntireMatch() {
        matchWins1 = 0; matchWins2 = 0;
        roundHistory.clear();
        matchOver = false;
        startNewRound();
    }

    void spawnApple() {
        Random r = new Random();
        appleX = r.nextInt(WIDTH / UNIT) * UNIT;
        appleY = r.nextInt(HEIGHT / UNIT) * UNIT;
        // avoid spawning on players
        if (isOccupiedBySnake(appleX, appleY)) spawnApple();
    }

    boolean isOccupiedBySnake(int xx, int yy) {
        for (int i = 0; i < len1; i++)
            if (x1[i] == xx && y1[i] == yy) return true;
        for (int i = 0; i < len2; i++)
            if (x2[i] == xx && y2[i] == yy) return true;
        return false;
    }

    void spawnDoorAtCorner() {
        java.util.List<Point> corners = Arrays.asList(
            new Point(0, 0),
            new Point(WIDTH - UNIT, 0),
            new Point(0, HEIGHT - UNIT),
            new Point(WIDTH - UNIT, HEIGHT - UNIT)
        );
        Collections.shuffle(corners);
        for (Point p : corners) {
            if (!isOccupiedBySnake(p.x, p.y)) {
                doorX = p.x; doorY = p.y;
                doorVisible = true;
                doorSpawnTime = System.currentTimeMillis();
                return;
            }
        }
        doorX = 0; doorY = 0; doorVisible = true;
        doorSpawnTime = System.currentTimeMillis();
    }

    /* game loop */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!running || matchOver) {
            timer.stop();
            return;
        }

        // speed scaling: every 10 seconds from round start, increase by +0.1 up to 1.6x
        long elapsedSec = (System.currentTimeMillis() - roundStartMillis) / 1000L;
        int increases = (int)(elapsedSec / 10);
        double targetSpeed = Math.min(1.5, Math.min(baseSpeed, 1.6) + 0.1 * increases);
        if (targetSpeed > currentSpeed) {
            currentSpeed = targetSpeed;
            updateTimerDelay();
        }

        if (roundActive) {
            moveSnakes();
            checkApple();
            checkBites();
            checkSelfCollision();
            checkWinConditions();
        }

        repaint();
    }

    private void updateTimerDelay() {
        int delay = (int)Math.max(20, BASE_DELAY / currentSpeed);
        // use swing Timer delay change
        if (timer != null) timer.setDelay(delay);
    }

    void moveSnakes() {
        // move player1
        for (int i = len1; i > 0; i--) {
            x1[i] = x1[i - 1];
            y1[i] = y1[i - 1];
        }
        switch (dir1) {
            case 'U': y1[0] -= UNIT; break;
            case 'D': y1[0] += UNIT; break;
            case 'L': x1[0] -= UNIT; break;
            case 'R': x1[0] += UNIT; break;
        }
        wrapHead(1);

        // move player2
        for (int i = len2; i > 0; i--) {
            x2[i] = x2[i - 1];
            y2[i] = y2[i - 1];
        }
        switch (dir2) {
            case 'U': y2[0] -= UNIT; break;
            case 'D': y2[0] += UNIT; break;
            case 'L': x2[0] -= UNIT; break;
            case 'R': x2[0] += UNIT; break;
        }
        wrapHead(2);
    }

    void wrapHead(int player) {
        if (player == 1) {
            if (x1[0] < 0) x1[0] = WIDTH - UNIT;
            if (x1[0] >= WIDTH) x1[0] = 0;
            if (y1[0] < 0) y1[0] = HEIGHT - UNIT;
            if (y1[0] >= HEIGHT) y1[0] = 0;
        } else {
            if (x2[0] < 0) x2[0] = WIDTH - UNIT;
            if (x2[0] >= WIDTH) x2[0] = 0;
            if (y2[0] < 0) y2[0] = HEIGHT - UNIT;
            if (y2[0] >= HEIGHT) y2[0] = 0;
        }
    }

    void checkApple() {
        if (x1[0] == appleX && y1[0] == appleY && alive1) {
            len1++;
            score1 += 5;
            spawnApple();
        }
        if (x2[0] == appleX && y2[0] == appleY && alive2) {
            len2++;
            score2 += 5;
            spawnApple();
        }
    }

    void checkBites() {
        if (alive1 && alive2 && score1 >= 25) {
            for (int i = 0; i < len2; i++) {
                if (x1[0] == x2[i] && y1[0] == y2[i]) {
                    doBite(1, 2);
                    break;
                }
            }
        }
        if (alive1 && alive2 && score2 >= 25) {
            for (int i = 0; i < len1; i++) {
                if (x2[0] == x1[i] && y2[0] == y1[i]) {
                    doBite(2, 1);
                    break;
                }
            }
        }
    }

    void doBite(int biter, int bitten) {
        if (biter == 1) score1 = Math.max(0, score1 - 10);
        else score2 = Math.max(0, score2 - 10);

        if (bitten == 1) {
            bitesOn1++;
            shortenSnake(1, 1 + (bitesOn1 - 1));
            flashOuch();
            if (bitesOn1 >= 3) alive1 = false;
        } else {
            bitesOn2++;
            shortenSnake(2, 1 + (bitesOn2 - 1));
            flashOuch();
            if (bitesOn2 >= 3) alive2 = false;
        }
    }

    void shortenSnake(int player, int amount) {
        if (player == 1) {
            len1 = Math.max(0, len1 - amount);
            if (len1 <= 0) alive1 = false;
        } else {
            len2 = Math.max(0, len2 - amount);
            if (len2 <= 0) alive2 = false;
        }
    }

    private transient long ouchUntil = 0L;
    void flashOuch() { ouchUntil = System.currentTimeMillis() + 700; }

    void checkSelfCollision() {
        for (int i = 1; i < len1; i++)
            if (alive1 && x1[0] == x1[i] && y1[0] == y1[i]) alive1 = false;
        for (int i = 1; i < len2; i++)
            if (alive2 && x2[0] == x2[i] && y2[0] == y2[i]) alive2 = false;
    }

    void checkWinConditions() {
        if (!alive1 && !alive2) {
            endRoundDraw();
            return;
        }

        if ((!alive1 && alive2) || (alive1 && !alive2)) {
            if (!doorVisible) {
                spawnDoorAtCorner();
            } else {
                long elapsed = (System.currentTimeMillis() - doorSpawnTime) / 1000L;
                if (elapsed >= DOOR_TIMEOUT) {
                    finalizeRoundAfterDoor(false);
                } else {
                    if (alive1 && x1[0] == doorX && y1[0] == doorY) finalizeRoundAfterDoor(true);
                    else if (alive2 && x2[0] == doorX && y2[0] == doorY) finalizeRoundAfterDoor(true);
                }
            }
        }
    }

    void endRoundDraw() {
        roundActive = false;
        doorVisible = false;
        roundHistory.add("Draw");
        timer.stop();
        doorTimer.stop();
        new javax.swing.Timer(900, e -> {
            ((javax.swing.Timer)e.getSource()).stop();
            startNewRound();
        }).start();
    }

    void finalizeRoundAfterDoor(boolean reached) {
        roundActive = false;
        doorVisible = false;
        timer.stop();
        doorTimer.stop();

        if (reached) {
            if (alive1 && !alive2) {
                matchWins1 += 1;
                roundHistory.add(player1Name + " Win");
            } else if (alive2 && !alive1) {
                matchWins2 += 1;
                roundHistory.add(player2Name + " Win");
            } else {
                roundHistory.add("No winner");
            }
        } else {
            if (alive1 && !alive2) roundHistory.add(player1Name + " (failed door)");
            else if (alive2 && !alive1) roundHistory.add(player2Name + " (failed door)");
            else roundHistory.add("No winner");
        }

        if (reached) {
            if (alive1 && !alive2) matchWins1++;
            else if (alive2 && !alive1) matchWins2++;
        }

        if (matchWins1 >= 2 || matchWins2 >= 2) {
            matchOver = true;
            String winner = matchWins1 > matchWins2 ? player1Name : player2Name;
            showMatchEnd(winner + " wins the match!");
        } else {
            new javax.swing.Timer(900, e -> {
                ((javax.swing.Timer)e.getSource()).stop();
                startNewRound();
            }).start();
        }
    }

    void showMatchEnd(String text) {
    cards.show(cardPanel, "END");
    if (endPanel != null) endPanel.showResult("| " + text + " |");
    saveWinnerToFile(text);
}

private void saveWinnerToFile(String winnerText) {
    try {
        java.io.File file = new java.io.File("Winners.txt");
        java.io.FileWriter writer = new java.io.FileWriter(file, true);
        writer.write(winnerText + System.lineSeparator());
        writer.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}


    /* painting */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // play area background
        g.setColor(new Color(12, 14, 20));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // draw apple
        g.setColor(appleColor);
        g.fillOval(appleX, appleY, UNIT, UNIT);

        // draw door if visible, and show bottom-right timer
        if (doorVisible) {
            long elapsed = (System.currentTimeMillis() - doorSpawnTime) / 1000L;
            int left = Math.max(0, DOOR_TIMEOUT - (int)elapsed);
            g.setColor(Color.GREEN);
            g.fillRect(doorX, doorY, UNIT, UNIT);
            g.setFont(small);
            g.setColor(Color.WHITE);
            g.drawString("Door: " + left + "s", WIDTH - 100, HEIGHT - 10); // bottom-right
        }

        // draw snakes
        for (int i = 0; i < len1; i++) {
            if (!alive1) break;
            if (i == 0) { g.setColor(Color.RED.darker()); g.fillRect(x1[i], y1[i], UNIT, UNIT); }
            else { g.setColor(Color.RED); g.fillRect(x1[i], y1[i], UNIT, UNIT); }
        }
        for (int i = 0; i < len2; i++) {
            if (!alive2) break;
            if (i == 0) { g.setColor(Color.ORANGE.darker()); g.fillRect(x2[i], y2[i], UNIT, UNIT); }
            else { g.setColor(Color.YELLOW); g.fillRect(x2[i], y2[i], UNIT, UNIT); }
        }

        // show UI: scores, names, round history, leader
        g.setFont(big);
        g.setColor(Color.WHITE);
        g.drawString(player1Name + ": " + score1, 10, 30);
        g.drawString(player2Name + ": " + score2, WIDTH - 220, 30);

        g.setFont(small);
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("Round History:", WIDTH - 170, 60);
        int y = 80;
        for (int i = Math.max(0, roundHistory.size() - 6); i < roundHistory.size(); i++) {
            g.drawString((i+1) + ". " + roundHistory.get(i), WIDTH - 170, y);
            y += 14;
        }

        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        String leader = (matchWins1 == matchWins2) ? "Leader: Tie" :
                 (matchWins1 > matchWins2 ? "Leader: " + player1Name : "Leader: " + player2Name);
        g.setColor(Color.CYAN);
        g.drawString(leader + "  (Wins " + matchWins1 + "-" + matchWins2 + ")", WIDTH/2 - 100, 30);

        // ouch message
        if (System.currentTimeMillis() < ouchUntil) {
            g.setFont(new Font("Arial", Font.BOLD, 28));
            g.setColor(Color.PINK);
            g.drawString("Ouch!", WIDTH/2 - 40, HEIGHT/2 - 20);
        }

        // instructions
        g.setFont(small);
        g.setColor(Color.GRAY);
        g.drawString("P1: W A S D  |  P2: Arrow keys  |  R to restart match  |  Backspace -> Main menu", 10, HEIGHT - 10);
    }

    /* key handling */
    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            // P1 controls (WASD)
            if ((key == KeyEvent.VK_W) && dir1 != 'D') dir1 = 'U';
            if ((key == KeyEvent.VK_S) && dir1 != 'U') dir1 = 'D';
            if ((key == KeyEvent.VK_A) && dir1 != 'R') dir1 = 'L';
            if ((key == KeyEvent.VK_D) && dir1 != 'L') dir1 = 'R';

            // P2 controls (arrows)
            if ((key == KeyEvent.VK_UP) && dir2 != 'D') dir2 = 'U';
            if ((key == KeyEvent.VK_DOWN) && dir2 != 'U') dir2 = 'D';
            if ((key == KeyEvent.VK_LEFT) && dir2 != 'R') dir2 = 'L';
            if ((key == KeyEvent.VK_RIGHT) && dir2 != 'L') dir2 = 'R';

            // restart whole match
            if (key == KeyEvent.VK_R) {
                resetEntireMatch();
            }

            // back to main menu
            if (key == KeyEvent.VK_BACK_SPACE) {
                // stop timers and show intro card
                timer.stop();
                doorTimer.stop();
                cards.show(cardPanel, "INTRO");
            }
        }
    }
}