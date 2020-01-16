package xxx.joker.apps.wrcbomber.gui.pane.wrc;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcSeason;
import xxx.joker.apps.wrcbomber.gui.model.GuiModel;
import xxx.joker.libs.core.util.JkStrings;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static xxx.joker.libs.core.lambda.JkStreams.*;
import static xxx.joker.libs.core.util.JkStrings.strf;
import static xxx.joker.libs.javafx.util.JfxControls.createHBox;
import static xxx.joker.libs.javafx.util.JfxControls.createVBox;

public class HistorySeasonPane extends BorderPane {

    private final GuiModel guiModel;

    private final List<WrcSeason> seasons;
    private final Button btnExpandAll = new Button("EXPAND ALL");

    public HistorySeasonPane(GuiModel guiModel) {
        this.guiModel = guiModel;
        this.seasons = new ArrayList<>();
        getStyleClass().addAll("childPane");
        guiModel.addRefreshAction(this::initPane);
    }

    private void initPane() {
        HBox lblBox = createHBox("captionBox", new Label("HISTORY SEASONS"));
        setTop(createHBox("center-left spacing40", lblBox, btnExpandAll));

        List<WrcSeason> closedSeasons = guiModel.getWrcClosedSeasons();
        if(closedSeasons.size() != seasons.size()) {
            seasons.clear();
            seasons.addAll(closedSeasons);
            setCenter(createHistorySeasonsPane());
        }
    }

    private Pane createHistorySeasonsPane() {
        VBox vbox = createVBox("historyBox");
        List<TitledPane> tpList = new ArrayList<>();

        BooleanBinding bb = Bindings.createBooleanBinding(
                () -> {
                    List<Boolean> list = mapUniq(tpList, TitledPane::isExpanded);
                    return !list.contains(false);
                }
        );

        for (int i = 0; i < seasons.size(); i++) {
            WrcSeason season = seasons.get(i);
            String title = strf("%2s.  %s  -  %s", (i + 1), season.getEndTm().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), season.getWinner().name());
            LeagueGridPane gpLeague = new LeagueGridPane(guiModel, season);
            TitledPane tp = new TitledPane(title, gpLeague);
            tp.setExpanded(false);
            bb = bb.and(tp.expandedProperty());
            tpList.add(tp);
            vbox.getChildren().add(tp);
        }

        BooleanBinding bbFinal = bb;
        bbFinal.addListener((obs,o,n) -> btnExpandAll.setText(n ? "CLOSE ALL" : "EXPAND ALL"));
        btnExpandAll.setOnAction(e -> {
            boolean allExpanded = bbFinal.getValue();
            tpList.forEach(tp -> tp.setExpanded(!allExpanded));
        });

        return vbox;
    }

}
