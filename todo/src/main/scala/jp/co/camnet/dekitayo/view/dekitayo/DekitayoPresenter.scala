package jp.co.camnet.dekitayo.view.dekitayo

import io.udash._
import jp.co.camnet.dekitayo._
import jp.co.camnet.dekitayo.storage.LocalDekitayoStorage

class DekitayoPresenter(model: ModelProperty[DekitayoViewModel]) extends Presenter[DekitayoState] {
  private val dekitayos = model.subSeq(_.dekitayos)


  dekitayos.set(
    LocalDekitayoStorage.load().map(dekitayo => Dekitayo(dekitayo.title, dekitayo.completed))
  )


  private val toggleButtonListener = dekitayos.listen {
    dekitayos => model.subProp(_.toggleAllChecked).set(dekitayos.forall(_.completed))
  }


  private val dekitayosPersistListener = dekitayos.listen { dekitayos =>
    LocalDekitayoStorage.dekitayoStore(
      dekitayos.map(dekitayo => storage.Dekitayo(dekitayo.name, dekitayo.completed))
    )
  }


  //model.subSeq()

  override def handleState(state: DekitayoState): Unit = {
    model.subProp(_.dekitayoFilter).set(state.filter)
  }

  override def onClose(): Unit = {
    super.onClose()
    toggleButtonListener.cancel()
    dekitayosPersistListener.cancel()

  }

  def addDekitayo(): Unit = {
    val namePropery: Property[String] = model.subProp(_.newDekitayoName)
    val name = namePropery.get.trim
    if (name.nonEmpty) {
      dekitayos.append(Dekitayo(name))
      namePropery.set("")
    }
  }


  def startItemEdit(item: ModelProperty[Dekitayo], nameEditor: Property[String]): Unit = {
    nameEditor.set(item.subProp(_.name).get)
    item.subProp(_.editing).set(true)
  }

  def cancelItemEdit(item: ModelProperty[Dekitayo]): Unit =  item.subProp(_.editing).set(false)

  def endItemEdit(item: ModelProperty[Dekitayo], nameEditor: Property[String]): Unit = {
    val name = nameEditor.get.trim
    if (item.subProp(_.editing).get && name.nonEmpty) {
      item.subProp(_.name).set(name)
      item.subProp(_.editing).set(false)
    } else if (name.isEmpty) {
      deleteItem(item.get)
    }
  }

  def deleteItem(item: Dekitayo): Unit = {
    dekitayos.remove(item)
  }

  def clearCompleted(): Unit = {
    dekitayos.set(dekitayos.get.filter(DekitayoFilter.Active.matcher))
  }

  def setItemsCompleted(): Unit = {
    CallbackSequencer().sequence {
      val targetValue = !model.subProp(_.toggleAllChecked).get
      dekitayos.elemProperties.foreach(p => p.asModel.subProp(_.completed).set(targetValue))
    }
  }

}
