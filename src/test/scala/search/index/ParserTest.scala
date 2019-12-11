package search.index

import java.io.File

import org.scalatest._
import org.scalatest.matchers.should.Matchers
import search.model.Filename

class ParserTest extends FlatSpec with Matchers {

  "Parse folder with files" should "return index with content of all readable files" in {
    val expected = new Index(Map(
      Filename("file1.txt") -> Set("word2", "565464335", "word3", "word1", "word4"),
      Filename("file2.txt") -> Set("apple", "banana", "word45")
    ))
    val file = new File("src//test//resources//test1")

    FileParser.parse(file) should be (expected)
  }

  "Parse folder with subfolders" should "return index with content of all readable files in folder and subfolders" in {
    val expected = new Index(Map(
      Filename("file1.txt") -> Set("word2", "565464335", "word3", "word1", "word4"),
      Filename("file_inside/file2.txt") -> Set("apple", "banana", "word45")
    ))
    val file = new File("src//test//resources//test2")

    FileParser.parse(file) should be (expected)
  }

  "Parse folder with no files" should "return index with no content" in {
    val file = new File("src//test//resources//test3")

    FileParser.parse(file) should be (new Index(Map.empty))
  }

  "Parse file with foreign words and words with numbers" should "correctly parse them and return corresponding index" in {
    val expected = new Index(Map(
      Filename("polyglot_file.txt") -> Set("ёж", "бык", "звір", "español", "narodowość", "3D", "1234")
    ))
    val file = new File("src//test//resources//test4")

    FileParser.parse(file) should be (expected)
  }

  "Parse file with noise" should "parse only words/numbers and ignore symbol noise" in {
    val expected = new Index(Map(
      Filename("file_with_noise.txt") -> Set("ёж", "бык", "звір", "español", "narodowość", "3D", "1234")
    ))
    val file = new File("src//test//resources//test5")

    FileParser.parse(file) should be (expected)
  }

}
