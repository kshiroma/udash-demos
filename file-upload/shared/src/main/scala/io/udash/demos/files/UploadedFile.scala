package io.udash.demos.files

import io.udash.rpc.HasGenCodecAndModelPropertyCreator

case class UploadedFile(name: String, serverFileName: String, size: Long)
object UploadedFile extends HasGenCodecAndModelPropertyCreator[UploadedFile]
