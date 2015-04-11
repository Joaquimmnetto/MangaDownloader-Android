package com.manLoader.enums;

public enum MangaStatus {
	
	naoLido("Nao Lido"),
	emAndamento("Em Anadamento"),
	encerrado("Encerrado"),
	emEspera("Em Espera");
	
	
	private final String label;
	
	private MangaStatus(String label){
		this.label = label;
	}
	
	
	public String getLabel() {
		return label;
	}

}
