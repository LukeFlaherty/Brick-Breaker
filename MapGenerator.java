/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brickbreaker;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author lukeflaherty
 */
public class MapGenerator {
    
    //2d array that contains all the bricks
    public int map[][];
    
    public int brickWidth;
    public int brickHeight;
    
    //constructor with the number of rows and columns
    public MapGenerator(int row, int col){
        map = new int[row][col];
        
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                //will make sure the ball hasnt contacted with the brick so it is shown
                //the number 1 stands for a brick that has not been contacted with
                map[i][j] = 1;
                
            }
        }
        brickWidth =  540 / col;
        brickHeight = 150 / row;
    }
    
    
    
    
    
    
    
    //will draw the bricks that have a value of 1
    public void draw(Graphics2D g){
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                if(map[i][j] > 0){
                    //creates a brick here
                    g.setColor(Color.white);
                    g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                    
                    //drawing the border between bricks
                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.black);
                    g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                    
                    
                }
            }
        }
    }
    
    
    
    
    public void setBrickValue(int value, int row, int col){
        map[row][col] = value;
    }
    
}
