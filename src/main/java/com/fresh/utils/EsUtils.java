package com.fresh.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

import static cn.hutool.http.HttpUtil.toParams;

public class EsUtils {

    private final static String ES_URL = "http://localhost:9200/";
    private static final HttpClient client = HttpClientBuilder.create().build();

    public static void main(String[] args) throws IOException {
        // shard
        createIndex("goods");
//        // 显示库
//        showIndex();
////         创建文档类型
//        Map<String, String> hmp = new HashMap<>();
//        hmp.put("id", "long");
//        hmp.put("name", "text");
//        hmp.put("description", "text");
//        hmp.put("createDate", "date");
//        hmp.put("state", "integer");
//        hmp.put("price", "double");
//        hmp.put("category", "keyword");
//        createMapping(hmp, "goods");

//        // 添加文档
//        EsGoods esGoods = new EsGoods();
//        esGoods.setId(System.currentTimeMillis()+ RandomUtil.randomLong(10000));
//        esGoods.setName("袜子");
//        esGoods.setCreateDate(new Date());
//        esGoods.setState(1);
//        esGoods.setPrice(1.01);
//        esGoods.setCategory("CLOTHING");
//        esGoods.setDescription("好穿吧");
//        JSONObject o = (JSONObject) JSONObject.toJSON(esGoods);
//        addDocData(o.toJSONString(),"goods");

//        getDocById("goods", "1637132177220");
//        getDocWithSearch("goods");// 查询所有
//        delDocWithId("goods", "1637132177220");
        esSearch("goods");


    }

    private static void esSearch(String goods) throws IOException {
        EsQuery esQuery = new EsQuery();
        esQuery.setFrom(0);
        esQuery.setSize(10);
        esQuery.set_source("[\"name\"]");
        esQuery.set_query("{}");

        String json = JSONObject.toJSONString(esQuery);
        System.out.println(json);
        //
        json = "{\"from\":0,\"query\":{ \"match_all\": {} },\"size\":10,\"_source\":\"[\\\"name\\\"]\"}";
        // term 为精确查找
        json = "{\"from\":0,\"query\":{ \"term\" : {\"name\": \"华为手机\"}  },\"size\":10,\"_source\":\"[\\\"name\\\"]\"}";

        HttpPost request = new HttpPost(ES_URL + goods + "/_doc/_search");
        request.setHeader("Content-Type","application/json");
        request.setEntity(new ByteArrayEntity(json.getBytes()));
        HttpResponse response = client.execute(request);
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(result);



    }

    private static void delDocWithId(String goods, String id) throws IOException {
        HttpDelete request = new HttpDelete(ES_URL + goods + "/_doc/" + id);
        HttpResponse response = client.execute(request);
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(result);
    }

    private static void getDocWithSearch(String goods) throws IOException {
        HttpGet request = new HttpGet(ES_URL + goods + "/_doc/_search");
        HttpResponse response = client.execute(request);
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(result);

    }

    /**
     * 根据ID 查询 此处id 为 es系统 的ID
     *
     * @param goods
     * @param id
     * @throws IOException
     */
    private static void getDocById(String goods, String id) throws IOException {
        HttpGet request = new HttpGet(ES_URL + goods + "/_doc/" + id);
        HttpResponse response = client.execute(request);
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(result);

    }

    private static void addDocData(String toJSONString, String goods) throws IOException {
        System.out.println(toJSONString);
        String url = ES_URL + goods + "/_doc/" + System.currentTimeMillis();
        System.out.println(url);
        HttpPost request = new HttpPost(url);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new ByteArrayEntity(toJSONString.getBytes()));
        HttpResponse response = client.execute(request);
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(result);
    }

    /**
     * 创建映射
     *
     * @param hmp
     * @param indexName
     */
    private static void createMapping(Map<String, String> hmp, String indexName) throws IOException {
        String url = ES_URL + indexName + "/_mapping";
        HttpPost request = new HttpPost(url);
        request.setHeader("Content-Type", "application/json");
        String params = mapToParams(hmp);
        System.out.println(params);
        request.setEntity(new ByteArrayEntity(params.getBytes()));
        HttpResponse response = client.execute(request);
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(result);
    }

    private static String mapToParams(Map<String, String> hmp) {
        Set<Map.Entry<String, String>> entries = hmp.entrySet();
        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
        StringBuffer sb = new StringBuffer("");
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            String format = String.format(", \"%s\": {  \"type\": \"%s\" }", next.getKey(), next.getValue());
            sb.append(format);
        }
        String s = sb.toString().replaceFirst(",", "");
        return String.format("{ \"properties\":{ %s } }", s);
    }

    /**
     * 查看所有索引库
     *
     * @throws IOException
     */
    private static void showIndex() throws IOException {
        String urlString = ES_URL + "_cat/indices?v=";
        System.out.println(urlString);
        HttpGet request = new HttpGet(urlString);
        request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36");
        HttpResponse response = client.execute(request);
        System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));

    }

    /**
     * 创建索引库
     *
     * @param goods
     * @throws IOException
     */
    private static void createIndex(String goods) throws IOException {
        HttpPut request = new HttpPut(ES_URL + "goods");
        request.setHeader("content-type", "application/json; charset=UTF-8");
        String params = getDefaultIndex();
        BasicHttpEntity entity = getEntity(params);
        request.setEntity(entity);
        HttpResponse execute = client.execute(request);
        String result = EntityUtils.toString(execute.getEntity(), "utf-8");
        System.out.println(result);
    }

    private static String getDefaultIndex() {
        JSONObject js = new JSONObject();
        JSONObject index = new JSONObject();
        JSONObject index1 = new JSONObject();
        JSONObject index2 = new JSONObject();
        index1.put("number_of_shards", "1");
        index2.put("number_of_replacas", "0");
        index.put("index", index1);

        js.put("settings", index);
        return js.toJSONString();

    }

    private static BasicHttpEntity getEntity(String params) {
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContentType("application/json");
        ByteArrayInputStream bs = new ByteArrayInputStream(params.getBytes());
        entity.setContent(bs);
        return entity;
    }


    static class EsGoods {
        private Long id;
        private String name;
        private Date createDate;
        private Integer state;
        private Double price;
        private String category;
        private String description;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getCreateDate() {
            return createDate;
        }

        public void setCreateDate(Date createDate) {
            this.createDate = createDate;
        }

        public Integer getState() {
            return state;
        }

        public void setState(Integer state) {
            this.state = state;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    static class EsQuery {
        private Integer from;
        private Integer size;
        // 条件
        private String _query;
        //查询字段
        private String _source;

        public Integer getFrom() {
            return from;
        }

        public void setFrom(Integer from) {
            this.from = from;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public String get_query() {
            return _query;
        }

        public void set_query(String _query) {
            this._query = _query;
        }

        public String get_source() {
            return _source;
        }

        public void set_source(String _source) {
            this._source = _source;
        }
    }

}
