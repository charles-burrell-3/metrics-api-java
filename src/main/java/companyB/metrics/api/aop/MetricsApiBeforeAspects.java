package companyB.metrics.api.aop;

import companyB.metrics.api.contract.insert.InsertMetricEntryRequest;
import companyB.metrics.api.contract.register.RegisterMetricRequest;
import companyB.metrics.api.contract.update.UpdateMetricRequest;
import companyB.metrics.api.service.MetricApiService;
import companyB.metrics.api.utils.DateUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Component;

@SuppressWarnings("SpringAutowiredFieldsWarningInspection")
@Aspect
@Component
public class MetricsApiBeforeAspects
{
    private final Logger LOGGER = LoggerFactory.getLogger(MetricApiService.class);

    @SuppressWarnings({"SpringJavaAutowiringInspection"})
    @Autowired
    private CounterService counterService;

    @Autowired
    private DateUtils dateUtils;

    @Before(value = "execution(* companyB.metrics.api.service.MetricApiService.register(companyB.metrics.api.contract.register.RegisterMetricRequest)) && args(registerMetricRequest)",
    argNames = "registerMetricRequest")
    public void register(RegisterMetricRequest registerMetricRequest)
    {
        LOGGER.info("Provider=AOP Operation=RegisterMetric Name={} ContactName={}",registerMetricRequest.getName(), registerMetricRequest.getContactName());
        increment("counter.register.metric.attempt");
    }

    @Before(value = "execution(* companyB.metrics.api.service.MetricApiService.update(java.lang.String,companyB.metrics.api.contract.update.UpdateMetricRequest)) && args(guid,updateMetricRequest)",
            argNames = "guid,updateMetricRequest")
    public void update(String guid,UpdateMetricRequest updateMetricRequest)
    {
        LOGGER.info("Provider=AOP Operation=UpdateMetric Guid={}",guid);
        increment("counter.update.metric.%s.attempts",guid);
    }

    @Before(value = "execution(* companyB.metrics.api.service.MetricApiService.delete(java.lang.String)) && args(guid)",
            argNames = "guid")
    public void delete(String guid)
    {
        LOGGER.info("Provider=AOP Operation=DeleteMetric Guid={}",guid);
        increment("counter.delete.metric.%s.attempts",guid);
    }

    @Before(value = "execution(* companyB.metrics.api.service.MetricApiService.get(java.lang.String)) && args(guid)",
            argNames = "guid")
    public void get(String guid)
    {
        LOGGER.info("Provider=AOP Operation=GetMetric Guid={}",guid);
        increment("counter.get.metric.%s.attempts",guid);
    }

    @Before(value = "execution(* companyB.metrics.api.service.MetricApiService.list(java.lang.String, java.lang.String, java.lang.String)) && args(guid,since,until)",
            argNames = "guid,since,until")
    public void list(String guid, String since, String until)
    {
        final Long sinceTimestamp = (null == since) ? 0L : dateUtils.toTimestamp(since);
        final Long untilTimestamp = dateUtils.toTimestamp(until);
        final String sinceString = dateUtils.fromTimestamp(sinceTimestamp);
        final String untilString = dateUtils.fromTimestamp(untilTimestamp);
        increment("counter.list.metrics.%s.%s.%s.attempts",guid,since,until);
        LOGGER.info("Provider=AOP Operation=ListMetricEntries Guid={} From={} Until={}.",guid,sinceString,untilString);
    }

    @Before(value = "execution(* companyB.metrics.api.service.MetricApiService.insert(companyB.metrics.api.contract.insert.InsertMetricEntryRequest)) && args(insertMetricEntryRequest)",
            argNames = "insertMetricEntryRequest")
    public void insert(InsertMetricEntryRequest insertMetricEntryRequest)
    {
        LOGGER.info("Provider=AOP Operation=InsertMetricEntry MetricGuid={}",insertMetricEntryRequest.getGuid());
        increment("counter.insert.metric.entry.%s.attempts",insertMetricEntryRequest.getGuid());
    }

    private void increment(String key, Object...args)
    {
        counterService.increment(key.replaceAll("[%s.]",""));
        if(0!= args.length)
            counterService.increment(String.format(key,args));
    }
}
