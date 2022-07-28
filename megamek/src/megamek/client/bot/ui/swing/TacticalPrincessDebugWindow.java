package megamek.client.bot.ui.swing;

import megamek.client.bot.princess.Princess;
import megamek.client.bot.tacticalPrincess.tasks.state.sector.Sector;
import megamek.common.Coords;
import megamek.common.event.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TacticalPrincessDebugWindow extends JFrame implements GameListener  {
    Princess owner;
    private static final int SECTOR_MAP_SIZE = 50;
    private static final Color ailled = Color.BLUE;
    private static final Color empty = Color.GREEN;
    private static final Color contested = Color.YELLOW;
    private static final  Color enemy = Color.RED;
    int sizeX;
    int sizeY;
    BufferedImage image;
    public TacticalPrincessDebugWindow(Princess owner) {

        this.owner = owner;
        sizeX = (int) Math.ceil((float)owner.getBoard().getWidth() / Sector.SECTOR_SIZE);
        sizeY = (int) Math.ceil((float)owner.getBoard().getHeight() / Sector.SECTOR_SIZE);
        this.setSize( sizeX  * SECTOR_MAP_SIZE , sizeY * SECTOR_MAP_SIZE);
        this.setVisible(true);
        this.setAlwaysOnTop(true);
        image = new BufferedImage(sizeX  * SECTOR_MAP_SIZE , sizeY * SECTOR_MAP_SIZE ,BufferedImage.TYPE_INT_ARGB);
    }

    public void redraw() {
        Graphics g = image.createGraphics();
        for(int i = 0; i < sizeX; i++) {
            for(int j = 0; j < sizeY; j++) {
                Sector sector = owner.getAiOrg().aiSectors.get(new Coords(i * Sector.SECTOR_SIZE, j * Sector.SECTOR_SIZE));
                int offsetX = i * SECTOR_MAP_SIZE;
                int offsetY = j * SECTOR_MAP_SIZE;
                g.setColor(getColorForSector(sector));
                g.fillRect(offsetX, offsetY, SECTOR_MAP_SIZE, SECTOR_MAP_SIZE);
            }
        }
        File file = new File("graphicTest.jpg");
        try {
            ImageIO.write(image,"jpg",file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.repaint();
    }

    private Color getColorForSector(Sector sector) {
        if(sector != null) {
            switch (sector.getSectorControl()) {
                case FRIENDLY:
                    return ailled;
                case ENEMY:
                    return enemy;
                case CONTESTED:
                    return contested;
                case EMPTY:
                    return empty;
            }
        }
        return Color.BLACK;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image,10,10,this);
    }

    @Override
    public void gamePlayerConnected(GamePlayerConnectedEvent e) {

    }

    @Override
    public void gamePlayerDisconnected(GamePlayerDisconnectedEvent e) {

    }

    @Override
    public void gamePlayerChange(GamePlayerChangeEvent e) {

    }

    @Override
    public void gamePlayerChat(GamePlayerChatEvent e) {

    }

    @Override
    public void gameTurnChange(GameTurnChangeEvent e) {

    }

    @Override
    public void gamePhaseChange(GamePhaseChangeEvent e) {


    }

    @Override
    public void gameReport(GameReportEvent e) {

    }

    @Override
    public void gameEnd(GameEndEvent e) {

    }

    @Override
    public void gameBoardNew(GameBoardNewEvent e) {

    }

    @Override
    public void gameBoardChanged(GameBoardChangeEvent e) {

    }

    @Override
    public void gameSettingsChange(GameSettingsChangeEvent e) {

    }

    @Override
    public void gameMapQuery(GameMapQueryEvent e) {

    }

    @Override
    public void gameEntityNew(GameEntityNewEvent e) {

    }

    @Override
    public void gameEntityNewOffboard(GameEntityNewOffboardEvent e) {

    }

    @Override
    public void gameEntityRemove(GameEntityRemoveEvent e) {

    }

    @Override
    public void gameEntityChange(GameEntityChangeEvent e) {

    }

    @Override
    public void gameNewAction(GameNewActionEvent e) {

    }

    @Override
    public void gameClientFeedbackRequest(GameCFREvent e) {

    }

    @Override
    public void gameVictory(GameVictoryEvent e) {

    }
}
