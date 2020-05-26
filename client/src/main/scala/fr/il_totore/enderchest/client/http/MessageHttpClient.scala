package fr.il_totore.enderchest.client.http

import java.io.{ByteArrayOutputStream, DataInputStream, DataOutputStream}

import fr.il_totore.enderchest.messaging.{ByteMessage, InMessage, OutMessage}

import scala.concurrent.{ExecutionContextExecutor, Future}

class MessageHttpClient(streamClient: HttpByteStreamClient)(implicit inRegistry: List[(DataInputStream) => InMessage], context: ExecutionContextExecutor) extends HttpClient[OutMessage, InMessage] {
  override def send(query: OutMessage): Future[InMessage] = {
    val byteOutput = new ByteArrayOutputStream()
    val dataStream = new DataOutputStream(byteOutput)
    query.write(dataStream)
    streamClient.send(byteOutput)
      .map(stream => ByteMessage(new DataInputStream(stream)))
  }
}