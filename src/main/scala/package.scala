import scalaz._
import scalaz.Id.Id
import scalaz.std.function._
import twitter4j.{Twitter, TwitterException}

package object twitterz {

  type InterpreterF[F[_]] = Command ~> F
  type Interpreter = InterpreterF[Id]

  private[twitterz] def freeCMonad[F[_]] =
    Free.freeMonad[({type l[a] = Coyoneda[F, a]})#l]

  private[twitterz] type EitherTInterpreter[A] =
    EitherT[({type λ[α] = Twitter => α})#λ, TwitterException, A]

  private[twitterz] val eitherTInterpreter: Monad[EitherTInterpreter] =
    EitherT.eitherTMonad[({type λ[α] = Twitter => α})#λ, TwitterException]
}
