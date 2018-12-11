package com.dm.security.core.userdetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UserDetailsDto implements UserDetails {
	private static final long serialVersionUID = -4337846050031244208L;

	private Long id;
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	private String username;
	private boolean accountExpired;
	private boolean credentialsExpired;
	@JsonProperty(access = Access.WRITE_ONLY)
	private boolean enabled;
	private boolean locked;
	private List<? extends GrantedAuthorityDto> grantedAuthority;
	private String fullname;
	private List<String> region;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthority;
	}

	public void setGrantedAuthority(List<? extends GrantedAuthorityDto> grantedAuthority) {
		this.grantedAuthority = grantedAuthority;
	}

	public Collection<GrantedAuthorityDto> getRoles() {
		List<GrantedAuthorityDto> result = new ArrayList<GrantedAuthorityDto>();
		result.addAll(grantedAuthority);
		return result;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return !accountExpired;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return !locked;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return !credentialsExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setAccountExpired(boolean accountExpired) {
		this.accountExpired = accountExpired;
	}

	public void setCredentialsExpired(boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getFullname() {
		return fullname;
	}

	public List<String> getRegion() {
		return region;
	}

	public void setRegion(List<String> region) {
		this.region = region;
	}

	@JsonIgnore
	public String getProvinceCode() {
		if (CollectionUtils.isNotEmpty(this.region) && this.region.size() > 0) {
			return region.get(0);
		} else {
			return null;
		}
	}

	@JsonIgnore
	public String getCityCode() {
		if (CollectionUtils.isNotEmpty(this.region) && this.region.size() > 1) {
			return region.get(1);
		} else {
			return null;
		}
	}

	@JsonIgnore
	public String getCountryCode() {
		if (CollectionUtils.isNotEmpty(this.region) && this.region.size() > 2) {
			return region.get(2);
		} else {
			return null;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDetailsDto other = (UserDetailsDto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
