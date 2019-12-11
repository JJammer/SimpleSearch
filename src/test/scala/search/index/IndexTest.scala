package search.index

import org.scalatest._
import org.scalatest.matchers.should.Matchers
import search.model.Filename

class IndexTest extends FlatSpec with Matchers {

  "Search with words from file" should "give 100% match" in {
    val map: Map[Filename, Set[String]] = Map(Filename("file1") -> Set("banana", "apple", "orange", "pizza", "water"))
    val index = new Index(map)

    index.search(Set("orange", "apple", "banana")) should be (Map(Filename("file1") -> 100))
  }

  "Search with no similar words from file" should "give 0% match" in {
    val map: Map[Filename, Set[String]] = Map(Filename("file1") -> Set("banana", "apple", "orange", "pizza", "water"))
    val index = new Index(map)

    index.search(Set("coco")) should be (Map(Filename("file1") -> 0))
  }

  "Search that found 1 word from 2 expected" should "give 50% match" in {
    val map: Map[Filename, Set[String]] = Map(Filename("file1") -> Set("banana", "apple", "orange", "pizza", "water"))
    val index = new Index(map)

    index.search(Set("orange", "lemon")) should be (Map(Filename("file1") -> 50))
  }

  "Search in different files" should "give different match" in {
    val map: Map[Filename, Set[String]] = Map(
      Filename("file1") -> Set("banana", "apple", "orange", "pizza", "water"),
      Filename("file2") -> Set("orange", "banana")
    )
    val index = new Index(map)

    index.search(Set("orange", "apple")) should be (Map(Filename("file1") -> 100, Filename("file2") -> 50))
  }

  "Search that found similar word" should "not give 0% match" in {
    val map: Map[Filename, Set[String]] = Map(
      Filename("file1") -> Set("mama"),
    )

    val index = new Index(map)

    index.search(Set("papa")) should be (Map(Filename("file1") -> 50))
  }
}
