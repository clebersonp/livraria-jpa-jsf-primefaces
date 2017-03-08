package br.com.caelum.livraria.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class TemaBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String tema = "omega";

	public String getTema() {
		return tema;
	}
	public void setTema(String tema) {
		this.tema = tema;
	}
}
