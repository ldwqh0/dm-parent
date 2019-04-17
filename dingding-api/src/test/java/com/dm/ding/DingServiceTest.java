package com.dm.ding;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.dm.dingding.service.DingTalkService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiConfig.class)
public class DingServiceTest {

	@Autowired
	private DingTalkService dingService;

	@Test
	public void listDepartment() {
		System.out.println(dingService.listDepartment());
	}

}
