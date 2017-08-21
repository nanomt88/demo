package com.nanomt88.demo.entity;

import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;
import java.util.Arrays;

/**
 *
 *  用java bean对象操作 solr
 *
 * @author nanomt88@gmail.com
 * @create 2017-08-06 21:18
 **/
public class User implements Serializable{

    private static final long serialVersionUID = -5436590876773717784L;

    @Field("id")
    private String id;

    @Field("user_name")
    private String name;

    @Field("user_sex")
    private String sex;

    @Field("user_age")
    private String age;

    @Field("user_hobby")
    private String[] hobby;

    public User() {}

    public User(String id, String name, String sex, String age, String[] hobby) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.hobby = hobby;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String[] getHobby() {
        return hobby;
    }

    public void setHobby(String[] hobby) {
        this.hobby = hobby;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age='" + age + '\'' +
                ", hobby=" + Arrays.toString(hobby) +
                '}';
    }
}
