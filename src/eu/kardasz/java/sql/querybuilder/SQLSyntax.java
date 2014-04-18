/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.kardasz.java.sql.querybuilder;

/**
 *
 * @author krzysiek
 */
public class SQLSyntax extends Exception {

    /**
     * Creates a new instance of <code>SQLSyntax</code> without detail message.
     */
    public SQLSyntax() {
    }

    /**
     * Constructs an instance of <code>SQLSyntax</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public SQLSyntax(String msg) {
        super(msg);
    }
}
