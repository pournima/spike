package com.campaignslibrary.models;


public class CauseCategoryResult {
	Category category;

	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}

	public CauseCategoryResult(String name,int id){
		category=new Category(name,id);
	}

	public class Category{
		String name;
		int id;

		public Category(String strName, int iId) {
			// TODO Auto-generated constructor stub
			this.name=strName;
			this.id=iId;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
	}
}