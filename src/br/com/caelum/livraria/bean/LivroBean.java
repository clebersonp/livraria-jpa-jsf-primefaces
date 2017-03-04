package br.com.caelum.livraria.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import br.com.caelum.livraria.dao.DAO;
import br.com.caelum.livraria.modelo.Autor;
import br.com.caelum.livraria.modelo.Livro;
import br.com.caelum.livraria.modelo.LivroDataModel;

@ViewScoped
@ManagedBean
public class LivroBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private LivroDataModel livroDataModel = new LivroDataModel();

	private Livro livro = new Livro();
	
	private List<String> generos = Arrays.asList("Romance", "Drama", "Ação", "Informática");

	private Integer autorId;

	private List<Livro> livros;
	
	public List<Autor> getAutoresDoLivro() {
		return this.livro.getAutores();
	}
	
	public List<Livro> getLivros() {
		DAO<Livro> dao = new DAO<Livro>(Livro.class);
		if (this.livros == null) {
			this.livros = dao.listaTodos(); 
		}
		return this.livros;
	}
	
	public void gravarAutor() {
		Autor autor = new DAO<Autor>(Autor.class).buscaPorId(autorId);
		if (!this.livro.getAutores().contains(autor)) {
			this.livro.adicionaAutor(autor);
		}
	}
	
	public void remover(Livro livro) {
		System.out.println("Removendo livro: " + livro.getTitulo());
		DAO<Livro> dao = new DAO<Livro>(Livro.class);
		dao.remove(livro);
		this.livros = dao.listaTodos();
	}
	
	public void carregarLivroParaAlteracao(Livro livro) {
		System.out.println("Carregando o livro para alteração: " + livro.getTitulo());
		this.livro = livro;
	}
	
	public void buscaLivroPorId() {
		System.out.println("Buscando um livro pelo Id no Request método GET");
		Integer livroId = this.livro.getId();
		
		this.livro = new DAO<Livro>(Livro.class).buscaPorId(livroId);
		
		if (this.livro == null) {
			this.livro = new Livro();
		}
	}
	
	public void removerAutorDoLivro(Autor autor) {
		System.out.println("Removendo o Autor da lista: " + autor.getNome());
		this.livro.removerAutor(autor);
	}
	
	// o JSF ao encontrar um tipo diferente de String, chamara o toString() do objeto para saber o nome da view
	public String formAutor() {
		System.out.println("Chamando o formulário do Autor");
		return "autor?faces-redirect=true";
	}
	
	public LivroDataModel getLivroDataModel() {
		return livroDataModel;
	}
	
	public Integer getAutorId() {
		return autorId;
	}

	public void setAutorId(Integer autorId) {
		this.autorId = autorId;
	}

	public Livro getLivro() {
		return livro;
	}
	
	public void setLivro(Livro livro) {
		this.livro = livro;
	}
	
	public List<String> getGeneros() {
		return generos;
	}
	
	public List<Autor> getAutores() {
		return new DAO<Autor>(Autor.class).listaTodos();
	}
	
	public boolean precoEhMenor(Object valorColuna, Object filtroDigitado, Locale locale) {
		
		String textoDigitado = (filtroDigitado == null) ? null : filtroDigitado.toString().trim();
		
		System.out.println("Filtrando pelo " + textoDigitado + ", valor do elemento: " + valorColuna);
		
		if (textoDigitado == null || textoDigitado.equals("")) {
			return true;
		}
		
		if (valorColuna == null) {
			return false;
		}
		
		try {
			
			Double precoDigitado = Double.valueOf(textoDigitado);
			Double precoColuna = (Double) valorColuna;
			
			return precoColuna.compareTo(precoDigitado) < 0;
			
		} catch(NumberFormatException e) {
			return false;
		}
		
	}
	
	public void gravar() {
		DAO<Livro> dao = new DAO<Livro>(Livro.class);

		if (livro.getAutores().isEmpty()) {
//			throw new RuntimeException("Livro deve ter pelo menos um Autor.");
			FacesContext.getCurrentInstance().addMessage("autor", new FacesMessage("Escolha pelo menos um Autor para gravar o livro!"));
			return;
		}
		if (this.livro.getId() == null) {
			System.out.println("Gravando livro " + this.livro.getTitulo());
			dao.adiciona(this.livro);
			this.livros = dao.listaTodos();
		} else {
			System.out.println("Alterando o livro: " + this.livro.getTitulo());
			dao.atualiza(this.livro);
		}
		
		this.livro = new Livro();
	}
	
	public void comecaComDigitoUm(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if (value != null && !value.toString().startsWith("1")) {
			throw new ValidatorException(new FacesMessage("Valor de ISBN é inválido!"));
		}
	}

}
