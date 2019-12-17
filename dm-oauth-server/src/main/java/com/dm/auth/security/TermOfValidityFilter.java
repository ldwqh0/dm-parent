package com.dm.auth.security;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TermOfValidityFilter extends OncePerRequestFilter {

	private String authorizationKey;

	private ZonedDateTime expirationDate = ZonedDateTime.now();

	private ZonedDateTime networkTime = ZonedDateTime.now();

	private int err = 0;

	public TermOfValidityFilter(String authorizationKey) {
		this.authorizationKey = authorizationKey;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// 检测系统时间和网络时间,测试软件是否过期
		if (Objects.isNull(networkTime)
				|| (expirationDate.isBefore(ZonedDateTime.now()) && networkTime.isBefore(expirationDate))) {
			request.getRequestDispatcher("/TermOfValidity").forward(request, response);
		} else {
			filterChain.doFilter(request, response);
		}
	}

	@Override
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();
		try {
			String ts = AesUtil.decrypt(authorizationKey, "我都不知道我要输入一段什么样的文本来加密这个内容");
			this.expirationDate = ZonedDateTime.parse(ts);
			this.getNetWorkTime();
		} catch (Exception e) {
			log.error("读取授权信息出错", e);
		}
	}

	@Scheduled(cron = "0/30 * * * * ?")
	public void getNetWorkTime() {
		try {
			URL url = new URL("http://www.baidu.com");
			URLConnection conn = url.openConnection();
			conn.connect();
			long dateL = conn.getDate();
			this.networkTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(dateL), ZoneId.of("Asia/Shanghai"));
			log.info("从网络获取到最新时间  {} .", networkTime.toString());
			err = 0;
		} catch (Exception e) {
			log.error("尝试从网络获取时间是出错", e);
			if (err++ > 3) {
				this.networkTime = null;
			}
		}
	}
}
