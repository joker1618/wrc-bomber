package xxx.joker.apps.wrcbomber.dl.enums;

public enum Player {

    FEDE,
    BOMBER
    ;

    public static Player getByName(String name) {
        for (Player player : values()) {
            if(player.name().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }
}
