/*
Assignment 5 COMP2800
- Matthew Connell
- 110034695
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DrawingFrame extends JFrame {
    JComboBox options;
    JPanel optionPanel;
    JLabel instructions, info;
    JPanel instructionPanel, tipPanel;
    JLabel tips;

    public DrawingFrame(){
        super("Graph Builder");

        //Add ComboBox
        String choices[] = {"Add Node", "Add Edge","Move","Delete"};
        options = new JComboBox(choices);
        optionPanel = new JPanel();
        optionPanel.setLayout(new BorderLayout());
        optionPanel.add(options);

        DrawingPanel drawingPanel = new DrawingPanel();
        instructions = new JLabel("left click to add node, right-click to prompt menu", SwingConstants.CENTER);
        tips = new JLabel(" ", SwingConstants.CENTER);
        tipPanel = new JPanel();
        tipPanel.setLayout(new BorderLayout());
        tipPanel.add(tips);
        optionPanel.add(tipPanel, BorderLayout.SOUTH);

        info = new JLabel("load and save features can be accessed by prompting menu", SwingConstants.CENTER);

        
        options.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (options.getSelectedItem().equals("Add Node")) {
                    drawingPanel.setState(0);
                    instructions.setText("left click to add node, right-click to prompt menu");
                    tips.setText("       ");
                } else if (options.getSelectedItem().equals("Add Edge")){
                    drawingPanel.setState(1);
                    instructions.setText("drag an edge from one node to another, right-click to prompt menu");
                    tips.setText("       ");

                } else if (options.getSelectedItem().equals("Delete")){
                    drawingPanel.setState(2);
                    instructions.setText("right-click anywhere on a line, node to delete, or to prompt menu");
                } else {
                    drawingPanel.setState(3);
                    instructions.setText("left-click desired node and drag to new location, or right-click to prompt menu");


                }
            }
        });
        instructionPanel = new JPanel();
        instructionPanel.setLayout(new GridLayout(2, 1));
        instructionPanel.add(instructions);
        instructionPanel.add(info);

        setLayout(new BorderLayout());
        add(optionPanel, BorderLayout.NORTH);
        add(instructionPanel, BorderLayout.SOUTH);
        add(drawingPanel,BorderLayout.CENTER);
    }

    class DrawingPanel extends JPanel{

        public int xBegin, xEnd;
        public int yBegin, yEnd;
        public int currentMouseX, currentMouseY;
        public int state = 0;
        public int removeIndex;
        public boolean drag = false;
        public ArrayList<Line> edges = new ArrayList<>();
        public ArrayList<Point> nodes = new ArrayList<>();
        JPopupMenu popupMenu, deleteMenu, promptMenu;
        JMenuItem confirm;
        JMenuItem menuTip;
        JMenuItem cancel, exit, exitP;
        JMenuItem switchAddEdge, switchAddEdgeD;
        JMenuItem switchAddNode, switchAddNodeD;
        JMenuItem switchMove, switchMoveD, switchDelete;
        JMenuItem saveGraph, saveGraphD, loadGraph, loadGraphD;
        JMenuItem clearScreen;
        JLabel tipPrompt;
        public double phi = Math.toRadians(40);
        public int moveNode = 0;

        File saveGraphFile;

        public DrawingPanel() {
            confirm = new JMenuItem("confirm delete");
            //confirm.setEnabled(false);
            cancel = new JMenuItem("cancel");
            exit = new JMenuItem("escape");
            switchAddNode = new JMenuItem("switch to add node");
            switchAddNode.setEnabled(false);
            switchAddNodeD = new JMenuItem("switch to add node");
            switchAddEdge = new JMenuItem("switch to add edge");
            switchAddEdgeD = new JMenuItem("switch to add edge");
            switchMove = new JMenuItem("switch to move");
            switchMoveD = new JMenuItem("switch to move");
            switchDelete = new JMenuItem("switch to delete");
            clearScreen = new JMenuItem("clear all nodes/edges");
            saveGraph = new JMenuItem("save graph");
            loadGraph = new JMenuItem("load graph");
            saveGraphD = new JMenuItem("save graph");
            loadGraphD = new JMenuItem("load graph");

            deleteMenu = new JPopupMenu();
            deleteMenu.add(confirm);
            deleteMenu.add(cancel);
            deleteMenu.add(clearScreen);
            deleteMenu.addSeparator();
            deleteMenu.add(switchAddNodeD);
            deleteMenu.add(switchAddEdgeD);
            deleteMenu.add(switchMoveD);
            deleteMenu.addSeparator();
            deleteMenu.add(saveGraphD);
            deleteMenu.add(loadGraphD);

            deleteMenu.setSize(100,50);
            deleteMenu.setVisible(false);

            popupMenu = new JPopupMenu();
            popupMenu.add(exit);
            popupMenu.addSeparator();
            popupMenu.add(switchAddNode);
            popupMenu.add(switchAddEdge);
            popupMenu.add(switchMove);
            popupMenu.add(switchDelete);
            popupMenu.addSeparator();
            popupMenu.add(saveGraph);
            popupMenu.add(loadGraph);
            //popupMenu.add(clearScreen);
            popupMenu.setSize(100,50);
            popupMenu.setVisible(false);

            menuTip = new JMenuItem("please select a node");
            exitP = new JMenuItem("escape");
            promptMenu = new JPopupMenu();
            promptMenu.add(menuTip);
            promptMenu.setSize(70, 50);
            promptMenu.setVisible(false);

            removeIndex = 100;


            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (state == 1){
                        super.mouseDragged(e);
                        //set the endpoints for x and y
                        xEnd = e.getX();
                        yEnd = e.getY();
                        drag = true;
                        //call the repaint function to draw rectangle
                        tips.setText("please drag edge to a node");

                        repaint();
                    } else if (state == 3){
                        super.mouseDragged(e);
                        tips.setText("currently moving node");
                        //drag = true;
                        currentMouseX = e.getX();
                        currentMouseY = e.getY();


                    }
                }
            });


            //add to panel
            addMouseListener(new MouseHandler());
        }

        public void setState(int i){
            //set shape if state changed on comboBox listener
            //will set state for buttons depending on which state is selected
            if (i == 0) {
                this.state = 0; //add node
                tips.setText(" ");
                switchAddNode.setEnabled(false);
                switchAddEdge.setEnabled(true);
                switchMove.setEnabled(true);

            } else if (i == 1) {
                tips.setText(" ");
                this.state = 1; //add edge
                switchAddEdge.setEnabled(false);
                switchAddNode.setEnabled(true);
                switchMove.setEnabled(true);
        


            } else if (i == 2) {
                tips.setText(" ");
                this.state = 2; //delete

            } else if (i == 3){
                tips.setText(" ");
                this.state = 3; //move
                switchMove.setEnabled(false);
                switchAddNode.setEnabled(true);
                switchAddEdge.setEnabled(true);
            }

        }

        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2 = (Graphics2D) g;



            //if drag is true, allows for the drag effect for adding/moving lines
            if (drag == true){
                g2.drawLine(xBegin, yBegin, xEnd, yEnd);
                g2.setColor(Color.blue);
            }

            //loop through all edges and paint
            for (Line edge: edges){
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int midPointX = (edge.xBegin + edge.xEnd)/2;
                int midPointY = (edge.yBegin + edge.yEnd)/2;
                // the calculations to draw arrowhead on endpoint of line
                double dy = edge.yEnd - edge.yBegin;
                double dx = edge.xEnd - edge.xBegin;

                //essentially tan(dy/dx)
                double theta = Math.atan2(dy, dx);

                double x, y, rho = theta+phi;

                //will draw 2 lines ^>
                for (int i = 0; i < 2; i++){
                    //x = edge.xEnd - 20  * Math.cos(rho);
                    x = midPointX - 20  * Math.cos(rho);
                    //y = edge.yEnd - 20 * Math.sin(rho);
                    y = midPointY - 20 * Math.sin(rho);

                    //draw each line with the two endpoints x/yEnd and the calculated x and y points
                    g2.draw(new Line2D.Double(midPointX, midPointY, x, y));
                    rho = theta - phi;

                }
                //draw the actual line
                g2.drawLine(edge.getxBegin(), edge.getyBegin(), edge.getxEnd(), edge.getyEnd());
            }

            for (Point p: nodes){
                g2.setColor(Color.black);
                g2.drawRect((int) p.getX(),(int)p.getY(), 20, 20);
                g2.setColor(Color.white);
                g2.fillRect((int) p.getX(), (int) p.getY(), 20, 20);

            }
        }

        public boolean isOnLine(int x, int y){
            for (int i = 0; i < edges.size(); i++){
                //will calculate if current mouse click is on the line
                double distance = Line2D.ptSegDist(edges.get(i).getxBegin(), edges.get(i).getyBegin(), edges.get(i).getxEnd(), edges.get(i).getyEnd(), x, y);

                //the offset is set to 5 so if mouseclick is within 5 distance
                if (distance < 5) {
                    //assign this edge to get removed
                    edges.get(i).setRemoveEdge(true);
                    removeIndex = i;
                    return true;
                }
            }
            return false;
        }

        public boolean isOnNode(double x, double y){
            Rectangle nodeRect = new Rectangle((int)x, (int)y, 20, 20);
            double distance1 = Line2D.ptSegDist(x-10, y, x+10, y, currentMouseX, currentMouseY);
            double distance2 = Line2D.ptSegDist(x, y-10, x, y+10, currentMouseX, currentMouseY);

            Point currPoint = new Point(currentMouseX, currentMouseY);

            if (distance1 < 20 || distance2 < 20){
                tips.setText("corresponding nodes/edges successfully deleted");
                return true;

            }
                
            tips.setText("no node has been selected");
            return false;
        }

        public void edgesOnNode(int x, int y){
            for (int i = 0; i < edges.size(); i++){
                if (x == edges.get(i).xBegin && y == edges.get(i).yBegin) {
                    edges.get(i).setRemoveEdgeDelete(true);
                }else if (x == edges.get(i).xEnd && y == edges.get(i).yEnd) {
                    edges.get(i).setRemoveEdgeDelete(true);
                }

            }
        }

        public int edgePointOnNode(int x, int y){
            for (int i = 0; i < edges.size(); i++){
                if (x == edges.get(i).xBegin && y == edges.get(i).yBegin) {
                    edges.get(i).setRemoveEdge(true);
                    return 1; //beginning point is on node
                }else if (x == edges.get(i).xEnd && y == edges.get(i).yEnd) {
                    edges.get(i).setRemoveEdge(true);
                    return 2; //ending point is on node
                }

            }
            return 0; //no edge is on node
        }

        public boolean doesEdgeExist(Line line){
            //this will search through all the edges to see if the edge previously exists from either direction
            for(Line edge: edges){
                if ((edge.xBegin == line.xBegin && edge.yBegin == line.yBegin) && (edge.xEnd == line.xEnd && edge.yEnd == line.yEnd))
                    return true;
                if ((edge.xBegin == line.xEnd && edge.yBegin == line.yEnd) && (edge.xEnd == line.xBegin && edge.yEnd == line.yBegin))
                    return true;
            }
            return false;
        }

        public void removeNodeEdges(Point node){
            for (int i = 0; i < edges.size(); i++){
                if (edges.get(i).xBegin == node.x && edges.get(i).yBegin == node.y){
                    edges.get(i).setRemoveEdge(true);
                    tips.setText("corresponding nodes/edges successfully deleted");

                } else if (edges.get(i).xEnd == node.x && edges.get(i).yEnd == node.y){
                    edges.get(i).setRemoveEdge(true);
                    tips.setText("corresponding nodes/edges successfully deleted");

                }
            }

            for (int i = 0; i < edges.size(); i++){
                if (edges.get(i).isRemoveEdge())
                    edges.remove(i);
                repaint();
            }

        }




        private class MouseHandler implements MouseListener {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (state == 2) {
                    //checks if right mouse click
                    if (SwingUtilities.isRightMouseButton(e)) {
                        deleteMenu.setVisible(true);
                        deleteMenu.setLocation(e.getX(), e.getY());


                    }
                    //will exit the popup menu if cancel is clicked
                    cancel.addActionListener(new ActionListener(){
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            deleteMenu.setVisible(false);
                            if (state == 0){
                                repaint();
                            }
                        }
                    });

                
                    //will delete selected edge
                    confirm.addActionListener(new ActionListener(){
                        @Override
                        public void actionPerformed(ActionEvent ev) {
                            for (int i = 0; i < nodes.size(); i++){
                                if  (Math.abs(e.getX() - nodes.get(i).getX()) < 20 && Math.abs(e.getY() - nodes.get(i).getY()) < 20){
                                    removeNodeEdges(nodes.get(i));
                                    nodes.remove(nodes.get(i));                            
                                    repaint();
                                    tips.setText("corresponding nodes/edges successfully deleted");

                                } 
                            }

                            for (int i = 0; i < edges.size(); i++){
                                if (isOnLine(e.getX(), e.getY()) || edges.get(i).isRemoveEdgeDelete())
                                    edges.remove(i);
                                repaint();
                            }

                            deleteMenu.setVisible(false);
                        }

                    });

                    //will delete selected edge
                    clearScreen.addActionListener(new ActionListener(){
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            nodes.clear();
                            edges.clear();
                            deleteMenu.setVisible(false);
                            tips.setText("the screen has been cleared");

                            repaint();
                        }

                    });

                    //will switch to the add mode
                    switchAddNodeD.addActionListener(new ActionListener(){
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            options.setSelectedItem("Add Node");
                            deleteMenu.setVisible(false);
                        }

                    });

                    switchAddEdgeD.addActionListener(new ActionListener(){
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            options.setSelectedItem("Add Edge");
                            deleteMenu.setVisible(false);
                        }

                    });


                    //will switch to the move mode
                    switchMoveD.addActionListener(new ActionListener(){
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            options.setSelectedItem("Move");
                            deleteMenu.setVisible(false);
                        }

                    });

                    saveGraphD.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            try{
                                
                                FileWriter fw = new FileWriter("graph.txt");
                                for(int i = 0; i < nodes.size(); i++){
                                    fw.write(Integer.toString(nodes.get(i).x));
                                    fw.write(",");
                                    fw.write(Integer.toString(nodes.get(i).y));
                                    if (i != (nodes.size()-1))
                                        fw.write(",");
                                    
                                }
                                fw.write("\n");

                                for(int i = 0; i < edges.size(); i++){
                                    fw.write(Integer.toString(edges.get(i).xBegin));
                                    fw.write(",");
                                    fw.write(Integer.toString(edges.get(i).yBegin));
                                    fw.write(",");
                                    fw.write(Integer.toString(edges.get(i).xEnd));
                                    fw.write(",");
                                    fw.write(Integer.toString(edges.get(i).yEnd));
                                    if (i != (edges.size()-1))
                                        fw.write(",");

                                }
                                deleteMenu.setVisible(false);

                                tips.setText("graph successfully saved");

                                fw.close();
                        
                            } catch (IOException err){
                                err.printStackTrace();
                            }
                            
                        }
                    });


                    loadGraphD.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            try{

                                FileReader fileReader = new FileReader("graph.txt");
                                BufferedReader bufferedReader = new BufferedReader(fileReader);
                                String line;
                                String[] data;
                                nodes.clear();
                                edges.clear();
                                
                                line = bufferedReader.readLine();
                                data = line.trim().split(",");
                                    
                                int pointX = 0, pointY = 0, pointX2 = 0, pointY2 = 0;
                                for (int j = 0; j < data.length; j++){
                                
                                    if (j % 2 == 0){
                                        pointX = Integer.parseInt(data[j]);
                                    } else {
                                        pointY = Integer.parseInt(data[j]);
                                        nodes.add(new Point(pointX, pointY));
                                    }  
                                    repaint();
                                }

                                line = bufferedReader.readLine();
                                data = line.trim().split(",");
                                
                                for (int j = 0; j < data.length; j+=4){
                                    pointX = Integer.parseInt(data[j]);
                                    pointY = Integer.parseInt(data[j+1]);
                                    pointX2 = Integer.parseInt(data[j+2]);
                                    pointY2 = Integer.parseInt(data[j+3]);
                                    edges.add(new Line(pointX, pointY, pointX2, pointY2));
                                }
                                repaint();
                                bufferedReader.close();
                                deleteMenu.setVisible(false);
                                tips.setText("graph successfully loaded");


                            } catch (IOException err){
                                err.printStackTrace();
                            }

 
                        }
                    });

                    




                } else if (state != 2){
                    if (SwingUtilities.isRightMouseButton(e)) {
                        popupMenu.setVisible(true);
                        popupMenu.setLocation(e.getX(), e.getY());

                    }

                    //will exit the popup menu if cancel is clicked
                    exit.addActionListener(new ActionListener(){
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            popupMenu.setVisible(false);
                            if (state == 0){
                                repaint();
                            }
                        }
                    });

                    //will switch to the add mode
                    switchAddNode.addActionListener(new ActionListener(){
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            options.setSelectedItem("Add Node");
                            popupMenu.setVisible(false);
                        }

                    });

                    switchAddEdge.addActionListener(new ActionListener(){
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            options.setSelectedItem("Add Edge");
                            popupMenu.setVisible(false);
                        }

                    });


                    //will switch to the move mode
                    switchMove.addActionListener(new ActionListener(){
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            options.setSelectedItem("Move");
                            popupMenu.setVisible(false);
                        }

                    });

                    //will switch to the delete mode
                    switchDelete.addActionListener(new ActionListener(){
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            options.setSelectedItem("Delete");
                            popupMenu.setVisible(false);

                        }

                    });
                    saveGraph.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            try{
                                FileWriter fw = new FileWriter("graph.txt");
                                for(int i = 0; i < nodes.size(); i++){
                                    fw.write(Integer.toString(nodes.get(i).x));
                                    fw.write(",");
                                    fw.write(Integer.toString(nodes.get(i).y));
                                    if (i != (nodes.size()-1))
                                        fw.write(",");
                                    
                                }
                                fw.write("\n");

                                for(int i = 0; i < edges.size(); i++){
                                    fw.write(Integer.toString(edges.get(i).xBegin));
                                    fw.write(",");
                                    fw.write(Integer.toString(edges.get(i).yBegin));
                                    fw.write(",");
                                    fw.write(Integer.toString(edges.get(i).xEnd));
                                    fw.write(",");
                                    fw.write(Integer.toString(edges.get(i).yEnd));
                                    if (i != (edges.size()-1))
                                        fw.write(",");

                                }
                                popupMenu.setVisible(false);
                                tips.setText("graph successfully saved");

                                fw.close();
                        
                            } catch (IOException err){
                                err.printStackTrace();
                            }
                            
                        }
                    });


                    loadGraph.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            
                            try{

                                FileReader fileReader = new FileReader("graph.txt");
                                BufferedReader bufferedReader = new BufferedReader(fileReader);
                                String line;
                                String[] data;
                                
                                
                                line = bufferedReader.readLine();
                                data = line.trim().split(",");
                                nodes.clear();
                                edges.clear(); 
                                int pointX = 0, pointY = 0, pointX2 = 0, pointY2 = 0;
                                for (int j = 0; j < data.length; j++){
                                
                                    if (j % 2 == 0){
                                        pointX = Integer.parseInt(data[j]);
                                    } else {
                                        pointY = Integer.parseInt(data[j]);
                                        nodes.add(new Point(pointX, pointY));
                                    }  
                                    repaint();
                                }

                                line = bufferedReader.readLine();
                                data = line.trim().split(",");
                                
                                for (int j = 0; j < data.length; j+=4){
                                    pointX = Integer.parseInt(data[j]);
                                    pointY = Integer.parseInt(data[j+1]);
                                    pointX2 = Integer.parseInt(data[j+2]);
                                    pointY2 = Integer.parseInt(data[j+3]);
                                    edges.add(new Line(pointX, pointY, pointX2, pointY2));
                                }
                                
                                repaint();
                                bufferedReader.close();
                                popupMenu.setVisible(false);
                                tips.setText("graph successfully loaded");


                        
                            } catch (IOException err){
                                err.printStackTrace();
                            }
 
                        }
                    });
                }
            }

            //if mouse is pressed
            @Override
            public void mousePressed(MouseEvent e) {

                if (state == 0){
                    if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
                        //the process of adding a new node
                        currentMouseX = e.getX();
                        currentMouseY = e.getY();
                        nodes.add(new Point(currentMouseX, currentMouseY));
                        repaint();

                    }

                
                } else if (state == 1){
                    int count = 0;
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        for (Point p : nodes) {
                            if (Math.abs(e.getX() - p.getX()) < 20 && Math.abs(e.getY() - p.getY()) < 20) {
                                //set the beginning points of an edge to the exact points of the node
                                xBegin = (int) p.getX();
                                yBegin = (int) p.getY();
                                drag = true;
                                tips.setText("current mouse click is on node");
                            } else {
                                tips.setText("mouse press is not on node");
                            }

                            repaint();
                        }
                       
                    }

                } else if (state == 3){
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        moveNode = 500;
                        for (int i = 0; i < nodes.size(); i++){
                            if (Math.abs(e.getX() - nodes.get(i).getX()) < 20 && Math.abs(e.getY() - nodes.get(i).getY()) < 20){
                                //this will decide if current mouse points is on a node, if so will determine which node
                                //moveNode is the index of the node we are trying to move
                                moveNode = i;
                                repaint();
                                tips.setText("current mouse click is on node");

                                break;
                            } else {
                                tips.setText("mouse press is not on node");
                            }
                        }
                    }
                }
            }

            //once mouse is released
            @Override
            public void mouseReleased(MouseEvent e) {
                if (state == 1) {
                    promptMenu.setVisible(false);
                    //set the x/y ending points
                    xEnd = e.getX();
                    yEnd = e.getY();
                    drag = false;
                    //add the new line to the arrayList
                    int count = 0;
                    for (Point p: nodes){
                        if (Math.abs(xEnd - p.getX()) < 20 && Math.abs(yEnd - p.getY()) < 20){
                            //create new line setting it to the exact point of the node
                            Line currLine1 = new Line(xBegin, yBegin, (int) p.getX(), (int) p.getY());
                            Line currLine2 = new Line((int) p.getX(), (int) p.getY(), xBegin, yBegin);


                            if (!doesEdgeExist(currLine1) && !doesEdgeExist(currLine2)) {
                                edges.add(new Line(xBegin, yBegin, (int) p.getX(), (int) p.getY()));
                                drag = false;
                                tips.setText("  ");

                                repaint();
                            } 
                            tips.setText("corresponding edges successfully drawn");

                        } else {
                            tips.setText("mouse release not on any node");

                        }
                    }


                    repaint();

                } else if (state == 3){
                    if (moveNode >= 0 && moveNode <= edges.size()) {
                        //moveNode represents the node that we are currently moving
                        for (int i = 0; i < edges.size(); i++) {
                            //iterates through all the edges
                            //check if the edge is connected to the node we are currently moving
                            if (edges.get(i).xBegin == nodes.get(moveNode).x && edges.get(i).yBegin == nodes.get(moveNode).y) {
                                //if true, means that we are updating the beginning points of the edge
                                edges.get(i).xBegin = e.getX();
                                edges.get(i).yBegin = e.getY();
                                tips.setText("corresponding nodes/edges successfully moved");

                            } else if (edges.get(i).xEnd == nodes.get(moveNode).x && edges.get(i).yEnd == nodes.get(moveNode).y) {
                                //if true, means that we are updating the ending points of the edge
                                edges.get(i).xEnd = e.getX();
                                edges.get(i).yEnd = e.getY();
                                tips.setText("corresponding nodes/edges successfully moved");

                            }
                            repaint();
                        }
                        //update the new node position
                        nodes.remove(moveNode);
                        nodes.add(new Point(e.getX(), e.getY()));


                        repaint();
                    }
                    //set moveNode to number this program will never reach
                    //ensures that popup menu will not be displayed if you are trying to move a node
                    moveNode = 500;
                    tips.setText("  ");


                }

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        }
    }






}