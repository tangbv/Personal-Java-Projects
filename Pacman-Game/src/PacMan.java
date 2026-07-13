import java.awt.*;
import java.util.HashSet;
import javax.swing.*;

public class PacMan extends JPanel {
  class Block {
    int startX, startY, x, y, width, height;
    Image image;

    Block(Image image, int x, int y, int width, int height) {
      this.image = image;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height= height;
      this.startX = x;
      this.startY = y;
    }
  }
  
  private int rowCount = 21;
  private int colCount = 19;
  private int tileSize = 32; // pixel size (px)
  private int boardWidth = colCount * tileSize;
  private int boardHeight = rowCount * tileSize;

  private Image wallImg;
  private Image blueGhostImg;
  private Image orangeGhostImg;
  private Image pinkGhostImg;
  private Image redGhostImg;

  private Image pacmanUpImg;
  private Image pacmanDownImg;
  private Image pacmanLeftImg;
  private Image pacmanRightImg;

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

  /**
   * PacMan game constructor
   */
  PacMan() {
    setPreferredSize(new Dimension(boardWidth, boardHeight));
    setBackground(Color.BLACK);

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
  }

  /**
   * loadMap function to load the map with objects
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
  }
}
