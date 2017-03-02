package br.com.caelum.livraria.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.caelum.livraria.dao.DAO;
import br.com.caelum.livraria.modelo.Autor;
import br.com.caelum.livraria.util.RedirectView;

@ManagedBean
@SessionScoped
public class AutorBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Autor autor = new Autor();

	public void buscaAutorPorId() {
		System.out.println("Buscando um autor pelo Id");
		
		Integer autorId = this.autor.getId();
		this.autor = new DAO<Autor>(Autor.class).buscaPorId(autorId);
		
		if (this.autor == null) {
			this.autor = new Autor();
		}
	}
	
	public RedirectView gravar() {
		
		if (this.autor.getId() == null) {
			System.out.println("Gravando autor " + this.autor.getNome());
			new DAO<Autor>(Autor.class).adiciona(this.autor);
		} else {
			System.out.println("Alterando autor: " + this.autor.getNome());
			new DAO<Autor>(Autor.class).atualiza(this.autor);
		}

		this.autor = new Autor();
		return new RedirectView("livro");

	}
	
	// isso parece mais um tipo de setter, podemos usar a propriedade f:setPropertyActionListener no xhtml, tem o mesmo efeito
	public void recuperarAutor(Autor autor) {
		System.out.println("Carregando o autor: " + autor.getNome());
		this.autor = autor;
	}
	
	public void remover(Autor autor) {
		System.out.println("Removendo o autor: " + autor.getNome());
		new DAO<Autor>(Autor.class).remove(autor);
	}
	
	public void limparCamposAutor() {
		this.autor = new Autor();
	}
	
	public List<Autor> getAutores() {
		return new DAO<Autor>(Autor.class).listaTodos();
	}
	
	public Autor getAutor() {
		return this.autor;
	}

	public void setAutor(Autor autor) {
		this.autor = autor;
	}
}
