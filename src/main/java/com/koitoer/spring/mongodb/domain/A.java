package com.koitoer.spring.mongodb.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by mauricio.mena on 20/04/2016.
 */
@Document(collection = "docsA")
public class A {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    @Reference
    private B b;


    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        A a = (A) o;

        if (id != null ? !id.equals(a.id) : a.id != null) return false;
        if (name != null ? !name.equals(a.name) : a.name != null) return false;
        return !(b != null ? !b.equals(a.b) : a.b != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (b != null ? b.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "A{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", b=" + b +
                '}';
    }
}