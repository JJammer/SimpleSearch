package search.index

import java.io.File

import search.model.Filename

import scala.annotation.tailrec
import scala.io.Source
import scala.util.Using

trait Parser {
  def parse(file: File): Index
}

object FileParser extends Parser {

  // it will match any kind of letter from any language and any kind of numeric character
  val WordPattern = "[\\p{L}\\p{N}]+".r

  override def parse(dir: File): Index = {
    @tailrec
    def parse(file: File,
              acc: Map[Filename, Set[String]] = Map.empty,
              unread: List[File] = List.empty) : Map[Filename, Set[String]] = {

      val files = if (file.isDirectory) unread ++ file.listFiles().toList else unread
      val localAcc = if (file.isDirectory) acc
      else acc + (Filename(dir.toPath.relativize(file.toPath).toString) -> parseFileContent(file))

      files match {
        case Nil => localAcc
        case head :: tail => parse(head, localAcc, tail)
      }
    }

    new Index(parse(dir))
  }

  private def parseFileContent(file: File): Set[String] =
    Using(Source.fromFile(file)) {
      source => source.getLines().flatMap(WordPattern.findAllIn).toSet
    }.getOrElse(Set.empty)

}