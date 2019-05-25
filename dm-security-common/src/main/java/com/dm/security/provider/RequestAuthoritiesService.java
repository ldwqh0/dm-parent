package com.dm.security.provider;

import java.util.List;

import com.dm.security.access.RequestAuthorityAttribute;

public interface RequestAuthoritiesService {

	public List<RequestAuthorityAttribute> listAllAttributes();
}
