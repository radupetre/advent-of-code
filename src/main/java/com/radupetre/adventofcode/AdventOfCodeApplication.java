package com.radupetre.adventofcode;

import com.radupetre.adventofcode.service.SolutionOrchestrator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AdventOfCodeApplication {

  public static void main(String[] args) {
    final ConfigurableApplicationContext context = SpringApplication
        .run(AdventOfCodeApplication.class, args);
    SolutionOrchestrator solutionOrchestrator = context.getBean(SolutionOrchestrator.class);

    solutionOrchestrator.runSolutions();
  }

}
