package search.index

import java.io.File

import search.model.Filename


case class Index private[index](filesToContent: Map[Filename, Set[String]]) {

  def search(words: Set[String]): Map[Filename, Double] = filesToContent.map {
    case (filename, fileWords) => (filename, matchingScore(words, fileWords))
  }

  private def matchingScore(searchWords: Set[String], content: Set[String]): Double = {
    (searchWords.toList.map(search => content.toList.map(word => comparisonScore(search, word)).max).sum * 100) / searchWords.size

    // old scoring method
    //(searchWords.count(word => content.contains(word)) * 100) / searchWords.size
  }

  private def comparisonScore(expected: String, actual: String): Double = {
    val dist = levenshteinDistance(expected, actual)
    if (dist >= expected.length) 0 else (expected.length - dist).toDouble / expected.length
  }

  private def levenshteinDistance(a: String, b: String): Int = {
    val startRow = (0 to b.length).toList
    a.foldLeft(startRow) { (prevRow, aElem) =>
      (prevRow.zip(prevRow.tail).zip(b)).scanLeft(prevRow.head + 1) {
        case (left, ((diag, up), bElem)) => {
          val aGapScore = up + 1
          val bGapScore = left + 1
          val matchScore = diag + (if (aElem == bElem) 0 else 1)
          List(aGapScore, bGapScore, matchScore).min
        }
      }
    }.last
  }

}

object Index {
  def apply(file: File): Index = FileParser.parse(file)
}
