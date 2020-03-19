package application.datamodel;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user_role")
public class UserRole {

	public static final String ALL = "All";
	public static final String ROLE = "Role";
	public static final String DESCRIPTION = "Description";

	@Id
	@Column(name = "role", length = 20)
	private String Role;
	
	@Column(name = "description", length = 500)
	private String Description;

	
	
	@OneToMany(mappedBy = "userrole")
	private List<User> users;
	
	@OneToMany(mappedBy = "userrolepermission")
	private List<RolePermission> rolePermissions;
	
	public String getRole() {
		return Role;
	}

	public void setRole(String role) {
		Role = role;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	@Override
	public String toString() {
		return Role;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<RolePermission> getRolePermissions() {
		return rolePermissions;
	}

	public void setRolePermissions(List<RolePermission> rolePermissions) {
		this.rolePermissions = rolePermissions;
	}
}
