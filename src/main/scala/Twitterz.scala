package twitterz

import scalaz.Free.FreeC
import scalaz.{Monad, Free, Inject}
import scalaz.std.function._
import twitter4j._
import java.io.{File, InputStream}

object Twitterz extends Twitterz[Command] {
  implicit def instance[F[_]](implicit I: Inject[Command, F]): Twitterz[F] =
    new Twitterz[F]


  def runTwitterz[M[_]: Monad, A](sa: FreeC[Command, A])(interpreter: InterpreterF[M]): M[A] =
    runFC(sa)(interpreter)

  def run[A](sa: FreeC[Command, A]): Twitter => A =
    runTwitterz[({type l[a] = Twitter => a})#l, A](sa)(Twitter4jInterpreter)

  def eitherT[A](sa: FreeC[Command, A]): EitherTInterpreter[A] =
    runTwitterz[EitherTInterpreter, A](sa)(Twitter4jInterpreterEither)(eitherTInterpreter)
}

class Twitterz[F[_]](implicit I: Inject[Command, F]) {

  private[this] type FreeCF[A] = FreeC[F, A]

  private[this] def lift[A](f: Command[A]): FreeCF[A] =
    Free.liftFC(I.inj(f))

  import Command._

  def anyCommand[A](function: Twitter => A): FreeCF[A] =
    lift(AnyCommand(function))

  def search(query: Query): FreeCF[QueryResult] =
    lift(Search(query))
  val getSavedSearches: FreeCF[ResponseList[SavedSearch]] =
    lift(GetSavedSearches)

  def updateFriendshipById(
    userId: Long, enableDeviceNotification: Boolean, retweets: Boolean
  ): FreeCF[Relationship] =
    lift(UpdateFriendshipById(userId, enableDeviceNotification, retweets))

  def updateFriendshipByScreenName(
    name: String, enableDeviceNotification: Boolean, retweets: Boolean
  ): FreeCF[Relationship] =
    lift(UpdateFriendshipByScreenName(
      name, enableDeviceNotification, retweets
    ))

  def reportSpanById(id: Long): FreeCF[User] =
    lift(ReportSpanById(id))
  def reportSpanByScreenName(name: String): FreeCF[User] =
    lift(ReportSpanByScreenName(name))

  def createBlockById(id: Long): FreeCF[User] =
    lift(CreateBlockById(id))
  def createBlockByScreenName(name: String): FreeCF[User] =
    lift(CreateBlockByScreenName(name))
  def destroyBlockById(id: Long): FreeCF[User] =
    lift(DestroyBlockById(id))
  def destroyBlockByScreenName(name: String): FreeCF[User] =
    lift(DestroyBlockByScreenName(name))
  val getBlockIds: FreeCF[IDs] =
    lift(GetBlockIds)

  def updateStatus(tweet: String): FreeCF[Status] =
    lift(UpdateStatus(tweet))
  def retweetStatus(id: Long): FreeCF[Status] =
    lift(RetweetStatus(id))
  def getRetweets(id: Long): FreeCF[ResponseList[Status]] =
    lift(GetRetweets(id))
  val getRetweetsOfMe: FreeCF[ResponseList[Status]] =
    lift(GetRetweetsOfMe)

  val getHomeTimeline: FreeCF[ResponseList[Status]] =
    lift(GetHomeTimeline)
  val getMentionsTimeline: FreeCF[ResponseList[Status]] =
    lift(GetMentionsTimeline)
  val getUserTimeline: FreeCF[ResponseList[Status]] =
    lift(GetUserTimeline)

  val getDirectMessages: FreeCF[ResponseList[DirectMessage]] =
    lift(GetDirectMessages)
  val getSentDirectMessages: FreeCF[ResponseList[DirectMessage]] =
    lift(GetSentDirectMessages)

  val getFavorites: FreeCF[ResponseList[Status]] =
    lift(GetFavorites)
  def getFavoritesById(id: Long): FreeCF[ResponseList[Status]] =
    lift(GetFavoritesById(id))
  def getFavoritesByScreenName(name: String): FreeCF[ResponseList[Status]] =
    lift(GetFavoritesByScreenName(name))
  def createFavorite(id: Long): FreeCF[Status] =
    lift(CreateFavorite(id))

  def getUserListsById(id: Long): FreeCF[ResponseList[UserList]] =
    lift(GetUserListsById(id))
  def getUserListsByScreenName(name: String): FreeCF[ResponseList[UserList]] =
    lift(GetUserListsByScreenName(name))

  def createUserListSubscriptionByListId(listId: Long): FreeCF[UserList] =
    lift(CreateUserListSubscriptionByListId(listId))
  def createUserListSubscriptionByOwnerId(ownerId: Long, slug: String): FreeCF[UserList] =
    lift(CreateUserListSubscriptionByOwnerId(ownerId, slug))
  def createUserListSubscriptionByScreenName(name: String, slug: String): FreeCF[UserList] =
    lift(CreateUserListSubscriptionByScreenName(name, slug))

  val getAvailableTrends: FreeCF[ResponseList[Location]] =
    lift(GetAvailableTrends)
  def getPlaceTriends(id: Int): FreeCF[Trends] =
    lift(GetPlaceTriends(id))

  def updateProfile(
    name: String, url: String, location: String, description: String
  ): FreeCF[User] =
    lift(UpdateProfile(name, url, location, description))

  def updateProfileImage(file: File): FreeCF[User] =
    lift(UpdateProfileImage(file))
  def updateProfileImageByStream(stream: InputStream): FreeCF[User] =
    lift(UpdateProfileImageByStream(stream))
  def updateProfileBanner(file: File): FreeCF[Unit] =
    lift(UpdateProfileBanner(file))
  def updateProfileBannerByStream(stream: InputStream): FreeCF[Unit] =
    lift(UpdateProfileBannerByStream(stream))
  def updateProfileBackground(file: File, tile: Boolean): FreeCF[User] =
    lift(UpdateProfileBackground(file, tile))
  def updateProfileBackgroundByStream(stream: InputStream, tile: Boolean): FreeCF[User] =
    lift(UpdateProfileBackgroundByStream(stream, tile))
  def updateProfileColors(
    profileBackgroundColor: String,
    profileTextColor: String,
    profileLinkColor: String,
    profileSidebarFillColor: String,
    profileSidebarBorderColor: String
  ): FreeCF[User] =
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
  ): FreeCF[AccountSettings] =
    lift(UpdateAccountSettings(
      trendLocationWoeid,
      sleepTimeEnabled,
      startSleepTime,
      endSleepTime,
      timeZone,
      lang
    ))

  val getRateLimitStatus: FreeCF[Map[String, RateLimitStatus]] =
    lift(GetRateLimitStatus)
}

