package com.cabservice.cab_service.strategy.impl;

import com.cabservice.cab_service.entity.Cab;
import com.cabservice.cab_service.strategy.CabAssignmentStrategy;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

@Component
public class LongestIdleCabStrategy implements CabAssignmentStrategy {
    private static final Logger logger = Logger.getLogger(String.valueOf(LongestIdleCabStrategy.class));
    @Override
    public Cab assignCab(List<Cab> cabs) {
        logger.info("cabs: "+ cabs);
        return cabs.stream()
                .sorted(Comparator.comparing(Cab::getLastStateChangeTime))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No cab available"));
    }
}
