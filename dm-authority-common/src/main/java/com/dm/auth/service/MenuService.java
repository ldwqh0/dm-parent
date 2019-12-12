package com.dm.auth.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.dm.auth.dto.MenuDto;
import com.dm.auth.entity.Menu;

public interface MenuService {

    public Menu save(MenuDto menuDto);

    public Menu update(long id, MenuDto menuDto);

    public Optional<Menu> get(Long id);

    public void delete(Long id);

    public Page<Menu> search(Long parentId, String key, Pageable pageable);

    public Menu patch(long id, MenuDto _menu);

    public Menu moveUp(Long id);

    public Menu moveDown(Long id);

    public List<Menu> listAllEnabled(Sort sort);

    public boolean exists();

}
