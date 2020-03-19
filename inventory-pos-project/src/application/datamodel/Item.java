package application.datamodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "item")
public class Item {

	public static final String ALL = "All";
	public static final String ITEM_ID = "Id";
	public static final String ITEM_NAME = "Item Name";
	public static final String ITEM_DESCRIPTION = "Item Description";
	public static final String ITEM_UNIT = "Item Unit";
	public static final String ITEM_CATEGORY = "Item Category";
	public static final String ITEM_BRAND = "Item Brand";
	
	@Id
	@Column(name = "item_id")
	private String Id;
	
	@Column(name = "item_name", nullable = false, length = 150)
	private String Name;
	
	@Column(name = "item_description", length = 300)
	private String Description;
	
	@ManyToOne
	@JoinColumn(name = "item_unit", nullable = false)
	private ItemUnit Unit;
	
	@ManyToOne
	@JoinColumn(name = "item_category", nullable = false)
	private ItemCategory Category;
	
	@ManyToOne
	@JoinColumn(name = "item_brand", nullable = false)
	private ItemBrand Brand;

	@OneToOne(mappedBy = "item")
	@Cascade(value = CascadeType.SAVE_UPDATE)
	private ItemImage itemImage;
	
	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public ItemUnit getUnit() {
		return Unit;
	}

	public void setUnit(ItemUnit unit) {
		Unit = unit;
	}

	public ItemCategory getCategory() {
		return Category;
	}

	public void setCategory(ItemCategory category) {
		Category = category;
	}

	public ItemBrand getBrand() {
		return Brand;
	}

	public void setBrand(ItemBrand brand) {
		Brand = brand;
	}

	public ItemImage getItemImage() {
		return itemImage;
	}

	public void setItemImage(ItemImage itemImage) {
		this.itemImage = itemImage;
	}
}
