package com.dm.region.service.impl;

import com.dm.region.entity.Region;
import com.dm.region.service.RegionService;
import com.dm.region.service.RegionSyncService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//@Service
//TODO 这里要添加一个service
public class RegionSyncServiceImpl implements RegionSyncService {

    private final RegionService regionService;
    private static final Logger log = LoggerFactory.getLogger(RegionServiceImpl.class);

    public RegionSyncServiceImpl(RegionService regionService) {
        this.regionService = regionService;
    }

    private String resolveHref(String url, String href) {
        return StringUtils.join(StringUtils.substringBeforeLast(url, "/"), "/", href);
    }

    public List<Region> readFromUrl(String url, Region parent) throws IOException {
        Document document = Jsoup.parse(new URL(url), 3000);
        List<Region> p = document.select(".provincetr a").stream().map(element -> {
            Region region = new Region();
            String href = element.attr("href");
            region.setCode(StringUtils.rightPad(href.split("\\.")[0], 12, "0"));
            if (StringUtils.isNotBlank(href)) {
                region.setHref(resolveHref(url, href));
            }
            region.setName(element.text());
            return region;
        }).collect(Collectors.toList());

        List<Region> c = document.select(".citytr,.countytr,.towntr").stream().map(element -> {
            Region region = new Region();
            region.setCode(element.child(0).text());
            region.setName(element.child(1).text());
            region.setParent(parent);
            String href = element.select("a").attr("href");
            if (StringUtils.isNotBlank(href)) {
                region.setHref(resolveHref(url, href));
            }
            return region;
        }).collect(Collectors.toList());

        List<Region> v = document.select(".villagetr").stream().map(element -> {
            Region region = new Region();
            region.setCode(element.child(0).text());
            region.setType(element.child(1).text());
            region.setName(element.child(2).text());
            region.setParent(parent);
            region.setSynced(true);
            return region;
        }).collect(Collectors.toList());

        List<Region> result = new ArrayList<>(p);
        result.addAll(c);
        result.addAll(v);
        return result;
    }

    @Override
    public void sync() throws IOException {
        if (!regionService.exist()) {
            List<Region> level1 = readFromUrl("http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2020/index.html", null);
            regionService.saveAll(level1);
        }
        Optional<Region> next = regionService.findNextSyncRegion();
        while (next.isPresent()) {
            Region region = next.get();
            try {
                List<Region> children = readFromUrl(region.getHref(), region);
                regionService.saveAll(children);
            } catch (Exception e) {
                log.error("同步时发生错误", e);
            }
            next = regionService.findNextSyncRegion();
        }
    }


}
