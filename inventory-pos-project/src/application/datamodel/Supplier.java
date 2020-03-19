package application.datamodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "supplier")
public class Supplier {

	public final static String ALL = "All";
	public final static String SUPPLIER_ID = "Supplier Id";
	public final static String SUPPLIER_NAME = "Supplier Name";
	public final static String BUSINESS_REGISTRATION_NUMBER = "BRN";
	public final static String ADDRESS = "Address";
	public final static String TELEPHONE = "Telephone";
	public final static String TELEFAX = "Telefax";
	public final static String EMAIL = "Email";
	public final static String MOBILE = "Mobile";
			
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "supplier_id")
	private int SupplierId;
	
	@Column(name = "supplier_name", unique = true, length = 100, nullable = false)
	private String SupplierName;
	
	@Column(name = "BRN", unique = true, length = 50, nullable = false)
	private String BRN;
	
	@Column(name = "address", length = 300, nullable = false)
	private String Address;
	
	@Column(name = "telephone", nullable = false, length = 30)
	private String Telephone;
	
	@Column(name = "telefax", length = 30)
	private String Telefax;
	
	@Column(name = "email", length = 150)
	private String Email;
	
	@Column(name = "mobile", length = 30)
	private String Mobile;

	public int getSupplierId() {
		return SupplierId;
	}

	public void setSupplierId(int supplierId) {
		SupplierId = supplierId;
	}

	public String getSupplierName() {
		return SupplierName;
	}

	public void setSupplierName(String supplier) {
		SupplierName = supplier;
	}

	public String getBRN() {
		return BRN;
	}

	public void setBRN(String bRN) {
		BRN = bRN;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getTelephone() {
		return Telephone;
	}

	public void setTelephone(String telephone) {
		Telephone = telephone;
	}

	public String getTelefax() {
		return Telefax;
	}

	public void setTelefax(String telefax) {
		Telefax = telefax;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getMobile() {
		return Mobile;
	}

	public void setMobile(String mobile) {
		Mobile = mobile;
	}
}
