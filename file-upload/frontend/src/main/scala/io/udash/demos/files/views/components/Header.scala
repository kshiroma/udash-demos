package io.udash.demos.files.views.components
import io.udash.bootstrap.navs.{UdashNav, UdashNavbar}
import io.udash.demos.files.Context._
import io.udash.demos.files.IndexState
import io.udash.demos.files.config.ExternalUrls
import io.udash.properties.seq.SeqProperty
import org.scalajs.dom.raw.Element

import scalatags.JsDom.all._

object Header {
  private val brand = a(href := IndexState.url)(
    Image("udash_logo_m.png", "Udash Framework", style := "height: 44px; margin-top: 10px;")
  ).render

  case class NavItem(href: String, imageSrc: String, name: String)
  private val navItems = SeqProperty(
    NavItem(ExternalUrls.udashGithub, "icon_github.png", "Github"),
    NavItem(ExternalUrls.stackoverflow, "icon_stackoverflow.png", "StackOverflow"),
    NavItem(ExternalUrls.avsystem, "icon_avsystem.png", "Proudly made by AVSystem")
  )
  private val nav = UdashNav.navbar(navItems)(
    (prop) => {
      val item = prop.get
      a(href := item.href, target := "_blank")(
        Image(item.imageSrc, item.name)
      ).render
    }
  )

  val header = UdashNavbar.inverted(
    brand = brand,
    nav = nav
  )

  lazy val getTemplate: Element =
    header.render
}