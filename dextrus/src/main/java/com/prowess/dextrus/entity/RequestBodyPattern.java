package com.prowess.dextrus.entity;

public class RequestBodyPattern {
	private ConnectionEntity properties;
	private String pattern;
	private String catalog;
	public ConnectionEntity getProperties() {
		return properties;
	}
	public void setProperties(ConnectionEntity properties) {
		this.properties = properties;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	

}
