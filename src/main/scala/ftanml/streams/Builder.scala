package ftanml.streams

import ftanml.objects._
import collection.mutable.{HashMap, LinkedHashMap, LinkedList, Stack}

/**
 * The builder constructs a FtanValue from a stream of events representing the value
 */
class Builder extends Acceptor {

  private val stack = new Stack[AssistantBuilder]
  stack.push(new ItemBuilder)

  def processString(value: String) {
    stack.top.add(FtanString(value))
    println("String: " + stack)
  }

  def processNumber(value: Double) {
    stack.top.add(FtanNumber(value))
    println("Number: " + stack)
  }

  def processBoolean(value: Boolean) {
    stack.top.add(FtanBoolean(value))
    println("Boolean: " + stack)
  }

  def processNull() {
    stack.top.add(FtanNull)
    println("Null: " + stack)
  }

  def processStartArray() {
    stack.push(new ArrayBuilder)
    println("ArrayStart: " + stack)
  }

  def processEndArray() {
    val arrayVal = stack.pop();
    stack.top.add(arrayVal.getValue)
    println("ArrayEnd: " + stack)
  }

  def processStartElement(name: Option[String]) {
    stack.push(new ElementBuilder(name))
    println("Elem: " + name + " " + stack)
  }

  def processAttributeName(name: String) {
    stack.top.asInstanceOf[ElementBuilder].attribute(FtanString(name))
    println("Attr: " + name + " " + stack)
  }

  def processStartContent(isElementOnly: Boolean) {
    stack.push(new ContentBuilder)
    println("Content: " + stack)
  }

  def processEndElement() {
    if (stack.top.isInstanceOf[ContentBuilder]) {
      val content = stack.pop().getValue
      stack.top.asInstanceOf[ElementBuilder].setContent(content.asInstanceOf[FtanArray])
    }

    val element = stack.pop().getValue
    stack.top.add(element)
    println("ElemEnd: " + stack)
  }

  def error(err: Exception) {}

  def value : FtanValue =
    stack.top.getValue

}