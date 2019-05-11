import java.awt.*;
import java.awt.event.*;

class graphicsPanel extends Canvas {

    private int[][] graph = new int[35][35];
    private String mode = "Edit";
    private int pressVal = 0;
    private int[] start = {0, 0};
    private int[] goal = {34, 34};

    graphicsPanel() {
        setSize(350, 350);
        addMouseListener(new MouseListener());
        addMouseMotionListener(new MouseMotionListener());
        fill(-1, -1);
    }

    /** Fills all tiles of the target value with the fill value */
    void fill(int target, int fill){
        for (int i = 0; i < graph.length; i ++) {
            for (int j = 0; j < graph[i].length; j ++) {
                if (graph[i][j] == target || target == -1) {
                    graph[i][j] = fill;
                }
            }
        }
    }

    /** Returns the value of the specified tile */
    int getTile(int x, int y) { return graph[x][y]; }

    /** Sets the value of the specified tile */
    void setTile(int x, int y, int val) { graph[x][y] = val; }

    /** Returns whether or not a specific tile can be found on the grid */
    boolean isTile(int target){
        for (int i = 0; i < graph.length; i ++) {
            for (int j = 0; j < graph[i].length; j ++) {
                if (graph[i][j] == target) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Returns the location of the first occurrence of a specific tile */
    int[] findTile(int target) {
        int[] pos = {0, 0};
        for (int i = 0; i < graph.length; i ++) {
            for (int j = 0; j < graph[i].length; j ++) {
                if (graph[i][j] == target) {
                    pos[0] = i;
                    pos[1] = j;
                    return pos;
                }
            }
        }
        return pos;
    }

    int[] findStart() { return findTile(-3); }
    int[] findEnd() { return findTile(-4); }

    /** Draws a pale grey and white checkerboard on all tiles containing value -1 */
    void paintCheckerboard(){
        for (int i = 0; i < 35; i ++) {
            for (int j = 0; j < 35; j ++) {
                if (graph[i][j] == -1) {
                    if ((i + j) % 2 == 0) paintTile(i, j, new Color(240, 240, 240));
                    else paintTile(i, j, Color.WHITE);
                }
                else if(graph[i][j] == -3) paintTile(i, j, Color.BLUE);
                else if (graph[i][j] == -4) paintTile(i, j, Color.RED);
            }
        }
    }

    /** Draws coloured tile at selected location based on its value */
    void paintDijkstraTile(int x, int y){
        int col_step = 5;
        int val = graph[x][y];
        if (val >= 0) {
            if (val <= 255/col_step) paintTile(x, y, new Color(0, val * col_step, 255 - val * col_step));
            else if (val <= 255/col_step*2) {
                val -= 255/col_step;
                paintTile(x, y, new Color(val * col_step, 255 - val * col_step, 0));
            }
            else paintTile(x, y, new Color(255, 0,0));
        }
    }

    /** Paints a tile, given its colour and location */
    public void paintTile(int x, int y, Color col) {
        Graphics g = this.getGraphics();
        g.setColor(col);
        g.fillRect(10 * x, 10 * y, 10, 10);
    }

    /** Toggles a tile from -1 (empty) to -2 (wall) */
    private void toggleTile(int x, int y) {
        if (graph[x][y] == -1) {
            graph[x][y] = -2;
            paintTile(x, y, Color.BLACK);
        } else if (graph[x][y] == -2) {
            graph[x][y] = -1;
            if((x+y) % 2 == 0) paintTile(x, y, new Color (240, 240, 240));
            else paintTile(x, y, Color.WHITE);
        }
    }

    @Override
    public void repaint() {}

    void setMode(String val) { mode = val; }

    void reset(){
        int i = 0;
        while(isTile(i)) {
            fill(i, -1);
            i ++;
        }
        setTile(start[0], start[1], -3);
        setTile(goal[0], goal[1], -4);
        paintCheckerboard();
    }

    public class MouseListener implements java.awt.event.MouseListener{
        public void mouseClicked(MouseEvent e) {
            int thisx = e.getX() / 10;
            int thisy = e.getY() / 10;
            if(thisx >= 0 && thisx <= 34 && thisy >= 0 && thisy <= 34) {
                switch (mode) {
                    case "Edit":
                        toggleTile(thisx, thisy);
                        break;
                    case "PlaceStart":
                        setTile(findStart()[0], findStart()[1], -1);
                        start[0] = thisx;
                        start[1] = thisy;
                        setTile(thisx, thisy, -3);
                        paintCheckerboard();
                        paintTile(thisx, thisy, Color.BLUE);
                        mode = "Edit";
                        break;
                    case "PlaceGoal":
                        setTile(findEnd()[0], findEnd()[1], -1);
                        goal[0] = thisx;
                        goal[1] = thisy;
                        setTile(thisx, thisy, -4);
                        paintCheckerboard();
                        paintTile(thisx, thisy, Color.RED);
                        mode = "Edit";
                        break;
                }
            }
        }

        public void mousePressed(MouseEvent e) {
            if (mode.equals("Edit")) pressVal = graph[e.getX()/10][e.getY()/10];
        }

        public void mouseEntered(MouseEvent e) { }
        public void mouseReleased(MouseEvent e) { }
        public void mouseExited(MouseEvent e) { }
    }

    public class MouseMotionListener implements java.awt.event.MouseMotionListener{
        public void mouseDragged(MouseEvent e) {
            if (mode.equals("Edit")) {
                int thisX = e.getX() / 10;
                int thisY = e.getY() / 10;
                if (graph[thisX][thisY] == pressVal) toggleTile(thisX, thisY);
            }
        }

        public void mouseMoved(MouseEvent e) { }
    }
}
