package search

import java.io.File

import search.index.Index
import search.model.{FileNotFound, Filename, MissingPathArg, NotDirectory, ReadFileError}

import scala.annotation.tailrec
import scala.io.StdIn.readLine
import scala.util.Try

object Program {

  val ExitPhrase = ":quit"

  def readFile(args: Array[String]): Either[ReadFileError, File] = {
    for {
      path <- args.headOption.toRight(MissingPathArg)
      file <- Try(new java.io.File(path))
        .fold(
          throwable => Left(FileNotFound(throwable)),
          file =>
            if (file.isDirectory) Right(file)
            else Left(NotDirectory(s"Path [$path] is not a directory"))
        )
    } yield file
  }

  @tailrec
  def iterate(index: Index): Unit = {
    println(s"search> ")
    val line = readLine()
    if (line != ExitPhrase) {
      val words: Set[String] = line.split("\\s+").toSet
      printByScore(index.search(words))(10)
      iterate(index)
    }
  }

  def printByScore(result: Map[Filename, Double])(limit: Int): Unit = {
    result.toList.sortWith(_._2 > _._2).take(limit).foreach{ case (filename, score) => {
      printDelimiter()
      printf("%-30s", filename.value)
      print("|")
      printf("%6.2f%%%n", score)
    }}
    println()
  }

  def printDelimiter(): Unit = println(List.fill(40)('-').mkString)

}
