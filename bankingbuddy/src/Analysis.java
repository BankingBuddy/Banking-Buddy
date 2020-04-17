import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Analysis {
    public XYDataset createLineDataset(ArrayList<Entry> entries){
        XYSeries series1 = new XYSeries("Food");
        series1.add(10, 500);
        series1.add(50, 200);
        series1.add(100, 300);

        XYSeries series2 = new XYSeries("Drink");
        series2.add(32, 500);
        series2.add(65, 340);
        series2.add(120, 340);

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        return dataset;
    }

    public JFreeChart createLineChart(ArrayList<Entry> entries){
        XYDataset dataset = createLineDataset(entries);
        JFreeChart chart = ChartFactory.createXYLineChart("My Chart", "x", "y", dataset, PlotOrientation.VERTICAL, true, true, false);

        XYPlot plot = chart.getXYPlot();

        XYLineAndShapeRenderer shapeRenderer = new XYLineAndShapeRenderer();

        shapeRenderer.setSeriesPaint(0, Color.red);
        shapeRenderer.setSeriesStroke(0, new BasicStroke(2.0f));
        shapeRenderer.setSeriesPaint(1, Color.blue);
        shapeRenderer.setSeriesStroke(1, new BasicStroke(2.0f));

        plot.setRenderer(shapeRenderer);
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinesVisible(false);
        plot.setDomainGridlinesVisible(false);

        chart.getLegend().setFrame(BlockBorder.NONE);
        chart.setTitle(new TextTitle("My Chart"));
        return chart;
    }

    public DefaultPieDataset createPieDataset(ArrayList<Entry> entries){
        BigDecimal totalAmount = new BigDecimal(BigInteger.ZERO);
        ArrayList<AnalysisCategory> cumulativeCategories = new ArrayList<>();
        for (Entry entry : entries){
            totalAmount = totalAmount.add(entry.getAmount());
            boolean contains = false;
            for (AnalysisCategory category : cumulativeCategories){
                if (category.getCategoryName().equals(entry.getTransactionCategory().getCategoryName())){
                    category.setAmount(category.getAmount().add(entry.getAmount()));
                    contains = true;
                    break;
                }
            }
            if (!contains){
                AnalysisCategory newAnalysisCategory = new AnalysisCategory(entry.getTransactionCategory().getCategoryName());
                newAnalysisCategory.setAmount(entry.getAmount());
                cumulativeCategories.add(newAnalysisCategory);
            }
        }

        DefaultPieDataset dataset = new DefaultPieDataset();
        if (!totalAmount.equals(BigDecimal.ZERO)){
            for (AnalysisCategory category : cumulativeCategories){
                dataset.setValue(category.getCategoryName(), category.getAmount().multiply(new BigDecimal(100).divide(totalAmount, RoundingMode.CEILING)).intValueExact());
            }
        }
        return dataset;
    }

    public JFreeChart createPieChart(ArrayList<Entry> entries){
        DefaultPieDataset dataset = createPieDataset(entries);
        return ChartFactory.createPieChart("Categories", dataset, false, true, false);
    }
}
