package application.datamodel;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "permissions")
public class Permissions {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer Id;
	
	@Column(name = "permission_name")
	private String PermissionName;

	@OneToMany(mappedBy = "permission")
	List<RolePermission> permissions;
	
	public List<RolePermission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<RolePermission> permissions) {
		this.permissions = permissions;
	}

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getPermissionName() {
		return PermissionName;
	}

	public void setPermissionName(String permissionName) {
		PermissionName = permissionName;
	}
}
