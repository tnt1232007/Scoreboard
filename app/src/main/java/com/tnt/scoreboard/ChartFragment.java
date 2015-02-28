package com.tnt.scoreboard;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tnt.scoreboard.models.Game;
import com.tnt.scoreboard.models.Player;
import com.tnt.scoreboard.models.Score;
import com.tnt.scoreboard.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

public class ChartFragment extends Fragment {

    private static Game mGame;
    private LineChartView mScoreChart;
    private ColumnChartView mPlayerChart;
    private List<Line> mLineData;

    public static ChartFragment getInstance(Game game) {
        mGame = game;
        return new ChartFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        mPlayerChart = (ColumnChartView) view.findViewById(R.id.playerChart);
        mScoreChart = (LineChartView) view.findViewById(R.id.scoreChart);
        initPlayerChartData();
        initScoreChartData();
        return view;
    }

    private void initPlayerChartData() {
        List<AxisValue> xValues = new ArrayList<>();
        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values;
        int i = 0;
        for (Player p : mGame.getPlayerList()) {
            values = new ArrayList<>();
            values.add(new SubcolumnValue(p.getScore(), (int) p.getColor()));
            columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
            xValues.add(new AxisValue(i++).setLabel(StringUtils.subString(p.getName(), 0, 12).toCharArray()));
        }

        ColumnChartData data = new ColumnChartData(columns);
        data.setAxisXBottom(new Axis(xValues).setTextColor(Color.BLACK));
        data.setAxisYLeft(new Axis().setHasLines(true).setTextColor(Color.BLACK));

        mPlayerChart.setZoomEnabled(false);
        mPlayerChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        mPlayerChart.setValueSelectionEnabled(true);
        mPlayerChart.setColumnChartData(data);
        mPlayerChart.setOnValueTouchListener(new ColumnChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int columnIndex, int subColumnIndex, SubcolumnValue value) {
                showSingleScore(columnIndex);
            }

            @Override
            public void onValueDeselected() {
                showAllScore();
            }
        });

        Viewport maxViewport = mPlayerChart.getMaximumViewport();
        mPlayerChart.setMaximumViewport(new Viewport(maxViewport.left, maxViewport.top + 0.5f, maxViewport.right, maxViewport.bottom));
        mPlayerChart.setCurrentViewport(new Viewport(maxViewport.left, maxViewport.top + 0.5f, 5.5f, maxViewport.bottom));
    }

    private void initScoreChartData() {
        mLineData = new ArrayList<>();
        for (Player p : mGame.getPlayerList()) {
            List<PointValue> values = new ArrayList<>();
            for (Score s : p.getScoreList())
                values.add(new PointValue(s.getRoundNumber(), s.getCurrentScore()));
            values.add(new PointValue(mGame.getNumberOfRounds() + 1, p.getScore()));

            Line line = new Line(values)
                    .setCubic(true)
                    .setStrokeWidth(2)
                    .setPointRadius(4)
                    .setColor((int) p.getColor())
                    .setHasLabelsOnlyForSelected(true);
            mLineData.add(line);
        }

        LineChartData data = new LineChartData(mLineData);
        data.setAxisXBottom(Axis.generateAxisFromRange(1, mGame.getNumberOfRounds(), 1).setTextColor(Color.BLACK));
        data.setAxisYLeft(new Axis().setHasLines(true).setTextColor(Color.BLACK));

        mScoreChart.setZoomType(ZoomType.HORIZONTAL);
        mScoreChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        mScoreChart.setValueSelectionEnabled(true);
        mScoreChart.setLineChartData(data);

        Viewport maxViewport = mScoreChart.getMaximumViewport();
        mScoreChart.setMaximumViewport(new Viewport(1, maxViewport.top + 1, maxViewport.right, maxViewport.bottom - 1));
        mScoreChart.setCurrentViewport(new Viewport(1, maxViewport.top + 1, 10.5f, maxViewport.bottom - 1));
        mScoreChart.setViewportCalculationEnabled(false);
    }

    private void showSingleScore(int index) {
        mScoreChart.cancelDataAnimation();
        for (Line l : mLineData)
            for (PointValue v : l.getValues())
                v.setTarget(v.getX(), mScoreChart.getMaximumViewport().bottom);

        List<PointValue> pointList = mLineData.get(index).getValues();
        Player player = mGame.getPlayerList().get(index);
        for (int i = 0; i < player.getScoreList().size(); i++) {
            Score score = player.getScoreList().get(i);
            pointList.get(i).setTarget(score.getRoundNumber(), score.getCurrentScore());
        }
        pointList.get(pointList.size() - 1).setTarget(mGame.getNumberOfRounds() + 1, player.getScore());
        mScoreChart.startDataAnimation();
    }

    private void showAllScore() {
        mScoreChart.cancelDataAnimation();
        for (int i = 0; i < mLineData.size(); i++) {
            List<PointValue> pointList = mLineData.get(i).getValues();
            Player player = mGame.getPlayerList().get(i);
            for (int j = 0; j < player.getScoreList().size(); j++) {
                Score score = player.getScoreList().get(j);
                pointList.get(j).setTarget(score.getRoundNumber(), score.getCurrentScore());
            }
            pointList.get(pointList.size() - 1).setTarget(mGame.getNumberOfRounds() + 1, player.getScore());
        }
        mScoreChart.startDataAnimation();
    }

    @Override
    public String toString() {
        return "Chart";
    }
}
