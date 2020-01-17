package xxx.joker.apps.wrcbomber.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcCar;
import xxx.joker.apps.wrcbomber.dl.entities.wrc.WrcRally;
import xxx.joker.apps.wrcbomber.gui.model.GuiModel;
import xxx.joker.apps.wrcbomber.stats.WinsStat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static xxx.joker.libs.core.lambda.JkStreams.toMap;

public interface StatsComputer {
    
    List<WinsStat> computeStatsByCar();
    List<WinsStat> computeStatsByCountry();
    List<WinsStat> computeStatsByPrimaryGround();

    
}
