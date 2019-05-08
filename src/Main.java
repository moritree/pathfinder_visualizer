import java.awt.*;
import java.awt.event.*;

//public class Main {
//    public static void main(String[] args) {
//        Search test = new Search();
//        test.runTest();
//    }
//}

public class Search {

    private Frame mainFrame;
    private Panel controlPanel;
    private graphicsPanel graphicsPanel;
    private int[][] graph = new int[35][35];

    class graphicsPanel extends Canvas {

        public graphicsPanel() {
            setSize(350, 350);
        }

        public void paintCheckerboard(){
            // draw pale grey and white checkerboard
            Graphics g = this.getGraphics();
            for (int i = 0; i < 35; i ++) {
                for (int j = 0; j < 35; j ++) {
                    if((i+j) % 2 == 0) g.setColor(new Color (240, 240, 240));
                    else g.setColor(Color.WHITE);
                    g.fillRect(10*i, 10*j, 10, 10);
                }
            }
        }

        @Override
        public void paint(Graphics g) {

        }
    }

    void runTest() {
        this.prepareGUI();
    }

    private void prepareGUI() {
        mainFrame = new Frame();
        mainFrame.setSize(400, 450);
        mainFrame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });

        controlPanel = new Panel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.setSize(mainFrame.getWidth(), 50);
        Button runButton = new Button("Run");
        runButton.setActionCommand("Run");
        runButton.addActionListener(new ButtonClickListener());
        controlPanel.add(runButton);
        mainFrame.add(controlPanel, c);

        graphicsPanel = new graphicsPanel();
        c.gridx = 0;
        mainFrame.add(graphicsPanel, c);


        mainFrame.setVisible(true);
    }

    void runVisualization() {
        graphicsPanel.paintCheckerboard();
    }

    private class ButtonClickListener implements ActionListener{
        public void actionPerformed (ActionEvent e) {
            String command = e.getActionCommand();
            if(command.equals("Run")) {
                runVisualization();
            }
        }
    }

    public static void main(String[] args) {
        Search test = new Search();
        test.runTest();
    }
}