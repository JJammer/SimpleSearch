package search

import java.io.File

case class Index private[search](map: Map[String, Set[String]]) {

  // TODO add logic
  def search(words: Set[String]): Map[String, Double] = ???
}

object Index {

  //TODO add parsing logic
  def apply(file: File): Index = new Index(Map.empty)
}
