package com.manLoader.pojo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.manLoader.core.webModule.parsers.SiteMangaWebParser;
import com.manLoader.enums.MangaStatus;

public class Manga implements Comparable<Manga>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4808623623317882520L;
	private SiteMangaWebParser parser;
	private Long id;
	private String pageLink;
	private String sinopse;
	private String name;
	private String tags;
	private String author;
	private Integer numChapters;
	//private boolean isFavorited;
	private byte[] coverImage;
	private Collection<Chapter> capitulos;
	private MangaStatus status;

	public Integer getNumChapters() {
		return numChapters;
	}

	public void setNumChapters(Integer numChapters) {
		this.numChapters = numChapters;
	}


	public MangaStatus getStatus() {
		return status;
	}

	public void setStatus(MangaStatus status) {
		this.status = status;
	}

	public SiteMangaWebParser getParser() {
		return parser;
	}

	public void setParser(SiteMangaWebParser parser) {
		this.parser = parser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<Chapter> getCapitulos() {
		return capitulos;
	}

	public void setCapitulos(List<Chapter> capitulos) {
		this.capitulos = capitulos;
	}

	public byte[] getCoverImage() {
		return coverImage;
	}

	public void setCoverImage(byte[] coverImage) {
		this.coverImage = coverImage;
	}

	public String getSinopse() {
		return sinopse;
	}

	public void setSinopse(String sinopse) {
		this.sinopse = sinopse;
	}

	public String getPageLink() {
		return pageLink;
	}

	public void setPageLink(String pageLink) {
		this.pageLink = pageLink;
	}

	// @Override
	public int compareTo(Manga o) {
		return getName().toLowerCase().compareTo(o.getName().toLowerCase());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthor() {
		return author;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
