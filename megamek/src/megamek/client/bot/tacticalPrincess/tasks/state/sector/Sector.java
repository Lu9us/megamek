package megamek.client.bot.tacticalPrincess.tasks.state.sector;

public class Sector {
    public static final int SECTOR_SIZE = 5;

    public Sector(int startCoordX, int startCoordY, int endCoordX, int endCoordY) {
        this.startCoordX = startCoordX;
        this.startCoordY = startCoordY;
        this.endCoordX = endCoordX;
        this.endCoordY = endCoordY;
    }

    public final int startCoordX;
    public final int startCoordY;
    public final int endCoordX;
    public final int endCoordY;

    public int friendlyBVPresent;
    public int enemyBVPresent;

    public SectorControl getSectorControl() {
        if (friendlyBVPresent == 0 && enemyBVPresent == 0) {
            return SectorControl.EMPTY;
        } else if (friendlyBVPresent > 0 && enemyBVPresent > 0) {
            return SectorControl.CONTESTED;
        } else if (friendlyBVPresent > 0 && enemyBVPresent == 0) {
            return SectorControl.FRIENDLY;
        } else if(friendlyBVPresent == 0 && enemyBVPresent > 0){
            return SectorControl.ENEMY;
        }
        return SectorControl.EMPTY;
    }

    @Override
    public String toString() {
        return "Sector{" + "startCoordX=" + startCoordX + ", startCoordY=" + startCoordY + ", endCoordX=" + endCoordX + ", endCoordY=" + endCoordY + ", friendlyBVPresent=" + friendlyBVPresent + ", enemyBVPresent=" + enemyBVPresent + ", controlled by: " + getSectorControl() + '}';
    }
}
