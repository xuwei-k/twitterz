package twitterz

import scalaz.{Monad, Free, Inject}
import scalaz.std.function._
import twitter4j._
import java.io.{File, InputStream}

object Twitterz extends Twitterz[Command] {
  implicit def instance[F[_]](implicit I: Inject[Command, F]): Twitterz[F] =
    new Twitterz[F]


  def runTwitterz[M[_]: Monad, A](sa: Free[Command, A])(interpreter: InterpreterF[M]): M[A] =
    sa.foldMap(interpreter)

  def run[A](sa: Free[Command, A]): Twitter => A =
    runTwitterz[({type l[a] = Twitter => a})#l, A](sa)(Twitter4jInterpreter)

  def eitherT[A](sa: Free[Command, A]): EitherTInterpreter[A] =
    runTwitterz[EitherTInterpreter, A](sa)(Twitter4jInterpreterEither)(eitherTInterpreter)
}

class Twitterz[F[_]](implicit I: Inject[Command, F]) {

  private[this] type FreeF[A] = Free[F, A]

  private[this] def lift[A](f: Command[A]): FreeF[A] =
    Free.liftF(I.inj(f))

  import Command._

  def anyCommand[A](function: Twitter => A): FreeF[A] =
    lift(AnyCommand(function))

  def search(query: Query): FreeF[QueryResult] =
    lift(Search(query))
  val getSavedSearches: FreeF[ResponseList[SavedSearch]] =
    lift(GetSavedSearches)

  def updateFriendshipById(
    userId: Long, enableDeviceNotification: Boolean, retweets: Boolean
  ): FreeF[Relationship] =
    lift(UpdateFriendshipById(userId, enableDeviceNotification, retweets))

  def updateFriendshipByScreenName(
    name: String, enableDeviceNotification: Boolean, retweets: Boolean
  ): FreeF[Relationship] =
    lift(UpdateFriendshipByScreenName(
      name, enableDeviceNotification, retweets
    ))

  def reportSpanById(id: Long): FreeF[User] =
    lift(ReportSpanById(id))
  def reportSpanByScreenName(name: String): FreeF[User] =
    lift(ReportSpanByScreenName(name))

  def createBlockById(id: Long): FreeF[User] =
    lift(CreateBlockById(id))
  def createBlockByScreenName(name: String): FreeF[User] =
    lift(CreateBlockByScreenName(name))
  def destroyBlockById(id: Long): FreeF[User] =
    lift(DestroyBlockById(id))
  def destroyBlockByScreenName(name: String): FreeF[User] =
    lift(DestroyBlockByScreenName(name))
  val getBlockIds: FreeF[IDs] =
    lift(GetBlockIds)

  def updateStatus(tweet: String): FreeF[Status] =
    lift(UpdateStatus(tweet))
  def retweetStatus(id: Long): FreeF[Status] =
    lift(RetweetStatus(id))
  def getRetweets(id: Long): FreeF[ResponseList[Status]] =
    lift(GetRetweets(id))
  val getRetweetsOfMe: FreeF[ResponseList[Status]] =
    lift(GetRetweetsOfMe)

  val getHomeTimeline: FreeF[ResponseList[Status]] =
    lift(GetHomeTimeline)
  val getMentionsTimeline: FreeF[ResponseList[Status]] =
    lift(GetMentionsTimeline)
  val getUserTimeline: FreeF[ResponseList[Status]] =
    lift(GetUserTimeline)

  val getDirectMessages: FreeF[ResponseList[DirectMessage]] =
    lift(GetDirectMessages)
  val getSentDirectMessages: FreeF[ResponseList[DirectMessage]] =
    lift(GetSentDirectMessages)

  val getFavorites: FreeF[ResponseList[Status]] =
    lift(GetFavorites)
  def getFavoritesById(id: Long): FreeF[ResponseList[Status]] =
    lift(GetFavoritesById(id))
  def getFavoritesByScreenName(name: String): FreeF[ResponseList[Status]] =
    lift(GetFavoritesByScreenName(name))
  def createFavorite(id: Long): FreeF[Status] =
    lift(CreateFavorite(id))

  def getUserListsById(id: Long): FreeF[ResponseList[UserList]] =
    lift(GetUserListsById(id))
  def getUserListsByScreenName(name: String): FreeF[ResponseList[UserList]] =
    lift(GetUserListsByScreenName(name))

  def createUserListSubscriptionByListId(listId: Long): FreeF[UserList] =
    lift(CreateUserListSubscriptionByListId(listId))
  def createUserListSubscriptionByOwnerId(ownerId: Long, slug: String): FreeF[UserList] =
    lift(CreateUserListSubscriptionByOwnerId(ownerId, slug))
  def createUserListSubscriptionByScreenName(name: String, slug: String): FreeF[UserList] =
    lift(CreateUserListSubscriptionByScreenName(name, slug))

  val getAvailableTrends: FreeF[ResponseList[Location]] =
    lift(GetAvailableTrends)
  def getPlaceTriends(id: Int): FreeF[Trends] =
    lift(GetPlaceTriends(id))

  def updateProfile(
    name: String, url: String, location: String, description: String
  ): FreeF[User] =
    lift(UpdateProfile(name, url, location, description))

  def updateProfileImage(file: File): FreeF[User] =
    lift(UpdateProfileImage(file))
  def updateProfileImageByStream(stream: InputStream): FreeF[User] =
    lift(UpdateProfileImageByStream(stream))
  def updateProfileBanner(file: File): FreeF[Unit] =
    lift(UpdateProfileBanner(file))
  def updateProfileBannerByStream(stream: InputStream): FreeF[Unit] =
    lift(UpdateProfileBannerByStream(stream))
  def updateProfileBackground(file: File, tile: Boolean): FreeF[User] =
    lift(UpdateProfileBackground(file, tile))
  def updateProfileBackgroundByStream(stream: InputStream, tile: Boolean): FreeF[User] =
    lift(UpdateProfileBackgroundByStream(stream, tile))
  def updateProfileColors(
    profileBackgroundColor: String,
    profileTextColor: String,
    profileLinkColor: String,
    profileSidebarFillColor: String,
    profileSidebarBorderColor: String
  ): FreeF[User] =
    lift(UpdateProfileColors(
      profileBackgroundColor,
      profileTextColor,
      profileLinkColor,
      profileSidebarFillColor,
      profileSidebarBorderColor
    ))

  def updateAccountSettings(
    trendLocationWoeid: Int,
    sleepTimeEnabled: Boolean,
    startSleepTime: String,
    endSleepTime: String,
    timeZone: String,
    lang: String
  ): FreeF[AccountSettings] =
    lift(UpdateAccountSettings(
      trendLocationWoeid,
      sleepTimeEnabled,
      startSleepTime,
      endSleepTime,
      timeZone,
      lang
    ))

  val getRateLimitStatus: FreeF[Map[String, RateLimitStatus]] =
    lift(GetRateLimitStatus)
}

