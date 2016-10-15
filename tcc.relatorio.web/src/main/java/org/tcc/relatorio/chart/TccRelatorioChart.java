package org.tcc.relatorio.chart;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.PieChartModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.dominio.ProdutoEntity;
import org.tcc.relatorio.negocio.util.CalendarUtil;

/**
 *
 * @author Eloy
 */
@SessionScoped
@ManagedBean(name = "tccRelatorioChart")
public class TccRelatorioChart implements Serializable {

    private static final long serialVersionUID = 118231614034054148L;

    private static final Logger LOGGER = LoggerFactory.getLogger(TccRelatorioChart.class);

    public ChartModel pieChart(List<ProdutoEntity> produtos) {
        PieChartModel pie = new PieChartModel();
        pie.setShowDataLabels(true);
        pie.setFill(true);
        pie.setLegendPosition("ne");
        Map<String, Integer> fatias = new HashMap<String, Integer>();
        for (ProdutoEntity produto : produtos) {
            if(fatias.get(produto.getDescricao())!=null){
                fatias.put(produto.getDescricao(), produto.getQuantidade() + fatias.get(produto.getDescricao()));
            }else{
                fatias.put(produto.getDescricao(), produto.getQuantidade());
            }
        }
        for (String fatia: fatias.keySet()) {
            pie.set(fatia, fatias.get(fatia));
        }
        return pie;
    }

    public ChartModel barChart(List<ProdutoEntity> produtos, String tituloSuperior, String tituloEsquerdo, String tituloInferior) {
        Integer qtdMax = 10;
        BarChartModel bar = new BarChartModel();
        bar.setAnimate(true);
        bar.setTitle(tituloSuperior);
        bar.setLegendPosition("ne");
        qtdMax = addSeries_ReturningQtdMax(produtos, qtdMax, bar);
        processaAxis(bar, qtdMax, tituloEsquerdo);
        return bar;
    }
    
    public ChartModel lineChart(List<ProdutoEntity> produtos, String tituloSuperior, String tituloEsquerdo, String tituloInferior) {
        Integer qtdMax = 10;
        LineChartModel line = new LineChartModel();
        line.setAnimate(true);
        line.setTitle(tituloSuperior);
        line.setLegendPosition("ne");
        line.setShowPointLabels(true);
        qtdMax = addSeries_ReturningQtdMax(produtos, qtdMax, line);
        processaAxis(line, qtdMax, tituloEsquerdo);
        return line;
    }

    private void processaAxis(CartesianChartModel chartModel, Integer qtdMax, String tituloEsquerdo) {
        Axis yAxis = chartModel.getAxis(AxisType.Y);
        yAxis.setMax(qtdMax + (qtdMax*20/100));
        yAxis.setMin(0);
        yAxis.setLabel(tituloEsquerdo);
        chartModel.getAxes().put(AxisType.X, new CategoryAxis());
    }

    private Integer addSeries_ReturningQtdMax(List<ProdutoEntity> produtos, Integer qtdMax, CartesianChartModel chartModel) {
        Map<String, ChartSeries> chartsSeriesMap = new HashMap<String, ChartSeries>();
        for (ProdutoEntity produto : produtos) {
            if(chartsSeriesMap.get(produto.getDescricao())!=null){
                chartsSeriesMap.get(produto.getDescricao()).set(CalendarUtil.formata(produto.getDataReferencia(), "yyyy/MM/dd"), produto.getQuantidade());
            }else{
                ChartSeries series = new ChartSeries();
                series.setLabel(produto.getDescricao());
                series.set(CalendarUtil.formata(produto.getDataReferencia(), "yyyy/MM/dd"), produto.getQuantidade());
                chartsSeriesMap.put(produto.getDescricao(), series);
            }
            qtdMax = Math.max(qtdMax, produto.getQuantidade());
        }
        for (ChartSeries series : chartsSeriesMap.values()) {
            chartModel.addSeries(series);
        }
        return qtdMax;
    }

}
