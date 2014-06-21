import scalaz._
import scalaz.Free.FreeC
import scalaz.Id.Id
import scalaz.std.function._
import twitter4j.{Twitter, TwitterException}

package object twitterz {

  type InterpreterF[F[_]] = Command ~> F
  type Interpreter = InterpreterF[Id]

  private[twitterz] def freeCMonad[F[_]] =
    Free.freeMonad[({type l[a] = Coyoneda[F, a]})#l]

  // https://github.com/scalaz/scalaz/blob/5904d7f6/core/src/main/scala/scalaz/Free.scala#L328
  private[twitterz] def runFC[S[_], M[_], A](sa: FreeC[S, A])(interp: S ~> M)(implicit M: Monad[M]): M[A] =
    sa.foldMap(new (({type λ[x] = Coyoneda[S, x]})#λ ~> M) {
      def apply[AA](cy: Coyoneda[S, AA]): M[AA] =
        M.map(interp(cy.fi))(cy.k)
      }
    )

  private[twitterz] type EitherTInterpreter[A] =
    EitherT[({type λ[α] = Twitter => α})#λ, TwitterException, A]

  private[twitterz] val eitherTInterpreter: Monad[EitherTInterpreter] =
    EitherT.eitherTMonad[({type λ[α] = Twitter => α})#λ, TwitterException]
}