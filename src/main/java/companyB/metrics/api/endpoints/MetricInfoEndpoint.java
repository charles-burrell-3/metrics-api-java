package companyB.metrics.api.endpoints;

import companyB.metrics.api.data_access.MetricDao;
import companyB.metrics.api.data_access.MetricsDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("SpringAutowiredFieldsWarningInspection")
@Component
public class MetricInfoEndpoint implements Endpoint
{
    @Autowired
    private MetricDao metricDao;
    @Autowired
    private MetricsDao metricsDao;
    private final Logger LOGGER = LoggerFactory.getLogger(MetricInfoEndpoint.class);

    @Override
    public String getId()
    {
        return "metricInfo";
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }

    @Override
    public boolean isSensitive()
    {
        return false;
    }

    @Override
    public Object invoke()
    {
        final AtomicInteger count = new AtomicInteger(0);
        final StringBuilder out = new StringBuilder();
        try
        {
            count.set(metricDao.getCount());
            out.append(String.format("registered.metric.count: %d",count.get()));
            final List<String> guids = metricDao.list();
            guids.forEach((guid)-> {
                out.append(String.format("\nmetrics.%s.count: %d", guid,getCountByGuid(guid)));
            });
        }
        catch (SQLException e)
        {
            LOGGER.error(e.getMessage());
        }
        return out.toString();
    }

    private Integer getCountByGuid(String guid)
    {
        final AtomicInteger count = new AtomicInteger(0);
        try
        {
            count.set(metricsDao.list(guid,null,null).size());
        }
        catch (SQLException e)
        {
            LOGGER.error(e.getMessage());
        }
        return count.get();
    }
}