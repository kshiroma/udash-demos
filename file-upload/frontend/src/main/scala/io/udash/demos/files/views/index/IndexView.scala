package io.udash.demos.files.views.index

import io.udash._
import io.udash.demos.files.utils.{FileUploadModel, FileUploader, JSFileData}
import io.udash.properties.seq.SeqProperty
import org.scalajs.dom.html.Input
import org.scalajs.dom.{Event, File}

class IndexView(model: Property[String], presenter: IndexPresenter) extends FinalView {
  import io.udash.demos.files.Context._

  import scalatags.JsDom.all._

  trait ViewModel {
    def singleFile: FileUploadModel
    def multiFile: FileUploadModel
  }

  val m = ModelProperty[ViewModel]

  override def getTemplate: Modifier = {
    val trashInput: Input = input(`type` := "text", name := "trash").render
    val (singleFileInput, selectedFile) = fileInput("file", acceptMultipleFiles = false)
    val (multiFileInput, selectedFiles) = fileInput("files", acceptMultipleFiles = true)
    div(
      form(
        trashInput,
        singleFileInput,
        multiFileInput,
        button("Send")(
          onclick := ((ev: Event) => {
            val uploader = new FileUploader
            uploader.upload("upload/", "file", singleFileInput.files).listen(m.subProp(_.singleFile).set)
            uploader.upload("upload/", "files", multiFileInput.files).listen(m.subProp(_.multiFile).set)
            ev.preventDefault()
          })
        )
      ),
      div(
        "Selected files: ",
        ul(
          repeat(selectedFile)(file => li(s"${file.get.name} (${file.get.size}B)").render),
          repeat(selectedFiles)(file => li(s"${file.get.name} (${file.get.size}B)").render)
        )
      ),
      div("single: ", bind(m.subProp(_.singleFile.bytesSent)), "/", bind(m.subProp(_.singleFile.bytesTotal))),
      div("multi: ", bind(m.subProp(_.multiFile.bytesSent)), "/", bind(m.subProp(_.multiFile.bytesTotal)))
    )
  }

  def fileInput(inputName: String, acceptMultipleFiles: Boolean): (Input, ReadableSeqProperty[JSFileData]) = {
    val selectedFiles = SeqProperty[JSFileData]
    val inp = input(
      `type` := "file", name := inputName,
      if (acceptMultipleFiles) multiple := "multiple" else ()
    ).render

    inp.onchange = (ev: Event) => {
      ev.preventDefault()

      selectedFiles.clear()
      for (i <- 0 until inp.files.length) {
        val file: File = inp.files(i)
        selectedFiles.append(JSFileData(file.name, file.size))
      }
    }

    (inp, selectedFiles)
  }
}