package io.udash.demos.rest

import io.udash._
import io.udash.demos.rest.model.{ContactId, PhoneBookId}

sealed abstract class RoutingState(val parentState: RoutingState) extends State {
  def url(implicit application: Application[RoutingState]): String = s"#${application.matchState(this).value}"
}

case object RootState extends RoutingState(null)

case object ErrorState extends RoutingState(RootState)

case object IndexState extends RoutingState(RootState)

case class ContactFormState(id: Option[ContactId] = None) extends RoutingState(RootState)

case class PhoneBookFormState(id: Option[PhoneBookId] = None) extends RoutingState(RootState)