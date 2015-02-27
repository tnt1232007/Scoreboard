package com.tnt.scoreboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.models.Player;
import com.tnt.scoreboard.models.Score;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class ChartFragment extends Fragment {

    private static Game mGame;
    private LineChartView mChart;

    public static ChartFragment getInstance(Game game) {
        mGame = game;
        return new ChartFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        mChart = (LineChartView) view.findViewById(R.id.chart);
        setChartData();
        return view;
    }

    private void setChartData() {
        List<Line> lines = new ArrayList<>();
        for (Player p : mGame.getPlayerList()) {
            List<PointValue> values = new ArrayList<>();
            for (Score s : p.getScoreList())
                values.add(new PointValue(s.getRoundNumber(), s.getCurrentScore()));
            values.add(new PointValue(mGame.getNumberOfRounds() + 1, p.getScore()));

            Line line = new Line(values).setCubic(true).setStrokeWidth(2).setPointRadius(4)
                    .setColor((int) p.getColor()).setHasLabelsOnlyForSelected(true);
            lines.add(line);
        }

        Axis axisX = Axis.generateAxisFromRange(1, mGame.getNumberOfRounds(), 1).setName("Round");
        Axis axisY = new Axis().setName("Score").setHasLines(true);

        LineChartData data = new LineChartData();
        data.setLines(lines);
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        mChart.setInteractive(true);
        mChart.setZoomType(ZoomType.HORIZONTAL);
        mChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        mChart.setValueSelectionEnabled(true);

        mChart.setLineChartData(data);
        Viewport maxViewport = mChart.getMaximumViewport();
        mChart.setMaximumViewport(new Viewport(1, maxViewport.top + 1,
                maxViewport.right, maxViewport.bottom - 1));
        maxViewport = mChart.getMaximumViewport();
        mChart.setCurrentViewport(new Viewport(1, maxViewport.top, 10, maxViewport.bottom));
    }

    @Override
    public String toString() {
        return "Chart";
    }
}
