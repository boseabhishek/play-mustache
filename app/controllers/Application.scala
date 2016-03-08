package controllers

import play.api.mvc._

object Application extends Controller {
  def index = Action {
    Ok(views.html.index("Scala Play with Mustache Example"))
  }

  def lib = Action {
    Ok(views.html.libView("Scala Play with Mustache Example Library"))
  }
}