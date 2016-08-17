package io.bigfast.chat

import java.util.concurrent.TimeUnit

import io.grpc.stub.StreamObserver
import io.grpc.{ManagedChannel, ManagedChannelBuilder}

import scala.util.Random

/**
  * Created by kykl on 8/16/16.
  */

object ChatterBox {
  def apply(host:String = "localhost", port:Int = 50051):ChatterBox = {
    val builder = ManagedChannelBuilder.forAddress(host, port)
    builder.usePlaintext(true)
    val channel = builder.build()
    new ChatterBox(channel, ChatGrpc.blockingStub(channel), ChatGrpc.stub(channel))
  }

  def main(args:Array[String]) = {
    val chatterBox = ChatterBox()

    try {
      val response = chatterBox.subscribe("my channel")
    }
    catch {
      case t:Throwable => {
        println(t)
        chatterBox.shutdown()
      }
    }
  }
}

class ChatterBox private (channel: ManagedChannel, blockingStub: ChatGrpc.ChatBlockingStub, asyncStub: ChatGrpc.ChatStub) {
  def subscribe(ch:String):StreamObserver[Event] = {
    val r = new StreamObserver[Event] {
      override def onError(t: Throwable): Unit = {
        println(t)
      }

      override def onCompleted(): Unit = {
        println("Completed")
      }

      override def onNext(value: Event): Unit = {
        println("Event: " + value.toString)
      }
    }

    val requestObserver = asyncStub.subscribeEvents(r)
    requestObserver.onNext(EventSubscription(1, 2, 3))
    Thread.sleep(Random.nextInt(1000) + 500)

    requestObserver.onCompleted()

    r
  }


  def shutdown(): Unit = {
    channel.shutdown.awaitTermination(5, TimeUnit.SECONDS)
  }
}