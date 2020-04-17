import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Analysis {
    public XYDataset createLineDataset(ArrayList<Entry> entries){
        XYSeries expenditureSeries = new XYSeries("Expenditure");
        XYSeries incomeSeries = new XYSeries("Income");
        for (Entry entry : entries){
            if (entry.getType().equals(Entry.Type.Expenditure)){
                expenditureSeries.add(entry.getTimeStamp().getTime(), entry.getAmount());
            }else{
                incomeSeries.add(entry.getTimeStamp().getTime(), entry.getAmount());
            }
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(expenditureSeries);
        dataset.addSeries(incomeSeries);
        return dataset;
    }

    public JFreeChart createLineChart(ArrayList<Entry> entries){
        XYDataset dataset = createLineDataset(entries);
        JFreeChart chart = ChartFactory.createTimeSeriesChart("Spending's", "Date", "Amount (£)", dataset, true, true, false);

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
        chart.setTitle(new TextTitle("Spending's"));
        return chart;
    }

    public DefaultPieDataset createPieDataset(ArrayList<Entry> entries){
        BigDecimal totalAmount = new BigDecimal(BigInteger.ZERO);
        ArrayList<Entry> cumulativeEntries = new ArrayList<>();
        for (Entry entry : entries){
            totalAmount = totalAmount.add(entry.getAmount());
            boolean contains = false;
            for (Entry cumEntry : cumulativeEntries){
                if (cumEntry.getTransactionCategory().getCategoryName().equals(entry.getTransactionCategory().getCategoryName())){
                    cumEntry.setAmount(cumEntry.getAmount().add(entry.getAmount()));
                    contains = true;
                    break;
                }
            }
            if (!contains){
                Entry newEntry = new Entry();
                newEntry.setTransactionCategory(entry.getTransactionCategory());
                newEntry.setAmount(entry.getAmount());
                cumulativeEntries.add(newEntry);
            }
        }

        DefaultPieDataset dataset = new DefaultPieDataset();
        if (!totalAmount.equals(BigDecimal.ZERO)){
            for (Entry entry : cumulativeEntries){
                dataset.setValue(entry.getTransactionCategory().getCategoryName(), entry.getAmount().multiply(new BigDecimal(100).divide(totalAmount, RoundingMode.CEILING)).intValueExact());
            }
        }
        return dataset;
    }

    public JFreeChart createPieChart(ArrayList<Entry> entries){
        DefaultPieDataset dataset = createPieDataset(entries);
        JFreeChart chart = ChartFactory.createPieChart("Categories", dataset, false, true, false);
        chart.getPlot().setBackgroundPaint(Color.white);
        return chart;
    }
}
