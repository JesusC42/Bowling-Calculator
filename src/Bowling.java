import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Bowling {
    private int[] rolls;
    private int currentRoll;
    private boolean firstRoll;
    private int currentFrame;

    //The below fields are for the interface
    private Rectangle[] squares;
    private Frame[] frames;
    private int[] bonusRolls = new int[10];
    private int lastButtonClicked = 0;
    boolean showWarningMessage = false;
    private Timer warningMessageTimer;

    public Bowling() {
        rolls = new int[21];
        currentRoll = 0;
        firstRoll = true;
        currentFrame = 1;
    }
    public int getScore() {
        if (currentFrame == 1 || currentFrame == 10)
            return getFrameScore(getCurrentFrame());
        return getFrameScore(getCurrentFrame() - 1);
    }
    public void roll(int pinsHit) {
        rolls[currentRoll++] = pinsHit;
        incrementCurrentFrameIfNeeded(pinsHit);
    }
    public int getFrameScore(int frame) {
        int index = 0;
        int score = 0;
        for (int i = 0; i < frame; i++) {
            int firstRoll = rolls[index++];
            if (firstRoll == 10) {
                score += firstRoll + rolls[index] + rolls[index + 1];
            }
            else {
                int secondRoll = rolls[index++];
                int tempScore = firstRoll + secondRoll;

                if (tempScore == 10)
                    score += tempScore + rolls[index];
                else
                    score += firstRoll + secondRoll;
            }
        }
        return score;
    }
    private void incrementCurrentFrameIfNeeded(int pinsHit) {
        if (currentFrame < 10)
            if (firstRoll) {
                if (pinsHit == 10)
                    currentFrame++;
                else
                    firstRoll = false;
            }
            else {
                firstRoll = true;
                currentFrame++;
            }
    }
    public int getCurrentFrame() {
        return currentFrame;
    }
    /*
    * All code below pertains to the interface
    */
    public void initializeInterFace() {
        //Initialize array holding each frame
        frames = new Frame[10];
        //Initialize each frame holding locations of each number for each frame
        for (int i = 0; i < 10; i++)
            frames[i] = new Frame();

        JFrame window = new JFrame("Bowling Calculator");
        JPanel drawPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(5));
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 20));//For text

                //Outer borders
                g2.drawLine(10, 175, 10, 275);
                g2.drawLine(10, 175, 970, 175);
                g2.drawLine(970, 175, 970, 275);
                g2.drawLine(10, 275, 970, 275);

                for (int i = 0; i < 10; i++) {
                    //Outer borders for each of the numbers
                    g2.drawLine(10 + 96 * i, 145, 10 + 96 * i, 175);
                    g2.drawLine(10 + 96 * i, 145, 10 + 96 * (i + 1), 145);

                    //Each line to divide into ten frames
                    g2.drawLine(10 + 96 * i, 175, 10 + 96 * i, 275);
                    //Location of each frame score
                    frames[i].scoreX = 58 + 96 * i;
                    frames[i].scoreY = 255;

                    //Upper-right inner square for each frame except last frame
                    if (i < 9) {
                        g2.drawLine(58 + 96 * i, 175, 58 + 96 * i, 210);
                        g2.drawLine(58 + 96 * i, 210, 106 + 96 * i, 210);
                        //Location of each number for each potential roll except last frame
                        frames[i].firstRollX = 29 + 96 * i;
                        frames[i].rollY = 203;
                        frames[i].secondRollX = 82 + 96 * i;
                    }
                    //Number for each frame
                    g2.drawString(String.valueOf((i + 1)), 48 * (i * 2 + 1), 167);
                }
                //Two upper-right inner squares for the tenth frame
                g2.drawLine(970, 145, 970, 175);
                g2.drawLine(906, 175, 906, 210);
                g2.drawLine(906, 210, 970, 210);
                g2.drawLine(938, 175, 938, 210);
                //Location of each number for each potential roll in the last frame
                frames[9].firstRollX = 884;
                frames[9].rollY = 203;
                frames[9].secondRollX = 916;
                frames[9].thirdRollX = 948;

                //Square buttons to choose number of pins knocked down for each roll
                squares = new Rectangle[11];
                for (int i = 0 ; i < 11; i++) {
                    g2.drawString(String.valueOf(i), 250 + i * 50, 120);
                    g2.drawLine(245 + i * 50, 100, 275 + i * 50, 100);
                    g2.drawLine(245 + i * 50, 100, 245 + i * 50, 130);
                    g2.drawLine(275 + i * 50, 100, 275 + i * 50, 130);
                    g2.drawLine(245 + i * 50, 130, 275 + i * 50, 130);
                    squares[i] = new Rectangle(245 + i * 50, 100, 30, 30);
                }
                drawFrame(g2);
            }
        };
        //Initialize timer
        warningMessageTimer = new Timer(1000, event -> {
            showWarningMessage = false;
            drawPanel.repaint();
        });
        warningMessageTimer.setRepeats(false);
        drawPanel.setBackground(Color.BLACK);
        mouseListener(drawPanel);
        window.add(drawPanel);
        window.setSize(1000, 500);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
    private void drawFrame(Graphics2D g2) {
        if(showWarningMessage && lastButtonClicked != 11)
            g2.drawString("Must click valid number of pins hit.", 350, 50);
        for (Frame thisFrame : frames) {
            if (thisFrame.frameNumber == 10) {//if tenth frame
                if (thisFrame.firstRollValue == 10) {//first roll cases
                    g2.drawString("X", thisFrame.firstRollX, thisFrame.rollY);
                    lastButtonClicked = 0;//Update lastButtonClicked
                }
                else if (thisFrame.firstRollValue != -1) {
                    g2.drawString(String.valueOf(thisFrame.firstRollValue), thisFrame.firstRollX, thisFrame.rollY);
                    lastButtonClicked = thisFrame.firstRollValue;//Update lastButtonClicked
                }
                if (thisFrame.secondRollValue == 11) {//second roll cases
                    g2.drawString("/", thisFrame.secondRollX, thisFrame.rollY);
                    lastButtonClicked = 0;//Update lastButtonClicked
                }
                else if (thisFrame.secondRollValue != -1) {
                    if (thisFrame.secondRollValue == 10) {
                        g2.drawString("X", thisFrame.secondRollX, thisFrame.rollY);
                        lastButtonClicked = 0;//Update lastButtonClicked
                    }
                    else {
                        g2.drawString(String.valueOf(thisFrame.secondRollValue), thisFrame.secondRollX, thisFrame.rollY);
                        lastButtonClicked = thisFrame.secondRollValue;//Update lastButtonClicked
                    }
                }
                if (thisFrame.thirdRollValue == 11) {//third roll cases
                    g2.drawString("/", thisFrame.thirdRollX, thisFrame.rollY);
                    lastButtonClicked = 0;//Update lastButtonClicked
                }
                else if (thisFrame.thirdRollValue != -1) {
                    if (thisFrame.thirdRollValue == 10) {
                        g2.drawString("X", thisFrame.thirdRollX, thisFrame.rollY);
                        lastButtonClicked = 0;//Update lastButtonClicked
                    }
                    else {
                        g2.drawString(String.valueOf(thisFrame.thirdRollValue), thisFrame.thirdRollX, thisFrame.rollY);
                        lastButtonClicked = thisFrame.thirdRollValue;//Update lastButtonClicked
                    }
                }
                //Decide if the tenth frame score can be shown
                if (thisFrame.secondRollX != -1) {
                    if (thisFrame.firstRollValue == 10) {
                        if (thisFrame.thirdRollValue != -1) {
                            g2.drawString(String.valueOf(thisFrame.scoreValue), thisFrame.scoreX, thisFrame.scoreY);
                            g2.drawString("Game complete.", 350, 50);
                            lastButtonClicked = 11;//Update lastButtonClicked
                        }
                    }
                    else if (thisFrame.secondRollValue == 11) {
                        if (thisFrame.thirdRollValue != -1) {
                            g2.drawString(String.valueOf(thisFrame.scoreValue), thisFrame.scoreX, thisFrame.scoreY);
                            g2.drawString("Game complete.", 350, 50);
                            lastButtonClicked = 11;//Update lastButtonClicked
                        }
                    }
                    else if (thisFrame.secondRollValue != -1) {
                        g2.drawString(String.valueOf(thisFrame.scoreValue), thisFrame.scoreX, thisFrame.scoreY);
                        g2.drawString("Game complete.", 350, 50);
                        lastButtonClicked = 11;//Update lastButtonClicked
                    }
                }
            }
            else {
                if (thisFrame.strike) {
                    g2.drawString("X", thisFrame.secondRollX, thisFrame.rollY);
                    lastButtonClicked = 0;//Update lastButtonClicked
                } else if (thisFrame.spare) {
                    g2.drawString(String.valueOf(thisFrame.firstRollValue), thisFrame.firstRollX, thisFrame.rollY);
                    g2.drawString("/", thisFrame.secondRollX, thisFrame.rollY);
                    lastButtonClicked = 0;//Update lastButtonClicked
                } else {
                    if (thisFrame.firstRollValue != -1) {
                        g2.drawString(String.valueOf(thisFrame.firstRollValue), thisFrame.firstRollX, thisFrame.rollY);
                        lastButtonClicked = thisFrame.firstRollValue;//Update lastButtonClicked
                    }
                    if (thisFrame.secondRollValue != -1) {
                        g2.drawString(String.valueOf(thisFrame.secondRollValue), thisFrame.secondRollX, thisFrame.rollY);
                        lastButtonClicked = thisFrame.secondRollValue;//Update lastButtonClicked
                    }
                }
                if (currentFrame > thisFrame.frameNumber && bonusRolls[thisFrame.frameNumber - 1] == 0) {//decide if the frame score can be shown
                    g2.drawString(String.valueOf(thisFrame.scoreValue), thisFrame.scoreX, thisFrame.scoreY);
                    lastButtonClicked = 0;//Update lastButtonClicked
                }
            }
        }
    }
    private void mouseListener(JPanel jPanel) {
        jPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int clickable = lastButtonClicked == 10 ? 11 : 11 - lastButtonClicked;//Only allow a proper number for pinsHit to be chosen
                for (int i = 0; i < 11; i++) {
                    if (squares[i].contains(e.getPoint()) && i < clickable) {
                        int frame = currentFrame - 1;
                        boolean wasFirstRoll = firstRoll;

                        roll(i);
                        addValueToFrame(frame, wasFirstRoll, i);
                        updateFrameScore(frame);
                        jPanel.repaint();
                    }
                    else if (squares[i].contains(e.getPoint()) && i >= clickable) {
                        showWarningMessage = true;
                        jPanel.repaint();
                        warningMessageTimer.restart();
                    }
                }
            }
        });
    }
    private void addValueToFrame(int frameIndex, boolean wasFirstRoll, int pinsHit) {
        Frame frame = frames[frameIndex];

        if (frameIndex == 9) {//special case for if the final frame
            if (frame.firstRollValue == -1) {//if first roll was just rolled
                frame.firstRollValue = pinsHit;
            }
            else if (frame.secondRollValue == -1) {//if second roll was just rolled
                if (frame.firstRollValue != 10 && pinsHit + frame.firstRollValue == 10)
                    frame.secondRollValue = 11;//special value to indicate spare in the tenth frame with the first two rolls
                else
                    frame.secondRollValue = pinsHit;
            }
            else {
                if (frame.secondRollValue != 10 && pinsHit + frame.secondRollValue == 10)
                    frame.thirdRollValue = 11;//special value to indicate spare in the tenth frame with the second and this roll
                else
                    frame.thirdRollValue = pinsHit;
            }
        }
        else {
            if (wasFirstRoll) {
                if (pinsHit == 10)
                    frame.strike = true;
                else
                    frame.firstRollValue = pinsHit;
            } else {
                if (frame.firstRollValue + pinsHit == 10)
                    frame.spare = true;
                else
                    frame.secondRollValue = pinsHit;
            }
            if (frame.strike)
                bonusRolls[frameIndex] = 2;
            if (frame.spare)
                bonusRolls[frameIndex] = 1;
        }
    }
    private void updateFrameScore(int currentRollIndex) {
        for (int i = 0; i < bonusRolls.length; i++) {
            if (i != currentRollIndex && bonusRolls[i] > 0)
                bonusRolls[i]--;
            if (bonusRolls[i] == 0)
                frames[i].scoreValue = getFrameScore(i + 1);
        }
    }
    private class Frame {
        static int frameCounter;
        int frameNumber;
        int firstRollX;
        int rollY;
        int secondRollX;
        int thirdRollX;
        int scoreX;
        int scoreY;
        int firstRollValue = -1;
        int secondRollValue = -1;
        int thirdRollValue = -1;
        int scoreValue;
        boolean strike = false;
        boolean spare = false;

        Frame() {
            frameNumber = ++frameCounter;
        }
    }
}
