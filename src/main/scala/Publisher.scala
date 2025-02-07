import com.rabbitmq.client.ConnectionFactory
import scala.io.StdIn

object Publisher {
  private val QUEUE_NAME = "queue"

  def main(args: Array[String]): Unit = {
    val factory = new ConnectionFactory()
    factory.setHost("localhost")
    factory.setPort(5672)
    factory.setUsername("admin")
    factory.setPassword("Dilshan@123")

    var connection: com.rabbitmq.client.Connection = null
    var channel: com.rabbitmq.client.Channel = null

    try {
      connection = factory.newConnection()
      channel = connection.createChannel()

      channel.queueDeclare(QUEUE_NAME, false, false, false, null)

      var message: String = ""

      println("Type message that you want to send and type 'done' when you need to exit")

      // Adding a delay to ensure the consumer is ready
      Thread.sleep(1000)

      while (message != "done") {
        message = StdIn.readLine("Enter Message to send: ")
        if (message != "done") {
          channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"))
          println("Sent to Queue")
        }
      }
    } catch {
      case ex: Exception => ex.printStackTrace()
    } finally {
      if (channel != null) channel.close()
      if (connection != null) connection.close()
    }
  }
}
