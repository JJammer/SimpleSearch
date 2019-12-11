package search

import java.io.File

import scala.io.StdIn.readLine
import scala.util.Try

object Program {

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
    val words: Set[String] = readLine().split(" ").toSet
    println(index.search(words))
    iterate(index)
  }
}
