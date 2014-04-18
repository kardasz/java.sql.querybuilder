/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.kardasz.java.sql.querybuilder;

import java.util.Map;

/**
 *
 * @author krzysiek
 */
public interface SQL {
    public String toSQL() throws SQLSyntax;
    public Map<String, Object> getBindValues();
}