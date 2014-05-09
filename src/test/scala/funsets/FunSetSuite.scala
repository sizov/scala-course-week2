package funsets

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 * - run the "test" command in the SBT console
 * - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {


  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   * - test
   * - ignore
   * - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  test("string take") {
    val message = "hello, world"
    assert(message.take(5) == "hello")
  }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  test("adding ints") {
    assert(1 + 2 === 3)
  }


  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   * val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)

    val from1to10 = fromToSet(1, 10)
    val from3to6 = fromToSet(3, 6)
    val from7to15 = fromToSet(7, 15)
    val from13to20 = fromToSet(13, 20)

    val paired = fromToSet(13, 20);
    val allPositivePlusNegative5 = allPositivePlusNegative5Set()
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singleton test") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3". 
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton of 1 contains 1")
      assert(!contains(s1, 2), "Singleton of 1 does not contain 2")
      assert(!contains(s1, -1), "Singleton of 1 does not contain -1")
      assert(!contains(s1, 0), "Singleton of 1 does not contain 0")
    }
  }

  test("union contains all elements") {
    new TestSets {
      val union1 = union(s1, s2)

      assert(contains(union1, 1), "Union 1")
      assert(contains(union1, 2), "Union 2")
      assert(!contains(union1, 3), "Union 3")

      assert(!contains(union1, 4), "Union 4")
      assert(!contains(union1, 0), "Union 5")
      assert(!contains(union1, 0), "Union 6")

      val union2 = union(s2, s3)

      assert(!contains(union2, 1), "Union 7")
      assert(contains(union2, 2), "Union 8")
      assert(contains(union2, 3), "Union 9")

    }
  }

  test("intersect test") {
    new TestSets {
      val intersect7to10 = intersect(from1to10, from7to15)

      assert(!contains(intersect7to10, 1), "intersect 1")
      assert(!contains(intersect7to10, 6), "intersect 2")
      assert(!contains(intersect7to10, 11), "intersect 3")
      assert(!contains(intersect7to10, 12), "intersect 4")

      assert(contains(intersect7to10, 7), "intersect 5")
      assert(contains(intersect7to10, 8), "intersect 6")
      assert(contains(intersect7to10, 9), "intersect 7")
      assert(contains(intersect7to10, 10), "intersect 8")

      val intersect13to15 = intersect(from7to15, from13to20)

      assert(!contains(intersect13to15, 12), "intersect 9")
      assert(!contains(intersect13to15, 16), "intersect 10")

      assert(contains(intersect13to15, 14), "intersect 11")
      assert(contains(intersect13to15, 13), "intersect 12")
      assert(contains(intersect13to15, 15), "intersect 13")

      val intersectNoMatch = intersect(from3to6, from7to15)

      assert(!contains(intersectNoMatch, 5), "intersect 14")
      assert(!contains(intersectNoMatch, 6), "intersect 15")
      assert(!contains(intersectNoMatch, 7), "intersect 16")
      assert(!contains(intersectNoMatch, 8), "intersect 17")
    }
  }

  test("diff test") {
    new TestSets {
      val diff1 = diff(from1to10, from7to15)

      assert(contains(diff1, 1), "diff 1")
      assert(contains(diff1, 6), "diff 2")
      assert(!contains(diff1, 7), "diff 3")
      assert(!contains(diff1, 8), "diff 4")

    }
  }

  test("filter test") {
    new TestSets {
      val filteredFrom1To10Except5 = filter(from1to10, (x: Int) => x != 5)

      assert(contains(filteredFrom1To10Except5, 1), "filter 1")
      assert(contains(filteredFrom1To10Except5, 2), "filter 2")
      assert(contains(filteredFrom1To10Except5, 3), "filter 3")
      assert(contains(filteredFrom1To10Except5, 10), "filter 4")
      assert(!contains(filteredFrom1To10Except5, 5), "filter 5")

      val filteredFrom3To6Except0 = filter(from3to6, (x: Int) => x != 0)

      assert(contains(filteredFrom3To6Except0, 3), "filter 6")
      assert(contains(filteredFrom3To6Except0, 4), "filter 7")
      assert(contains(filteredFrom3To6Except0, 5), "filter 8")
      assert(contains(filteredFrom3To6Except0, 6), "filter 9")

    }
  }

  test("forall test") {
    new TestSets {
      assert(!forall(from13to20, (x: Int) => x % 2 == 0), "forall 1")
      assert(forall(from13to20, (x: Int) => x / 1 == x), "forall 2")
      assert(!forall(allPositivePlusNegative5, (x: Int) => x > 1), "forall 3")
      assert(forall(allPositivePlusNegative5, (x: Int) => x > 0 || x == -5), "forall 4")
    }
  }

  test("exists test") {
    new TestSets {
      assert(!exists(from13to20, (x: Int) => x < 13), "exists 1")
      assert(exists(from13to20, (x: Int) => x == 13), "exists 2")
      assert(exists(allPositivePlusNegative5, (x: Int) => x == -5), "exists 3")
      assert(exists(allPositivePlusNegative5, (x: Int) => x > 0), "exists 4")
      assert(exists(allPositivePlusNegative5, (x: Int) => x < 5), "exists 5")
    }
  }

}
