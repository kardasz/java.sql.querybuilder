/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.kardasz.java.sql.querybuilder;

/**
 *
 * @author Krzysztof Kardasz <krzysztof@kardasz.eu>
 * @version 1.0
 */
public class Expr {
    private String expr = "";
    
    /**
     * 
     * @param expr 
     */
    public Expr(String expr) {
        this.expr = expr;
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public String toString() {
        return expr;
    }
}
