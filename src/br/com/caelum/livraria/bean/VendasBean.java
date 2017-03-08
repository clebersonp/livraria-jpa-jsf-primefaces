package br.com.caelum.livraria.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import br.com.caelum.livraria.dao.DAO;
import br.com.caelum.livraria.modelo.Livro;
import br.com.caelum.livraria.modelo.Venda;

@ManagedBean
@ViewScoped
public class VendasBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Venda> getVendas(long seed) {

		List<Venda> vendas = new ArrayList<>();
		List<Livro> livros = new DAO<Livro>(Livro.class).listaTodos();

		Random random = new Random(seed);

		for (Livro livro : livros) {
			Integer quantidade = random.nextInt(500);
			vendas.add(new Venda(livro, quantidade));
		}
		return vendas;
	}

	public BarChartModel getVendasModel() {
		BarChartModel model = new BarChartModel();
		model.setAnimate(true);
		
		ChartSeries vendaSerie2016 = new ChartSeries();
		vendaSerie2016.setLabel("Vendas 2016");

		List<Venda> vendas = getVendas(1234);

		for (Venda venda : vendas) {
			vendaSerie2016.set(venda.getLivro().getTitulo(), venda.getQuantidade());
		}

		model.addSeries(vendaSerie2016);

		ChartSeries vendaSerie2015 = new ChartSeries();
		vendaSerie2015.setLabel("Vendas 2015");

		vendas = getVendas(4321);

		for (Venda venda : vendas) {
			vendaSerie2015.set(venda.getLivro().getTitulo(), venda.getQuantidade());
		}

		model.addSeries(vendaSerie2015);

		model.setTitle("Vendas de Livros");
        model.setLegendPosition("ne");
         
        Axis xAxis = model.getAxis(AxisType.X);
        xAxis.setLabel("Livros");
         
        Axis yAxis = model.getAxis(AxisType.Y);
        yAxis.setLabel("Quantidade");
        yAxis.setMin(0);
        yAxis.setMax(500);
		
		return model;
	}

}
