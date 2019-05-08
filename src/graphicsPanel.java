import java.awt.*;
import java.awt.event.*;

class graphicsPanel extends Canvas {

    private int[][]graph = new int[35][35];
    private String mode = "Edit";
    private int pressVal = 0;

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

    /** Returns the location of the first occurence of a specific tile */
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

    int[] findStart() { return findTile(2); }
    int[] findEnd() { return findTile(3); }

    /** Draws a pale grey and white checkerboard on all tiles containing value -1 */
    void paintCheckerboard(){
        for (int i = 0; i < 35; i ++) {
            for (int j = 0; j < 35; j ++) {
                if (graph[i][j] == -1) {
                    if ((i + j) % 2 == 0) paintTile(i, j, new Color(240, 240, 240));
                    else paintTile(i, j, Color.WHITE);
                }
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

    public void setMode(String val) { mode = val; }

    public class MouseListener implements java.awt.event.MouseListener{
        public void mouseClicked(MouseEvent e) {
            switch(mode){
                case "Edit":
                    toggleTile(e.getX() / 10, e.getY() / 10);
                    break;
                case "PlaceStart":
                    setTile(findStart()[0], findStart()[1], -1);
                    setTile(e.getX()/10, e.getY()/10, 2);
                    paintCheckerboard();
                    paintTile(e.getX()/10, e.getY()/10, Color.BLUE);
                    mode = "Edit";
                    break;
                case "PlaceGoal":
                    setTile(findEnd()[0], findEnd()[1], -1);
                    setTile(e.getX()/10, e.getY()/10, 3);
                    paintCheckerboard();
                    paintTile(e.getX()/10, e.getY()/10, Color.RED);
                    mode = "Edit";
                    break;
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
