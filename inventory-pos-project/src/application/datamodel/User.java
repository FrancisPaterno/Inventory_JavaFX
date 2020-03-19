package application.datamodel;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {
	public static final String ALL = "All";
	public static final String ID = "Id";
	public static final String USERNAME = "Username";
	public static final String EMAIL = "Email";
	public static final String PASSWORD = "Password";
	public static final String CREATED = "Created";
	public static final String ROLE = "Role";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer Id;

	@Column(name = "username", length = 45, unique = true, nullable = false)
	private String Username;

	@Column(name = "email", length = 300, nullable = false)
	private String Email;

	@Column(name = "password", length = 45, nullable = false)
	private String Password;

	@Column(name = "created", nullable = false)
	private Date Created;

	@Column(name = "lastupdate", nullable = true)
	private Date LastUpdate;
	
	@ManyToOne
	@JoinColumn(name = "role", nullable = false)
	private UserRole userrole;

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public Date getCreated() {
		return Created;
	}

	public void setCreated(Date created) {
		Created = created;
	}

	public Date getLastUpdate() {
		return LastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		LastUpdate = lastUpdate;
	}

	public UserRole getUserrole() {
		return userrole;
	}

	public void setUserrole(UserRole userrole) {
		this.userrole = userrole;
	}
}
