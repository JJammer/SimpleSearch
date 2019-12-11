package search

import org.scalatest._
import org.scalatest.matchers.should.Matchers
import java.io.File

class IndexTest extends FlatSpec with Matchers {

  "Search with words from file" should "give 100% match" in {
    val map: Map[String, Set[String]] = Map("file1" -> Set("banana", "apple", "orange", "pizza", "water"))
    val index = new Index(map)

    index.search(Set("orange", "apple", "banana")) should be (Map("file1" -> 100))
  }

  "Search with no words from file" should "give 0% match" in {
    val map: Map[String, Set[String]] = Map("file1" -> Set("banana", "apple", "orange", "pizza", "water"))
    val index = new Index(map)

    index.search(Set("silver", "gold")) should be (Map("file1" -> 0))
  }

  "Search that found 1 word from 2 expected" should "give 50% match" in {
    val map: Map[String, Set[String]] = Map("file1" -> Set("banana", "apple", "orange", "pizza", "water"))
    val index = new Index(map)

    index.search(Set("orange", "lemon")) should be (Map("file1" -> 50))
  }

  "Search in different files" should "give different match" in {
    val map: Map[String, Set[String]] = Map(
      "file1" -> Set("banana", "apple", "orange", "pizza", "water"),
      "file2" -> Set("orange", "banana"),
      "file3" -> Set("fork")
    )
    val index = new Index(map)

    index.search(Set("orange", "apple")) should be (Map("file1" -> 100, "file2" -> 50, "file3" -> 0))
  }

  "Parse folder with files" should "return index with content of all readable files" in {
    val expected = new Index(Map(
      "file1.txt" -> Set("word2", "565464335", "word3", "word1", "word4"),
      "file2.txt" -> Set("apple", "banana", "word45")
    ))
    val file = new File("src//test//resources//test1")

    Index(file) should be (expected)
  }

  "Parse folder with subfolders" should "return index with content of all readable files in folder and subfolders" in {
    val expected = new Index(Map(
      "file1.txt" -> Set("word2", "565464335", "word3", "word1", "word4"),
      "file2.txt" -> Set("apple", "banana", "word45")
    ))
    val file = new File("src//test//resources//test2")

    Index(file) should be (expected)
  }

  "Parse folder with no files" should "return index with no content" in {
    val file = new File("src//test//resources//test3")

    Index(file) should be (new Index(Map.empty))
  }
}
