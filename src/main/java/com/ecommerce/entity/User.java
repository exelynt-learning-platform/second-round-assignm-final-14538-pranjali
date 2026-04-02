package com.ecommerce.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ecommerce.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails{

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Column(name = "username", nullable = false, unique = true)
	@NotBlank(message = "UserName is required")
	private String username;

	@Column(name = "user_email", nullable = false, unique = true)
	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String userEmail;

	@Column(name = "password", nullable = false)
	@NotBlank(message = "Password is required")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	@NotNull(message = "Role is required")
	private Role role; // USER or ADMIN

	// One user contains many orders
	@JsonIgnore
	@ToString.Exclude
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<Order> orders;

	// one cart belongs to one user
	@JsonIgnore
	@ToString.Exclude
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private Cart cart;
	
	@PrePersist
    public void setDefaultRole() {
        if (role == null) {
            role = Role.USER;
        }
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    return List.of(); // no roles for now
	}

	@Override
	public boolean isAccountNonExpired() {
	    return true;
	}

	@Override
	public boolean isAccountNonLocked() {
	    return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
	    return true;
	}

	@Override
	public boolean isEnabled() {
	    return true;
	}
}
