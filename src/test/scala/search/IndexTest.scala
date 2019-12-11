package search

import org.scalatest._
import org.scalatest.matchers.should.Matchers

class IndexTest extends FlatSpec with Matchers {

  "Search with words from file" should "give 100% match" in {
    val map: Map[String, Set[String]] = Map("file1" -> Set("banana", "apple", "orange", "pizza", "water"))
    val index = new Index(map)

    index.search(Set("orange", "apple", "banana")) should be (Map[String, Double]("file1" -> 100))
  }
}
