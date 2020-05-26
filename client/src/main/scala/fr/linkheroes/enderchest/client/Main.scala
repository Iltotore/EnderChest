package fr.linkheroes.enderchest.client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.util.{ Failure, Success }
object Main {

  def main(args: Array[String]): Unit = {
    println("[Enderchest] -> Ready to use !")

    connect("http://localhost:8080")

  }

  def connect (host: String): Unit = {
    println("[EnderChest] Connect to " + host)


  }
}
