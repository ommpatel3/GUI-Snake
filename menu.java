/**
 * Om Patel 
 * June 7, 2019
 * GUI Game Project - Criterion C
 * 
 * this class opens the main menu, instructions and settings screen, and will call the actual game class  
 */
import javax.swing.*;//first 3 libraries are for gui
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.AudioSystem;//this and all below libraries are to make music work
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import java.net.URL;
//learned music technique from https://www.youtube.com/watch?v=QVrxiJyLTqU - (libraries and sound() method)
public class menu extends JPanel implements ActionListener{
    //making clip variable for background music
    Clip music;
    
    //making object for cardlayout so it can be added to this page
    CardLayout cdlayout=new CardLayout();

    //public static is added onto the next two variables so they can be acessed in both contexts, since they will be used across methods and classes
    //set difficulty to easy by default (can be changed by player in the settings screen)
    public static char difficulty='e';
    //set image to normal space background by default (can be changed by player in the settings screen)
    public static String image="spacebackground.jpg";
    
    public static void main( String[] args){
        menu content = new menu();
        JFrame window = new JFrame("Space Snake");
        window.setContentPane( content);
        window.setSize(1085,620);
        window.setLocation( 50,0);
        window.setVisible(true);
        window.setResizable(false);//fixes window size as the dimensions listed above
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//will completely shut off the program when x is clicked, without it music won't shut off 
    }

    public menu (){
        //having the menu and instructions page in cardlayout with eachother
        setLayout(cdlayout);
        menu();
        rules();
        setting();
    }

    public void menu(){
        sound();//calling music method
        
        //making background panel
        JLabel main=new JLabel (img("spacebackground.jpg"));
        main.setLayout(new BorderLayout());
        
        //making panel for north widgets (title and credit)
        JPanel north=new JPanel();
        north.setBackground (new Color(0,0,0,0));
        //title label
        JLabel title=new JLabel("Space Snake");
        title.setFont(new Font("Bauhaus 93",Font.BOLD,72));
        title.setForeground(Color.white);
        //label with name
        JLabel cred=new JLabel("By: Om Patel");
        cred.setFont(new Font("Bauhaus 93",Font.BOLD,12));
        cred.setForeground(Color.white);
        //adding to north panel
        north.add(title);north.add(cred);

        //making panel for buttons on west side
        JPanel west = new JPanel (new GridLayout(3,1));
        west.setPreferredSize(new Dimension(300, 480));
        //making button for instructions
        JButton rules=new JButton(img("buttonhowto.png"));
        rules.setActionCommand("rules");
        rules.addActionListener(this);
        //making play button
        JButton play=new JButton(img("buttonplay.png"));
        play.setActionCommand("play");
        play.addActionListener(this);
        //making exit button
        JButton exit=new JButton (img("buttonexit.png"));
        exit.setActionCommand("exit");
        exit.addActionListener(this);
        //adding buttons to sub panel
        west.add(rules);west.add(play);west.add(exit);

        //making jpanel for south widgets
        JPanel south=new JPanel();
        //making jtogglebutton widget to stop/start music. it is similar to JButton, except it has two states, selected or not selected
        //actionlistener is initiated and actionperformed is included in the constructor instead in order to differentiate it 
        //from the other jbuttons' actionperformed, to make the code more organized
        //learned toggle button from https://www.geeksforgeeks.org/java-swing-jtogglebutton-class/
        JToggleButton musictgl = new JToggleButton("Turn Music Off");
        musictgl.setActionCommand("musictgl");
        musictgl.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e){
                    if(musictgl.isSelected()){//if the button is in the selected state
                        musictgl.setText("Turn Music On");
                        music.close();
                    }
                    else {
                        musictgl.setText("Turn Music Off");
                        sound();
                    }
                }
            });
        musictgl.setBackground(new Color(0,0,0,0));
        musictgl.setForeground(new Color(255,255,255));
        //making 2 non functional, invisible filler buttons to add in to grid layout so music toggle doesnt take up full length of the screen
        JButton filler=new JButton ();
        filler.setVisible(false);
        JButton filler2=new JButton ();
        filler2.setVisible(false);
        //setting background to transparent and gridlayout 1x3 for south panel
        south.setBackground(new Color(0,0,0,0));
        south.setLayout(new GridLayout(1,3));
        //adding to panel
        south.add (filler);south.add (musictgl);south.add (filler2);

        //making label for spinning planet gif
        JLabel east = new JLabel (img("spin.gif"));

        //adding all subpanels into respective borderlayout zones
        main.add (west, BorderLayout.WEST);
        main.add (north, BorderLayout.NORTH);
        main.add (south, BorderLayout.SOUTH);
        main.add (east, BorderLayout.EAST);

        //adding main panel into the cardlayout and designating number 1 to it
        add ("1",main);
    }

    public void rules(){
        //making main panel for rules page
        JPanel main=new JPanel();
        main.setBackground(Color.black);
        main.setLayout(new BorderLayout());//using borderlayout
        
        //title label - will be on north side
        JLabel north=new JLabel("How to Play");
        north.setFont(new Font("Bauhaus 93",Font.BOLD,72));
        north.setForeground(Color.white);
        north.setHorizontalAlignment(JLabel.CENTER);
        
        //making label for gif - will be on east side 
        JLabel east=new JLabel(img("playing.gif"));

        //making JPanel for instruction texts
        JPanel west=new JPanel(new GridLayout(5,1));//making jpanel for gridlayout 5x1
        west.setBackground(Color.black);

        //all instruction messages are stored in the array
        String[]message={"- In this game, you are a convoy of alien UFOs and spaceships","- The object of the game is to eat as many planets as possible","- Use the 4 arrow keys to steer the army","- As you destroy more planets, the convoy becomes longer, earning you a point","- Running into your own convoy or the boundaries of space-time will result in a loss"};
        //array of jlabels is created
        JLabel l[] = new JLabel [5]; 
        //using for loop with the arrays to make labels, write message, format colours and font and add to west subpanel
        for (int i=0;i<5;i++)
        {
            l [i] = new JLabel (""+message[i]);
            l [i].setBackground(Color.black);
            l [i].setForeground(Color.white);
            l [i].setFont(new Font("Consolas",Font.BOLD,15));
            west.add (l [i]);
        }

        //making jbutton to go back to the main menu page
        JButton back=new JButton("Back");
        back.setForeground(Color.white);
        back.setBackground(Color.black);
        back.setFont(new Font("Arial",Font.BOLD,32));
        back.setActionCommand("back");
        back.addActionListener(this);

        //adding subpanels/widgets to main panel
        main.add (back, BorderLayout.SOUTH);
        main.add (east, BorderLayout.EAST);
        main.add (north, BorderLayout.NORTH);
        main.add (west, BorderLayout.WEST);

        //adding main panel and designating number 2
        add ("2",main);
    }

    public void setting()
    {
        //background panel
        JLabel main=new JLabel (img("spacebackground.jpg"));
        main.setLayout(null);//using absolute layout since it allows for more precise control over item positions

        //making title JLabel
        JLabel title=new JLabel("Settings");
        title.setFont(new Font("Bauhaus 93",Font.BOLD,72));
        title.setForeground(Color.white);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setBounds(392,15,300,100);//setbounds used because of null layout

        //using two arrays and a for loop to create the difficulty and themes label
        JLabel l[] = new JLabel [2]; 
        String[] subtitle={"Difficulty", "Themes"};
        for (int i=0;i<2;i++)
        {
            l[i]=new JLabel(""+subtitle[i]);
            l[i].setFont(new Font("Arial",Font.BOLD,42));
            l[i].setForeground(Color.white);
            l[i].setHorizontalAlignment(JLabel.CENTER);
            l[i].setBounds(50+(i*675),150,300,100);
            main.add(l[i]);
        }

        //for loop and 2 arrays make one "easy" button and one "hard" button for difficulty
        JButton b[] = new JButton [2]; 
        String[] path={"buttoneasy.png","buttonhard.png"};
        for (int i=0;i<2;i++)
        {
            b[i]= new JButton (img(path[i]));
            String command;
            //making actioncommand for first button "easy" and the other one "hard"
            if (i==0)
                command="easy";
            else
                command="hard";
            b[i].setActionCommand(""+command);
            b[i].addActionListener(this);
            b[i].setBounds(50,(250+(i*100)),300,100);
            main.add (b[i]);
        }

        //for loop and arrays to create 4 buttons for themes
        JButton c[] = new JButton [4];
        String[] pics={"spacebackground.jpg","galaxybackground.jpg","blackholebackground.jpg","saturnbackground.jpg"};//names of buttons and paths for imageicon
        for (int i=0;i<4;i++)
        {
            c[i]= new JButton (img(pics[i]));
            c[i].setActionCommand(""+pics[i]);
            c[i].addActionListener(this);
            //following 6 lines change the x and y coordinate for each button, giving a set of 4 buttons arranged in a 2x2 grid
            int x=725;
            int y=250;
            if (i==0||i==2)
                x=875;
            if (i==2||i==3)
                y=350;
            c[i].setBounds(x,y,150,100);
            main.add(c[i]);
        }

        //making play button
        JButton play=new JButton(img("buttonplaysetting.png"));
        play.setActionCommand("startgame");
        play.addActionListener(this);
        play.setBounds(440,250,200,200);

        //making button to go back to main menu
        JButton back= new JButton (img("buttonbacksettings.png"));
        back.setActionCommand("back");
        back.addActionListener(this);
        back.setBounds(385,500,300,75);        
        main.add (back);
        
        //Adding subpanels to main
        main.add(title);
        main.add(play);

        //adding main panel and designating cardlayout 3
        add ("3",main);
    }

    //the two below methods are used by the game method to pass on parameters more efficiently
    //technique learned from https://www.daniweb.com/programming/software-development/threads/326941/passing-data-between-classes
    public static char getdifficulty() {
        return difficulty;
    }

    public static String getimage(){
        return image;
    }

    public void actionPerformed (ActionEvent e){
        if (e.getActionCommand().equals("rules"))
        {
            //rules button shows panel "2", which is the instructions panel
            cdlayout.show (this, "2");
        }
        else if (e.getActionCommand().equals("play"))
        {
            //shows the settings panel, which will lead to the game
            cdlayout.show (this, "3");
        }
        else if (e.getActionCommand().equals("back"))
        {
            //shows panel "1", which is the main menu panel
            cdlayout.show(this, "1");
        }
        else if (e.getActionCommand().equals("exit"))
        {
            //closes window and exits program
            System.exit(0);
        }
        else if (e.getActionCommand().equals("easy"))
        {
            //set difficulty to easy
            difficulty='e';
        }
        else if (e.getActionCommand().equals("hard"))
        {
            difficulty='h';
        }
        else if (e.getActionCommand().equals("startgame"))
        {
            //play button will open a new window with the game - game is made in separate class for organization 
            //making object for the game class
            game a = new game();
            a.main(null);//calling main and indicating there are no arguements to pass on in the (String [] args) line
        }
        //if any of the theme buttons are pressed
        else if (e.getActionCommand().equals("spacebackground.jpg")||e.getActionCommand().equals("galaxybackground.jpg")||e.getActionCommand().equals("blackholebackground.jpg")||e.getActionCommand().equals("saturnbackground.jpg"))
        {
            //set variable image to to buttons name (which is also the image file name)
            image=e.getActionCommand();
        }
    }

    //music method is copied from youtube video mentioned at the top of the class
    public void sound ()//runs background music
    {
        try{
            //music is from 1996 computer game DxBall - taken from https://www.youtube.com/watch?v=Wa-pUo2_Oa8
            URL path = this.getClass().getResource("dxball.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(path);
            music = AudioSystem.getClip();
            music.open(audioIn);
            music.loop(Clip.LOOP_CONTINUOUSLY);
        }
        catch (Exception error){
            System.out.println ("File not found");
        }
    }

    //imageicon class is simply called "img" to make typing it easier
    protected static ImageIcon img (String path){
        java.net.URL imgURL = menu.class.getResource( path);
        if (imgURL != null){
            return new ImageIcon (imgURL);
        } else {
            System.err.println( "Couldn't find file: " + path);
            return null;
        }
    }//end ImageIcon

}