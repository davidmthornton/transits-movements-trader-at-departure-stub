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

package models

import cats.data.NonEmptyList
import models.errors.FunctionalError
import models.errors.XmlError
import models.values.CurrencyCode
import play.api.libs.json.Json
import play.api.libs.json.OFormat
import uk.gov.hmrc.play.json.Union

sealed abstract class BalanceRequestResponse extends Product with Serializable

case class BalanceRequestSuccess(
  balance: BigDecimal,
  currency: CurrencyCode
) extends BalanceRequestResponse

case class BalanceRequestFunctionalError(
  errors: NonEmptyList[FunctionalError]
) extends BalanceRequestResponse

case class BalanceRequestXmlError(
  errors: NonEmptyList[XmlError]
) extends BalanceRequestResponse

object BalanceRequestResponse {
  import models.formats.CommonFormats._

  implicit val balanceRequestSuccessFormat: OFormat[BalanceRequestSuccess] =
    Json.format[BalanceRequestSuccess]

  implicit val balanceRequestFunctionalErrorFormat: OFormat[BalanceRequestFunctionalError] =
    Json.format[BalanceRequestFunctionalError]

  implicit val balanceRequestXmlErrorFormat: OFormat[BalanceRequestXmlError] =
    Json.format[BalanceRequestXmlError]

  implicit val balanceRequestResponseFormat: OFormat[BalanceRequestResponse] =
    Union
      .from[BalanceRequestResponse](BalanceRequestResponseStatus.FieldName)
      .and[BalanceRequestSuccess](BalanceRequestResponseStatus.Success)
      .and[BalanceRequestFunctionalError](BalanceRequestResponseStatus.FunctionalError)
      .and[BalanceRequestXmlError](BalanceRequestResponseStatus.XmlError)
      .format
}
