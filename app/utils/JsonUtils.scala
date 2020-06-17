/*
 * Copyright 2020 HM Revenue & Customs
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

package utils

import java.io.FileInputStream

import com.google.inject.Inject
import play.api.Environment
import play.api.libs.json.{JsValue, Json}

class JsonUtils @Inject()(environment: Environment) {

  def readJsonFromFile(filePath: String): JsValue = {
    val jsonSchemaFile = environment.getExistingFile(filePath)
    jsonSchemaFile match {
      case Some(schemaFile) =>
        val inputStream = new FileInputStream(schemaFile)
        Json.parse(inputStream)
      case _ =>
        throw new RuntimeException("No Response file found")
    }
  }
}
