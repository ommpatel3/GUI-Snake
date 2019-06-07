/**
 * Om Patel 
 * June 7, 2019
 * GUI Game Project - Criterion C
 * 
 * this class runs the actual game, and contains the graphics needed to do so
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import javax.sound.sampled.AudioSystem;//this and all below libraries are to make music work
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import java.net.URL;
//learned keylistener from https://examples.javacodegeeks.com/desktop-java/awt/event/a-complete-keylistener-example/
public class game extends JPanel implements ActionListener,KeyListener{
    //making clip variable for music variable - used for sound effects when game is lost or food is eaten
    Clip music;

    //retrieving difficulty variable from the getdifficulty method in the menu class
    char difficulty = menu.getdifficulty();
    //retrieving image variable from the getimage method in the menu class
    String image = menu.getimage();

    //below arrays store the positin of the snake segment locations
    //660 is used since there are 660 potential places the segments can go, since segments are 20x20 and a 600x440 b oard exists
    int[] snakex=new int[660];//stores x coordinate
    int[] snakey=new int[660];//stores y coordinate

    //declaring variables for food coordinates
    //first food to pop up in every game will be at 560,220 so that it pops up right infront of the snake
    int foodx=560;
    int foody=320;

    //tracks number of segments or length of snake, will be used to make images and for repeated actions in for loops
    int segments;
    //score starts off at 0
    int score=0;

    //4 booleans declared to represent which direction snake is moving
    boolean right=false,left=false,up=false,down=false;

    //declaring global timer, which is started in the gamestart method
    //learned timer from https://docs.oracle.com/javase/7/docs/api/java/util/Timer.html
    Timer timer;

    //making imageicon for the snake's head since it can have different images depending on which way the snake is travelling
    ImageIcon snakehead;

    //this variable tracks the amount of moves made. its purpose is not the track how many moves the player makes,
    //but rather to check when no moves have been made, in order to orient the snake with the correct head at its starting position
    int moves=0;

    //choosing a random number corresponding to the food icon, since there are different planet images representing the food
    //* all 7 of the planet images were made by me in ms paint
    //there are planet images of the earth, jupiter, saturn, the moon, mars, uranus and the sun
    int foodimage=(int)(Math.random() * (7) + 1);

    public static void main (String[] args){
        game content = new game();
        JFrame window = new JFrame("Space Snake");
        window.setContentPane(content);
        window.setSize(1085,620);
        window.setLocation( 50,0);
        window.setVisible(true);
        window.setResizable(false);//fixes window size as the dimensions listed above so it cannot be changed
    }

    public game(){
        //lines 72,73 and 75 are all needed to set up and make keylistener functional
        addKeyListener(this);
        setFocusable(true);
        //takes the focus off traversal keys such as tab and shift+tab-needed to make keylistener function as intended
        setFocusTraversalKeysEnabled(false);
        //gamestart method orients snake in the initial position and serves other functions like start timer
        gamestart();
    }

    public void gamestart(){
        segments=3;//all games will start off with 3 segments - 2 body and one head
        for (int i=0;i<segments;i++){//repeats as many times as there are segments
            //making the snake start in a horizontal line with a y coordinate of 220 and the head at x=60
            snakey[i]=320;//x stays the same for all, at 220
            snakex[i]=260-(20*i);//y is 260 for first segment(head), 240 for second and 220 for third
        }

        //timer goes off every 100ms if on easy mode, 85 if on hard mode
        int delay=100;
        if(difficulty=='h')
        {
            delay=85;
        }

        //making and starting the timer from the global variable
        timer=new Timer (delay,this);
        timer.start();
    }

    public void paint(Graphics g){//sets up background/snake assets
        //paint class is called automatically when program is run and can also be called by repaint method (which occurs every "tick")
        //learned graphics g from https://docs.oracle.com/javase/7/docs/api/java/awt/Graphics.html

        //making background
        ImageIcon background=new ImageIcon(image);
        background.paintIcon(this,g,0,0);

        //making game box
        g.setColor(Color.white);
        g.drawRect(220,120,600,439);//drawRect makes a rectangular outline of specified dimensions, at specified coordinates

        //making title
        g.setFont(new Font("Bauhaus 93",Font.BOLD,72));
        g.drawString("Space Snake",299,75);

        //making text to show score
        g.setFont(new Font("Bauhaus 93",Font.BOLD,24));
        g.drawString("Score: "+score,800,75);

        //making imageicons for snake head
        if(right||moves==0)//if snake is moving right, or no moves have been made
            snakehead=new ImageIcon("spaceshipright.png");

        if(up)//if snake is moving up
            snakehead=new ImageIcon("spaceshipup.png");

        if(down)//...etc
            snakehead=new ImageIcon("spaceshipdown.png");

        if(left)
            snakehead=new ImageIcon("spaceshipleft.png");
        //painting snake's head at the 0th indices of the x and y coordinate arrays (first position in x and y array)
        snakehead.paintIcon(this,g,snakex[0],snakey[0]);

        //making imageicon for snake body
        ImageIcon snakebody=new ImageIcon("ufo.png");
        //painting it on all of the other indices in two arrays        
        for (int i=1;i<segments;i++)
        {
            snakebody.paintIcon(this,g,snakex[i],snakey[i]);
        }

        //painting the food (planets)
        //recall that the foodimage variable is a random integer relating to one of the 6 posible planet sprites
        ImageIcon food=new ImageIcon("planet"+foodimage+".png");
        //painting the image at the x and y coordinate, depending on the value foodx and foody variables
        food.paintIcon(this,g,foodx,foody);
    }

    @Override//override is necessary to make keylistener work in this context since it needs to override its parent class
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){//if the right arrow key is pressed...
            if (!left){//as long as the snake is not moving left...
                //make the right boolean true and all others false
                right=true;up=false;down=false;
            }
        }//all below 'else if' statements are the same structure as above
        else if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if (!right){//in this case, as long as the dsnake isnt moving right
                left=true;up=false;down=false;
            }
        }
        else if(e.getKeyCode() == KeyEvent.VK_UP){
            if (!down){//an exception has to be made for the opposite directiion of intended travel since the snake can't go backwards 
                up=true;right=false;left=false;
            }
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN){
            if (!up){
                down=true;left=false;right=false;
            }
        }
    }

    //method repeated every time timer goes off
    public void actionPerformed (ActionEvent e){

        //if a collision is present, the timer will stop, making the snake freeze
        //calls on collision method to check for collisions and display the lose message
        if (collision()){
            timer.stop();
        }

        //snake is moved according to the booleans
        if(right){
            moveright();//calling on moveright method
        }
        else if(left){
            moveleft();//same as above
        }
        else if(down){
            movedown();
        }
        else if(up){
            moveup();
        }
        
        //checking if the snake has eaten any food, calling on checkfood method
        checkfood();

        //adding 1 to the number of moves
        moves++;

        //calling the paint method,to update changes made to the positions of the snake segments, food and score label 
        repaint();
    }

    //***GENERAL CONCEPT/PSEUDOCODE FOR ALL MOVING METHODS***
    //for the axis which the snake is moving in:
    //      the head moves independently, going 20 pixels in the respective direction
    //      all other segments recieve the axis coordinate from the segment before it
    //for the other axis:
    //      all segments(other than the head) recieve the axis coordinate from the segment before it

    //OVERALL: This means that all pieces except the head will recieve both the x and y axis coordinates from the segment before it, 
    //         effectively "following" it. The head moves independently, and can move in only one axis, either x or y depending on keys
    public void moveright(){
        for(int i=segments;i>=0;i--)//deals with x coordinates
        {
            if (i==0)//for the head of the snake
            {
                //it moves 20 (width of the sprite) to the right by adding 20 to the x coordinate
                snakex[i]+=20;
            }
            else
            {
                //for all other segments, they will just follow the x coordinate of the segment before it
                snakex[i]=snakex[i-1];
            }
        }
        for (int i=segments-1;i>=0;i--)//deals with y coordinates
        {
            //gives the y coordinate of the segment i to the segment after i (i+1) - each segment will follow y of the segment before it
            //this is done so that if the snake curved, it will not simply translate to the right
            //i starts at segments-1 so that the head of the snake isnt moved on the y axis
            snakey[i+1]=snakey[i];
        }
    }
    //below 3 methods all follow the same concept as above
    public void moveleft(){
        for(int i=segments;i>=0;i--)
        {
            if (i==0)
            {
                snakex[i]-=20;//subtracts from x to go left
            }
            else
            {
                snakex[i]=snakex[i-1];
            }
        }
        for (int i=segments-1;i>=0;i--)
        {
            snakey[i+1]=snakey[i];
        }
    }

    public void moveup(){
        for(int i=segments;i>=0;i--)
        {
            if (i==0)
            {
                snakey[i]-=20;//subtracts from y to go up
            }
            else
            {
                snakey[i]=snakey[i-1];
            }
        }
        for (int i=segments-1;i>=0;i--)
        {
            snakex[i+1]=snakex[i];
        }
    }

    public void movedown(){
        for(int i=segments;i>=0;i--)
        {
            if (i==0)
            {
                snakey[i]+=20;//adds to y to go down
            }
            else
            {
                snakey[i]=snakey[i-1];
            }
        }
        for (int i=segments-1;i>=0;i--)
        {
            snakex[i+1]=snakex[i];
        }
    }

    public boolean collision (){//checking for collisions - boolean returns true if snake hit itself or the wall
        //for loop is for the second "if" condition, since both of the snake coordinate value arrays need to be looked through 
        //to see if there exists any segment of the snake's body that has the same x AND y value as the head
        for(int i=1;i<segments;i++)
        {
            //if the snakes head is touching the boundary
            if (snakex[0]<=200||snakex[0]>=820||snakey[0]<=100||snakey[0]>540)
            {
                sound("lose.wav");//plays lose sound

                //makes jOptionPane showing that player lost and asking if they want to play again or not
                int n = JOptionPane.showConfirmDialog(null,"You Lost with a score of "+score+"! Play Again?","",JOptionPane.YES_NO_OPTION);
                if(n==JOptionPane.YES_OPTION)//if they want to playa again
                {
                    right=false;left=false;up=false;down=false;//makes all booleans false so snake is stationary
                    snakehead=new ImageIcon("spaceshipright.png");//resets the head icon
                    gamestart();//calls gamestart method to start the game again
                }
                else if(n==JOptionPane.NO_OPTION)
                {
                    System.exit(0);//stops running program to exit
                }
                return true;
            }
            else if ((snakex[0]==snakex[i]&&(snakey[0]==snakey[i]))){//if the xy coordinates of the head match the xy of any segment
                //same as above
                sound("lose.wav");

                int n = JOptionPane.showConfirmDialog(null,"You Lost with a score of "+score+"! Play Again?","",JOptionPane.YES_NO_OPTION);
                if(n==JOptionPane.YES_OPTION)
                {
                    right=false;left=false;up=false;down=false;
                    snakehead=new ImageIcon("spaceshipright.png");
                    gamestart();
                }
                else if(n==JOptionPane.NO_OPTION)
                {
                    System.exit(0);
                }
                return true;
            }
        }
        //if none of the conditions are met (meaning that there are no collisions) false will be returned
        return false;
    }

    public void checkfood(){//this method checks for collisions with food pieces
        if((snakex[0]==foodx)&&(snakey[0]==foody))//if the snake head has same coordinates as the food sprite...
        {
            //assigning a new value to food x
            foodx=(int)(Math.random() * (600) + 220);
            foodx/=20;//getting a number that is divisible by 20
            foodx*=20;
            //same as above, for y coordinate
            foody=(int)(Math.random() * (440) + 120);
            foody/=20;
            foody*=20;

            //getting a random number, sorresponding to what planet the new food will be
            foodimage=(int)(Math.random() * (7) + 1);

            //increasing number of segments by and score by 1
            segments++;
            score++;
            if(difficulty=='h')//if difficulty is hard, 3 segments and 2 points total will be given for each food eaten
            { //meaning 2 more segments and one point need to be added
                segments+=2;
                score++;
            }

            sound("pew.wav");//plays sound effect
        }
    }

    //method was taken from youtube video credited in menu class
    public void sound (String filename)
    {
        try{
            URL path = this.getClass().getResource(filename);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(path);
            music = AudioSystem.getClip();
            music.open(audioIn);
            music.start();
        }
        catch (Exception error){
            System.out.println ("File not found");
        }
    }

    protected static ImageIcon img (String path){
        java.net.URL imgURL = game.class.getResource(path);//gets the path of the image from same folder as class
        if (imgURL != null){//if the path exists
            return new ImageIcon (imgURL);//return an imageiceon with the path
        } else {//else print error message
            System.err.println( "Couldn't find file: " + path);
            return null;
        }
    }//end ImageIcon

    //keys pressed/released are not tracked, but methods need to be present so they can be overriden for keylistener to work properly
    @Override
    public void keyReleased(KeyEvent e){

    }

    @Override
    public void keyTyped(KeyEvent e){

    }

}