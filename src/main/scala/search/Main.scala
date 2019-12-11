package search

object Main extends App {
  Program
    .readFile(args)
    .fold(
      println,
      file => Program.iterate(Index(file))
    )
}
