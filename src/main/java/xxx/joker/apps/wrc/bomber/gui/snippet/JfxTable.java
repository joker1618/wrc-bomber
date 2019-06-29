package xxx.joker.apps.wrc.bomber.gui.snippet;

import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class JfxTable<T> extends TableView<T> {

    private static final String CSS_FILEPATH = "/css/snippet/JfxTable.css";
    private static final double EXTRA_COL_WIDTH = 30d;

    private List<JfxTableCol<T, ?>> columns;

    @SafeVarargs
    public JfxTable(JfxTableCol<T, ?>... cols) {
        setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        this.columns = new ArrayList<>();
        add(cols);
        getStylesheets().add(getClass().getResource(CSS_FILEPATH).toExternalForm());
    }

    public void add(JfxTableCol... cols) {
        Arrays.stream(cols).forEach(c -> {
            columns.add(c);
            getColumns().add(c);
        });
    }

    public void update(Collection<T> items) {
        getItems().setAll(items);
        resizeWidth(true);
    }

    public void resizeWidth(boolean reserveScrollSpace) {
        double tablePrefWidth = 2d + (reserveScrollSpace ? 22d : 0d);

        //Set the right policy
        for (JfxTableCol<T, ?> col : columns) {
            if(col.isVisible()) {
                double max;
                if (col.getFixedWidth() != -1) {
                    max = col.getFixedWidth();
                } else {
                    //Minimal width = columnheader
                    Text t = new Text(col.getText());
                    max = t.getLayoutBounds().getWidth();
                    for (int i = 0; i < getItems().size(); i++) {
                        //cell must not be empty
                        if (col.getCellData(i) != null) {
                            t = new Text(col.formatCellData(i));
                            double calcwidth = t.getLayoutBounds().getWidth();
                            if (calcwidth > max) {
                                max = calcwidth;
                            }
                        }
                    }
                }
                // add extra space
                double wcol = max + EXTRA_COL_WIDTH;
                col.setPrefWidth(wcol);
                tablePrefWidth += wcol;
            }
        }

        setPrefWidth(tablePrefWidth);

    }

    public void setFixedWidths(double... colWidths) {
        //Set the right policy
        for(int colNum = 0; colNum < columns.size(); colNum++) {
            JfxTableCol<T, ?> col = columns.get(colNum);
            col.setFixedWidth(colWidths[colNum % colWidths.length]);
        }
    }

    public List<JfxTableCol<T, ?>> getCols() {
        return columns;
    }

}