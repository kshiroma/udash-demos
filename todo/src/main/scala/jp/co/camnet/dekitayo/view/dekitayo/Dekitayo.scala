package jp.co.camnet.dekitayo.view.dekitayo

import io.udash.properties.HasModelPropertyCreator

case class DekitayoViewModel(
                              dekitayos: Seq[Dekitayo],
                              dekitayoFilter: DekitayoFilter,
                              newDekitayoName: String,
                              toggleAllChecked: Boolean,

                              newSiteName: String,
                              newAppliName: String,
                              newBranchName: String,
                              newTargetUSerName: String
                            )

object DekitayoViewModel extends HasModelPropertyCreator[DekitayoViewModel]


case class Dekitayo(name: String, completed: Boolean = false, editing: Boolean = false)

object Dekitayo extends HasModelPropertyCreator[Dekitayo]

sealed abstract class DekitayoFilter(val matcher: Dekitayo => Boolean)

object DekitayoFilter {

  case object All extends DekitayoFilter(_ => true)

  case object Active extends DekitayoFilter(!_.completed)

  case object Completed extends DekitayoFilter(_.completed)

}