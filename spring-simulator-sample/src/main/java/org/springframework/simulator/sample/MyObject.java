package org.springframework.simulator.sample;

import java.util.Date;

/**
 * Created by gman on 8/01/16.
 */
public class MyObject {

    private int num;
    private Date date = new Date();

    public MyObject() {
    }

    public MyObject(int num) {
        this.num = num;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MyObject{");
        sb.append("num=").append(num);
        sb.append(", date=").append(date);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        MyObject myObject = (MyObject) object;

        return num == myObject.num;

    }

    @Override
    public int hashCode() {
        return num;
    }
}
