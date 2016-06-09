package io.udash.demos.rest.views.index

import io.udash.demos.rest.model.{Contact, PhoneBookId}

trait IndexViewModel {
  def books: DataLoadingModel[PhoneBookExtInfo]
  def contacts: DataLoadingModel[Contact]
}

trait DataLoadingModel[T] {
  def loaded: Boolean
  def loadingText: String
  def elements: Seq[T]
}

trait PhoneBookExtInfo {
  def id: PhoneBookId
  def name: String
  def description: String
  def contactsCount: Int
}

object PhoneBookExtInfo {
  def apply(_id: PhoneBookId, _name: String, _description: String, _contactsCount: Int): PhoneBookExtInfo = {
    new PhoneBookExtInfo {
      override def description: String = _description
      override def name: String = _name
      override def contactsCount: Int = _contactsCount
      override def id: PhoneBookId = _id
    }
  }
}