package xxx.joker.apps.wrcbomber.dl.enums;

public enum GameType {

    WRC_6("WRC_6"),
    WRC_7("WRC_7"),
    FIFA_19("FIFA_19"),
    FIFA_20("FIFA_20"),
    ;

    private final String label;

    GameType(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
