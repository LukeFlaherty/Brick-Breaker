/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brickbreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author lukeflaherty
 */
public class Gameplay extends JPanel implements KeyListener, ActionListener{
//the panel class
    
    private boolean play = false;
    private int score = 0;
    
    private int totalBricks = 21;
    
    //for ball movement
    private Timer timer;
    private int delay = 8;
         
    //player
    private int playerX = 310;
            
    private int ballposX = 120;
    private int ballposY = 350;
    
    //direction of ball
    private int ballXdir = -1;
    private int ballYdir = -2;
    
    //create obj of MapGen class
    private MapGenerator map;
    
    //constructor
    public Gameplay(){
        
        //MapGen initialization
        map = new MapGenerator(3, 7);
        
        //add a key listener
        addKeyListener(this);
        
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        
        //add a timer & start it
        timer = new Timer(delay, this);
        timer.start();
    }
    
    
    public void paint(Graphics g){
        
        //background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);
        
        //drawing map by calling the draw method fro mthe MapGenerator class
        map.draw((Graphics2D)g);
        
        //borders 
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);
        
        //showing the scores
        g.setColor(Color.white);
        g.setFont( new Font("serif", Font.BOLD, 25));
        g.drawString("" + score, 590, 30);
        
        //paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);
        
        //the ball
        g.setColor(Color.yellow);
        g.fillOval(ballposX, ballposY, 20, 20);
        
        //if the game is over
        if(totalBricks <= 0){
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Won: ", 190, 300);
            
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 330);
        }
        
        //game over code
        if(ballposY > 570){
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Scores: ", 190, 300);
            
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 330);
        }
        
        g.dispose();
        
    }
    
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        
        if(play){
            
            //if ball intersects with the player
            if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))){
                ballYdir = -ballYdir;
            }
            
            //logic for collisions with bricks
            //first map is the field in this class
            //second map is the 2d array in MapGen
      A:    for(int i = 0; i < map.map.length; i++){
                for(int j = 0; j < map.map[0].length; j++){
                    if(map.map[i][j] > 0){
                        //detects position of ball and brick with respect to the width and height of that brick
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;
                        
                        //creats the hitbox or rectangle around the brick
                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        //rectangle around ball
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
                        //creates another rectangle for some reason
                        Rectangle brickRect = rect;
                        
                        //detects if it intersects or not
                        if(ballRect.intersects(brickRect)){
                            //call SetBrick function and set brick value to 0
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;
                            
                            //code for left and right intersection
                            if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width){
                                ballXdir = -ballXdir;
                            }
                            else{
                                ballYdir = -ballYdir;
                            }
                            //to take us out of the whole loop
                            break A;
                        }
                    }
                }
            }
            
            ballposX += ballXdir;
            ballposY += ballYdir;
            
            //ball collisions with the bordern
            if(ballposX < 0){
                 ballXdir = -ballXdir;
            }
            if(ballposY < 0){
                 ballYdir = -ballYdir;
            }
            if(ballposX > 670){
                 ballXdir = -ballXdir;
            }
        }
        
        //everytime something happens everything in paint is redrawn
        repaint();
    }
    
    
    
    
    @Override
    public void keyPressed(KeyEvent e) {
        
        //detect arrow keys
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            //check if it runs into the border
            if(playerX >= 600){
                playerX = 600;
            }
            else{
                moveRight();
            }
        }
    
    
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            //check if it runs into the border
            if(playerX < 10){
                playerX = 10;
            }
            else{
                moveLeft();
            }
        }
        
        //pressed enter to restart
        //set everything to default
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play){
                play = true;
                ballposX = 120;
                ballposY = 350;
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3, 7);
                
                repaint();
            }
        }
    }
    
    
    
    //move right method
    public void moveRight(){
        play = true;
        playerX += 20;
    }
    
    //move left method
    public void moveLeft(){
        play = true;
        playerX -= 20;
    }
    
    
    
    
    
    
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    

    @Override
    public void keyReleased(KeyEvent e) {
    }

    
    
    
    
}
