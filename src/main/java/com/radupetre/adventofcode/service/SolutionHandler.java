package com.radupetre.adventofcode.service;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.radupetre.adventofcode.solution.AbstractAdventSolution;
import com.radupetre.adventofcode.solution.SolveContext;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

@Service
@Log4j2
public class SolutionHandler {

  private final static String INPUT_PATH_TEMPLATE = "classpath:input/year%d/day%02d/input.txt";

  private final static String OUTPUT_PATH_TEMPLATE = "classpath:output/year%d/day%02d/output.txt";

  public void handle(AbstractAdventSolution adventSolution) {
    final SolveContext solveContext = adventSolution.getSolveContext();
    String input = fetchInput(solveContext);
    log.info(String.format("Solving advent for year:%s day:%s", solveContext.getYear(),
        solveContext.getDay()));
    adventSolution.solve(input);
  }

  public String fetchInput(SolveContext solveContext) {
    return fetchFileContents(createInputPath(solveContext));
  }

  private String createInputPath(SolveContext solveContext) {
    return String
        .format(INPUT_PATH_TEMPLATE, solveContext.getYear(), solveContext.getDay());
  }

  public String fetchOutput(SolveContext solveContext) {
    return fetchFileContents(createOutputPath(solveContext));
  }

  private String createOutputPath(SolveContext solveContext) {
    return String
        .format(OUTPUT_PATH_TEMPLATE, solveContext.getYear(), solveContext.getDay());
  }

  private String fetchFileContents(String path) {
    ResourceLoader resourceLoader = new DefaultResourceLoader();
    Resource resource = resourceLoader.getResource(path);

    try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
      return FileCopyUtils.copyToString(reader);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}
