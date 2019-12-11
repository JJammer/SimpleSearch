package search

import search.index.Index

object Main extends App {
  Program
    .readFile(args)
    .fold(
      println,
      file => Program.iterate(Index(file))
    )
}
