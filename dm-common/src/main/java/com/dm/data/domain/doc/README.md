这里的文件仅仅为了smart-doc生成文档使用  
Page和RangePage作为响应，暂时不能生效  
需要配置smart-doc的配置文件，添加以下配置

```json
{
  "allInOne": true,
  "isStrict": false,
  "outPath": "target/classes/static/doc",
  "apiObjectReplacements": [
    {
      "className": "org.springframework.data.domain.Pageable",
      "replacementClassName": "com.dm.data.domain.doc.PageableDto"
    },
    {
      "className": "com.dm.data.domain.RangePageable",
      "replacementClassName": "com.dm.data.domain.doc.RangePageableDto"
    }
  ]
}

```
