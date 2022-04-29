/*
Assignment 3 COMP2800
- Matthew Connell
- 110034695
 */

import java.awt.*;

public class Line {
    public int xBegin;
    public int xEnd;
    public int yBegin;
    public int yEnd;
    public Point point1, point2;
    public boolean removeEdge, removeEdgeDelete;
    public boolean moveEnd;
    public boolean moveBeg;

    public Line(int x1, int y1, int x2, int y2){
        this.xBegin = x1;
        this.xEnd = x2;
        this.yBegin = y1;
        this.yEnd = y2;
        this.point1 = new Point(xBegin, yBegin);
        this.point2 = new Point(xEnd, yEnd);
        this.removeEdge = false;
        this.removeEdgeDelete = false;
        this.moveEnd = false;
        this.moveBeg = false;
    }

    //getters and setters for all variables
    public int getxBegin() {
        return xBegin;
    }

    public int getyBegin() {
        return yBegin;
    }

    public int getxEnd() {
        return xEnd;
    }

    public int getyEnd(){
        return yEnd;
    }

    //set beginning points
    public void setxBegin(int x){
        this.xBegin = x;
    }

    public void setyBegin(int y){
        this.yBegin = y;
    }

    //checks if current edges is gonna be removed
    //if removeEdge = true, then yes, otherwise, no
    public boolean isRemoveEdge(){
        return this.removeEdge ;
    }

    public void setRemoveEdge(boolean x){
        removeEdge = x;
    }

    public boolean isRemoveEdgeDelete(){
        return this.removeEdgeDelete;
    }

    public void setRemoveEdgeDelete(boolean x){
        removeEdgeDelete = x;
    }


    public boolean moveEndPoint() {
        return moveEnd;
    }

    public boolean moveBegPoint(){
        return moveBeg;
    }

    public void setMoveBegPoint(boolean x){
        this.moveBeg = x;
    }

    public void setMoveEndPoint(boolean x){
        this.moveEnd = x;
    }

}