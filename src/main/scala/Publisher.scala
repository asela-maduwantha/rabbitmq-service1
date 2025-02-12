import com.rabbitmq.client.ConnectionFactory

import scala.io.StdIn
import scala.util.{Failure, Success, Try, Using}

object Publisher {
  private val QUEUE_NAME = "queue"

  def main(args: Array[String]): Unit = {
    val factory = new ConnectionFactory()
    factory.setHost("localhost")
    factory.setPort(5672)
    factory.setUsername("guest")
    factory.setPassword("guest")

    Try(factory.newConnection()) match {
      case Success(connection) =>
        Using(connection.createChannel()) { channel =>
          channel.queueDeclare(QUEUE_NAME, false, false, false, null)

          var message: String = ""
          println("Type message that you want to send and type 'x' when you need to exit")

          while (message != "x") {
            message = StdIn.readLine("Enter Message to send: ")
            if (message != "x" && message.nonEmpty) {
              Try(channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"))) match {
                case Success(_) => println("Sent")
                case Failure(exception) => println(exception.getMessage)
              }
            }
          }
        } match {
          case Success(_) =>
          case Failure(exception) => println(exception.getMessage)
        }

      case Failure(exception) => println(exception.getMessage)
    }
  }
}
