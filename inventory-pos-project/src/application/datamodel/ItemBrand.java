package application.datamodel;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "item_brand")
public class ItemBrand {

	public static final String ALL = "All";
	public static final String BRAND = "Brand";
	public static final String DESCRIPTION = "Description";
	
	@Id
	@Column(name = "brandname", length = 80)
	private String Brandname;
	
	@Override
	public String toString() {
		return Brandname;
	}

	@Column(name = "description", length = 300)
	private String Description;

	@OneToMany(mappedBy = "Brand")
	private List<Item> items;
	
	public String getBrandname() {
		return Brandname;
	}

	public void setBrandname(String brandname) {
		Brandname = brandname;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
}
