package com.manLoader.pojo;

import java.io.Serializable;

public class SearchResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5298841111304583702L;
	private String name;
	private int numChapters;
	private String link;
	private String authority;

	public SearchResult(String name, int numChapters, String link,
			String authority) {
		this.name = name;
		this.link = link;
		this.numChapters = numChapters;
		this.authority = authority;
	}

	public String getName() {
		return name;
	}

	public int getNumChapters() {
		return numChapters;
	}

	public String getLink() {
		return link;
	}

	public String getAuthority() {
		return authority;
	}

	public Manga toManga() {
		Manga manga = new Manga();
		manga.setName(name);
		manga.setPageLink(link);

		return manga;

	}

	@Override
	public String toString() {
		return name;
	}

}
