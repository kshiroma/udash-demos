package io.udash.demos.rest.model

import com.avsystem.commons.serialization.{GenCodec, transparent}

@transparent
case class ContactId(value: Int)
case class Contact(id: ContactId, firstName: String, lastName: String, phone: String, email: String)

object ContactId {
  implicit val contactIdGenCodec = GenCodec.materialize[ContactId]
}

object Contact {
  implicit val contactGenCodec = GenCodec.materialize[Contact]
}