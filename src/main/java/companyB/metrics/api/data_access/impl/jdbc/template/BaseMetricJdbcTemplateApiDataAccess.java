package companyB.metrics.api.data_access.impl.jdbc.template;

import companyB.metrics.api.utils.SqlGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;

@SuppressWarnings("SpringAutowiredFieldsWarningInspection")
public class BaseMetricJdbcTemplateApiDataAccess
{
    final Logger LOGGER = LoggerFactory.getLogger(BaseMetricJdbcTemplateApiDataAccess.class);

    @Autowired
    SqlGenerator sqlGenerator;
    @Autowired
    RowSetMapperProvider rowSetMapperProvider;
    @Autowired
    JdbcTemplate jdbcTemplate;

    Integer insertUpdateDelete(String sql) throws SQLException
    {
        return this.jdbcTemplate.update(sql);
    }
}