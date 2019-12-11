package search

import java.io.File

import scala.io._
import scala.util.Using

case class Index private[search](map: Map[String, Set[String]]) {

  def search(words: Set[String]): Map[String, Double] = map.map(entry => (entry._1, search(words, entry._2)))

  def search(searchWords: Set[String], content: Set[String]): Double =
    (searchWords.count(word => content.contains(word)) * 100) / searchWords.size
}

object Index {

  def apply(file: File): Index = {

    def parseFile(file: File): Set[String] =
      Using(Source.fromFile(file)) {
        source => source.getLines().flatMap(x => x.split("\\s")).filter(!_.isBlank).toSet
      }.getOrElse(Set.empty)

    def parse(file: File, acc: Map[String, Set[String]] = Map.empty, unread: List[File] = List.empty) : Map[String, Set[String]] = {

      val files = if (file.isDirectory) unread ++ file.listFiles().toList else unread
      val localAcc = if (file.isDirectory) acc else acc + (file.getName -> parseFile(file))

      files match {
        case Nil => localAcc
        case head :: tail => parse(head, localAcc, tail)
      }
    }

    new Index(parse(file))
  }
}
