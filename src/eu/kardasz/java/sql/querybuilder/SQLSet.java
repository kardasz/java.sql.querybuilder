package eu.kardasz.java.sql.querybuilder;

/**
 * Created by krzysiek on 18.04.2014.
 */
public interface SQLSet extends SQL {
    public <T extends SQL> Object set (String field, Object value);
}
