package io.udash.demos.files.utils

import io.udash._
import io.udash.properties.ImmutableValue
import org.scalajs.dom.{Event, File, FileList, ProgressEvent}
import org.scalajs.dom.raw.{FormData, XMLHttpRequest}

import scala.concurrent.ExecutionContext
import scala.scalajs.js

class FileUploader(implicit ec: ExecutionContext) {
  def upload(url: String, fieldName: String, files: FileList,
             extraData: Map[js.Any, js.Any] = Map.empty): ReadableProperty[FileUploadModel] = {
    val p = ModelProperty[FileUploadModel]
    val data = new FormData()

    for (i <- 0 until files.length) {
      val file: File = files(i)
      data.append(s"$fieldName[]", file)
      p.subSeq(_.files).append(JSFileData(file.name, file.size))
    }
    extraData.foreach(item => data.append(item._1, item._2))

    val xhr = new XMLHttpRequest
    xhr.upload.addEventListener("progress", (ev: ProgressEvent) => {
      p.subProp(_.bytesSent).set(ev.loaded)
      p.subProp(_.bytesTotal).set(ev.total)
    })
    xhr.addEventListener("load", (ev: Event) =>
      p.subProp(_.state).set(FileUploadState.Completed)
    )
    xhr.addEventListener("error", (ev: Event) =>
      p.subProp(_.state).set(FileUploadState.Failed)
    )
    xhr.addEventListener("abort", (ev: Event) =>
      p.subProp(_.state).set(FileUploadState.Cancelled)
    )
    xhr.open(method = "POST", url = url)
    xhr.send(data)

    p.subProp(_.state).set(FileUploadState.InProgress)
    p
  }
}
