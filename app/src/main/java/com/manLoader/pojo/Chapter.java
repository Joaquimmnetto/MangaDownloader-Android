package com.manLoader.pojo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


public class Chapter implements Comparable<Chapter>,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1129585011119251259L;
	private String id;
	private Manga manga;
	private int numero;
	private String nome;
	private String link;
	private Collection<Page> paginas;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public Manga getManga() {
		return manga;
	}

	public void setManga(Manga manga) {
		this.manga = manga;
	}

	public Collection<Page> getPaginas() {
		return paginas;
	}

	public void setPaginas(List<Page> paginas) {
		this.paginas = paginas;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	// @Override
	public int compareTo(Chapter o) {
		return Integer.valueOf(getNumero()).compareTo(
				Integer.valueOf(o.getNumero()));
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}


}
