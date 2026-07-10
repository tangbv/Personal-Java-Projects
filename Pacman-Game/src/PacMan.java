import java.awt.*;
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
  private int tileSize = 32;
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
  }
}
