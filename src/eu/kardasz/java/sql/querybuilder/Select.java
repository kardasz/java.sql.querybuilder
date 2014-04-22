/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.kardasz.java.sql.querybuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Krzysztof Kardasz <krzysztof@kardasz.eu>
 * @version 1.0
 */
public class Select implements SQL {
    protected Map<String,Object> bind    = null;
    protected List<String> cols    = null;
    protected List<String> from    = null;
    protected List<String> join    = null;
    protected List<Map> where   = null;
    protected Long limit   = null;
    protected Long offset  = null;
    protected String having  = null;
    protected List<String> order   = null;
    protected List<String> group   = null;
    
    /**
     * 
     */
    public Select() {
        bind    = new HashMap();
        cols    = new ArrayList();
        from    = new ArrayList();
        join    = new ArrayList();
        where   = new ArrayList();
        order   = new ArrayList();
        group   = new ArrayList();
    }
    
    /**
     * 
     * @param col
     * @return 
     */
    public Select select (String col) {
        cols.add(col);
        return this;
    }
    
    /**
     * 
     * @param cols
     * @return 
     */
    public Select select (String[] cols) {
        this.cols.addAll(Arrays.asList(cols));
        return this;
    }
    
    /**
     * 
     * @param table
     * @return 
     */
    public Select from (String table) {
        from.add(table);
        return this;
    }
    
    /**
     * 
     * @param type
     * @param table
     * @param condition
     * @param cols
     * @return 
     */
    public Select join (String type, String table, String condition, String[] cols) {
        StringBuilder sql = new StringBuilder(type.toUpperCase());
        sql.append(" JOIN ");
        sql.append(table);
        sql.append(" ON ( ");
        sql.append(condition);
        sql.append(" )");
        
        if (null != cols && cols.length > 0) {
            select(cols);
        }
        
        join.add(sql.toString());
        
        return this;
    }
    
    /**
     * 
     * @param table
     * @param condition
     * @param cols
     * @return 
     */
    public Select joinInner (String table, String condition, String[] cols) {
        join ("inner", table, condition, cols);
        return this;
    }
    
    /**
     * 
     * @param table
     * @param condition
     * @return 
     */
    public Select joinInner (String table, String condition) {
        join ("inner", table, condition, null);
        return this;
    }
    
    /**
     * 
     * @param table
     * @param condition
     * @param cols
     * @return 
     */
    public Select joinLeft (String table, String condition, String[] cols) {
        join ("left", table, condition, cols);
        return this;
    }
    
    /**
     * 
     * @param table
     * @param condition
     * @return 
     */
    public Select joinLeft (String table, String condition) {
        join ("left", table, condition, null);
        return this;
    }
    
    /**
     * 
     * @param table
     * @param condition
     * @param cols
     * @return 
     */
    public Select joinRight (String table, String condition, String[] cols) {
        join ("right", table, condition, cols);
        return this;
    }
    
    /**
     * 
     * @param table
     * @param condition
     * @return 
     */
    public Select joinRight (String table, String condition) {
        join ("right", table, condition, null);
        return this;
    }
    
    /**
     * 
     * @param where
     * @param bindValue
     * @param type
     * @return 
     */
    public Select where (String where, Object bindValue, String type) {
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
    public Select where (String where, Object bindValue) {
        return where(where, bindValue, "and");
    }
    
    /**
     * 
     * @param where
     * @return 
     */
    public Select where (String where) {
        return where(where, null, "and");
    }
    
    /**
     * 
     * @param where
     * @param bindValue
     * @return 
     */
    public Select orWhere (String where, Object bindValue) {
        return where(where, bindValue, "or");
    }
    
    /**
     * 
     * @param where
     * @return 
     */
    public Select orWhere (String where) {
        return where(where, null, "or");
    }

    /**
     *
     * @param field
     * @param value
     * @return
     */
    public Select whereEquals (String field, Object value) {
        return where(field + " = :_" + field, value, "and");
    }

    /**
     *
     * @param field
     * @param value
     * @return
     */
    public Select whereNotEquals (String field, Object value) {
        return where(field + " != :_" + field, value, "and");
    }

    /**
     *
     * @param field
     * @param value
     * @return
     */
    public Select whereLike (String field, Object value) {
        return where(field + " LIKE :_" + field, value, "and");
    }

    /**
     *
     * @param field
     * @param value
     * @return
     */
    public Select whereNotLike (String field, Object value) {
        return where(field + " LIKE :_" + field, value, "and");
    }

    /**
     * 
     * @param limit
     * @return 
     */
    public Select limit (int limit) {
        return limit(Long.valueOf(limit));
    }
    
    /**
     * 
     * @param offset
     * @return 
     */
    public Select offset (int offset) {
        return offset(Long.valueOf(offset));
    }

    /**
     *
     * @param limit
     * @return
     */
    public Select limit (Long limit) {
        this.limit = limit;
        return this;
    }

    /**
     *
     * @param offset
     * @return
     */
    public Select offset (Long offset) {
        this.offset = offset;
        return this;
    }
    
    /**
     * 
     * @param column
     * @return 
     */
    public Select order (String column) {
        order.add(column);
        return this;
    }
    
    /**
     * 
     * @param columns
     * @return 
     */
    public Select order (String[] columns) {
        order.addAll(Arrays.asList(columns));
        return this;
    }
    
    /**
     * 
     * @param column
     * @return 
     */
    public Select group (String column) {
        group.add(column);
        return this;
    }
    
    /**
     * 
     * @param columns
     * @return 
     */
    public Select group (String[] columns) {
        group.addAll(Arrays.asList(columns));
        return this;
    }
    
    /**
     * 
     * @param expr
     * @return 
     */
    public Select having (String expr) {
        having = expr;
        return this;
    }

    /**
     *
     * @param variable
     * @param bindValue
     * @return
     */
    public Select bindValue (String variable, Object bindValue) {
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
        StringBuilder sql = new StringBuilder("SELECT ");
        
        /* SELECT fields */
        if (cols.size() > 0) {
            boolean first = true;
            for (String col : cols) {
                if (!first) {
                    sql.append(", ");
                } else {
                    first = false;
                }
                sql.append(col);
            }
        } else {
            sql.append("*");
        }
        
        /* FROM */
        if (from.size() > 0) {
            sql.append(" FROM ");
            boolean first = true;
            for (String table : from) {
                if (!first) {
                    sql.append(", ");
                } else {
                    first = false;
                }
                sql.append(table);
            }
        } else {
            throw new SQLSyntax("Missing SQL FROM table");
        }
        
        /* JOIN */
        if (this.join.size() > 0) {
            for (String join : this.join) {
                sql.append(" ");
                sql.append(join);
            }
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
        
        /* GROUP */
        if (this.group.size() > 0) {
            sql.append(" GROUP BY ");
            boolean first = true;
            for (String expr : this.group) {
                if (!first) {
                    sql.append(", ");
                } else {
                    first = false;
                }
                sql.append(expr);
            }
        }
        
        /* HAVING */
        if (null != having && !having.isEmpty()) {
            sql.append(" HAVING ");
            sql.append(having);
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
        
        if (null != offset) {
            sql.append(" OFFSET ");
            sql.append(String.valueOf(offset));
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