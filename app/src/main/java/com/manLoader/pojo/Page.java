package com.manLoader.pojo;

import java.io.Serializable;


public class Page implements Serializable, Comparable<Page> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5385599450009740012L;
	private String id;
	private int numero;
	private byte[] imagem;
	private String imageLink;
	private Chapter capitulo;
	private String externalFile;

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

	public byte[] getImagem() {
		return imagem;
	}

	public void setImagem(byte[] imagem) {
		this.imagem = imagem;
	}

	public Chapter getCapitulo() {
		return capitulo;
	}

	public void setCapitulo(Chapter capitulo) {
		this.capitulo = capitulo;
	}

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

	public int compareTo(Page another) {
		return Integer.valueOf(this.numero).compareTo(another.getNumero());
	}

	public String getExternalFile() {
		return externalFile;
	}

	public void setExternalFile(String externalFile) {
		this.externalFile = externalFile;
	}

}
