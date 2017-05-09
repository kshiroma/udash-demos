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

case class PhoneBookExtInfo(id: PhoneBookId, name: String, description: String, contactsCount: Int)