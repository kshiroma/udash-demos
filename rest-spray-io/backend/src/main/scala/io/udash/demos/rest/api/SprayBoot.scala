package io.udash.demos.rest.api

import akka.actor.{ActorRef, ActorSystem}
import io.udash.demos.rest.jetty.ApplicationServer
import spray.servlet.WebBoot

class SprayBoot extends WebBoot {
  override def system: ActorSystem = ActorSystem("spray-system")
  override def serviceActor: ActorRef = system.actorOf(PhoneBookWebServiceActor.props(ApplicationServer.restPrefix))
}
