package AppMananger;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Chess extends JDialog {

    private Image chessFigures;
    private JPanel headPanel;
    private JPanel deskPanel;
    private JButton restartButton;
    private JButton prevButton;
    private Color color;
    private Color prevColor;
    private boolean isMoving;
    private int currentPlayer;
    private JButton buttons[][];
    private final String filename = "C:\\Users\\enazy\\IdeaProjects\\ChessGame\\src\\resources\\Figures.png";

    private int[][] map =
            {
                    {25, 24, 23, 22, 21, 23, 24, 25},
                    {26, 26, 26, 26, 26, 26, 26, 26},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {16, 16, 16, 16, 16, 16, 16, 16},
                    {15, 14, 13, 12, 11, 13, 14, 15}
            };

    public Chess() {
        setTitle("ChessGame");
        setContentPane(headPanel);
        setSize(800, 800);
        isMoving = false;
        currentPlayer = 1;
        buttons = new JButton[8][8];

        init(this.getContentPane());
    }

    private void init(Container container) {

        container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        container.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        deskPanel = new JPanel(new GridLayout(8, 8, 0, 0));
        constraints.weightx = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        container.add(deskPanel, constraints);

        restartButton = new JButton("Restart");
        constraints.weightx = 1.0;
        constraints.gridx = 2;
        constraints.gridy = 0;
        container.add(restartButton, constraints);

        restartButton.addActionListener(e -> {
            map = new int[][]{
                    {25, 24, 23, 22, 21, 23, 24, 25},
                    {26, 26, 26, 26, 26, 26, 26, 26},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {16, 16, 16, 16, 16, 16, 16, 16},
                    {15, 14, 13, 12, 11, 13, 14, 15}
            };
            headPanel.removeAll();

            isMoving = false;
            currentPlayer = 1;

            init(this.headPanel);
            headPanel.revalidate();
            headPanel.repaint();
        });

        createMap();
    }

    private void createMap() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton button = new JButton();
                button.setBounds(j * 100, i * 100, 100, 100);
                button.setBorder(new EmptyBorder(0, 0, 0, 0));
                deskPanel.add(button);

                if ((i + j) % 2 == 0)
                    button.setBackground(Color.WHITE);
                else
                    button.setBackground(Color.LIGHT_GRAY);

                switch (map[i][j] / 10) {
                    case 1:
                        try {
                            chessFigures = ImageIO.read(new File(filename));

                            //filter the input image to get images of figures
                            ImageFilter filter = new CropImageFilter(200 * (map[i][j] % 10 - 1), 0, 200, 200);
                            chessFigures = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(chessFigures.getSource(), filter));

                            //place the modified image in the button
                            button.setIcon(new ImageIcon(chessFigures.getScaledInstance(80, 80, Image.SCALE_SMOOTH)));

                        } catch (IOException ioEx) {
                            ioEx.printStackTrace();
                        }
                        break;
                    case 2:
                        try {
                            chessFigures = ImageIO.read(new File(filename));

                            ImageFilter filter = new CropImageFilter(200 * (map[i][j] % 10 - 1), 200, 200, 200);
                            chessFigures = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(chessFigures.getSource(), filter));

                            button.setIcon(new ImageIcon(chessFigures.getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
                        } catch (IOException ioEx) {
                            ioEx.printStackTrace();
                        }
                        break;
                }

                button.addActionListener(e -> {
                    color = button.getBackground();

                    if (prevButton == null) {
                        if (color == Color.LIGHT_GRAY) {
                            prevColor = Color.LIGHT_GRAY;
                        }
                        if (color == Color.WHITE) {
                            prevColor = Color.WHITE;
                        }

                    } else if (!prevColor.equals(color)) {
                        if (prevColor.equals(Color.LIGHT_GRAY)) {
                            prevButton.setBackground(Color.LIGHT_GRAY);
                        }
                        prevButton.setBackground(Color.WHITE);
                    }

                    if (map[button.getLocation().y / 80][button.getLocation().x / 80] != 0 &&
                            map[button.getLocation().y / 80][button.getLocation().x / 80] / 10 == currentPlayer) {

                        closeSteps();
                        button.setBackground(new Color(103, 246, 186));
                        //deactivateAllButtons();
                        button.setEnabled(true);
                        showMovement(button.getLocation().y / 80, button.getLocation().x / 80, map[button.getLocation().y / 80][button.getLocation().x / 80]);

                        if(isMoving) {
                            closeSteps();
                            activateAllButtons();
                            isMoving = false;
                        }
                        else isMoving = true;

                    } else {
                        if (isMoving) {
                            int temp = map[button.getLocation().y / 80][button.getLocation().x / 80];
                            map[button.getLocation().y / 80][button.getLocation().x / 80] = map[prevButton.getLocation().y / 80][prevButton.getLocation().x / 80];
                            map[prevButton.getLocation().y / 80][prevButton.getLocation().x / 80] = temp;
                            button.setIcon(prevButton.getIcon());
                            prevButton.setIcon(null);
                            isMoving = false;
                            closeSteps();
                            activateAllButtons();
                            switchTurn();
                        }
                    }

                    if (prevButton != null) {
                        prevButton.setBackground(prevColor);
                        prevColor = color;
                    }
                    prevButton = button;
                });

                buttons[i][j] = button;
            }
        }
    }

    private void switchTurn() {
        currentPlayer = currentPlayer == 1 ? 2 : 1;
    }

    private void deactivateAllButtons() {
        Arrays.stream(buttons).forEach(buttons -> setEnabled(false));
    }

    private void activateAllButtons() {
        Arrays.stream(buttons).forEach(buttons -> setEnabled(true));
    }

    private boolean insideDesk(int i, int j) {
        return i < 8 && j < 8 && i >= 0 && j >= 0;
    }

    private void showMovement(int iCurrFigure, int jCurrFigure, int currFigure) {
        int direction = currentPlayer == 1 ? 1 : -1;
        switch (currFigure % 10) {
            case 6:
                if (insideDesk(iCurrFigure + direction, jCurrFigure)) {
                    if (map[iCurrFigure + direction][jCurrFigure] == 0) {
                        buttons[iCurrFigure + direction][jCurrFigure].setBackground(Color.CYAN);
                        buttons[iCurrFigure + direction][jCurrFigure].setEnabled(true);
                    }
                }
                if (insideDesk(iCurrFigure + direction, jCurrFigure + 1)) {
                    if (map[iCurrFigure + direction][jCurrFigure + 1] != 0 && map[iCurrFigure + direction][jCurrFigure + 1] / 10 != currentPlayer) {
                        buttons[iCurrFigure + direction][jCurrFigure + 1].setBackground(Color.CYAN);
                        buttons[jCurrFigure + direction][jCurrFigure + 1].setEnabled(true);
                    }
                }
                if (insideDesk(iCurrFigure + direction, jCurrFigure - 1)) {
                    if (map[iCurrFigure + direction][jCurrFigure - 1] != 0 && map[iCurrFigure + direction][jCurrFigure - 1] / 10 != currentPlayer) {
                        buttons[iCurrFigure + direction][jCurrFigure - 1].setBackground(Color.CYAN);
                        buttons[jCurrFigure + direction][jCurrFigure - 1].setEnabled(true);
                    }
                }
                break;
            case 5:
                showVerticalHorizontalSteps(iCurrFigure, jCurrFigure, false);
                break;
            case 4:
                showHorseSteps(iCurrFigure, jCurrFigure);
                break;
            case 3:
                showDiagonalSteps(iCurrFigure, jCurrFigure, false);
                break;
            case 2:
                showVerticalHorizontalSteps(iCurrFigure, jCurrFigure, false);
                showDiagonalSteps(iCurrFigure, jCurrFigure, false);
                break;
            case 1:
                showVerticalHorizontalSteps(iCurrFigure, jCurrFigure, true);
                showDiagonalSteps(iCurrFigure, jCurrFigure, true);
                break;
        }
    }

    private void showVerticalHorizontalSteps(int iCurrFigure, int jCurrFigure, boolean isOneStep) {
        for (int i = iCurrFigure + 1; i < 8; i++) {
            if (insideDesk(i, jCurrFigure)) {
                if (!determinePath(i, jCurrFigure)) break;
            }
            if (isOneStep) break;
        }

        for (int i = iCurrFigure - 1; i >= 0; i--) {
            if (insideDesk(i, jCurrFigure)) {
                if (!determinePath(i, jCurrFigure)) break;
            }
            if (isOneStep) break;
        }

        for (int j = jCurrFigure + 1; j < 8; j++) {
            if (insideDesk(iCurrFigure, j)) {
                if (!determinePath(iCurrFigure, j)) break;
            }
            if (isOneStep) break;
        }

        for (int j = jCurrFigure - 1; j >= 0; j--) {
            if (insideDesk(iCurrFigure, j)) {
                if (!determinePath(iCurrFigure, j)) break;
            }
            if (isOneStep) break;
        }
    }

    private void showDiagonalSteps(int iCurrFigure, int jCurrFigure, boolean isOneStep) {
        int j = jCurrFigure + 1;
        for (int i = iCurrFigure - 1; i >= 0; i--) {
            if (insideDesk(i, j)) {
                if (!determinePath(i, j)) break;
            }
            if (j < 7) j++;
            else break;

            if (isOneStep) break;
        }

        j = jCurrFigure - 1;
        for (int i = iCurrFigure - 1; i >= 0; i--) {
            if (insideDesk(i, j)) {
                if (!determinePath(i, j)) break;
            }
            if (j > 0) j--;
            else break;

            if (isOneStep) break;
        }

        j = jCurrFigure - 1;
        for (int i = iCurrFigure - 1; i < 8; i++) {
            if (insideDesk(i, j)) {
                if (!determinePath(i, j)) break;
            }
            if (j > 0) j--;
            else break;

            if (isOneStep) break;
        }

        j = jCurrFigure + 1;
        for (int i = iCurrFigure + 1; i < 8; i++) {
            if (insideDesk(i, j)) {
                if (!determinePath(i, j)) break;
            }
            if (j < 7) j++;
            else break;

            if (isOneStep) break;
        }
    }

    private void showHorseSteps(int iCurrFigure, int jCurrFigure) {
        if (insideDesk(iCurrFigure - 2, jCurrFigure + 1)) {
            determinePath(iCurrFigure - 2, jCurrFigure + 1);
        }
        if (insideDesk(iCurrFigure - 2, jCurrFigure - 1)) {
            determinePath(iCurrFigure - 2, jCurrFigure - 1);
        }
        if (insideDesk(iCurrFigure + 2, jCurrFigure + 1)) {
            determinePath(iCurrFigure + 2, jCurrFigure + 1);
        }
        if (insideDesk(iCurrFigure + 2, jCurrFigure - 1)) {
            determinePath(iCurrFigure + 2, jCurrFigure - 1);
        }
        if (insideDesk(iCurrFigure - 1, jCurrFigure + 2)) {
            determinePath(iCurrFigure - 1, jCurrFigure + 2);
        }
        if (insideDesk(iCurrFigure + 1, jCurrFigure + 2)) {
            determinePath(iCurrFigure + 1, jCurrFigure + 2);
        }
        if (insideDesk(iCurrFigure - 1, jCurrFigure - 2)) {
            determinePath(iCurrFigure - 1, jCurrFigure - 2);
        }
        if (insideDesk(iCurrFigure + 1, jCurrFigure - 2)) {
            determinePath(iCurrFigure + 1, jCurrFigure - 2);
        }
    }

    private boolean determinePath(int i, int j) {
        if (map[i][j] == 0) {
            buttons[i][j].setBackground(Color.CYAN);
            buttons[i][j].setEnabled(true);
        } else {
            if (map[i][j] / 10 != currentPlayer) {
                buttons[i][j].setBackground(Color.CYAN);
                buttons[i][j].setEnabled(true);
            }
            return false;
        }
        return true;
    }

    private void closeSteps() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0)
                    buttons[i][j].setBackground(Color.WHITE);
                else
                    buttons[i][j].setBackground(Color.LIGHT_GRAY);
            }
        }
    }
}