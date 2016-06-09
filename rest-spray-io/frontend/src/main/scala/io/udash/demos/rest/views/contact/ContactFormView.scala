package io.udash.demos.rest.views.contact

import io.udash._
import io.udash.bootstrap.button.{ButtonStyle, UdashButton}
import io.udash.bootstrap.form.UdashForm
import org.scalajs.dom.Element

class ContactFormView(model: ModelProperty[ContactEditorModel], presenter: ContactFormPresenter) extends View {
  import scalatags.JsDom.all._

  val saveButton = UdashButton(buttonStyle = ButtonStyle.Primary)(
    produce(model.subProp(_.isNewContact)) {
      case true => span("Create").render
      case false => span("Save changes").render
    }
  )

  saveButton.listen {
    case UdashButton.ButtonClickEvent(_) =>
      model.subProp(_.isNewContact).get match {
        case true => presenter.createContact()
        case false => presenter.updateContact()
      }
  }

  private val content = div(
    produce(model.subProp(_.loaded)) {
      case false =>
        span(bind(model.subProp(_.loadingText))).render
      case true =>
        div(
          produce(model.subProp(_.isNewContact)) {
            case true => h2("Contact creator").render
            case false => h2("Contact editor").render
          },
          UdashForm(
            UdashForm.group(
              UdashForm.textInput()("First name: ")(model.subProp(_.firstName))
            ),
            UdashForm.group(
              UdashForm.textInput()("Last name: ")(model.subProp(_.lastName))
            ),
            UdashForm.group(
              UdashForm.textInput()("Phone: ")(model.subProp(_.phone))
            ),
            UdashForm.group(
              UdashForm.textInput()("E-mail: ")(model.subProp(_.email))
            ),
            UdashForm.group(
              saveButton.render
            )
          ).render
        ).render
    }
  ).render

  override def getTemplate: Element = content

  override def renderChild(view: View): Unit = {}
}