package search

import java.io.File

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

  def iterate(index: Index): Unit = {
    print(s"search> ")
    val line = readLine()
    if (line != ExitPhrase) {
      val words: Set[String] = line.split("//s").toSet
      val topResult = index.search(words).toList.sortWith(_._2 > _._2).take(10)
      println(topResult)
      iterate(index)
    }
  }
}
