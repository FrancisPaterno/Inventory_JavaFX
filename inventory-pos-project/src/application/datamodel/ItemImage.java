package application.datamodel;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "item_image")
public class ItemImage {

	@Id
	@Column(name = "image_id")
	@GeneratedValue(generator = "gen")
	@GenericGenerator(name = "gen", strategy = "foreign", parameters= {@Parameter(name = "property", value = "item")})
	private String Id;
	
	@Column(name = "image")
	private Blob image;
	
	@OneToOne
	@PrimaryKeyJoinColumn
	private Item item;

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public Blob getImage() {
		return image;
	}

	public void setImage(Blob image) {
		this.image = image;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
}
