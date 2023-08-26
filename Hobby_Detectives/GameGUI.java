import java.awt.*;
import java.awt.event.*;
import java.util.Map;

import javax.swing.*;

public class GameGUI {

    Image tilImage = Toolkit.getDefaultToolkit().getImage("square2.png");
    Image builImage = Toolkit.getDefaultToolkit().getImage("building2.png");
    Image doorTopImage = Toolkit.getDefaultToolkit().getImage("topdoor2.png");
    Image doorBottomImage = Toolkit.getDefaultToolkit().getImage("bottomdoor2.png");
    Image doorLeftImage = Toolkit.getDefaultToolkit().getImage("leftdoor2.png");
    Image doorRightImage = Toolkit.getDefaultToolkit().getImage("rightdoor2.png");
    Board b;
    Game game;
    Map<String,Player> players;
    Player selectedPlayer;
    Frame frame;
    Boolean clicked;

    public static String direction;

    public GameGUI(Board b){
        this.b = b;
        this.players = b.getPlayersMap(); 
        buildGUI();
    }

    public GameGUI(Game g){
        this.game = g;
        this.b = g.getBoard();
        this.players = b.getPlayersMap(); 
        buildGUI();
    }

    /**
     * Creates the Menu at the Top
     * @param frame - JFrame object
     */
    public void createMenu(JFrame frame){
        JMenu menu;
        JMenuItem i1, i2;
        JMenuBar m =new JMenuBar();
        menu = new JMenu("Menu");
        i1 = new JMenuItem("New Game");
        i2 = new JMenuItem("Exit");
        i2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                frame.dispose();
            }
        });
        menu.add(i1);
        menu.add(i2);
        m.add(menu);
        frame.setJMenuBar(m);
    }

    /**
     * Builds the panels with all the buttons
     * @param frame

    public void buildFieldPanel(JFrame frame){
        JPanel fieldJPanel = new JPanel();
        fieldJPanel.setLayout(new GridLayout(15,1));

        JPanel rollDicPanel =new JPanel();
        JButton rollButton = new JButton("Roll Dice");
        rollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                int moves = Dice.roll2()+Dice.roll2();
                System.out.println("You have " + moves + " moves.");
                //selectedPlayer.movement(b, moves);
            }
        });
        rollDicPanel.add(rollButton);

        JPanel guessPanel =new JPanel();
        JButton guessButton = new JButton("Guess");
        guessPanel.add(guessButton);

        JPanel SolveAttemptPanel =new JPanel();
        JButton SolveAttemptButton = new JButton("Solve Attempt");
        SolveAttemptPanel.add(SolveAttemptButton);

        fieldJPanel.add(SolveAttemptPanel, SwingConstants.CENTER);
        fieldJPanel.add(guessPanel, SwingConstants.CENTER);
        fieldJPanel.add(rollDicPanel, SwingConstants.CENTER);

        frame.add(fieldJPanel, BorderLayout.EAST);
    }*/

    /**
     * Puts all the names of the rooms in the room
     * @param g - Graphics object
     * @param e - map of each room and their names
     */
    public void buildRoomName(Graphics g, Map.Entry<String, Room> e){
        switch(e.getValue().getName()){
        case "Haunted House":
            g.drawString(e.getValue().getName(), (e.getValue().getTopX()+1)*24, (e.getValue().getBottomY()-1)*24);
            break;
        case "Visitation Villa":
            g.drawString(e.getValue().getName(), (e.getValue().getTopX()+1)*24, (e.getValue().getBottomY()-1)*24);
            break;
        case "Peril Palace":
            g.drawString(e.getValue().getName(), (e.getValue().getTopX()+1)*24, (e.getValue().getBottomY()-1)*24);
            break;
        case "Manic Manor":
            g.drawString(e.getValue().getName(), (e.getValue().getTopX()+1)*24, (e.getValue().getBottomY()-1)*24);
            break;
        default:
            g.drawString(e.getValue().getName(), (e.getValue().getTopX()+1)*24, (e.getValue().getBottomY()-1)*24); 
        }
    }

    /**
     * Builds characters a puts onto the board
     * @param g - Graphics object
     */
    public void buildCharacters(Graphics g){
        for(Map.Entry<String,Player> e : b.getPlayersMap().entrySet()){
            if("Lucilla".equals(e.getKey())){
                g.setColor(Color.green);
            }else if("Bert".equals(e.getKey())){
                g.setColor(Color.yellow);
            }else if("Malina".equals(e.getKey())){
                g.setColor(Color.blue);
            }else if("Percy".equals(e.getKey())){
                g.setColor(Color.red);
            }
            g.fillOval(e.getValue().getX()*24, e.getValue().getY()*24, 24, 24);
        }
    }


    /**
     * Adds mouse motion Listener to the GUI
     * @param frame

    public void frameMouseMotionListener(JFrame frame){
        frame.addMouseMotionListener(
        new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e){
                if(selectedPlayer!=null){
                    selectedPlayer.setX(e.getX()/24);
                    selectedPlayer.setY((e.getY()/24)-2);
                }
            }

            @Override
            public void mouseMoved(MouseEvent e){

            }
        }
        );
    }*/

    /**
     * Adds Mouse Motion Listener to the GUI
     * @param frame  - The JFrame in which the mouse activity is being performed
     */
    public void frameMouseListener(JFrame frame){
        frame.addMouseListener(
                new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                selectedPlayer = getPlayer(e.getX(), e.getY());
                //System.out.println(getPlayer(e.getX(), e.getY()));
            }

            @Override
            public void mouseReleased(MouseEvent e){
                if(selectedPlayer == null){return;}
                else if(!selectedPlayer.getActivePlayer() || selectedPlayer.getMoves()==0 ||
                        (!selectedPlayer.getActivePlayer() && selectedPlayer.getMoves()==0)){return;}

                String direction = null;
                if(selectedPlayer.getX() == (e.getX()/24)-1){
                    direction = "Right";
                }else if(selectedPlayer.getX() == (e.getX()/24)+1){
                    direction = "Left";
                }else if(selectedPlayer.getY() == (e.getY()/24)-2+1){
                    direction = "Up";
                } else if(selectedPlayer.getY() == (e.getY()/24)-2-1){
                    direction = "Down";
                } else{return;}

                GameGUI.setDirection(direction);
                selectedPlayer.movement(b, direction, GameGUI.this);
                //b.movePlayer(selectedPlayer.getName(), direction, selectedPlayer.getX(), selectedPlayer.getY(), new int[5+1][2]);

                for(Map.Entry<String, Room> entry : b.getRooms().entrySet()){
                    System.out.println(entry.getValue().getName()+": "
                    + entry.getValue().inRoom((e.getX()/24), ((e.getY()/24)-1)));
                }
                frame.repaint();

                if(selectedPlayer.getMoves()==0){
                    showInformation("You have exhausted all your moves. Please hit OK, then ENTER " +
                            "in the console to proceed.");
                }
            }
        }
        );
    }

    //----------------------------------------------------
    //            clicked Getters and Setters
    //----------------------------------------------------
    public Boolean getClicked(){
        return clicked;
    }

    public void setClicked(Boolean c){
        clicked = c;
    }
    //----------------------------------------------------
    //           direction Getters and Setters
    //----------------------------------------------------
    public static void setDirection(String d){
        direction = d;
    }

    public static String getDirection(){
        return direction;
    }

    /**
     * Builds the GUI
     */
    public void buildGUI(){
        Cell[][] cells = returnCells();

        JFrame frame = new JFrame();
        this.frame = frame;
        createMenu(frame);

        frame.setLayout(new BorderLayout());
        frame.setBounds(10, 10, 700, 650);
        frame.setUndecorated(false);

        //buildFieldPanel(frame);

        System.out.println("Size: " + b.getRooms().size());
        

        JPanel pn = new JPanel(){
            @Override
            public void paint(Graphics g) {

            //draws the board
            for(int y= 0;y<24;y++){
                for(int x= 0;x<24;x++){
                    g.drawImage(tilImage, x*24, y*24, 24, 24, this);
                }
            }

            //draws the grey squares
            for(int r = 0; r<cells.length; r++){
                g.setColor(new Color(211, 211, 211));
                for(int c = 0; c<cells[r].length; c++){
                    if("W".equals(cells[r][c].toString())){
                        //g.drawImage(builImage, r*24, c*24, 24, 24, this);
                        g.fillRect(r*24, c*24, 24, 24);
                    }
                }
            }

            //draws the rooms
            for(Map.Entry<String,Room> e : b.getRooms().entrySet()){
                for(int c = e.getValue().getTopY(); c<=e.getValue().getBottomY(); c++){
                    for(int r = e.getValue().getTopX(); r<=e.getValue().getBottomX(); r++){
                        if(cells[r][c].isDoor() && c == e.getValue().getTopY()){
                            g.drawImage(doorTopImage, r*24, c*24, 24, 24, this);
                        }else if(cells[r][c].isDoor() && c == e.getValue().getBottomY()){
                            g.drawImage(doorBottomImage, r*24, c*24, 24, 24, this);
                        }else if(cells[r][c].isDoor() && r == e.getValue().getTopX()){
                            g.drawImage(doorLeftImage, r*24, c*24, 24, 24, this);
                        }else if(cells[r][c].isDoor() && r == e.getValue().getBottomX()){
                            g.drawImage(doorRightImage, r*24, c*24, 24, 24, this);
                        }else{g.drawImage(builImage, r*24, c*24, 24, 24, this);}
                    }
                }
                
                //puts the room name inside the room
                buildRoomName(g, e);
            }

            //builds the characters
            buildCharacters(g);
        }
        
    };

    frame.add(pn);
    
    
    //frameMouseMotionListener(frame);
    frameMouseListener(frame);

    frame.setDefaultCloseOperation(3);
    frame.setVisible(true);

    //System.out.println(askQuestion("Number of Players 3 or 4? "));
    }

    /**
     * Returns all the cells in the board
     * @return Cel[][] of the board
     */
    public Cell[][] returnCells(){
        return this.b.getCells();
    }

    /**
     * Gets the player at a given position on the board position
     * @param x - x coordinate
     * @param y - y coordinate
     * @return Player object of the player at the given coordinate
     */
    public Player getPlayer(int x, int y){
        int xp = x/24;
        int yp = (y/24)-2;
        for(Map.Entry<String,Player> e : b.getPlayersMap().entrySet()){
            System.out.println("xp: " + xp + " plyx: " + e.getValue().getX() 
            + " yp: " + yp + " plyY: " + e.getValue().getY());
            if(e.getValue().getX() == xp && e.getValue().getY() == yp){
                return e.getValue();
            }
        }
        return null;
    }

    /**
     * Asks user a question and provides an input text pane for the users response
     * @param message - Question to the user
     * @return String of the user's response
     */
    public String askQuestion(String message){   
        String output = JOptionPane.showInputDialog(this.frame,message);
        //System.out.println(output);
        return output;
    }

    /**
     * Displays a message to the user via the option pane
     * @param messageString
     */
    public void showInformation(String messageString){
        JOptionPane.showMessageDialog(this.frame,messageString);
    }

    /**
     * Creates and JOptionPane which contains a drop-down of the provided choices with the given message
     * @param message - the type of choice to be selected from
     * @param choices - all the user's choices/options
     * @return String of the choice the user selected
     */
    public String showComboOptions(String message, String[] choices){
        JComboBox<String> choiceBox = new JComboBox<>(choices);
        int result = JOptionPane.showOptionDialog(null, choiceBox, message,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

        if(result == JOptionPane.OK_OPTION){ return (String) choiceBox.getSelectedItem(); }
        return null;
    }

    /**
     * Informational buttons used to
     * @param msg
     * @return
     */
    public String showInfoButtons(String msg){
        String[] buttons = { "Check Hand", "Check Cards Known", "Make Final Guess", "Make Roll" };

        int rc = JOptionPane.showOptionDialog(this.frame, msg, "Choose move",
            JOptionPane.INFORMATION_MESSAGE, 1, null, buttons, buttons[2]);

        return (rc+1) + "";
    }


    public static void main(String[] args) {
        Board b = new Board();
        b.buildBoard();
        new GameGUI(b);
    }
}
