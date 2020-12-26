package com.radupetre.adventofcode.year2020.day02;

import static java.lang.Integer.parseInt;

class PasswordPolicy {

  final int minOccurrence;
  final int maxOccurrence;
  final char mandatoryChar;
  final String password;

  PasswordPolicy(String passwordPolicy) {
    final String[] policyFragments = passwordPolicy.split(" ");
    this.password = policyFragments[2].trim();
    this.mandatoryChar = policyFragments[1].charAt(0);

    final String[] occurrenceFragments = policyFragments[0].split("-");
    this.minOccurrence = parseInt(occurrenceFragments[0]);
    this.maxOccurrence = parseInt(occurrenceFragments[1]);
  }
}
