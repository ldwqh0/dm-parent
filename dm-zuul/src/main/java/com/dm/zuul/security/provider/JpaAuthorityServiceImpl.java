package com.dm.zuul.security.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.dm.security.access.RequestAuthorityAttribute;
import com.dm.security.provider.RequestAuthoritiesService;
import com.dm.zuul.security.entity.Authority;
import com.dm.zuul.security.entity.ResourceOperation;
import com.dm.zuul.security.repository.AuthorityRepository;

@Service
public class JpaAuthorityServiceImpl implements RequestAuthoritiesService {

	@Autowired
	private AuthorityRepository authorityRepository;

	@Override
	public List<RequestAuthorityAttribute> listAllAttributes() {
		List<Authority> authorities = authorityRepository.findAll();
		List<RequestAuthorityAttribute> attributes = new ArrayList<>();
		for (Authority authority : authorities) {
			Set<ResourceOperation> operations = authority.getResourceOperations();
			String roleName = authority.getRole().getName();
			for (ResourceOperation operation : operations) {
				if (!Objects.isNull(operation.getSaveable())) {
					attributes.add(new RequestAuthorityAttribute(roleName, operation.getResource().getMatcher(),
							operation.getResource().getMatchType(), HttpMethod.POST, operation.getSaveable()));
				}
				if (!Objects.isNull(operation.getReadable())) {
					attributes.add(new RequestAuthorityAttribute(roleName, operation.getResource().getMatcher(),
							operation.getResource().getMatchType(), HttpMethod.GET, operation.getReadable()));
				}
				if (!Objects.isNull(operation.getUpdateable())) {
					attributes.add(new RequestAuthorityAttribute(roleName, operation.getResource().getMatcher(),
							operation.getResource().getMatchType(), HttpMethod.PUT, operation.getUpdateable()));
				}

				if (!Objects.isNull(operation.getDeleteable())) {
					attributes.add(new RequestAuthorityAttribute(roleName, operation.getResource().getMatcher(),
							operation.getResource().getMatchType(), HttpMethod.DELETE, operation.getDeleteable()));
				}
			}
		}
		return attributes;
	}

}
