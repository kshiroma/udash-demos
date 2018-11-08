package jp.co.camnet.dekitayo.storage

import com.avsystem.commons.serialization.HasGenCodec
import com.avsystem.commons.serialization.json.{JsonStringInput, JsonStringOutput}
import org.scalajs.dom.ext.LocalStorage


case class Dekitayo(title: String, completed: Boolean)

object Dekitayo extends HasGenCodec[Dekitayo]


trait DekitayoStorage {
  def dekitayoStore(dekitayo: Seq[Dekitayo]): Unit

  def load(): Seq[Dekitayo]
}


object LocalDekitayoStorage extends DekitayoStorage {
  private val storage = LocalStorage
  private val namespace = "dekita-yo"


  override def dekitayoStore(dekitayo: Seq[Dekitayo]): Unit = {
    storage(namespace) = JsonStringOutput.write(dekitayo)
  }

  override def load(): Seq[Dekitayo] = {
    storage(namespace) match {
      case Some(dekitayo) => JsonStringInput.read[Seq[Dekitayo]](dekitayo)
      case None => Seq.empty
    }
  }


}
