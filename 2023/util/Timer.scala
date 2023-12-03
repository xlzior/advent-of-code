package util

class Timer {
  var times = List[Long]()

  def checkpoint() = {
    times = System.currentTimeMillis() :: times
  }

  def print() = {
    println("Elapsed time: " + (times(0) - times(1)) + "ms")
  }

  def checkpointPrint() = {
    checkpoint()
    print()
  }

  def time[R](block: => R): R = {
    val t0 = System.currentTimeMillis()
    val result = block // call-by-name
    val t1 = System.currentTimeMillis()
    println("Elapsed time: " + (t1 - t0) + "ms")
    result
  }
}
