/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers

import com.google.inject.Inject
import config.AppConfig
import play.api.Logging
import play.api.http.HeaderNames
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.ControllerComponents
import play.api.mvc.Request
import services.HeaderValidatorService
import services.SimulatedResponseService
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import utils.JsonUtils

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.xml.NodeSeq

class StubController @Inject()(appConfig: AppConfig,
                               cc: ControllerComponents,
                               headerValidatorService: HeaderValidatorService,
                               responseService: SimulatedResponseService,
                               jsonUtils: JsonUtils)(implicit ec: ExecutionContext)
    extends BackendController(cc)
    with Logging {

  val OneHundredKilobytes = 100000

  def gbpost: Action[NodeSeq] =
    internal_post("gb endpoint called", appConfig.eisgbBearerToken)

  def nipost: Action[NodeSeq] =
    internal_post("ni endpoint called", appConfig.eisniBearerToken)

  private def internal_post(logMessage: String, bearerToken: String): Action[NodeSeq] =
    Action.async(parse.xml) {
      implicit request: Request[NodeSeq] =>
        logger.info(logMessage)
        request.headers.get(HeaderNames.AUTHORIZATION) match {
          case Some(value) =>
            if (value == s"Bearer $bearerToken") {
              if (headerValidatorService.validate(request.headers)) {
                val bodySize = request.headers.get(HeaderNames.CONTENT_LENGTH).map(_.toInt)

                if (bodySize.exists(_ < OneHundredKilobytes)) {
                  logger.warn(s"validated XML ${request.body.toString()}")
                } else {
                  logger.warn("validated XML")
                }

                responseService.simulateResponseTo(request.body).map {
                  case Some(response) =>
                    Status(response.status)(response.body)
                  case None =>
                    Accepted
                }
              } else {
                logger.warn("FAILED VALIDATING headers")
                Future.successful(BadRequest)
              }
            } else {
              Future.successful(Forbidden)
            }
          case None =>
            Future.successful(Forbidden)
        }
    }

  def get: Action[AnyContent] = Action {
    _ =>
      val json =
        jsonUtils.readJsonFromFile("conf/resources/departure-response.json")

      Ok(json).as("application/json")
  }

  def getDeparture(departureId: Int): Action[AnyContent] = Action {
    _ =>
      val json =
        jsonUtils.readJsonFromFile("conf/resources/single-departure-response.json")

      Ok(json).as("application/json")
  }

}
