package snake;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Board extends JPanel implements ActionListener{

    int numDots;
    Image fruit;
    Image head;
    Image dot;
    int dotSize=10;
    int totDots=900;
    int x[]=new int[totDots];
    int y[]=new int[totDots];
    int fruit_x;
    int fruit_y;
    int random_pos=30;
    Timer timer;
    boolean leftDirection=false;
    boolean rightDirection=true;
    boolean upDirection=false;
    boolean downDirection=false;
    boolean startPage=true;
    boolean inGame=false;
    int score=0;

    public Board(){
        addKeyListener(new TAdapter());
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(300,300));
        setFocusable(true);
        loadImages();
        startGame();
    }

    public void loadImages(){
        ImageIcon i1=new ImageIcon(ClassLoader.getSystemResource("snake/icons/fruit.png"));
        fruit=i1.getImage();
        ImageIcon i2=new ImageIcon(ClassLoader.getSystemResource("snake/icons/dot.png"));
        dot=i2.getImage();
        ImageIcon i3=new ImageIcon(ClassLoader.getSystemResource("snake/icons/head.png"));
        head=i3.getImage();
    }

    public void startGame(){
        inGame=true;
        numDots=3;
        for(int i=0;i<numDots;i++){
            y[i]=50;
            x[i]=50-i*dotSize;
        }
        locateFruit();
        timer=new Timer(140,this);
        timer.start();
    }

    public void locateFruit(){
        int pos=(int)(Math.random()*random_pos);
        fruit_x=pos*dotSize;
        pos=(int)(Math.random()*random_pos);
        fruit_y=pos*dotSize;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(startPage)
            showStartPage(g);
        else 
            draw(g);
    }

    public void draw(Graphics g){
        if(inGame){
            g.setColor(Color.WHITE);
            g.setFont(new Font("SAN_SERIF",Font.BOLD,14));
            g.drawString("Score: " + score,5,15);
            g.drawImage(fruit,fruit_x,fruit_y,this);
            for(int i=0;i<numDots;i++){
                if(i==0)
                    g.drawImage(head,x[i],y[i],this);
                else
                    g.drawImage(dot,x[i],y[i],this);
            }
            Toolkit.getDefaultToolkit().sync();
        }else
            gameOver(g);
    }

    public void showStartPage(Graphics g){
        String msg = "WELCOME TO SNAKE GAME";
        String msg2 = "Press ENTER to Start";
        g.setColor(Color.WHITE);
        g.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        FontMetrics m = getFontMetrics(g.getFont());
        g.drawString(msg, (300 - m.stringWidth(msg)) / 2, 120);
        g.setFont(new Font("SAN_SERIF", Font.PLAIN, 14));
        FontMetrics m2 = getFontMetrics(g.getFont());
        g.drawString(msg2, (300 - m2.stringWidth(msg2)) / 2, 160);
    }

    public void gameOver(Graphics g){
        String msg="Game Over!";
        String scoreMsg="Your Score: "+score;
        String restartMsg="Press ENTER to Play Again";
        Font font=new Font("SAN_SERIF",Font.BOLD,14);
        FontMetrics metrices=getFontMetrics(font);
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(msg,(300-metrices.stringWidth(msg))/2,120);
        g.setFont(new Font("SAN_SERIF", Font.PLAIN, 14));
        g.drawString(scoreMsg,(300-metrices.stringWidth(scoreMsg))/2,150);
        g.drawString(restartMsg,(300-metrices.stringWidth(restartMsg))/2,180);
    }

    public void actionPerformed(ActionEvent e){
        if(inGame){
            checkFruit();
            checkCollision();
            move();
        }
        repaint();
    }

    public void checkFruit(){
        if(x[0]==fruit_x && y[0]==fruit_y){
            numDots++;
            score+=10;
            locateFruit();
        }
    }

    public void checkCollision(){
        for(int i=numDots-1;i>0;i--){
            if(i>3 && x[0]==x[i] && y[0]==y[i])
                inGame=false;
        }
        if(x[0]<0 || x[0]>=300 || y[0]<0 || y[0]>=300)
            inGame=false;
        if(!inGame)
            timer.stop();
    }

    public void move(){
        for(int i=numDots-1;i>0;i--){
            x[i]=x[i-1];
            y[i]=y[i-1];
        }
        if(leftDirection)
            x[0]-=dotSize;
        else if(rightDirection)
            x[0]+=dotSize;
        else if(upDirection)
            y[0]-=dotSize;
        else if(downDirection)
            y[0]+=dotSize;
    }

    public class TAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent k){
            int key=k.getKeyCode();
            if(key==KeyEvent.VK_ENTER) {
                if(startPage) {
                    startPage=false;
                    startGame();
                }else if(!inGame){
                    resetGame();
                    startGame();
                }
            }
            if(key==KeyEvent.VK_LEFT && !rightDirection){
                leftDirection=true;
                upDirection=false;
                downDirection=false;
            }else if(key==KeyEvent.VK_RIGHT && !leftDirection){
                rightDirection=true;
                upDirection=false;
                downDirection=false;
            }else if(key==KeyEvent.VK_UP &&!downDirection){
                upDirection=true;
                leftDirection=false;
                rightDirection=false;
            }else if(key==KeyEvent.VK_DOWN &&!upDirection){
                downDirection=true;
                leftDirection=false;
                rightDirection=false;
            }
        }
    }

    public void resetGame(){
        startPage=false;
        score=0;
        startGame();
    }
}
