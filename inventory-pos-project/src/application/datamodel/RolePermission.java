package application.datamodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "role_permissions")
public class RolePermission {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer Id;
	
	@ManyToOne
	@JoinColumn(name = "role", nullable = false)
	private UserRole userrolepermission;
	
	@ManyToOne
	@JoinColumn(name = "permission_name", nullable = false)
	private Permissions permission;
	
	@Column(name = "isAllowed", nullable = false)
	private Boolean isAllowed;

	public UserRole getUserrole() {
		return userrolepermission;
	}

	public void setUserrole(UserRole userrole) {
		this.userrolepermission = userrole;
	}

	public Permissions getPermission() {
		return permission;
	}

	public void setPermission(Permissions permission) {
		this.permission = permission;
	}

	public Boolean getIsAllowed() {
		return isAllowed;
	}

	public void setIsAllowed(Boolean isAllowed) {
		this.isAllowed = isAllowed;
	}

	public Integer getId() {
		return Id;
	}
}
