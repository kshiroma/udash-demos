package io.udash.demos.rest.views.contact

import io.udash.demos.rest.model.ContactId

trait ContactEditorModel {
  def loaded: Boolean
  def loadingText: String

  def isNewContact: Boolean
  def id: ContactId
  def firstName: String
  def lastName: String
  def phone: String
  def email: String
}
