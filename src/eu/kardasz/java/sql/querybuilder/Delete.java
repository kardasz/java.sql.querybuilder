package eu.kardasz.java.sql.querybuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SQL Query Builder for MySQL
 *
 * @author Krzysztof Kardasz <krzysztof@kardasz.eu>
 * @version 1.1
 */
public class Delete implements SQL {
    protected Map<String,Object> bind    = null;
    protected String table = null;
    protected List<Map> where = null;
    protected Integer limit = null;
    protected List<String> order = null;
    
    /**
     * 
     */
    public Delete () {
        bind   = new HashMap();
        where  = new ArrayList();
        order  = new ArrayList();
    }
    
    /**
     * 
     * @param table
     * @return
     */
    public Delete table (String table) {
        this.table = table;
        return this;
    }
    
    /**
     * 
     * @param where
     * @param bindValue
     * @param type
     * @return 
     */
    public Delete where (String where, Object bindValue, String type) {
        if (type == null) {
            type = "and";
        } else if (!type.equalsIgnoreCase("and")) {
            type = "or";
        }
        
        if (null != bindValue) {
            if (bindValue instanceof Expr) {
                where = where.replaceAll(":\\S+", ((Expr)bindValue).toString());
            } else {
                Pattern pattern = Pattern.compile(":\\S+");
                Matcher matcher = pattern.matcher(where);
                while (matcher.find()) {
                    bindValue(matcher.group(), bindValue);
                }
            }
        }
        
        Map<String, String> obj = new HashMap();
        obj.put("type", type.toUpperCase());
        obj.put("where", where);
        this.where.add(obj);
        return this;
    }
    
    /**
     * 
     * @param where
     * @param bindValue
     * @return 
     */
    public Delete where (String where, Object bindValue) {
        return where(where, bindValue, "and");
    }
    
    /**
     * 
     * @param where
     * @return 
     */
    public Delete where (String where) {
        return where(where, null, "and");
    }
    
    /**
     * 
     * @param where
     * @param bindValue
     * @return 
     */
    public Delete orWhere (String where, Object bindValue) {
        return where(where, bindValue, "or");
    }
    
    /**
     * 
     * @param where
     * @return 
     */
    public Delete orWhere (String where) {
        return where(where, null, "or");
    }


    /**
     *
     * @param field
     * @param value
     * @return
     */
    public Delete whereEquals (String field, Object value) {
        return where(field + " = :_" + field, value, "and");
    }

    /**
     *
     * @param field
     * @param value
     * @return
     */
    public Delete whereNotEquals (String field, Object value) {
        return where(field + " != :_" + field, value, "and");
    }

    /**
     *
     * @param field
     * @param value
     * @return
     */
    public Delete whereLike (String field, Object value) {
        return where(field + " LIKE :_" + field, value, "and");
    }

    /**
     *
     * @param field
     * @param value
     * @return
     */
    public Delete whereNotLike (String field, Object value) {
        return where(field + " LIKE :_" + field, value, "and");
    }

    /**
     * 
     * @param limit
     * @return 
     */
    public Delete limit (int limit) {
        this.limit = limit;
        return this;
    }
    
    /**
     * 
     * @param column
     * @return 
     */
    public Delete order (String column) {
        order.add(column);
        return this;
    }
    
    /**
     * 
     * @param columns
     * @return 
     */
    public Delete order (String[] columns) {
        order.addAll(Arrays.asList(columns));
        return this;
    }
    
    
    /**
     * 
     * @param variable
     * @param bindValue
     * @return 
     */
    public Delete bindValue (String variable, Object bindValue) {
        bind.put(variable, bindValue);
        return this;
    }
    
    
    /**
     * 
     * @return 
     * @throws eu.kardasz.java.sql.querybuilder.SQLSyntax
     */
    @Override
    public String toSQL () throws SQLSyntax  {
        StringBuilder sql = new StringBuilder("DELETE FROM ");
        
        if (null == table || table.isEmpty()) {
            throw new SQLSyntax("Missing SQL UPDATE table");
        }
        sql.append(table);
        
        /* WHERE */
        if (this.where.size() > 0) {
            sql.append(" WHERE ");
            boolean first = true;
            for (Map obj : this.where) {
                if (!first) {
                    sql.append(" ");
                    sql.append(obj.get("type"));
                    sql.append(" ");
                } else {
                    first = false;
                }
                sql.append(obj.get("where"));
            }
        }
                
        /* ORDER */
        if (this.order.size() > 0) {
            boolean first = true;
            for (String expr : this.order) {
                if (!first) {
                    sql.append(", ");
                } else {
                    first = false;
                }
                sql.append(expr);
            }
        }
        
        if (null != limit) {
            sql.append(" LIMIT ");
            sql.append(String.valueOf(limit));
        }
                
        return sql.toString();
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public Map<String, Object> getBindValues() {
        return bind;
    }
}