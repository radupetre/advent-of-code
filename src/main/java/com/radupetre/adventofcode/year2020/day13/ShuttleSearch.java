package com.radupetre.adventofcode.year2020.day13;

import static com.radupetre.adventofcode.utils.StringUtility.getLines;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Solving of https://adventofcode.com/2020/day/13
 */
@Service
@Log4j2
public class ShuttleSearch extends AbstractAdventSolution {

  private static final String OUT_OF_SERVICE = "x";
  private static final String BUS_INFO_SEPARATOR = ",";

  @Override
  public SolveContext getSolveContext() {
    return new SolveContext(2020, 13);
  }

  @Override
  public Object solvePart1(String input) {
    final List<String> lines = getLines(input);
    final int departingTime = parseInt(lines.get(0));
    final List<BusInfo> busInfos = getBusInfos(lines.get(1));

    return getMinWaitingTimeTimesBusNumber(busInfos, departingTime);
  }

  @Override
  public Object solvePart2(String input) {
    final List<String> lines = getLines(input);
    final List<BusInfo> busInfos = getBusInfos(lines.get(1));

    return getEarliestTimestampOfConsecutiveDepartures(busInfos);
  }

  private long getEarliestTimestampOfConsecutiveDepartures(List<BusInfo> busInfos) {
    /**
     * Start searching increments of "firstBusTime"
     * once it matches sequence with second, increment by firstBusTime * secondBusTime
     * once it matches sequence with third, increment by firstBusTime * secondBusTime * thirdBusTime
     * repeat until all busses are considered in sequence.
     */

    long earliestTimestamp = 0;
    int nextBusInSequence = 1;
    long incrementToNextMatch = busInfos.get(0).busId;

    while (true) {
      earliestTimestamp += incrementToNextMatch;

      int nextBussOrderOffset = busInfos.get(nextBusInSequence).busOrder;
      int nextBussId = busInfos.get(nextBusInSequence).busId;
      if ((earliestTimestamp + nextBussOrderOffset) % nextBussId == 0) {
        // matched another buss in sequence

        if (nextBusInSequence == busInfos.size() - 1) {
          // last bus matches sequence, found latest timestamp;
          return earliestTimestamp;

        } else {
          // start matching next bus in sequence, with bigger increment
          incrementToNextMatch *= nextBussId;
          nextBusInSequence++;

        }
      } else {
        // keep searching with current increment
        continue;

      }
    }
  }

  private long getMinWaitingTimeTimesBusNumber(List<BusInfo> busInfos, int departingTime) {
    int minWaitingTime = Integer.MAX_VALUE;
    int busNumber = Integer.MAX_VALUE;

    for (BusInfo busInfo : busInfos) {
      int busTime = busInfo.busId;
      int timeSinceLastBus = departingTime % busTime;

      int waitingTime = timeSinceLastBus == 0
          ? 0
          : busTime - timeSinceLastBus;

      if (waitingTime < minWaitingTime) {
        minWaitingTime = waitingTime;
        busNumber = busTime;
      }
    }

    return (long) minWaitingTime * busNumber;
  }

  private List<BusInfo> getBusInfos(String busInfosLine) {
    final String[] busTimes = busInfosLine.split(BUS_INFO_SEPARATOR);

    return range(0, busTimes.length)
        .filter(busOrder -> isInService(busTimes[busOrder]))
        .mapToObj(busOrder -> new BusInfo(busOrder, parseInt(busTimes[busOrder])))
        .collect(toList());
  }

  private static boolean isInService(String busTime) {
    return !OUT_OF_SERVICE.equals(busTime);
  }
}

