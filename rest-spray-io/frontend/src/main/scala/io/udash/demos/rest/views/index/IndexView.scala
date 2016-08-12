package io.udash.demos.rest.views.index

import io.udash._
import io.udash.bootstrap.BootstrapStyles
import io.udash.bootstrap.table.UdashTable
import io.udash.demos.rest._
import io.udash.demos.rest.model.Contact
import io.udash.properties.ModelSeq
import org.scalajs.dom.{Element, Event}

class IndexView(model: ModelProperty[IndexViewModel], presenter: IndexPresenter) extends View {
  import Context._

  import scalatags.JsDom.all._

  private def headerButtons(loadedProp: Property[Boolean], creatorState: RoutingState) =
    produce(loadedProp)(loaded =>
      if (loaded) a(
        href := creatorState.url, style := "float: right",
        BootstrapStyles.Button.btn, BootstrapStyles.Button.btnPrimary
      )("Create new").render
      else span().render
    )

  private def actionButtons(editUrl: String, removeCallback: () => Any): Modifier =
    span(
      a(href := editUrl)("Edit"), " | ",
      a(href := "#", onclick := ((_: Event) => { removeCallback(); false }))("Remove")
    )

  private def elementsTable[T](model: ModelProperty[DataLoadingModel[T]], headers: Seq[String],
                               tableElementsFactory: CastableProperty[T] => Seq[Modifier])(implicit ev: ModelSeq[Seq[T]]) =
    produce(model.subProp(_.loaded))(loaded =>
      if (loaded) {
        val tab = UdashTable(
          hover = Property(true)
        )(model.subSeq(_.elements))(
          rowFactory = (p) => tr(tableElementsFactory(p).map(name => td(name))).render,
          headerFactory = Some(() => tr(headers.map(name => th(name))).render)
        )
        tab.render
      } else span(bind(model.subProp(_.loadingText))).render
    )

  private val content = div(
    div(
      headerButtons(model.subProp(_.books.loaded), PhoneBookFormState(None)),
      h2("Phone Books")
    ),
    hr,
    elementsTable[PhoneBookExtInfo](
      model.subModel(_.books),
      Seq("Id", "Name", "Description", "Contacts",  "Actions"),
      (prop: CastableProperty[PhoneBookExtInfo]) => {
        val book: PhoneBookExtInfo = prop.get
        Seq(
          book.id.value,
          book.name,
          book.description,
          i(bind(prop.asModel.subProp(_.contactsCount))).render,
          actionButtons(
            PhoneBookFormState(Some(book.id)).url,
            () => presenter.removePhoneBook(book.id)
          )
        )
      }
    ),

    div(
      headerButtons(model.subProp(_.contacts.loaded), ContactFormState(None)),
      h2("Contacts")
    ),
    hr,
    elementsTable[Contact](
      model.subModel(_.contacts),
      Seq("Id", "Name", "Phone", "E-mail", "Actions"),
      (prop: Property[Contact]) => {
        val contact: Contact = prop.get
        Seq(
          contact.id.value,
          s"${contact.firstName} ${contact.lastName}",
          contact.phone,
          contact.email,
          actionButtons(
            ContactFormState(Some(contact.id)).url,
            () => presenter.removeContact(contact.id)
          )
        )
      }
    )
  ).render

  override def getTemplate: Modifier = content

  override def renderChild(view: View): Unit = {}
}