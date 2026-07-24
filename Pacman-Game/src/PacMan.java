import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class PacMan extends JPanel implements ActionListener, KeyListener {
  class Block {
    int startX, startY, x, y, width, height;
    Image image;
    char direction = 'U'; // U D L R
    int velocityX = 0;
    int velocityY = 0;

    Block(Image image, int x, int y, int width, int height) {
      this.image = image;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height= height;
      this.startX = x;
      this.startY = y;
    }
    
    /**
     * method to update pacman or ghosts direction of movement
     * @param direction direction to move
     */
    void updateDirection(char direction) {
      char prevDir = this.direction;
      this.direction = direction;
      updateVelocity();

      this.x += this.velocityX;
      this.y += this.velocityY;
      for (Block wall : walls) {
        if (collision(this, wall)) { // used for both pacman and ghosts
          this.x -= this.velocityX;
          this.y -= this.velocityY;
          this.direction = prevDir;
          updateVelocity();
        }
      }
    }

    /**
     * method to update velocity of object
     */
    void updateVelocity() {
      switch(this.direction) {
        case 'U' -> {
            this.velocityX = 0;
            this.velocityY = -tileSize / 4; // up 8 px
        }
        case 'D' -> {
            this.velocityX = 0;
            this.velocityY = tileSize / 4; // down 8 px
        }
        case 'L' -> {
            this.velocityX = -tileSize / 4;
            this.velocityY = 0; // left 8 px
        }
        case 'R' -> {
            this.velocityX = tileSize / 4;
            this.velocityY = 0; // right 8 px
        }
      }
    }

    /**
     * reset the x and y position of objects on death or new game
     */
    void reset() {
      this.x = this.startX;
      this.y = this.startY;
    }
  }
  
  private final int rowCount = 21;
  private final int colCount = 19;
  private final int tileSize = 32; // pixel size (px)
  private final int boardWidth = colCount * tileSize;
  private final int boardHeight = rowCount * tileSize;

  private final Image wallImg;
  private final Image blueGhostImg;
  private final Image orangeGhostImg;
  private final Image pinkGhostImg;
  private final Image redGhostImg;

  private final Image pacmanUpImg;
  private final Image pacmanDownImg;
  private final Image pacmanLeftImg;
  private final Image pacmanRightImg;

  // X = wall, O = skip, P = pac man, ' ' = food
  // Ghosts: b = blue, o = orange, p = pink, r = red
  private String[] tileMap = {
    "XXXXXXXXXXXXXXXXXXX",
    "X        X        X",
    "X XX XXX X XXX XX X",
    "X                 X",
    "X XX X XXXXX X XX X",
    "X    X       X    X",
    "XXXX XXXX XXXX XXXX",
    "OOOX X       X XOOO",
    "XXXX X XXrXX X XXXX",
    "O       bpo       O",
    "XXXX X XXXXX X XXXX",
    "OOOX X       X XOOO",
    "XXXX X XXXXX X XXXX",
    "X        X        X",
    "X XX XXX X XXX XX X",
    "X  X     P     X  X",
    "XX X X XXXXX X X XX",
    "X    X   X   X    X",
    "X XXXXXX X XXXXXX X",
    "X                 X",
    "XXXXXXXXXXXXXXXXXXX" 
  };

  // object representations in the game
  HashSet<Block> walls;
  HashSet<Block> foods;
  HashSet<Block> ghosts;
  Block pacman;

  Timer gameLoop;
  char[] directions = {'U', 'D', 'L', 'R'};
  Random random = new Random();

  int score = 0;
  int lives = 3;
  boolean gameOver = false;
  boolean paused = false;

  char nextDirection = ' '; // next direction to make when possible

  /**
   * PacMan game constructor
   */
  PacMan() {
    setPreferredSize(new Dimension(boardWidth, boardHeight));
    setBackground(Color.BLACK);
    addKeyListener(this);
    setFocusable(true);

    // now load images
    wallImg = new ImageIcon(getClass().getResource("./wall.png")).getImage();
    blueGhostImg = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
    orangeGhostImg = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
    pinkGhostImg = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
    redGhostImg = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();

    pacmanUpImg = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
    pacmanDownImg = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
    pacmanLeftImg = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
    pacmanRightImg = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();

    loadMap();
    for (Block ghost : ghosts) {
      char newDir = directions[random.nextInt(4)];
      ghost.updateDirection(newDir);
    }
    gameLoop = new Timer(50, this); // 50 milliseconds, 20 fps (1000/50)
    gameLoop.start();
  }

  /**
   * loadMap function to initially load the map with objects
   */
  public void loadMap() {
    walls = new HashSet<>();
    foods = new HashSet<>();
    ghosts = new HashSet<>();
    
    for (int r = 0; r < rowCount; r++) {
      for (int c = 0; c < colCount; c ++) {
        String row = tileMap[r];
        char tileMapChar = row.charAt(c);

        int x = c * tileSize; // x-pos is how many tiles from the left
        int y = r * tileSize; // y-pos is how many rows from the top

        switch (tileMapChar) {
          case 'X' -> { // block wall
              Block wall = new Block(wallImg, x, y, tileSize, tileSize);
              walls.add(wall);
              }
          case 'b' -> { // blue ghost
              Block ghost = new Block(blueGhostImg, x, y, tileSize, tileSize);
              ghosts.add(ghost);
              }
          case 'o' -> { // orange ghost
              Block orangeGhost = new Block(orangeGhostImg, x, y, tileSize, tileSize);
              ghosts.add(orangeGhost);
              }
          case 'p' -> { // pink ghost
              Block pinkGhost = new Block(pinkGhostImg, x, y, tileSize, tileSize);
              ghosts.add(pinkGhost);
              }
          case 'r' -> { // red ghost
              Block redGhost = new Block(redGhostImg, x, y, tileSize, tileSize);
              ghosts.add(redGhost);
              }
          case 'P' -> // pink ghost
            pacman = new Block(pacmanRightImg, x, y, tileSize, tileSize);
          case ' ' -> { // food
              Block food = new Block(null, x + 14, y + 14, 4, 4);
              foods.add(food);
              }
        }
      }
    }
  }

  /**
   * PaintComponent function to paint components
   * @param g the graphic to paint
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    draw(g);
  }

  /**
   * draw method to draw components
   * @param g the graphic to draw
   */
  public void draw(Graphics g) {
    g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

    for (Block ghost : ghosts) {
      g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
    }
 
    for (Block wall : walls) {
      g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
    }

    g.setColor(Color.WHITE);
    for (Block food : foods) {
      g.fillRect(food.x, food.y, food.width, food.height);
    }

    // score
    g.setFont(new Font("Arial", Font.PLAIN, 18));
    if (gameOver) {
      g.drawString("Game Over " + String.valueOf(score), tileSize / 2, tileSize / 2);
    } else {
      g.drawString("x" + String.valueOf(lives) + " Score: " + String.valueOf(score), tileSize / 2, tileSize / 2 );
    }
  }

  // Helper methods to queue next immediate move for pacman to make when possible
  /**
   * CanTurn method to check if pacman is lined up with empty space to turn
   * @param direction next direction to move
   * @return boolean value if direction change is valid
   */
  private boolean canTurn(char direction) {
    if (direction == 'U' || direction == 'D') {
      return pacman.x % tileSize == 0;
    }
    if (direction == 'L' || direction == 'R') {
      return pacman.y % tileSize == 0;
    }
    return false;
  }
  
  /**
   * CanMove method checks if the next step in a direction would hit a wall
   * @param block pacman object to check
   * @param direction next direction to move
   * @return boolean value if potential collision is found
   */
  private boolean canMove(Block block, char direction) {
    int nextX = block.x;
    int nextY = block.y;

    switch (direction) {
      case 'U' -> nextY -= tileSize / 4;
      case 'D' -> nextY += tileSize / 4;
      case 'L' -> nextX -= tileSize / 4;
      case 'R' -> nextX += tileSize / 4;
    }
    Block probe = new Block(null, nextX, nextY, block.width, block.height);
    for (Block wall : walls) {
      if (collision(probe, wall)) {
        return false;
      }
    }
    return true;
  }

  /**
   * applyNextTurn method to apply next direction change
   */
  private void applyNextTurn() {
    if (nextDirection == ' ') {
      return;
    }
    if (!canTurn(nextDirection)) {
      return;
    }
    if (!canMove(pacman, nextDirection)) {
      return;
    }
    pacman.direction = nextDirection;
    pacman.updateVelocity();
    switch (pacman.direction) {
        case 'U' -> pacman.image = pacmanUpImg;
        case 'D' -> pacman.image = pacmanDownImg;
        case 'L' -> pacman.image = pacmanLeftImg;
        case 'R' -> pacman.image = pacmanRightImg;
    }
    nextDirection = ' ';
  }

  /**
   * update object x and y positions
   */
  public void move() {
    // pacman teleport
    if (pacman.y == tileSize * 9 && pacman.x == 0 && pacman.direction == 'L') {
        pacman.x = boardWidth;
    } else if (pacman.y == tileSize * 9 && pacman.x == boardWidth && pacman.direction == 'R') {
        pacman.x = 0;
    }

    // update pacman direction from nextDirection
    applyNextTurn();
    
    pacman.x += pacman.velocityX;
    pacman.y += pacman.velocityY;

    // Check for pacman wall collision
    for (Block wall : walls) {
      if (collision (pacman, wall)) {
        pacman.x -= pacman.velocityX;
        pacman.y -= pacman.velocityY;
        break;
      }
    }
    
    // check for ghost collisions
    for (Block ghost : ghosts) {
      if (collision(ghost, pacman)) {
        lives -= 1;
        if (lives == 0) {
          gameOver = true;
          return;
        }
        resetPositions();
      }

      if (ghost.y == tileSize * 9 && ghost.direction != 'U' && ghost.direction != 'D') {
        ghost.updateDirection('U');
      }

      ghost.x += ghost.velocityX;
      ghost.y += ghost.velocityY;
      for (Block wall : walls) {
        if (collision (ghost, wall) || ghost.x <= 0 || ghost.x + ghost.width >= boardWidth) {
          ghost.x -= ghost.velocityX;
          ghost.y -= ghost.velocityY;
          char newDir = directions[random.nextInt(4)];
          ghost.updateDirection(newDir);
        }
      }
    } 

    // Check for food collisions
    Block foodEaten = null;
    for (Block food : foods) {
      if (collision(pacman, food)) {
        foodEaten = food;
        score += 10;

      }
    }
    foods.remove(foodEaten);

    // pacman has eaten all food pellets. level complete
    if (foods.isEmpty()) {
      loadMap(); // update to load another map layout
      resetPositions();
    }
  }

  /**
   * detect pacman collisions between ghosts, walls, food 
   * @param a pacman 
   * @param b other object
   * @return boolean if collision was detected
   */
  public boolean collision(Block a, Block b) {
    return a.x < b.x + b.width &&
           a.x + a.width > b.x &&
           a.y < b.y + b.height &&
           a.y + a.height > b.y; 
  }

  public void resetPositions() {
    pacman.reset();
    pacman.velocityX = 0;
    pacman.velocityY = 0;

    for (Block ghost : ghosts) {
      ghost.reset();
      char newDir = directions[random.nextInt(4)];
      ghost.updateDirection(newDir);
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    move();
    repaint();
    if (gameOver) {
      gameLoop.stop();
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {} 

  @Override
  public void keyPressed(KeyEvent e) {} // do not want to hold arrows to move

  @Override
  public void keyReleased(KeyEvent e) {
     if (gameOver) {
       loadMap();
       resetPositions();
       lives = 3;
       score = 0;
       gameOver = false;
       gameLoop.start();
     }

    switch (e.getKeyCode()) {
      case KeyEvent.VK_UP -> nextDirection = 'U';
      case KeyEvent.VK_DOWN -> nextDirection = 'D';
      case KeyEvent.VK_LEFT -> nextDirection = 'L';
      case KeyEvent.VK_RIGHT -> nextDirection = 'R';
      case KeyEvent.VK_P -> { // pause game
        if (!paused) {
          paused = true;
          gameLoop.stop();
        } else {
          paused = false;
          gameLoop.start();
        }
      }
    }
  }
}
