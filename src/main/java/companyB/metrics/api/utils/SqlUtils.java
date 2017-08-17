package companyB.metrics.api.utils;

import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class SqlUtils
{
    public String handleException(Exception e)
    {
        return e instanceof SQLException ?
                handleSqlException((SQLException) e) :
                e.getMessage();
    }

    private String handleSqlException(SQLException e)
    {
        final StringBuilder stringBuilder = new StringBuilder();
        final String sep = "->";
        e.iterator().forEachRemaining((exception) -> stringBuilder.append(String.format("%s%s ", exception.getMessage(),sep)));
        final Integer index = stringBuilder.lastIndexOf(sep);
        return stringBuilder.toString().substring(0,index).trim();
    }
}
