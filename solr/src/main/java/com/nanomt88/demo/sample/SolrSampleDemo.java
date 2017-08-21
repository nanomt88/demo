package com.nanomt88.demo.sample;

import com.nanomt88.demo.entity.User;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.SolrParams;

import java.io.IOException;
import java.util.UUID;

/**
 * 第一个简单的 solr 小例子
 *
 * @author nanomt88@gmail.com
 * @create 2017-08-06 21:10
 **/
public class SolrSampleDemo {

    private static final String URL = "http://192.168.1.141:8983/solr/my_news";

    private static SolrClient solrClient;

    public static void main(String[] args) throws Exception {
        solrClient = new HttpSolrClient.Builder(URL).build();

//        testAdd();
//        testQuery();
//        testDelete();
        testModify();

        solrClient.close();
    }

    public static void testAdd() throws IOException, SolrServerException {
        String prefix = "user_";
        String id = prefix + UUID.randomUUID().toString();
        User user1 = new User(id, "张三", "男" , "45", new String[]{"篮球", "足球", "跳水"});
        //使用java bean形式添加
        solrClient.addBean(user1);

        String id2 = prefix + UUID.randomUUID().toString();
        User user2 = new User(id2, "王思聪", "男" , "30", new String[]{"钱", "女人", "啪啪啪"});
        solrClient.addBean(user2);

        //提交到远程solr服务器
        solrClient.commit();
    }

    public static void testQuery() throws IOException, SolrServerException {
        //构造查询类
        SolrQuery query = new SolrQuery();
        //设置查询关键字
        query.set("q","user_name:王");
        //查询数据
        QueryResponse response = solrClient.query(query);
        //查询出来的数据
        SolrDocumentList results = response.getResults();
        System.out.println("查询总条数：" + results.getNumFound());
        for (SolrDocument doc : results){
            User user = solrClient.getBinder().getBean(User.class, doc);
            System.out.println("查询结果： " + user);
        }
    }

    /**
     * 删除数据
     * @throws IOException
     * @throws SolrServerException
     */
    public static void testDelete() throws IOException, SolrServerException {
        //根据条件删除
        solrClient.deleteByQuery("user_name:王");
        solrClient.commit();
    }

    /**
     * 测试修改
     */
    public static void testModify() throws IOException, SolrServerException {
        SolrDocument doc = new SolrDocument();
        doc.addField("id", "user_6f0f1a35-0aba-407b-932b-48976d04949a");
        doc.addField("user_name", "王思聪");
        doc.put("user_sex", "男");
        doc.addField("user_age", "30");
        doc.addField("user_hobby", new String[]{"啪啪","啪啪啪","啪啪啪啪"});
        User bean = solrClient.getBinder().getBean(User.class, doc);
        solrClient.addBean(bean);
        solrClient.commit();
        System.out.println("修改后：" + bean);
    }
}
