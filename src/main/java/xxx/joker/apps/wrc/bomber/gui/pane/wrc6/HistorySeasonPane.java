package xxx.joker.apps.wrc.bomber.gui.pane.wrc6;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import xxx.joker.apps.wrc.bomber.dl.WrcRepo;
import xxx.joker.apps.wrc.bomber.dl.WrcRepoImpl;
import xxx.joker.apps.wrc.bomber.dl.entities.WrcSeason;

import java.util.List;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class HistorySeasonPane extends BorderPane {

    private final WrcRepo repo = WrcRepoImpl.getInstance();

    private List<WrcSeason> seasons;

    public HistorySeasonPane() {
        getStyleClass().addAll("childPane");

        HBox topBox = new HBox(new Label("HISTORY SEASONS"));
        topBox.getStyleClass().add("captionBox");
        setTop(topBox);

        setCenter(createSeasonsView());

        repo.registerActionChangeStats(r -> {
            if(r.getClosedSeasons().size() != seasons.size()) {
                setCenter(createSeasonsView());
            }
        });
    }

    private Pane createSeasonsView() {
        VBox mainBox = new VBox();
        mainBox.getStyleClass().addAll("bgRed", "spacing20");

        seasons = repo.getClosedSeasons();
        for (int i = 0; i < seasons.size(); i++) {
            WrcSeason season = seasons.get(i);
            VBox vbox = new VBox();
            Label lbl = new Label(strf("%2s.  %s  -  %s", (i + 1), season.getWinner(), season.getCreationTm().format("dd/MM/yyyy")));
            Button btnShow = new Button("Show");
            SimpleObjectProperty<LeagueGridPane> lres = new SimpleObjectProperty<>();
            btnShow.setOnAction(e -> {
                String text = btnShow.getText();
                if("Show".equals(text)) {
                    btnShow.setText("Hide");
                    if(lres.get() == null)  {
                        lres.set(new LeagueGridPane(season));
                    }
                    vbox.getChildren().add(lres.getValue());

                } else {
                    btnShow.setText("Show");
                    vbox.getChildren().remove(lres.getValue());
                }
            });
            HBox hBox = new HBox(lbl, btnShow);
            vbox.getChildren().add(hBox);
            vbox.getStyleClass().addAll("bgCyan", "spacing10");
            mainBox.getChildren().add(vbox);
        }

        return mainBox;
    }

}
