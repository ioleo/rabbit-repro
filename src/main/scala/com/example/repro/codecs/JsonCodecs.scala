package com.example.repro.codecs

import com.bilalfazlani.zioUlid.ULID
import zio.json.*

object JsonCodecs:

  given JsonDecoder[ULID] = JsonDecoder.string.mapOrFail(ULID(_).left.map(_ => "Not a valid ULID"))
  given JsonEncoder[ULID] = JsonEncoder.string.contramap(_.toString)

end JsonCodecs
