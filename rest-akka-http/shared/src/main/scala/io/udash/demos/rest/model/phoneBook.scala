package io.udash.demos.rest.model

import com.avsystem.commons.serialization.{GenCodec, transparent}

@transparent
case class PhoneBookId(value: Int)
case class PhoneBookInfo(id: PhoneBookId, name: String, description: String)

object PhoneBookId {
  implicit val phoneBookIdGenCodec = GenCodec.materialize[PhoneBookId]
}

object PhoneBookInfo {
  implicit val phoneBookInfoGenCodec = GenCodec.materialize[PhoneBookInfo]
}