package com.alphago365.octopus.job;


import com.alphago365.octopus.config.HandicapAnalysisConfig;
import com.alphago365.octopus.model.*;
import com.alphago365.octopus.service.HandicapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Scope("prototype")
@Component
@Slf4j
public class HandicapAnalysisJob extends HandicapRelatedJob {

    @Autowired
    private HandicapService handicapService;

    @Autowired
    private HandicapAnalysisConfig handicapAnalysisConfig;

    private final Instant analysisTime;

    public HandicapAnalysisJob(long delay, Handicap handicap, Instant analysisTime) {
        super(String.format("HA-%d-%d", handicap.getMatch().getId(),
                handicap.getProvider().getId()), delay, handicap);
        this.analysisTime = analysisTime;
    }

    @Override
    public void runJob() {
        save(analyze());
    }

    private List<HandicapAnalysis> save(List<HandicapAnalysis> handicapChangeList) {
        return handicapService.saveAllAnalyses(handicapChangeList);
    }

    private List<HandicapAnalysis> analyze() {
        List<HandicapAnalysis> analysisList = new ArrayList<>();
        List<HandicapChange> handicapChangeList = handicap.getChangeHistories();
        handicapChangeList.sort(Comparator
                .comparing(HandicapChange::getUpdateTime));
        HandicapChange first = handicapChangeList.get(0);
        HandicapAnalysis firstAnalysis = createFirstAnalysis(first);
        analysisList.add(firstAnalysis);

        HandicapChange changed = first;
        int size = handicapChangeList.size();
        for (int i = 1; i < size; i++) {
            HandicapChange current = handicapChangeList.get(i);
            if (isBoundaryChanged(current, changed)) {
                HandicapAnalysis boundaryChangedAnalysis = createBoundaryChangedAnalysis(current, changed);
                analysisList.add(boundaryChangedAnalysis);
                if (i - 1 > 0) {
                    HandicapChange latestPrev = handicapChangeList.get(i - 1);
                    HandicapAnalysis waterChangedAnalysis = createWaterChangedAnalysis(latestPrev, changed);
                    analysisList.add(waterChangedAnalysis);
                }
                changed = current;
                continue;
            }
            if (isWaterOverflow(current, changed)) {
                HandicapAnalysis waterOverflowAnalysis = createWaterChangedAnalysis(current, changed);
                analysisList.add(waterOverflowAnalysis);
                changed = current;
            }
        } // end for loop
        return analysisList;
    } // end analyze method

    private HandicapAnalysis createWaterChangedAnalysis(HandicapChange current, HandicapChange changed) {
        WaterChange waterChange = getWaterChange(current, changed);
        HandicapAnalysis analysis = partialCopy(current);
        analysis.setWaterChange(waterChange);
        return analysis;
    }

    private WaterChange getWaterChange(HandicapChange current, HandicapChange changed) {
        final double overflowThreshold = handicapAnalysisConfig.getOverflowThreshold();
        double delta;
        if (changed.getBoundary() < 0 || (changed.getBoundary() == 0 && (changed.getHome() <= changed.getAway()))) {
            if (changed.getHome() < current.getHome()) {
                delta = Math.abs(changed.getHome() - current.getHome());
                return Math.abs(delta) >= overflowThreshold ? WaterChange.UP_OVERFLOW : WaterChange.UP;
            } else if (changed.getHome() > current.getHome()) {
                delta = Math.abs(changed.getHome() - current.getHome());
                return Math.abs(delta) >= overflowThreshold ? WaterChange.DOWN_OVERFLOW : WaterChange.DOWN;
            }
        } else if (changed.getBoundary() > 0 || (changed.getBoundary() == 0 && (changed.getHome() > changed.getAway()))) {
            if (changed.getAway() < current.getAway()) {
                delta = Math.abs(changed.getAway() - current.getAway());
                return Math.abs(delta) >= overflowThreshold ? WaterChange.UP_OVERFLOW : WaterChange.UP;
            } else if (changed.getAway() > current.getAway()) {
                delta = Math.abs(changed.getAway() - current.getAway());
                return Math.abs(delta) >= overflowThreshold ? WaterChange.DOWN_OVERFLOW : WaterChange.DOWN;
            }
        }
        return WaterChange.KEEP;
    }

    private boolean isWaterOverflow(HandicapChange current, HandicapChange changed) {
        if (current.getBoundary() != changed.getBoundary()) {
            log.debug("Boundary not equals, skip to check if water overflow");
            return false;
        }
        double delta;
        if (changed.getBoundary() < 0) {
            delta = Math.abs(changed.getHome() - current.getHome());
        } else if (changed.getBoundary() > 0) {
            delta = Math.abs(changed.getAway() - current.getAway());
        } else { // 0 boundary
            if (changed.getHome() <= changed.getAway()) {
                delta = Math.abs(changed.getHome() - current.getHome());
            } else {
                delta = Math.abs(changed.getAway() - current.getAway());
            }
        }
        double overflowThreshold = handicapAnalysisConfig.getOverflowThreshold();
        return delta >= overflowThreshold;
    }

    // @formatter:off
    // -------current-----changed------0-------------------------------->
    // -------changed-----current------0-------------------------------->
    // --------------------------------0--------changed----current------>
    // --------------------------------0--------current----changed------>
    // -------------------changed------0--------current----------------->
    // -------------------current------0--------changed----------------->
    // @formatter:on
    private HandicapAnalysis createBoundaryChangedAnalysis(HandicapChange current, HandicapChange changed) {
        BoundaryChange boundaryChange = BoundaryChange.KEEP;
        double currBoundary = current.getBoundary();
        double changedBoundary = changed.getBoundary();
        if (currBoundary < changedBoundary && changedBoundary <= 0) {
            boundaryChange = BoundaryChange.UP;
        } else if (changedBoundary < currBoundary && currBoundary <= 0) {
            boundaryChange = BoundaryChange.DOWN;
        } else if (0 >= changedBoundary && changedBoundary > currBoundary) {
            boundaryChange = BoundaryChange.UP;
        } else if (0 >= currBoundary && currBoundary < changedBoundary) {
            boundaryChange = BoundaryChange.DOWN;
        }
        HandicapAnalysis analysis = partialCopy(current);
        analysis.setBoundaryChange(boundaryChange);
        return analysis;
    }

    private boolean isBoundaryChanged(HandicapChange current, HandicapChange changed) {
        return current.getBoundary() != changed.getBoundary();
    }

    private HandicapAnalysis createFirstAnalysis(HandicapChange change) {
        HandicapAnalysis first = partialCopy(change);
        first.setFirst(true);
        return first;
    }

    private HandicapAnalysis partialCopy(HandicapChange change) {
        HandicapAnalysis analysis = new HandicapAnalysis();
        analysis.setAnalysisTime(analysisTime);
        analysis.setUpdateTime(change.getUpdateTime());
        analysis.setHandicap(change.getHandicap());
        analysis.setKickoffTime(change.getKickoffTime());
        analysis.setHome(change.getHome());
        analysis.setBoundary(change.getBoundary());
        analysis.setAway(change.getAway());
        return analysis;
    }
}
