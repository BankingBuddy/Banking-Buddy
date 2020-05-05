import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Analysis {
    public XYDataset createTypeLineDataset(ArrayList<Entry> entries){
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

    public JFreeChart createTypeLineChart(ArrayList<Entry> entries, DateTickUnitType range){
        XYDataset dataset = createTypeLineDataset(entries);
        JFreeChart chart = ChartFactory.createTimeSeriesChart("Income/Expenditure Spending's", "Date", "Amount (£)", dataset, true, true, false);
        setProperties(chart, range, dataset.getSeriesCount());
        return chart;
    }

    public XYDataset createCategoryLineDataset(ArrayList<Entry> entries){
        ArrayList<Entry> cumulativeEntries = new ArrayList<>();
        for (Entry entry : entries){
            boolean contains = false;
            for (Entry cumEntry : cumulativeEntries){
                if (cumEntry.getTransactionCategory().getCategoryName().equals(entry.getTransactionCategory().getCategoryName())){
                    cumEntry.setAmount(cumEntry.getAmount().add(entry.getAmount()));
                    cumEntry.setTimeStamp(entry.getTimeStamp());
                    contains = true;
                    break;
                }
            }
            if (!contains){
                Entry newEntry = new Entry();
                newEntry.setTransactionCategory(entry.getTransactionCategory());
                newEntry.setAmount(entry.getAmount());
                newEntry.setTimeStamp(entry.getTimeStamp());
                cumulativeEntries.add(newEntry);
            }
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        for (Entry entry : cumulativeEntries){
            XYSeries series = new XYSeries(entry.getTransactionCategory().getCategoryName());
            for (Entry fullEntry : entries){
                if (fullEntry.getTransactionCategory().equals(entry.getTransactionCategory())){
                    series.add(fullEntry.getTimeStamp().getTime(), fullEntry.getAmount());
                }
            }
            dataset.addSeries(series);
        }

        return dataset;
    }

    public JFreeChart createCategoryLineChart(ArrayList<Entry> entries, DateTickUnitType range){
        XYDataset dataset = createCategoryLineDataset(entries);
        JFreeChart chart = ChartFactory.createTimeSeriesChart("Category Spending's", "Date", "Amount (£)", dataset, true, true, false);
        setProperties(chart, range, dataset.getSeriesCount());
        return chart;
    }

    private JFreeChart setProperties(JFreeChart chart, DateTickUnitType range, int dataLength) {
        XYPlot plot = chart.getXYPlot();
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setNumberFormatOverride(new DecimalFormat("0.00"));
        yAxis.setAutoRangeMinimumSize(1.0);
        DateAxis xAxis = (DateAxis) plot.getDomainAxis();
        xAxis.setDateFormatOverride(new SimpleDateFormat("HH:mm:ss"));
        xAxis.setTickUnit(new DateTickUnit(range, 15));

        XYLineAndShapeRenderer shapeRenderer = new XYLineAndShapeRenderer();

        Color[] colours = {Color.red, Color.blue, Color.green,
                Color.yellow, Color.orange, Color.cyan,
                Color.magenta};
        for (int i = 0; i < dataLength; i++){
            if (i < colours.length){
                shapeRenderer.setSeriesPaint(i, colours[i]);
            }else{
                Random rand = new Random();
                shapeRenderer.setSeriesPaint(i, new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
            }
            shapeRenderer.setSeriesStroke(i, new BasicStroke(2.0f));
        }

        plot.setRenderer(shapeRenderer);
        return chart;
    }

    public DefaultPieDataset createPieDataset(ArrayList<Entry> entries, Entry.Type type){
        BigDecimal totalAmount = new BigDecimal(BigInteger.ZERO);
        ArrayList<Entry> cumulativeEntries = new ArrayList<>();
        for (Entry entry : entries){
            if (entry.getType().equals(type)){
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
        }

        DefaultPieDataset dataset = new DefaultPieDataset();
        if (!totalAmount.equals(BigDecimal.ZERO)){
            for (Entry entry : cumulativeEntries){
                dataset.setValue(entry.getTransactionCategory().getCategoryName(), entry.getAmount().multiply(new BigDecimal(100).divide(totalAmount, RoundingMode.CEILING)).intValueExact());
            }
        }
        return dataset;
    }

    public JFreeChart createPieChart(ArrayList<Entry> entries, Entry.Type type){
        DefaultPieDataset dataset;
        dataset = createPieDataset(entries, type);
        JFreeChart chart = ChartFactory.createPieChart("Categories", dataset, false, true, false);
        chart.getPlot().setBackgroundPaint(Color.white);
        return chart;
    }
}
