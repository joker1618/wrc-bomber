package xxx.joker.apps.wrcbomber.gui.pane.wrc;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcCountry;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcRally;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcSeason;
import xxx.joker.apps.wrcbomber.gui.model.GuiModel;
import xxx.joker.apps.wrcbomber.stats.SingleStat;
import xxx.joker.apps.wrcbomber.stats.StatsUtil;
import xxx.joker.libs.core.lambda.JkStreams;
import xxx.joker.libs.javafx.builder.JfxGridPaneBuilder;
import xxx.joker.libs.javafx.util.JfxControls;

import java.util.Comparator;
import java.util.List;

import static xxx.joker.apps.wrcbomber.dl.enums.Player.BOMBER;
import static xxx.joker.apps.wrcbomber.dl.enums.Player.FEDE;

public class LeagueGridPane extends GridPane {

    private final GuiModel guiModel;

    public LeagueGridPane(GuiModel guiModel, WrcSeason season) {
        this.guiModel = guiModel;
        getStyleClass().add("gpResults");
        getStylesheets().add(getClass().getResource("/css/wrc/leagueGridPane.css").toExternalForm());
        initPane(season);
    }

    private void initPane(WrcSeason season) {
        JfxGridPaneBuilder cgrid = new JfxGridPaneBuilder();
        cgrid.add(1, 0, FEDE.name());
        cgrid.add(2, 0, BOMBER.name());

        List<WrcCountry> allCountries = JkStreams.sorted(guiModel.getWrcCountries(), Comparator.comparing(WrcCountry::getNumInSeason));

        int colNum = 1;
        for (WrcRally rally : season.getRallies()) {
            ImageView ivFlag = createFlagImageView(rally.getCountry());
            SingleStat stat = StatsUtil.countMatchWins(rally.getMatches());
            cgrid.add(0, colNum, ivFlag);
            cgrid.add(1, colNum, ""+stat.getNumFede());
            cgrid.add(2, colNum, ""+stat.getNumBomber());

            colNum++;

            allCountries.remove(rally.getCountry());
        }

        for (WrcCountry country : allCountries) {
            ImageView ivFlag = createFlagImageView(country);
            cgrid.add(0, colNum, ivFlag);
            colNum++;
        }

        SingleStat statRallies = StatsUtil.countRallyWins(season.getRallies());
        cgrid.add(0, colNum, "N.R.");
        cgrid.add(1, colNum, ""+statRallies.getNumFede());
        cgrid.add(2, colNum, ""+statRallies.getNumBomber());
        colNum++;

        SingleStat statMatches = StatsUtil.countMatchWins(JkStreams.flatMap(season.getRallies(), WrcRally::getMatches));
        cgrid.add(0, colNum, "N.S.");
        cgrid.add(1, colNum, ""+statMatches.getNumFede());
        cgrid.add(2, colNum, ""+statMatches.getNumBomber());
        colNum++;

        cgrid.createGridPane(this);
    }

    private ImageView createFlagImageView(WrcCountry country) {
        return JfxControls.createImageView(guiModel.getFlag(country), 40d, 25d, false);
    }
}
