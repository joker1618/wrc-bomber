package xxx.joker.apps.wrc.bomber.gui.snippet;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.HashMap;
import java.util.Map;

import static xxx.joker.libs.core.utils.JkStrings.strf;

public class GridPaneBuilder {

    // row, cols
    private Map<Integer, Map<Integer, HBox>> boxMap = new HashMap<>();

    public GridPaneBuilder add(int row, int col, String lbl, Object... params) {
        return add(row, col, new Label(strf(lbl, params)));
    }
    public GridPaneBuilder add(int row, int col, Object obj) {
        return add(row, col, "{}", obj);
    }
    public GridPaneBuilder add(int row, int col, Node node) {
        HBox hBox = new HBox(node);
        boxMap.putIfAbsent(row, new HashMap<>());
        boxMap.get(row).put(col, hBox);
        return this;
    }

    public GridPane createGridPane() {
        GridPane gp = new GridPane();
        createGridPane(gp);
        return gp;
    }

    public void createGridPane(GridPane gp) {
        gp.getStyleClass().addAll("customGrid");


        Integer maxRow = 1 + boxMap.keySet().stream().mapToInt(i -> i).max().orElse(-1);
        Integer maxCol = -1;
        for (Map<Integer, HBox> map : boxMap.values()) {
            Integer max = map.keySet().stream().mapToInt(i -> i).max().orElse(-1);
            if(max > maxCol) {
                maxCol = max;
            }
        }
        maxCol++;

        for(int r = 0; r < maxRow; r++) {
            for(int c = 0; c < maxCol; c++) {
                HBox hbox = boxMap.getOrDefault(r, new HashMap<>()).getOrDefault(c, new HBox(new Label("")));
                hbox.getStyleClass().addAll("row"+r, "col"+c, "cellBox");
                hbox.getStyleClass().add(r % 2 == 0 ? "oddRow" : "evenRow");
                hbox.getStyleClass().add(c % 2 == 0 ? "oddCol" : "evenCol");
                gp.add(hbox, c, r);
            }
        }
    }




}
