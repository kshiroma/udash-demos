package io.udash.demos.files.services

import io.udash.demos.files.UploadedFile

import scala.collection.mutable

object FilesStorage {
  private val files: mutable.ArrayBuffer[UploadedFile] = mutable.ArrayBuffer.empty

  def add(file: UploadedFile): Unit =
    files.append(file)

  def allFiles: Seq[UploadedFile] =
    files
}
