import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class Search {

    private Frame mainFrame;
    private Panel controlPanel;
    private graphicsPanel graphicsPanel;

    void prepareGUI() {
        mainFrame = new Frame();
        mainFrame.setSize(500, 450);
        mainFrame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        ButtonClickListener ButtonListener = new ButtonClickListener();

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        controlPanel = new Panel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.setSize(mainFrame.getWidth(), 50);

        Button runButton = new Button("Run");
        runButton.setActionCommand("Run");
        runButton.addActionListener(ButtonListener);
        controlPanel.add(runButton);

        Button clearButton = new Button("Clear");
        clearButton.setActionCommand("Clear");
        clearButton.addActionListener(ButtonListener);
        controlPanel.add(clearButton);

        Button placeStartButton = new Button("Starting Position");
        placeStartButton.setActionCommand("PlaceStart");
        placeStartButton.addActionListener(ButtonListener);
        controlPanel.add(placeStartButton);

        Button placeGoalButton = new Button("Goal Position");
        placeGoalButton.setActionCommand("PlaceGoal");
        placeGoalButton.addActionListener(ButtonListener);
        controlPanel.add(placeGoalButton);

        mainFrame.add(controlPanel, c);

        graphicsPanel = new graphicsPanel();
        c.gridx = 0;
        mainFrame.add(graphicsPanel, c);

        mainFrame.setVisible(true);

        try {
            Thread.sleep(100);
        } catch (Exception e) {
            System.exit(1);
        }

        graphicsPanel.fill(-1, -1);
        graphicsPanel.paintCheckerboard();
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            switch (command) {
                case "Run":
                    new Dijkstra(graphicsPanel);
                    break;
                case "Clear":
                    graphicsPanel.fill(-1, -1);
                    graphicsPanel.paintCheckerboard();
                    break;
                case "PlaceStart":
                    graphicsPanel.setMode("PlaceStart");
                    break;
                case "PlaceGoal":
                    graphicsPanel.setMode("PlaceGoal");
                    break;
            }
        }
    }
}
