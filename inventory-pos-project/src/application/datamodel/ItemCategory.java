package application.datamodel;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "item_category")
public class ItemCategory {
	
	public static final String ALL = "All";
	public static final String CATEGORY = "Category";
	public static final String DESCRIPTION = "Description";
	
	@Id
	@Column(name = "itemcategory")
	private String ItemCategory;
	
	@Column(name = "description", length = 300)
	private String Description;

	@OneToMany(mappedBy ="Category")
	private List<Item> items;
	
	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public String getItemCategory() {
		return ItemCategory;
	}

	public void setItemCategory(String itemCategory) {
		ItemCategory = itemCategory;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	@Override
	public String toString() {
		return ItemCategory;
	}
}