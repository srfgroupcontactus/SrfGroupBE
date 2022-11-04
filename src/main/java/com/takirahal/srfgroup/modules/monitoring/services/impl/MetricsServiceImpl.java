package com.takirahal.srfgroup.modules.monitoring.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.takirahal.srfgroup.modules.monitoring.dto.MetricsDto;
import com.takirahal.srfgroup.modules.monitoring.mapper.MetricsMapper;
import com.takirahal.srfgroup.modules.monitoring.models.Metrics;
import com.takirahal.srfgroup.modules.monitoring.services.MetricsService;
import com.takirahal.srfgroup.restclient.RestTemplateClientProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MetricsServiceImpl implements MetricsService {

    private final Logger log = LoggerFactory.getLogger(MetricsServiceImpl.class);

    @Autowired
    RestTemplateClientProvider restTemplateClientProvider;

    @Autowired
    MetricsMapper metricsMapper;

    final String[] listUrlMetrics = {"actuator/metrics/jvm.memory.used",
    "actuator/metrics/process.cpu.usage", "actuator/metrics/system.cpu.usage"};

    @Override
    public List<MetricsDto> getAllMetrics() {
        List<MetricsDto> metricsDtos = new ArrayList<>();

        for (String url : listUrlMetrics) {
            ResponseEntity<Metrics> result = restTemplateClientProvider.getHttpRequest(url,
                    new ParameterizedTypeReference<>() {},
                    null);
            metricsDtos.add(metricsMapper.toDto(result.getBody()));
        }

        log.debug("Request to get list of Metrics : {}", metricsDtos);
        return metricsDtos;
    }
}
