/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.kardasz.java.sql.querybuilder;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Krzysztof Kardasz <krzysztof@kardasz.eu>
 * @version 1.0
 */
public class Update implements SQLSet {
    protected Map<String,Object> bind    = null;
    protected String table = null;
    protected List<String> set   = null;
    protected List<Map> where = null;
    protected Long limit = null;
    protected List<String> order = null;
    private static final String BIND_PREFIX = ":_";
    
    
    public Update () {
        bind   = new HashMap();
        set    = new ArrayList();
        where  = new ArrayList();
        order  = new ArrayList();
    }
    
    /**
     * 
     * @param table
     * @return 
     */
    public Update table (String table) {
        this.table = table;
        return this;
    }
    
    /**
     * 
     * @param field
     * @param value
     * @return 
     */
    public Update set (String field, Object value) {
        bindValue(BIND_PREFIX + field, value);
        set.add(field);
        return this;
    }
    
    /**
     * 
     * @param where
     * @param bindValue
     * @param type
     * @return 
     */
    public Update where (String where, Object bindValue, String type) {
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
    public Update where (String where, Object bindValue) {
        return where(where, bindValue, "and");
    }
    
    /**
     * 
     * @param where
     * @return 
     */
    public Update where (String where) {
        return where(where, null, "and");
    }
    
    /**
     * 
     * @param where
     * @param bindValue
     * @return 
     */
    public Update orWhere (String where, Object bindValue) {
        return where(where, bindValue, "or");
    }
    
    /**
     * 
     * @param where
     * @return 
     */
    public Update orWhere (String where) {
        return where(where, null, "or");
    }

    /**
     *
     * @param field
     * @param value
     * @return
     */
    public Update whereEquals (String field, Object value) {
        return where(field + " = :_" + field, value, "and");
    }

    /**
     *
     * @param field
     * @param value
     * @return
     */
    public Update whereNotEquals (String field, Object value) {
        return where(field + " != :_" + field, value, "and");
    }

    /**
     *
     * @param field
     * @param value
     * @return
     */
    public Update whereLike (String field, Object value) {
        return where(field + " LIKE :_" + field, value, "and");
    }

    /**
     *
     * @param field
     * @param value
     * @return
     */
    public Update whereNotLike (String field, Object value) {
        return where(field + " LIKE :_" + field, value, "and");
    }

    /**
     *
     * @param limit
     * @return
     */
    public Update limit (Long limit) {
        this.limit = limit;
        return this;
    }
    /**
     *
     * @param limit
     * @return
     */
    public Update limit (int limit) {
        return limit(Long.valueOf(limit));
    }
    
    /**
     * 
     * @param column
     * @return 
     */
    public Update order (String column) {
        order.add(column);
        return this;
    }
    
    /**
     * 
     * @param columns
     * @return 
     */
    public Update order (String[] columns) {
        order.addAll(Arrays.asList(columns));
        return this;
    }
    
    
    /**
     * 
     * @param variable
     * @param bindValue
     * @return 
     */
    public Update bindValue (String variable, Object bindValue) {
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
        StringBuilder sql = new StringBuilder("UPDATE ");
        
        if (null == table || table.isEmpty()) {
            throw new SQLSyntax("Missing SQL UPDATE table");
        }
        sql.append(table);
        
        
        /* SET */
        sql.append(" SET ");
        if (set.size() > 0) {
            boolean first = true;
            for (String field : set) {
                if (!first) {
                    sql.append(", ");
                } else {
                    first = false;
                }
                
                String fieldBind = BIND_PREFIX + field;
                Object bindValue = bind.get(fieldBind);
                sql.append(field);
                sql.append(" = ");
                
                if (bindValue instanceof Expr) {
                    sql.append(((Expr)bindValue).toString());
                } else {
                    sql.append(fieldBind);
                }
            }
        } else {
            throw new SQLSyntax("Missing SQL SET data");
        }

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
            sql.append(" ORDER BY ");
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