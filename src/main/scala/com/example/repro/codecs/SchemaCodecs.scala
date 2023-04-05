package com.example.repro.codecs

import scala.util.Try

import com.bilalfazlani.zioUlid.ULID
import zio.schema.*

object SchemaCodecs:

  given Schema[ULID] =
    Schema[String].transformOrFail[ULID](str => ULID(str).left.map(_ => s"Invalid ULID $str"), ulid => Right(ulid.toString))
end SchemaCodecs
