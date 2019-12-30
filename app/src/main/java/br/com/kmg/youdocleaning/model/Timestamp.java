package br.com.kmg.youdocleaning.model;

public class Timestamp extends java.sql.Timestamp {

    public Timestamp(long time) {
        super(time);
    }

    public Timestamp() {
        super(System.currentTimeMillis());
    }

}
