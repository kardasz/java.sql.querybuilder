/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.kardasz.java.sql.querybuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Krzysztof Kardasz <krzysztof@kardasz.eu>
 * @version 1.0
 */
public class Insert implements SQLSet {
    protected Map<String,Object> bind    = null;
    protected String table = null;
    protected List<String> set   = null;
    private static final String BIND_PREFIX = ":_";
    
    public Insert () {
        bind   = new HashMap();
        set    = new ArrayList();
    }
    
    /**
     * 
     * @param table
     * @return 
     */
    public Insert table (String table) {
        this.table = table;
        return this;
    }
    
    /**
     * 
     * @param field
     * @param value
     * @return 
     */
    public Insert set (String field, Object value) {
        bindValue(BIND_PREFIX + field, value);
        set.add(field);
        return this;
    }
    
    /**
     * 
     * @param variable
     * @param bindValue
     * @return 
     */
    public Insert bindValue (String variable, Object bindValue) {
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
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        
        if (null == table || table.isEmpty()) {
            throw new SQLSyntax("Missing SQL UPDATE table");
        }
        sql.append(table);
        
        if (set.size() > 0) {
            StringBuilder sql2 = new StringBuilder(); 
            sql.append("(");
            sql2.append("(");
            boolean first = true;
            for (String field : set) {
                if (!first) {
                    sql.append(", ");
                    sql2.append(", ");
                } else {
                    first = false;
                }
                sql.append(field);
                
                String fieldBind = BIND_PREFIX + field;
                Object bindValue = bind.get(fieldBind);
       
                
                if (bindValue instanceof Expr) {
                    sql2.append(((Expr)bindValue).toString());
                } else {
                    sql2.append(fieldBind);
                }
            }
            sql.append(")");
            sql2.append(")");
            
            sql.append(" VALUES ");
            sql.append(sql2.toString());
        } else {
            throw new SQLSyntax("Missing SQL SET data");
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