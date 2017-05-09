package io.udash.demos.rest.views.book

import io.udash.demos.rest.model.{Contact, ContactId, PhoneBookId}

trait PhoneBookEditorModel {
  def loaded: Boolean
  def loadingText: String

  def isNewBook: Boolean
  def id: PhoneBookId
  def name: String
  def description: String

  def allContacts: Seq[Contact]
  def selectedContacts: Seq[ContactId]
}
