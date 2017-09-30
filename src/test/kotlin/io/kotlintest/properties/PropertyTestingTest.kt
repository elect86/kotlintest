package io.kotlintest.properties

import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldThrow
import io.kotlintest.specs.StringSpec

class PropertyTestingTest : StringSpec() {
  init {

    "startsWith" {
      var actualAttempts = 0
      forAll(Gen.string(), Gen.string(), { a, b ->
        actualAttempts++
        (a + b).startsWith(a)
      })
      actualAttempts shouldBe 1000
    }

    "endsWith a hundred times" {
      var actualAttempts = 0
      forAll(100, Gen.string(), Gen.string()) { a, b ->
        actualAttempts++
        (a + b).endsWith(b)
      }
      actualAttempts shouldBe 100
    }

    "size" {
      var actualAttempts = 0
      forAll { a: String, b: String ->
        actualAttempts++
        (a + b).length == a.length + b.length
      }
      actualAttempts shouldBe 1000
    }

    "failure after 4 attempts" {
      var element1 = ""
      var element2 = ""
      val exception =
          shouldThrow<AssertionError> {
            var attempts = 0
            forAll(20, Gen.string(), Gen.string()) { a, b ->
              element1 = a
              element2 = b
              attempts++
              attempts < 4
            }
          }
      exception.message shouldBe "Property failed for\n$element1\n$element2\nafter 4 attempts"
    }

    "failure after 50 attempts" {
      var element1 = ""
      var element2 = ""
      val exception =
          shouldThrow<AssertionError> {
            var attempts = 0
            forAll(200, Gen.string(), Gen.string()) { a, b ->
              element1 = a
              element2 = b
              attempts++
              attempts < 50
            }
          }
      exception.message shouldBe "Property failed for\n$element1\n$element2\nafter 50 attempts"
    }

    "running for 0 attempts" {
      val exception = shouldThrow<IllegalArgumentException> {
        forAll(0, Gen.string(), Gen.string()) { a, b ->
          (a + b).startsWith(a)
        }
      }
      exception.message shouldBe "The number of attempts should be a positive number"
    }

    "running for -1 attempts" {
      val exception = shouldThrow<IllegalArgumentException> {
        forAll(-1, Gen.string(), Gen.string()) { a, b ->
          (a + b).startsWith(a)
        }
      }
      exception.message shouldBe "The number of attempts should be a positive number"
    }

    "explicitGenerators" {
      forAll(Gen.string(), Gen.string(), Gen.string(), { a, b, c ->
        (a + b + c).contains(b)
      })
    }

    "inferredGenerators" {
      forAll { a: String, b: String, c: String, d: String ->
        (a + b + c + d).contains(a)
        (a + b + c + d).contains(b)
        (a + b + c + d).contains(c)
        (a + b + c + d).contains(d)
      }
    }

    "forAll single generator explicit 1000 attempts" {
      var attempts = 0
      forAll(Gen.int()) { a ->
        attempts++
        Math.abs(a) > 0
      }

      attempts shouldBe 1000
    }

    "forAll single generator explicit 20 attempts" {
      var attempts = 0
      forAll(20, Gen.int()) { a ->
        attempts++
        a * 2 == 2 * a
      }
      attempts shouldBe 20
    }

    "forAll single gnerator explicit 500 atempts" {
      var attempts = 0
      forAll(500, Gen.positiveIntegers()) { a ->
        attempts++
        a + 2 > a
      }
      attempts shouldBe 500
    }

    "forAll one explicit generator: test fails after second attempt" {
      var attempts = 0
      var element = 0.0
      val exception = shouldThrow<AssertionError> {
        forAll(4, Gen.double()) { a ->
          attempts++
          element = a
          attempts < 2
        }
      }
      exception.message shouldBe "Property failed for\n$element\nafter 2 attempts"
    }

    "forAll one explicit generator: test fails after 300 attempts" {
      var attempts = 0
      var element = ""
      val exception = shouldThrow<AssertionError> {
        forAll(350, Gen.string()) { s ->
          element = s
          attempts++
          attempts < 300
        }
      }

      exception.message shouldBe "Property failed for\n$element\nafter 300 attempts"
    }

    "forALl one explicit generator with 0 attempts" {
      val exception = shouldThrow<IllegalArgumentException> {
        forAll(0, Gen.long()) { a ->
          a > a - 1
        }
      }
      exception.message shouldBe "Attempts should be a positive number"
    }

    "forAll one explicit generator with -2 attempts" {
      val exception = shouldThrow<IllegalArgumentException> {
        forAll(-2, Gen.long()) { a ->
          a > a - 1
        }
      }
      exception.message shouldBe "Attempts should be a positive number"
    }

    "forAll one generator implicit 10 attempts" {
      var attempts = 0
      forAll(10) { a: Int ->
        attempts++
        2 * a % 2 == 0
      }
      attempts shouldBe 10
    }

    "forAll: one generator implicit 200 attempts" {
      var attempts = 0
      forAll(200) { a: Int ->
        attempts++
        2 * a % 2 == 0
      }
      attempts shouldBe 200
    }

    "forAll: two generators implicit 30 attempts" {
      var attempts = 0
      forAll(30) { a: String, b: String ->
        attempts++
        (a + b).startsWith(a)
      }
      attempts shouldBe 30
    }

    "forAll: Two implicit generators 500 attempts" {
      var attempts = 0
      forAll(500) { a: Int, b: Int ->
        attempts++
        a * b == b * a
      }
      attempts shouldBe 500
    }

    "pad" {
      forAll { a: Int, b: String ->
        a <= 0 || a > 100 || b.padStart(a, ' ').length >= a
      }
    }

    "double" {
      forAll { a: Double, b: Double ->
        a < b || a >= b
      }
    }

    "forAll: Three explicit generators 1000 attempts" {
      var attempts = 0
      forAll(Gen.int(), Gen.int(), Gen.int()) { a, b, c ->
        attempts++
        (a + b) + c == a + (b + c)
      }
      attempts shouldBe 1000
    }

    "forAll: Three explicit generators 30 attempts" {
      var attempts = 0
      forAll(30, Gen.int(), Gen.int(), Gen.int()) { a, b, c ->
        attempts++
        (a * b) * c == a * (b * c)
      }
      attempts shouldBe 30
    }

    "forAll: Three explicit generators 0 attempts" {
      val exception = shouldThrow<IllegalArgumentException> {
        forAll(0, Gen.int(), Gen.int(), Gen.int()) { a, b, c ->
          true
        }
      }
      exception.message shouldBe "Attempts should be a positive number"
    }

    "forAll: Three explicit generators -3 attempts" {
      val exception = shouldThrow<IllegalArgumentException> {
        forAll(-3, Gen.string(), Gen.int(), Gen.double()) { a, b, c ->
          true
        }
      }
      exception.message shouldBe "Attempts should be a positive number"
    }

    "forAll: Three explicit generators failure at the third attempt" {
      var attempts = 0
      var elementA = 0
      var elementB = 0
      var elementC = 0
      val exception = shouldThrow<AssertionError> {
        forAll(50, Gen.int(), Gen.int(), Gen.int()) { a, b, c ->
          elementA = a
          elementB = b
          elementC = c
          attempts++
          attempts < 3
        }
      }

      exception.message shouldBe "Property failed for\n$elementA\n$elementB\n$elementC\nafter 3 attempts"
    }

    "forAll : Three explicit generators failure at the 26th attempt" {
      var attempts = 0
      var elementA = 0
      var elementB = 0
      var elementC = 0
      val exception = shouldThrow<AssertionError> {
        forAll(50, Gen.int(), Gen.int(), Gen.int()) { a, b, c ->
          elementA = a
          elementB = b
          elementC = c
          attempts++
          attempts < 26
        }
      }

      exception.message shouldBe "Property failed for\n$elementA\n$elementB\n$elementC\nafter 26 attempts"
    }

    "forAll : Three implicit generators 1000 attempts" {
      var attempts = 0
      forAll { a: Int, b: Int, c: Int ->
        attempts++
        a + b + c == (a + b) + c
      }
      attempts shouldBe 1000
    }

    "forAll: Three implicit generators 26 attempts" {
      var attempts = 0
      forAll(26) { a: Int, b: Int, c: Int ->
        attempts++
        a + b * c == (b * c) + a
      }
      attempts shouldBe 26
    }

    "forAll: Four explicit generators success after 1000 attempts" {
      var attempts = 0
      forAll(Gen.int(), Gen.int(), Gen.int(), Gen.int()) { a, b, c, d ->
        attempts++
        a + b + c + d == d + c + b + a
      }
      attempts shouldBe 1000
    }

    "forAll: Four explicit generators success after 50 attempts" {
      var attempts = 0
      forAll(50, Gen.int(), Gen.int(), Gen.int(), Gen.int()) { a, b, c, d ->
        attempts++
        a + b + c + d == d + c + b + a
      }
      attempts shouldBe 50
    }

    "forAll: Four explicit generators failed after 4 attempts" {
      var attempts = 0
      var elementA = 0
      var elementB = 0
      var elementC = 0
      var elementD = 0
      val exception = shouldThrow<AssertionError> {
        forAll(Gen.int(), Gen.int(), Gen.int(), Gen.int()) { a, b, c, d ->
          attempts++
          elementA = a
          elementB = b
          elementC = c
          elementD = d
          attempts < 4
        }
      }
      exception.message shouldBe "Property failed for \n$elementA\n$elementB\n$elementC\n$elementD\nafter $attempts attempts"
    }

    "forAll: Four explicit generators failed after 50 attempts" {
      var attempts = 0
      var elementA = 0
      var elementB = 0
      var elementC = 0
      var elementD = 0
      val exception = shouldThrow<AssertionError> {
        forAll(100, Gen.int(), Gen.int(), Gen.int(), Gen.int()) { a, b, c, d ->
          elementA = a
          elementB = b
          elementC = c
          elementD = d
          attempts++
          attempts < 50
        }
      }
      exception.message shouldBe "Property failed for \n$elementA\n$elementB\n$elementC\n$elementD\nafter $attempts attempts"
    }

    "forAll: Four explicit generators with -100 attempts" {
      val exception = shouldThrow<IllegalArgumentException> {
        forAll(-100, Gen.int(), Gen.int(), Gen.int(), Gen.int()) { a, b, c, d ->
          true
        }
      }

      exception.message shouldBe "Attempts should be a positive number"
    }

    "forAll: four explicit generators with 0 attempts" {
      val exception = shouldThrow<IllegalArgumentException> {
        forAll(0, Gen.int(), Gen.int(), Gen.int(), Gen.int()) { a, b, c, d ->
          true
        }
      }
    }

    "forAll: four implicit generators with 1000 attempts" {
      var attempts = 0
      forAll { a: Int, b: Int, c: Int, d: Int ->
        attempts++
        true
      }
      attempts shouldBe 1000
    }

    "forAll: four implicit generators with 20 attempts" {
      var attempts = 0
      forAll(20) { a: Int, b: Int, c: Int, d: Int ->
        attempts++
        true
      }
      attempts shouldBe 20
    }

    "forAll: four implicit generators with 250 attempts" {
      var attempts = 0
      forAll(250) { a: Int, b: Int, c: Int, d: Int ->
        attempts++
        true
      }
      attempts shouldBe 250
    }

    "forAll: five explicit generators with 1000 attempts" {
      var attempts = 0
      forAll(Gen.int(), Gen.int(), Gen.int(), Gen.int(), Gen.int()) { a, b, c, d, e ->
        attempts++
        true
      }
      attempts shouldBe 1000
    }

    "forAll: five explicit generators with 200 attempts" {
      var attempts = 0
      forAll(200, Gen.int(), Gen.int(), Gen.int(), Gen.int(), Gen.int()) { a, b, c, d, e ->
        attempts++
        true
      }
      attempts shouldBe 200
    }

    "forAll: five explicit generators failed after 10 attempts" {
      var attempts = 0
      var elementA = 0
      var elementB = 0
      var elementC = 0
      var elementD = 0
      var elementE = 0

      val exception = shouldThrow<AssertionError> {
        forAll(500, Gen.int(), Gen.int(), Gen.int(), Gen.int(), Gen.int()) { a, b, c, d, e ->
          elementA = a
          elementB = b
          elementC = c
          elementD = d
          elementE = e
          attempts++
          attempts < 10
        }
      }

      exception.message shouldBe "Property failed for \n$elementA\n$elementB\n$elementC\n$elementD\n$elementE\nafter $attempts attempts"
    }

    "forAll: five explicit generators failed after 50 attempts" {
      var attempts = 0
      var elementA = 0
      var elementB = 0
      var elementC = 0
      var elementD = 0
      var elementE = 0

      val exception = shouldThrow<AssertionError> {
        forAll(500, Gen.int(), Gen.int(), Gen.int(), Gen.int(), Gen.int()) { a, b, c, d, e ->
          elementA = a
          elementB = b
          elementC = c
          elementD = d
          elementE = e
          attempts++
          attempts < 50
        }
      }

      exception.message shouldBe "Property failed for \n$elementA\n$elementB\n$elementC\n$elementD\n$elementE\nafter $attempts attempts"
    }

    "forAll: five explicit generators with 0 attempts" {
      val exception = shouldThrow<IllegalArgumentException> {
        forAll(0, Gen.int(), Gen.int(), Gen.int(), Gen.int(), Gen.int()) { a, b, c, d, e ->
          true
        }
      }
      exception.message shouldBe "Attempts should be a positive number"
    }

    "forAll: five explicit generators with -20 attempts" {
      val exception = shouldThrow<IllegalArgumentException> {
        forAll(-20, Gen.int(), Gen.int(), Gen.int(), Gen.int(), Gen.int()) { a, b, c, d, e ->
          true
        }
      }
      exception.message shouldBe "Attempts should be a positive number"
    }

    "forAll five implicit generators with 1000 attempts" {
      var attempts = 0
      forAll { a: Int, b: Int, c: Int, d: Int, e: Int ->
        attempts++
        true
      }
      attempts shouldBe 1000
    }

    "forAll five implicit generators with 20 attempts" {
      var attempts = 0
      forAll(20) { a: Int, b: Int, c: Int, d: Int, e: Int ->
        attempts++
        true
      }
      attempts shouldBe 20
    }

    "forAll five implicit generators with 500 attempts" {
      var attempts = 0
      forAll(500) { a: Int, b: Int, c: Int, d: Int, e: Int ->
        attempts++
        true
      }
      attempts shouldBe 500
    }

    "forAll six explicit arguments with 1000 attempts" {
      var attempts = 0
      forAll(Gen.int(), Gen.int(), Gen.int(), Gen.int(), Gen.int(), Gen.int()) { a, b, c, d, e, f ->
        attempts++
        true
      }
      attempts shouldBe 1000
    }

    "forAll six explicit arguments with 50 attempts" {
      var attempts = 0
      forAll(50, Gen.int(), Gen.int(), Gen.int(), Gen.int(), Gen.int(), Gen.int()) { a, b, c, d, e, f ->
        attempts++
        true
      }
      attempts shouldBe 50
    }

    "forAll six explicit arguments failing at 40 attempts" {
      var attempts = 0
      var elementA = 0
      var elementB = 0
      var elementC = 0
      var elementD = 0
      var elementE = 0
      var elementF = 0
      val exception = shouldThrow<AssertionError> {
        forAll(500, Gen.int(), Gen.int(), Gen.int(), Gen.int(), Gen.int(), Gen.int()) { a, b, c, d, e, f ->
          elementA = a
          elementB = b
          elementC = c
          elementD = d
          elementE = e
          elementF = f
          attempts++
          attempts < 40
        }
      }
      exception.message shouldBe
          "Property failed for \n$elementA\n$elementB\n$elementC\n$elementD\n$elementE\n$elementF\nafter $attempts attempts"
    }

    "forAll six explicit arguments failing at 500 attempts" {
      var attempts = 0
      var elementA = 0
      var elementB = 0
      var elementC = 0
      var elementD = 0
      var elementE = 0
      var elementF = 0
      val exception = shouldThrow<AssertionError> {
        forAll(700, Gen.int(), Gen.int(), Gen.int(), Gen.int(), Gen.int(), Gen.int()) { a, b, c, d, e, f ->
          elementA = a
          elementB = b
          elementC = c
          elementD = d
          elementE = e
          elementF = f
          attempts++
          attempts < 500
        }
      }
      exception.message shouldBe
          "Property failed for \n$elementA\n$elementB\n$elementC\n$elementD\n$elementE\n$elementF\nafter $attempts attempts"
    }

    "forAll six explicit arguments with 0 attempts" {
      val exception = shouldThrow<IllegalArgumentException> {
        forAll(0, Gen.int(), Gen.int(), Gen.int(), Gen.int(), Gen.int(), Gen.int()) { a, b, c, d, e, f ->
          true
        }
      }
      exception.message shouldBe "Attempts should be a positive number"
    }

    "forAll six explicit arguments with -300 attempts" {
      val exception = shouldThrow<IllegalArgumentException> {
        forAll(-300, Gen.int(), Gen.int(), Gen.int(), Gen.int(), Gen.int(), Gen.int()) { a, b, c, d, e, f ->
          true
        }
      }
      exception.message shouldBe "Attempts should be a positive number"
    }

    "forAll: six implicit arguments 1000 attempts" {
      var attempts = 0
      forAll { a: Int, b: Double, c: String, d: Long, e: Float, f: Int ->
        attempts++
        true
      }
      attempts shouldBe 1000
    }

    "forAll: six implicit arguments 500 attempts" {
      var attempts = 0
      forAll(500) { a: Int, b: Double, c: String, d: Long, e: Float, f: Int ->
        attempts++
        true
      }
      attempts shouldBe 500
    }

    "forAll: six implicit arguments 20 attempts" {
      var attempts = 0
      forAll(20) { a: Int, b: Double, c: String, d: Long, e: Float, f: Int ->
        attempts++
        true
      }
      attempts shouldBe 20
    }

    "forNoneTestStrings" {
      forNone { a: String, b: String ->
        a + 1 == b + 2
      }
    }

    "forNoneTestBooleanDouble" {
      forNone { a: Boolean, b: Double ->
        a.toString() == b.toString()
      }
    }

    "sets" {
      forAll { a: Set<String>, b: Set<Int>, c: Set<Double>, d: Set<Boolean> ->
        a.plus(b).plus(c).plus(d).size == a.size + b.size + c.size + d.size
      }
    }

    "lists" {
      forAll { a: List<String>, b: List<Int>, c: List<Double>, d: List<Boolean> ->
        a.plus(b).plus(c).plus(d).size == a.size + b.size + c.size + d.size
      }
    }

    "pairs" {
      forAll { (f, s): Pair<String, String> ->
        (f + s).contains(f)
        (f + s).contains(s)
      }
    }

    "maps" {
      forAll { a: Map<Int, String>, b: Map<out Double, Boolean> ->
        a.keys.plus(a.values.toSet()).plus(b.keys).plus(b.values.toSet()).size ==
            a.keys.size + a.values.toSet().size + b.keys.size + b.values.toSet().size
      }
    }
  }
}