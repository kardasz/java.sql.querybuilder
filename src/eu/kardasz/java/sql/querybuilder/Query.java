package eu.kardasz.java.sql.querybuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Krzysztof Kardasz <krzysztof@kardasz.eu>
 */
public class Query {
    /**
     *
     * @param connection
     * @param query
     * @return
     * @throws java.sql.SQLException
     * @throws SQLSyntax
     */
    private static java.sql.PreparedStatement setPreparedStatement (java.sql.Connection connection, SQL query) throws java.sql.SQLException, SQLSyntax {
        String sql = query.toSQL();
        Map<String, Object> bind = query.getBindValues();
        List<Object> binds = new ArrayList();
        Pattern pattern = Pattern.compile(":\\w+");
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
            binds.add(bind.get(matcher.group()));
        }
        sql = sql.replaceAll(":\\w+", "?");
        java.sql.PreparedStatement stmt = connection.prepareStatement(sql);
        int k = 1;
        for (Object obj : binds) {
            if (null == obj) {
                stmt.setNull(k++, java.sql.Types.NULL);
            } else {
                stmt.setObject(k++, obj);
            }
        }
        return stmt;
    }
}
