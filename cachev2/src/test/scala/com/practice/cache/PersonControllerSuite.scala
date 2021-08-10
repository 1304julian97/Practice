package com.practice.cache

import cats.effect.IO
import cats.effect.IO.ioEffect
import com.practice.cache.controllers.{DTOS, JsonDecoders, PersonController}
import com.practice.cache.domain.PersonService
import com.practice.cache.domain.interpreters.PersonRepositoryAlgebraInterpreter
import com.practice.cache.testinterpreters.PersonAlgebraTestInterpreter
import io.circe._
import org.http4s._
import org.http4s.implicits._
import munit.CatsEffectSuite
import org.http4s.circe._

import java.time.LocalDate

class PersonControllerSuite extends CatsEffectSuite with JsonDecoders {


  test("Person controller returns status code 200 with correct body V1r"){
    val f: Request[IO] = Request(method = Method.GET, uri = uri"/person/ju?isExactly=false", headers = Headers.of(Header("Token","I am a token")))
    val bool = personControllerValidation(f)
    assert(bool,s"The request does nto response correctly")

  }


  test("Person controller returns status code 200 with correct body V2") {
    val request: Request[IO] = Request(method = Method.GET, uri = uri"/person/ju?isExactly=false", headers = Headers.of(Header("Token","I am a token")))

    val personRepositoryAlgebra = new PersonRepositoryAlgebraInterpreter[IO]
    val personAlgebraInterpreter = new PersonAlgebraTestInterpreter[IO]
    val personService = new PersonService[IO](personRepositoryAlgebra, personAlgebraInterpreter)
    val routes = PersonController.endpoints(personService)
    val response: IO[Response[IO]] = routes.orNotFound.run(request)

    val expectedJson = Json.obj{
      "people"  → Json.arr(
        Json.obj(
          "name" -> Json.fromString("Julian"),
          "lastName" -> Json.fromString("Carvajal"),
          ("year" , Json.fromInt(1997)),
          "dateTrace" → Json.fromString(LocalDate.now().toString)
        ))
    }

    assertIO(response.map(_.as[Json].unsafeRunSync),expectedJson,"Ups! these are not the same")


    assertIO(response.map(_.status) ,Status.Ok)
  }


  test("Person controller returns status code 404 because of Token is missing, validate correct body") {
    val request: Request[IO] = Request(method = Method.GET, uri = uri"/person/ju?isExactly=false")
    val personRepositoryAlgebra = new PersonRepositoryAlgebraInterpreter[IO]
    val personAlgebraInterpreter = new PersonAlgebraTestInterpreter[IO]
    val personService = new PersonService[IO](personRepositoryAlgebra, personAlgebraInterpreter)
    val routes = PersonController.endpoints(personService)
    val response: IO[Response[IO]] = routes.orNotFound.run(request)

    val expectedJson1 = Json.obj{
      "people"  → Json.arr(
        Json.obj(
          "name" -> Json.fromString("Julian"),
          "lastName" -> Json.fromString("Carvajalh"),
          ("year" , Json.fromInt(1997)),
          "dateTrace" → Json.fromString(LocalDate.now().toString)
        ))
    }


    val ioJson: IO[Json] = response.map(_.as[Json].unsafeRunSync)

    val jsonUnWrapped: Json = ioJson.unsafeRunSync

    //TODO WHY THIS ONE IS CORRECT? it MUST fail
    assertIO(ioJson,expectedJson1,"Ups! these are not the same")

    assertIO(response.map(_.status) ,Status.NotFound)

    //TODO THIS ONE IS FAILING AS EXPECTED
    assertEquals(jsonUnWrapped,expectedJson1,"The Json are not the same")
  }


 /*
  test("HelloWorld returns hello world message") {
    assertIO(retHelloWorld.flatMap(_.as[String]), "{\"message\":\"Hello, world\"}")
  }

  private[this] val retHelloWorld: IO[Response[IO]] = {
    val getHW: Request[IO] = Request[IO](Method.GET, uri"/hello/world")
    val helloWorld: HelloWorld[IO] = HelloWorld.impl[IO]
    val response: IO[Response[IO]] = dcachev2Routes.helloWorldRoutes(helloWorld).orNotFound(getHW)
    response
  }*/

  def check[A](actual:        IO[Response[IO]],
               expectedStatus: Status,
               expectedBody:   Option[A])(
                implicit ev: EntityDecoder[IO, A]
              ): Boolean =  {
    val actualResp: Response[IO] = actual.unsafeRunSync
    val statusCheck        = actualResp.status == expectedStatus
    val bodyCheck          = expectedBody.fold[Boolean](
      actualResp.body.compile.toVector.unsafeRunSync.isEmpty) { // Verify Response's body is empty.
      println(actualResp.as[A].unsafeRunSync)
      expected => actualResp.as[A].unsafeRunSync == expected
    }
    statusCheck && bodyCheck
  }


  def personControllerValidation(request: Request[IO]): Boolean = {

    val personRepositoryAlgebra = new PersonRepositoryAlgebraInterpreter[IO]
    val personAlgebraInterpreter = new PersonAlgebraTestInterpreter[IO]
    val personService = new PersonService[IO](personRepositoryAlgebra, personAlgebraInterpreter)
    val routes = PersonController.endpoints(personService)
    val response: IO[Response[IO]] = routes.orNotFound.run{
      request
    }


    val expectedJson = Json.obj{
      "people"  → Json.arr(
        Json.obj(
        "name" -> Json.fromString("Julian"),
        "lastName" -> Json.fromString("Carvajal"),
          ("year" , Json.fromInt(1997)),
        "dateTrace" → Json.fromString(LocalDate.now().toString)
        ))
    }

    println(response.unsafeRunSync().body)
    check[Json](response,Status.Ok, Some(expectedJson))

  }

}