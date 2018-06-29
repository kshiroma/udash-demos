package io.udash.demos.rest

import com.avsystem.commons.rpc.rpcName
import io.udash.demos.rest.model._
import io.udash.rest._

import scala.concurrent.Future

trait MainServerREST {
  @RESTName("book")
  def phoneBooks(): PhoneBooksREST

  @RESTName("book") @rpcName("selectBook")
  def phoneBooks(@URLPart id: PhoneBookId): PhoneBookManagementREST

  @RESTName("contact")
  def contacts(): ContactsREST

  @RESTName("contact") @rpcName("selectContact")
  def contacts(@URLPart id: ContactId): ContactManagementREST
}
object MainServerREST extends DefaultRESTFramework.RPCCompanion[MainServerREST]

trait PhoneBooksREST {
  @GET @SkipRESTName @rpcName("loadAll")
  def load(): Future[Seq[PhoneBookInfo]]

  @POST @SkipRESTName
  def create(@Body book: PhoneBookInfo): Future[PhoneBookInfo]
}
object PhoneBooksREST extends DefaultRESTFramework.RPCCompanion[PhoneBooksREST]

trait PhoneBookManagementREST {
  @GET @SkipRESTName
  def load(): Future[PhoneBookInfo]

  @PUT @SkipRESTName
  def update(@Body book: PhoneBookInfo): Future[PhoneBookInfo]

  @DELETE @SkipRESTName
  def remove(): Future[PhoneBookInfo]

  def contacts(): PhoneBookContactsREST
}
object PhoneBookManagementREST extends DefaultRESTFramework.RPCCompanion[PhoneBookManagementREST]

trait PhoneBookContactsREST {
  @GET @SkipRESTName
  def load(): Future[Seq[ContactId]]

  @GET
  def count(): Future[Int]

  @POST @RESTName("manage")
  def add(@Body contactId: ContactId): Future[Unit]

  @DELETE @RESTName("manage")
  def remove(@Body contactId: ContactId): Future[Unit]
}
object PhoneBookContactsREST extends DefaultRESTFramework.RPCCompanion[PhoneBookContactsREST]

trait ContactsREST {
  @GET @SkipRESTName @rpcName("loadAll")
  def load(): Future[Seq[Contact]]

  @POST @SkipRESTName
  def create(@Body contact: Contact): Future[Contact]
}
object ContactsREST extends DefaultRESTFramework.RPCCompanion[ContactsREST]

trait ContactManagementREST {
  @GET @SkipRESTName
  def load(): Future[Contact]

  @PUT @SkipRESTName
  def update(@Body book: Contact): Future[Contact]

  @DELETE @SkipRESTName
  def remove(): Future[Contact]
}
object ContactManagementREST extends DefaultRESTFramework.RPCCompanion[ContactManagementREST]