package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;


public class Player extends Entity {
    
    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    public int hasKey = 0;


    public Player(GamePanel gp, KeyHandler keyH) {

        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 15;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();
        getPlayerImage();
    }
    public void setDefaultValues() {

        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";
    }
    public void getPlayerImage() {

        up1 = setup("hero_up_1");
        up2 = setup("hero_up_2");
        up3 = setup("hero_up_3");
        down1 = setup("hero_down_1");
        down2 = setup("hero_down_2");
        down3 = setup("hero_down_3");
        left1 = setup("hero_left_1");
        left2 = setup("hero_left_2");
        left3 = setup("hero_left_3");
        right1 = setup("hero_right_1");
        right2 = setup("hero_right_2");
        right3 = setup("hero_right_3");
    }

    public BufferedImage setup(String imageName) {
        
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/player/" + imageName + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    public void update() {

        if(keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true) {
            if(keyH.upPressed == true) {
                direction = "up";
            }
            else if(keyH.downPressed == true) {
                direction = "down";
            }
            else if(keyH.leftPressed == true) {
                direction = "left";
                
            }
            else if(keyH.rightPressed == true) {
                direction = "right";
            }

            //CHECK TILE COLLISION
            collisionOn = false;
            gp.cChecker.checkTile(this);

            //CHECK OBJECT COLLISION
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            //IF COLLISION IS FALSE, PLAYER CAN MOVE
            if(collisionOn == false) {

                switch(direction) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }

            spriteCounter++;

            if(spriteCounter > 12) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                    
                }
                else if(spriteNum == 2) {
                    spriteNum = 3;
                }
                else if(spriteNum == 3) {
                    spriteNum = 4;
                }
                else if(spriteNum == 4) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }

        }

    }
    public void pickUpObject(int i) {

        if(i != 999) {

            String objectName = gp.obj[i].name;

            switch(objectName) {
                case "Key":
                    gp.playSE(1);
                    hasKey++;
                    gp.obj[i] = null;
                    gp.ui.showMessage("You got a key!");
                    break;
                case "Door":
                    if(hasKey > 0) {
                        gp.playSE(3);
                        gp.obj[i] = null;
                        hasKey--;
                        gp.ui.showMessage("Door unlocked!");
                    }
                    else {
                        gp.ui.showMessage("You need a key!");
                    }
                    break;
                case "Boots":
                    gp.ui.showMessage("Speed up!");
                    gp.playSE(2);
                    speed += 1;
                    gp.obj[i] = null;  
                    break;
                case "Chest":
                    gp.ui.gameFinished = true;
                    gp.stopMusic();
                    gp.playSE(4);
                    break;

            }
        }
    }

    public void draw(Graphics2D g2) {

        //g2.setColor(Color.white);

        //g2.fillRect(x, y, gp.tileSize, gp.tileSize);

        BufferedImage image = null;

        switch(direction) {
        case "up":
            if(spriteNum == 1) {
                image = up1;
            }
            if(spriteNum == 2 || spriteNum == 4) {
                image = up2;
            }
            if(spriteNum == 3) {
                image = up3;
            }
            break;
        case "down":
            if(spriteNum == 1) {
                image = down1;
            }
            if(spriteNum == 2 || spriteNum == 4) {
                image = down2;
            }
            if(spriteNum == 3) {
                image = down3;
            }
            break;
        case "left":
            if(spriteNum == 1) {
                image = left1;
            }
            if(spriteNum == 2 || spriteNum == 4) {
                image = left2;
            }
            if(spriteNum == 3) {
                image = left3;
            }
            break;
        case "right":
            if(spriteNum == 1) {
                image = right1;
            }
            if(spriteNum == 2 || spriteNum == 4) {
                image = right2;
            }
            if(spriteNum == 3) {
                image = right3;
            }
            break;
        }
        g2.drawImage(image, screenX, screenY, null);
    }
}
