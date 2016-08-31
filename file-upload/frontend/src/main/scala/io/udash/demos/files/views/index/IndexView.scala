package io.udash.demos.files.views.index

import io.udash._
import io.udash.bootstrap.UdashBootstrap
import io.udash.bootstrap.button.{ButtonStyle, UdashButton}
import io.udash.bootstrap.form.UdashForm
import io.udash.bootstrap.label.UdashLabel
import io.udash.bootstrap.panel.UdashPanel
import io.udash.bootstrap.progressbar.UdashProgressBar
import io.udash.bootstrap.table.UdashTable
import io.udash.demos.files.ApplicationServerContexts
import org.scalajs.dom.File

class IndexView(model: ModelProperty[UploadViewModel], presenter: IndexPresenter) extends FinalView {
  import io.udash.demos.files.Context._
  import FileUploader._

  import scalatags.JsDom.all._

  def normalizeSize(size: Double): String = {
    val units = Iterator("B", "KB", "MB", "GB", "TB")
    var selectedUnit = units.next()
    var sizeWithUnit = size
    while (sizeWithUnit >= 1024) {
      sizeWithUnit /= 1024
      selectedUnit = units.next()
    }
    "%.2f %s".format(sizeWithUnit, selectedUnit)
  }

  override def getTemplate: Modifier = {
    val sendButton = UdashButton(block = true, buttonStyle = ButtonStyle.Primary)("Send")
    model.subProp(_.state.state).listen {
      case FileUploadState.InProgress => sendButton.disabled.set(true)
      case _ => sendButton.disabled.set(false)
    }

    val progressBar = UdashProgressBar.animated()()
    val progress = model.subProp(_.state.bytesSent)
      .combine(model.subProp(_.state.bytesTotal))(
        (sent, total) => ((100 * sent) / total).toInt
      )
    progress.listen(progressBar.progress.set)

    sendButton.listen {
      case UdashButton.ButtonClickEvent(_) =>
        presenter.uploadSelectedFiles()
    }

    div(
      h3("Select files and click send..."),
      UdashForm(
        UdashForm.fileInput()("Select files")("files",
          acceptMultipleFiles = Property(true),
          selectedFiles = model.subSeq(_.selectedFiles)
        ),
        sendButton.render
      ).render,
      h3("Check upload progress here..."),
      UdashPanel()(
        UdashPanel.heading(
          "Upload state ",
          produce(model.subProp(_.state.state), checkNull = false) {
            case null => UdashLabel.info(UdashBootstrap.newId(), "Waiting").render
            case FileUploadState.InProgress => UdashLabel.info(UdashBootstrap.newId(), "In progress").render
            case FileUploadState.Completed => UdashLabel.success(UdashBootstrap.newId(), "Completed").render
            case FileUploadState.Cancelled => UdashLabel.warning(UdashBootstrap.newId(), "Cancelled").render
            case FileUploadState.Failed => UdashLabel.danger(UdashBootstrap.newId(), "Failed").render
          }
        ),
        UdashPanel.body(
          h4("Selected files"),
          ul(
            repeat(model.subSeq(_.selectedFiles))(file =>
              li(s"${file.get.name} (${normalizeSize(file.get.size)})").render
            ),
            showIf(model.subSeq(_.selectedFiles).transform((s: Seq[File]) => s.isEmpty))(
              i("No files were selected.").render
            )
          ),
          showIf(model.subProp(_.state.state).transform(_ == FileUploadState.InProgress))(Seq(
            h4("Sending progress").render,
            progressBar.render
          ))
        )
      ).render,
      h3("You can download uploaded files..."),
      UdashTable(
        bordered = Property(true), hover = Property(true)
      )(model.subSeq(_.uploadedFiles))(
        headerFactory = Some(() => tr(th("File name"), th("Size")).render),
        rowFactory = (file) => {
          val fileModel = file.asModel
          tr(
            td(produce(fileModel)(f =>
              a(href := ApplicationServerContexts.downloadContextPrefix + "/" + f.serverFileName)(f.name).render
            )),
            td(produce(fileModel.subProp(_.size))(s =>
              i(normalizeSize(s)).render
            ))
          ).render
        }
      ).render
    )
  }
}