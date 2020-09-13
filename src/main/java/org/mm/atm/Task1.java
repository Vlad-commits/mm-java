package org.mm.atm;

public class Task1 {
  public static void main(String[] args) {
    final var cli = new Cli().start();
    final var atm = new Atm(cli.getDenominations());
    atm.solve(cli.getValue())
        .subscribe(System.out::println);
  }
}
