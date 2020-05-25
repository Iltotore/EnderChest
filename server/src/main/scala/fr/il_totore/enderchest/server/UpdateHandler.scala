package fr.il_totore.enderchest.server

import akka.http.scaladsl.model.HttpRequest
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.StandardRoute
import akka.stream.ActorMaterializer

class UpdateHandler {

  def receive(request: HttpRequest): StandardRoute = {
    if(request.method != HttpMethods.GET)
    complete(request.method.name)
  }
}