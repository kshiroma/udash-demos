package io.udash.demos.rest.api

import akka.actor.{Actor, ActorRefFactory, Props}
import com.avsystem.commons.serialization.GenCodec
import io.udash.demos.rest.model._
import io.udash.demos.rest.services.{ContactService, InMemoryContactService, InMemoryPhoneBookService, PhoneBookService}
import io.udash.rpc.DefaultUdashSerialization
import spray.http.{HttpEntity, MediaTypes, StatusCodes}
import spray.httpx.marshalling.{Marshaller, ToResponseMarshallable, ToResponseMarshaller}
import spray.httpx.unmarshalling.Unmarshaller
import spray.routing.{HttpServiceBase, RequestContext}

import scala.concurrent.ExecutionContext

trait PhoneBookWebService extends HttpServiceBase with DefaultUdashSerialization {
  val phoneBookService: PhoneBookService
  val contactService: ContactService

  implicit def optionMarshaller[T](implicit codec: GenCodec[T]) =
    ToResponseMarshaller.fromMarshaller()(gencodecMarshaller[Option[T]](GenCodec.optionCodec(codec)))

  implicit def gencodecMarshaller[T](implicit codec: GenCodec[T]): Marshaller[T] = Marshaller.of[T](MediaTypes.`application/json`)(
    (value, contentType, ctx) => {
      var string: String = null
      val output = outputSerialization((serialized) => string = serialized)
      codec.write(output, value)
      ctx.marshalTo(HttpEntity(contentType, string))
    }
  )

  implicit def gencodecUnmarshaller[T](implicit codec: GenCodec[T]): Unmarshaller[T] = Unmarshaller[T](MediaTypes.`application/json`) {
    case HttpEntity.NonEmpty(contentType, data) =>
      val input = inputSerialization(data.asString)
      codec.read(input)
  }

  private def completeIfNonEmpty[T](ctx: RequestContext)(opt: Option[T])(implicit rm: ToResponseMarshaller[T]) =
    opt match {
      case Some(v) => complete(ToResponseMarshallable.isMarshallable(v))(ctx)
      case None => complete(StatusCodes.NotFound)(ctx)
    }

  val route = {
    pathPrefix("book") {
      pathPrefix(Segment) { segment =>
        val bookId = PhoneBookId(segment.toInt)
        pathPrefix("contacts") {
          pathPrefix("count") {
            get {
              /** Adds contact to phone book */
              complete { phoneBookService.contactsCount(bookId) }
            }
          } ~
          pathPrefix("manage") {
            post {
              /** Adds contact to phone book */
              entity(as[ContactId]) { contactId =>
                complete { phoneBookService.addContact(bookId, contactId) }
              }
            } ~
              delete {
                /** Removes contact from phone book */
                entity(as[ContactId]) { contactId =>
                  complete { phoneBookService.removeContact(bookId, contactId) }
                }
              }
          } ~
          get {
            /** Return contacts ids from selected book */
            complete { phoneBookService.contacts(bookId) }
          }
        } ~
        get { ctx =>
          /** Return phone book info */
          completeIfNonEmpty(ctx) { phoneBookService.load(bookId) }
        } ~
        put {
          /** Updates phone book info */
          entity(as[PhoneBookInfo]) { phoneBookInfo =>
            complete { phoneBookService.update(bookId, phoneBookInfo) }
          }
        } ~
        delete { ctx =>
          /** Removes phone book */
          completeIfNonEmpty(ctx) { phoneBookService.remove(bookId) }
        }
      } ~
      get {
        /** Return phone books list */
        complete { phoneBookService.load() }
      } ~
      post {
        /** Creates new phone book */
        entity(as[PhoneBookInfo]) { phoneBookInfo =>
          complete { phoneBookService.create(phoneBookInfo) }
        }
      }
    } ~
    pathPrefix("contact") {
      path(Segment) { segment =>
        val contactId = ContactId(segment.toInt)
        get { ctx =>
          /** Return contact details */
          completeIfNonEmpty(ctx) { contactService.load(contactId) }
        } ~
        put {
          /** Updates contact */
          entity(as[Contact]) { contact =>
            complete { contactService.update(contactId, contact) }
          }
        } ~
        delete { ctx =>
          /** Removes contact */
          completeIfNonEmpty(ctx) { contactService.remove(contactId) }
          phoneBookService.contactRemoved(contactId)
        }
      } ~
      get {
        /** Creates new contact */
        complete { contactService.load() }
      } ~
      post {
        /** Creates new contact */
        entity(as[Contact]) { contact =>
          complete { contactService.create(contact) }
        }
      }
    }
  }
}

object PhoneBookWebServiceActor {
  def props(prefix: String = "") = Props(classOf[PhoneBookWebServiceActor], prefix)
}

class PhoneBookWebServiceActor(prefix: String) extends Actor with PhoneBookWebService {
  implicit lazy val executionContext: ExecutionContext = context.dispatcher
  def actorRefFactory: ActorRefFactory = context
  def receive = runRoute(if (prefix.nonEmpty) pathPrefix(prefix) {route} else route)

  override val phoneBookService: PhoneBookService = InMemoryPhoneBookService
  override val contactService: ContactService = InMemoryContactService
}
