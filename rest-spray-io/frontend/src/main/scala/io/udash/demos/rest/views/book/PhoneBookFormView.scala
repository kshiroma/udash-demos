package io.udash.demos.rest.views.book

import io.udash._
import io.udash.bootstrap.BootstrapStyles
import io.udash.bootstrap.button.{ButtonStyle, UdashButton}
import io.udash.bootstrap.form.UdashForm
import io.udash.demos.rest.model.ContactId
import org.scalajs.dom.Element

class PhoneBookFormView(model: ModelProperty[PhoneBookEditorModel], presenter: PhoneBookFormPresenter) extends View {
  import scalatags.JsDom.all._

  val selectedStrings: SeqProperty[String] = model.subSeq(_.selectedContacts).transform(
    (id: ContactId) => id.value.toString,
    (s: String) => ContactId(s.toInt)
  )

  val saveButton = UdashButton(buttonStyle = ButtonStyle.Primary)(
    produce(model.subProp(_.isNewBook)) {
      case true => span("Create").render
      case false => span("Save changes").render
    }
  )

  saveButton.listen {
    case UdashButton.ButtonClickEvent(_) =>
      model.subProp(_.isNewBook).get match {
        case true => presenter.createPhoneBook()
        case false => presenter.updatePhoneBook()
      }
  }

  private val content = div(
    produce(model.subProp(_.loaded)) {
      case false =>
        span(bind(model.subProp(_.loadingText))).render
      case true =>
        div(
          produce(model.subProp(_.isNewBook)) {
            case true => h2("Phone Book creator").render
            case false => h2("Phone Book editor").render
          },

          UdashForm(
            UdashForm.group(
              UdashForm.textInput()("Name: ")(model.subProp(_.name))
            ),
            UdashForm.group(
              UdashForm.textInput()("Description: ")(model.subProp(_.description))
            ),
            UdashForm.group(
              saveButton.render
            )
          ).render,

          produce(model.subProp(_.isNewBook)) {
            case true => h3("Create phone book to manage contacts").render
            case false => div(
              h3("Contacts in book"),
              produce(model.subSeq(_.allContacts))(contacts => {
                UdashForm(
                  {
                    val tmpMap = contacts.map(c => (c.id.value.toString, c)).toMap
                    UdashForm.checkboxes()(
                      selectedStrings, tmpMap.keys.toSeq,
                      decorator = (input, id) =>
                        label(BootstrapStyles.Form.checkbox)(input, {
                          val contact = tmpMap(id)
                          s"${contact.firstName} ${contact.lastName}"
                        }).render
                    )
                  }
                ).render
              })
            ).render
          }
        ).render
    }
  ).render

  override def getTemplate: Modifier = content

  override def renderChild(view: View): Unit = {}
}